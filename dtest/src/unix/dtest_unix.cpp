#include "../sysconf.h"
#ifdef UNIX

#include "../dtest.h"

#include <sys/time.h>
#include <sys/resource.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <pwd.h>
#include <errno.h>
#include <string.h>
#include <syslog.h>

#include <cstdlib>
#include <cstdio>
#include <string>
#include <iostream>
#include <list>
#include <vector>
#include <sstream>

using namespace std;

bool is_debug = true;

#include "unix_ops.h"

const string tst_username = "nobody";
const int cpu_msec_bonus = 50;
const int out_buf_size = 512;

bool route_to_stdin(istream& data);
pid_t kill_after(pid_t sol_pid, int interval_msec);

bool dtest_init()
{
	openlog("dtest", 0, LOG_USER);
	return true;
}

std::string create_test_dir()
{
	/*Генерируем случайное имя папки.*/
	char tnam[] = "/tmp/dtest-XXXXXX";
	string tdir = mkdtemp(tnam);
	
	/*Создаем временную директорию.*/
	mkdir(tdir.c_str(), 0750);

	return tdir;
}

bool delete_test_dir(const std::string& path)
{
	if(!recur_rmdir(path.c_str(), NULL))
		return false;
	
	return true;
}

checking_result check_solution(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output
	)
{
	std::stringstream ss;
	return check_solution(limits, command, test_dir_path, input_test, output, ss);
}

checking_result check_solution(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output,
	std::ostream& error_stream
	)
{
	return check_solution_as_user(limits, command, test_dir_path, input_test, output, error_stream, "", "", "");
}

checking_result check_solution_as_user(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output,
	std::ostream& error_stream,
	const std::string& username,
	const std::string& domain,
	const std::string& password
	)
{
	checking_result res;
	res.res_type = DTEST_INTERNAL_ERROR;
	res.ret_value = 0;
	
	//Мера предосторожности.
	if(test_dir_path == "/")
	{
		return res;
	}
	
	pid_t sol_pid;
	
	int opipe[2]; pipe(opipe);
	int epipe[2]; pipe(epipe);
	
	switch( sol_pid = fork() )
	{
	case -1:
		syslog(LOG_CRIT, "fork() failed (errno=%d): %s", errno, strerror(errno));
		return res;
	break;
	
	case 0:
		if( !route_to_stdin(input_test) )
		{
			syslog(LOG_CRIT, "Input data routing failed (errno=%d): %s", errno, strerror(errno));
			_exit(DTEST_INTERNAL_ERROR);
		}

		close(opipe[0]);
		if( dup2(opipe[1], 1) == -1 )
		{
			syslog(LOG_CRIT, "stdout dup2() failed (errno=%d): %s", errno, strerror(errno));
			_exit(DTEST_INTERNAL_ERROR);
		}

		close(epipe[0]);
		if( dup2(epipe[1], 2) == -1 )
		{
			syslog(LOG_CRIT, "stderr dup2() failed (errno=%d): %s", errno, strerror(errno));
			_exit(DTEST_INTERNAL_ERROR);
		}
		
		/*Уменьшаем приоритет.*/
		errno = 0;
		int pr = setpriority(PRIO_PROCESS, 0, -5);
		if(errno != 0)
			syslog(LOG_WARNING, "setpriority() failed (errno=%d): %s", errno, strerror(errno));
		
		bool is_root = false;
		
		passwd *cred = getpwnam(tst_username.c_str());
		syslog(LOG_INFO, "uid=%d, euid=%d", getuid(), geteuid());
		
		if(geteuid() == 0) is_root = true;
		
		chdir(test_dir_path.c_str());
		
		if(is_root)
		{
			chmod(test_dir_path.c_str(), 0500);
			
/*			chmod(
				(test_dir_path + "/" + exe_filename).c_str(),
				0500);*/
			
			chown(test_dir_path.c_str(), cred->pw_uid, cred->pw_gid);
			
/*			chown(
				(test_dir_path + "/" + exe_filename).c_str(),
				cred->pw_uid,
				cred->pw_gid);*/
		
			/*Делаем chroot().*/
			//chroot(".");
		
			/*Сбрасываем привилегии.*/
			if( (setuid(cred->pw_uid) != 0) )
                            //|| (setgid(cred->pw_gid) != 0))
			{
				syslog(LOG_ERR, "Unable to drop privs (errno=%d): %s", errno, strerror(errno));
				_exit(DTEST_INTERNAL_ERROR);
			}
		}
		
		rlimit proglim;

		proglim.rlim_cur = 0;
		proglim.rlim_max = 0;
		setrlimit(RLIMIT_CORE, &proglim);
		
		proglim.rlim_cur = limits.cpu_msec / 1000;
		proglim.rlim_max = int(limits.cpu_msec * 1 / 1000);
		setrlimit(RLIMIT_CPU, &proglim);

		proglim.rlim_cur = limits.mem_bytes;
		proglim.rlim_max = int(limits.mem_bytes * 1);
		setrlimit(RLIMIT_DATA, &proglim);
		
		proglim.rlim_cur = limits.mem_bytes;
		proglim.rlim_max = int(limits.mem_bytes * 1);
		setrlimit(RLIMIT_STACK, &proglim);
		
		proglim.rlim_cur = limits.proc_num;
		proglim.rlim_max = limits.proc_num;
		//setrlimit(RLIMIT_NPROC, &proglim);
		
		//Запускаем решение.
		
		vector<char*> args;
		stringstream ss(command + " ");
		while(ss.good())
		{
			string tmps;
			ss >> tmps; ss.get();
			char *buf = new char[tmps.length() + 1];
			strcpy(buf, tmps.c_str());
			args.push_back(buf);
		}
		args.push_back(NULL);
		
		execvp(args[0], &(args[0]));
		syslog(LOG_CRIT, "execvp() is returned (errno=%d): %s", errno, strerror(errno));
		syslog(LOG_CRIT, "execvp() 0='%s' 1='%s'", args[0], args[1]?args[1]:"");
		_exit(DTEST_INTERNAL_ERROR);
	break;
	} // switch fork()

	close(opipe[1]);
	close(epipe[1]);

	pid_t killer_pid = kill_after(sol_pid, limits.timeout_msec);

	int readed_bytes = 0;
	int readed_ebytes = 0;
	bool is_out_limit = false;
	char buf[out_buf_size + 1];
	
//	if(is_debug)
//		syslog(LOG_ERR, "Output limit = %d", limits.out_bytes);
	
	ssize_t len = 0;
	bool ok=true;
	while(ok) {
	    ok=false;
	    if((len = read(opipe[0], buf, out_buf_size)) > 0) {
		ok=true;
		if( readed_bytes + len > limits.out_bytes ) {
			kill(sol_pid, SIGKILL);
			is_out_limit = true;
		}
		else {
			readed_bytes += len;
			// Пишем полученные от решения байты в выходной поток.
			output.write(buf, len);
		}
	    }

	    if((len = read(epipe[0], buf, out_buf_size)) > 0) {
		ok=true;
		buf[len]=0; syslog(LOG_WARNING, "stderr: %s", buf);
		if( readed_ebytes < limits.out_bytes ) {
			readed_ebytes += len;
			error_stream.write(buf, len);
		}
	    }
	}
	
	rusage ru;
	int sol_ex_st, killer_ex_st;
	
	wait4(sol_pid, &sol_ex_st, 0, &ru);
	
	waitpid(killer_pid, &killer_ex_st, 0);
	
	bool is_sol_timeout = (killer_ex_st == 1);
	
	if(is_sol_timeout)
	{
		//Превышен лимит по времени реального мира.
		res.res_type = DTEST_TIME_LIMIT;
	}
	else if(is_out_limit)
	{
		//Превышен лимит вывода.
		res.res_type = DTEST_OUTPUT_LIMIT;
	}
	else if( WIFEXITED(sol_ex_st) )
	{
		syslog(LOG_INFO,"exited with code=%d",WEXITSTATUS(sol_ex_st));
		if( WEXITSTATUS(sol_ex_st) == 0)
		{
			//Задача выполнилась успешно.
			res.res_type = DTEST_PASSED;
		}
		else if( WEXITSTATUS(sol_ex_st) == DTEST_INTERNAL_ERROR )
		{
			//Внутренняя ошибка.
			res.res_type = DTEST_INTERNAL_ERROR;
		}
		else
		{
			//Ошибка времени выполнения.
			res.res_type = DTEST_RUNTIME_ERROR;
		}
	}
	else if( WIFSIGNALED(sol_ex_st) )
	{
		syslog(LOG_INFO,"terminated by signal %d",WTERMSIG(sol_ex_st));
		if( (ru.ru_stime.tv_sec * 1000 +
			ru.ru_stime.tv_usec / 1000 +
			ru.ru_utime.tv_sec * 1000 +
			ru.ru_utime.tv_usec / 1000
			> limits.cpu_msec - cpu_msec_bonus)
		|| (WTERMSIG(sol_ex_st) == SIGXCPU) )
		{
			//Превышен лимит по процессорному времени.
			res.res_type = DTEST_TIME_LIMIT;
		}
		else if(WTERMSIG(sol_ex_st) == SIGSEGV)
		{
			//Превышен лимит по памяти (стек).
			res.res_type = DTEST_MEMORY_LIMIT;
		}
/*		else if(ru.ru_ixrss + ru.ru_idrss +
			ru.ru_isrss > limits.mem_bytes)
		{
			//Превышен лимит по памяти.
			res.res_type = DTEST_MEMORY_LIMIT;
		}*/
		else
		{
			//Ошибка времени выполнения.
			res.res_type = DTEST_RUNTIME_ERROR;
		}
	}

	//Чтобы не было зомби.
	while( waitpid(-1, NULL, WNOHANG) > 0 ) {};
	
	return res;
}

bool route_to_stdin(istream& data)
{
	int conn[2];
	if( pipe(conn) != 0)
		return false;
	
	switch(fork())
	{
		case -1:
			return false;
		break;
		case 0:
			close(conn[0]);
			
			FILE *out = fdopen(conn[1], "wb");
			while(data.good())
			{
				char b = data.get();
				if(b == -1) break;
				if( fwrite(&b, 1, 1, out) < 1 )
					break;
			}
			fclose(out);
			
			_exit(0);
		break;
	}
	
	close(conn[1]);
	if( dup2(conn[0], 0) == -1)
		return false;
	
	return true;
}

pid_t kill_after(pid_t sol_pid, int interval_msec)
{
	pid_t pid;
	int ret;
	
	switch(pid = fork())
	{
		case -1:
			ret=errno;
			syslog(LOG_ERR, "fork is failed, errno=%d",ret);
			return -1;
		break;
		case 0:
			timespec ts;
			int ret;
			
			ts.tv_sec = interval_msec / 1000;
			ts.tv_nsec = (interval_msec % 1000) * 1000000;
			syslog(LOG_INFO, "Wait for %d msecs ...",interval_msec);
			
			nanosleep(&ts, NULL);
			_exit(2);
			
			if(kill(sol_pid, 0)==0) {
			    // Решение превысило таймаут
			    if( (ret=kill(sol_pid, SIGKILL))!=0) ret=errno;
			    syslog(LOG_INFO, "Timeout detected, solution terminated by SIGKILL, errno=%d",ret);
			    _exit(1);
			    }
			ret=errno;
			if(ret==ESRCH) {
			    syslog(LOG_INFO,"Solution is finished before timeout, none todo");
			    _exit(0);
			    }
			
			syslog(LOG_ERR, "Kill for pid=%d is failed, errno=%d",pid,ret);
			_exit(2); // timeout detection mashinery is failed
		break;
		default:
			//syslog(LOG_INFO, "fork succesful with pid=%d",pid);
		break;
	}

	return pid;
}

#endif
