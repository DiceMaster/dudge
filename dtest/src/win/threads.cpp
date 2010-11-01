#include "../sysconf.h"
#ifdef WINDOWS

#include "threads.h"

using namespace std;

#include "../dtest.h"
#include "winapi_ops.h"

const DWORD pipe_buffer_size = 1024;

DWORD WINAPI output_thread_func(LPVOID param)
{
	output_thread_args_t args = *(output_thread_args_t *)param;
	delete (output_thread_args_t *)param;

	*args.p_is_limit = false;
	char buf[pipe_buffer_size];
	DWORD total_bytes = 0;
	DWORD bytes_readed;
	BOOL pipe_is_alive;

	while(
		pipe_is_alive = ReadFile(
			args.hRead,							//Читаем из выходного потока решения.
			buf,								//Пишем в буфер.
			1,//pipe_buffer_size,					//Сколько хотим прочитать.
			&bytes_readed,						//Куда записывать количество прочитанных байт.
			NULL								//Должно быть NULL.
			)
		)
	{
		if(total_bytes + bytes_readed > (DWORD) args.output_limit)
		{
			*args.p_is_limit = true;

			TerminateProcess(args.hProcess, 255);

			CloseHandle(args.hRead);
			return 1;
		}

		total_bytes += bytes_readed;

		args.out->write(buf, bytes_readed);

		bytes_readed = 0;
	}

	CloseHandle(args.hRead);
	return 0;
}

DWORD WINAPI input_thread_func(LPVOID param)
{
	input_thread_args_t args = *(input_thread_args_t *)param;
	delete (input_thread_args_t *)param;
	
	DWORD bytes_written;

	while(args.inp->good())
	{
		int b = args.inp->get();
		if(b == -1) break;
		
		if( (!WriteFile(
				args.hWrite,
				(char*) &b,
				1,
				&bytes_written,
				NULL))
			|| (bytes_written == 0)
			)
		{
			break;
		}
	}

	CloseHandle(args.hWrite);

	return 0;
}

#ifndef LEGACY_OS
DWORD WINAPI monitor_thread_func(LPVOID param)
{
	monitor_thread_args_t args = *(monitor_thread_args_t *)param;
	delete (monitor_thread_args_t *)param;

	DWORD msg;
	ULONG_PTR comp_key;
	LPOVERLAPPED poverlapped;

	do
	{
		BOOL gresult = GetQueuedCompletionStatus(
			args.hPort,
			&msg,
			&comp_key,
			&poverlapped,
			args.timeout_msec
			);

		if((!gresult)&&(poverlapped == NULL))
		{
			*(args.presult) = DTEST_TIME_LIMIT;
			CloseHandle(args.hPort);
			TerminateProcess(args.hProcess, 0);
			return 0;
		}
	}while(msg == JOB_OBJECT_MSG_NEW_PROCESS);

	CloseHandle(args.hPort);

	switch(msg)
	{
		case JOB_OBJECT_MSG_EXIT_PROCESS:
			*(args.presult) = DTEST_PASSED;
			break;
		case JOB_OBJECT_MSG_ACTIVE_PROCESS_ZERO:
		case JOB_OBJECT_MSG_END_OF_JOB_TIME:
			*(args.presult) = DTEST_TIME_LIMIT;
			break;
		case JOB_OBJECT_MSG_JOB_MEMORY_LIMIT:
			*(args.presult) = DTEST_MEMORY_LIMIT;
			break;
		case JOB_OBJECT_MSG_ABNORMAL_EXIT_PROCESS:
			*(args.presult) = DTEST_RUNTIME_ERROR;
			break;
		case JOB_OBJECT_MSG_ACTIVE_PROCESS_LIMIT:
			*(args.presult) = DTEST_PROCESS_LIMIT;
			break;
		default:
			*(args.presult) = DTEST_INTERNAL_ERROR;
			break;
	}

	return 0;
}
#endif

#ifdef LEGACY_OS
DWORD WINAPI monitor_thread_func(LPVOID param)
{
	monitor_thread_args_t args = *(monitor_thread_args_t *)param;
	delete (monitor_thread_args_t *)param;

	const DWORD interval = 200;
	DWORD elapsed = 0;
	
	do
	{
		DWORD ex_code;
		GetExitCodeProcess(args.hProcess, &ex_code);
		if(ex_code != STILL_ACTIVE)
			break;
		Sleep(interval);
		elapsed += interval;
	}while(elapsed <= args.timeout_msec);
	
	if(elapsed > args.timeout_msec)
	{
		*(args.presult) = DTEST_TIME_LIMIT;
		TerminateProcess(args.hProcess, 0);
		return 0;
	}

	//TODO: Сделать обнаружение превышение памяти решением в Windows 98.

	*(args.presult) = DTEST_PASSED;
	return 0;
}
#endif

#endif
