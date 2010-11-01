#include "Checker.h"
#include "dtest.h"
#include "Task.h"
#include <iostream>
#include <fstream>

using namespace std;

Checker::Checker(const char *pWorkDir) : workDirectory(pWorkDir)
{
	InitializeCriticalSection(&mQueueLock);
	mNewTasksEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
	_beginthreadex(NULL, 0, &CheckThread, this, 0, NULL);
	if (!dtest_init())
	{
		MessageBox(NULL, "Все ужасно, тестить нечем!", "ААА!", MB_ICONERROR);
		ExitProcess(0);
	}
}

Checker::~Checker()
{
	DeleteCriticalSection(&mQueueLock);
	CloseHandle(mNewTasksEvent);
}

void Checker::SetListener( ICheckerListener *pListener )
{
	mpListener = pListener;
}

void Checker::AddTask( User *from, int iTask, const char *text, int iLang )
{
	EnterCriticalSection(&mQueueLock);
	mTasks.push(STask(from, iTask, text, iLang));
	LeaveCriticalSection(&mQueueLock);
	SetEvent(mNewTasksEvent);
}

unsigned WINAPI Checker::CheckThread( void *pData )
{
	Checker *pChecker = (Checker *)pData;

	while (true)
	{
		WaitForSingleObject(pChecker->mNewTasksEvent, INFINITE);

		while (true)
		{
			STask nextTask;
			EnterCriticalSection(&pChecker->mQueueLock);
			if (pChecker->mTasks.empty())
			{
				LeaveCriticalSection(&pChecker->mQueueLock);
				break;
			}
			nextTask = pChecker->mTasks.front();
			pChecker->mTasks.pop();
			LeaveCriticalSection(&pChecker->mQueueLock);

			pChecker->CheckTask(nextTask);
		}
	}

	return 0;
}

void Checker::CheckTask( const STask &task )
{
	char srcPath[MAX_PATH];

	task.from->Lock();
	wsprintf(srcPath, "%s\\Users\\%s\\%c", 
		workDirectory.c_str(), task.from->GetFolder().c_str(), 'A' + task.iTask);

	char testDir[MAX_PATH];
	char multibyteTestDir[MAX_PATH * 3 + 1];
	wsprintf(testDir, "%s\\Users\\%s", workDirectory.c_str(), task.from->GetFolder().c_str());
	AnsiToMultiByte(testDir, multibyteTestDir, MAX_PATH * 3, CP_UTF8);

	FILE *file;
	errno_t error = fopen_s(&file, srcPath, "wb");
	if (error != 0)
	{
		mpListener->OnCheckResult(task.from, task.iTask, eCheckInternalError, 0, 0);
		task.from->Unlock();
		return;
	}

	fwrite(task.text.c_str(), task.text.size(), 1, file);
	fclose(file);
	char exePath[MAX_PATH + 1];
	char multibyteExePath[MAX_PATH * 3 + 1];
	wsprintf(exePath, "%s.exe", srcPath);
	AnsiToMultiByte(exePath, multibyteExePath, MAX_PATH * 3, CP_UTF8);
  
	bool bCompiled;
	switch (task.iLang)
	{
	case 0:
		bCompiled = CompilePascal(srcPath);
		break;
	case 1:
		bCompiled = CompileCpp(srcPath);
		break;
	case 2:
		bCompiled = CompileCSharp(srcPath);
		break;
	}

	if (bCompiled)
	{
		run_limits limits;
		switch (task.iLang)
		{
			case 2:
				limits.proc_num = 2;
				break;
			default:
				limits.proc_num = 1;
		}
		Task *pTask = Task::GetTask(task.iTask);
		limits.cpu_msec = pTask->GetTimeout() * 1000;
		limits.timeout_msec = 5000;
		limits.mem_bytes = 67108864;
		limits.out_bytes = 67108864;

		char testIn[MAX_PATH + 1];
		char testOut[MAX_PATH + 1];
		char taskOutput[MAX_PATH + 1];
		int iTest = 1;
		wsprintf(testIn, "%s\\Tasks\\%c\\%d.in", workDirectory.c_str(), 'A' + task.iTask, iTest);
		bool errors = false;
		while (GetFileAttributes(testIn) != INVALID_FILE_ATTRIBUTES)
		{
			wsprintf(testOut, "%s\\Tasks\\%c\\%d.out", workDirectory.c_str(), 'A' + task.iTask, iTest);
			wsprintf(taskOutput, "%s\\Users\\%s\\%c%d.output", 
				workDirectory.c_str(), task.from->GetFolder().c_str(), 'A' + task.iTask, iTest);
			ifstream fin(testIn);
			ofstream fout(taskOutput);
			checking_result result = check_solution(limits, multibyteExePath, multibyteTestDir, fin, fout);
			fin.close();
			fout.close();

			if (result.res_type == DTEST_TIME_LIMIT)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckTimeLimit, iTest, 0);
				task.from->Unlock();
				break;
			}

			if (result.res_type == DTEST_RUNTIME_ERROR)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckRuntimeError, iTest, result.ret_value);
				task.from->Unlock();
				break;
			}

			if (result.res_type == DTEST_PROCESS_LIMIT)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckProcessLimit, iTest, result.ret_value);
				task.from->Unlock();
				break;
			}

			if (result.res_type == DTEST_OUTPUT_LIMIT)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckOutputLimit, iTest, result.ret_value);
				task.from->Unlock();
				break;
			}

			if (result.res_type == DTEST_MEMORY_LIMIT)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckMemoryLimit, iTest, result.ret_value);
				task.from->Unlock();
				break;
			}

			if (result.res_type == DTEST_INTERNAL_ERROR)
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckInternalError, iTest, result.ret_value);
				task.from->Unlock();
				break;
			}

			if (!CheckTestOutput(testOut, taskOutput))
			{
				errors = true;
				mpListener->OnCheckResult(task.from, task.iTask, eCheckWrongAnswer, iTest, 0);
				task.from->Unlock();
				break;
			}

			iTest++;
			wsprintf(testIn, "%s\\Tasks\\%c\\%d.in", workDirectory.c_str(), 'A' + task.iTask, iTest);
		}
		
		if (!errors)
		{
			mpListener->OnCheckResult(task.from, task.iTask, eCheckOk, 0, 0);
			task.from->Unlock();
			User::SortUsers();
		}
	}
	else
	{
		mpListener->OnCheckResult(task.from, task.iTask, eCheckCompileError, 0, 0);
		task.from->Unlock();
	}

	DeleteFile(exePath);
}

bool Checker::CompilePascal( const char *srcPath )
{
	char command[MAX_PATH * 3];
	char pasPath[MAX_PATH + 1];
	char exePath[MAX_PATH + 1];
	wsprintf(pasPath, "%s.pas", srcPath);
	CopyFile(srcPath, pasPath, FALSE);
	wsprintf(command, "%s\\Compilers\\Pascal\\dcc32.exe -CC -U\"%s\\Compilers\\Pascal\" \"%s\"", 
		workDirectory.c_str(), workDirectory.c_str(), pasPath);

	PROCESS_INFORMATION pinf;
	STARTUPINFO si;
	si.cb = sizeof(si);
	si.lpReserved = NULL;
	si.lpDesktop = NULL;
	si.lpTitle = NULL;
	si.cbReserved2 = 0;
	si.lpReserved2 = NULL;
	si.dwFlags = STARTF_USESHOWWINDOW;
	si.wShowWindow = SW_HIDE;

	CreateProcess(NULL, command, NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL, &si, &pinf);
	WaitForSingleObject(pinf.hProcess, INFINITE);
	CloseHandle(pinf.hThread);
	CloseHandle(pinf.hProcess);

	DeleteFile(pasPath);
	wsprintf(exePath, "%s.exe", srcPath);
	if (GetFileAttributes(exePath) == INVALID_FILE_ATTRIBUTES)
		return false;
	else
		return true;
}

bool Checker::CompileCpp( const char *path )
{
	char command[MAX_PATH * 3];
	char cppPath[MAX_PATH + 1];
	char exePath[MAX_PATH + 1];
	wsprintf(cppPath, "%s.pas", path);
	CopyFile(path, cppPath, FALSE);
	wsprintf(exePath, "%s.exe", path);
	wsprintf(command, "%s\\Compilers\\CPP\\cl.exe /I \"%s\\Compilers\\CPP\\include\" /EHsc /RTC1 /ML /W3 /nologo /TP \"%s\" /link /OUT:\"%s\" /NOLOGO /LIBPATH:\"%s\\Compilers\\CPP\\lib\" /SUBSYSTEM:CONSOLE /MACHINE:IX86",
		workDirectory.c_str(), workDirectory.c_str(), cppPath, exePath, workDirectory.c_str());

	PROCESS_INFORMATION pinf;
	STARTUPINFO si;
	si.cb = sizeof(si);
	si.lpReserved = NULL;
	si.lpDesktop = NULL;
	si.lpTitle = NULL;
	si.cbReserved2 = 0;
	si.lpReserved2 = NULL;
	si.dwFlags = STARTF_USESHOWWINDOW;
	si.wShowWindow = SW_HIDE;

	CreateProcess(NULL, command, NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL, &si, &pinf);
	WaitForSingleObject(pinf.hProcess, INFINITE);
	CloseHandle(pinf.hThread);
	CloseHandle(pinf.hProcess);

	DeleteFile(cppPath);
	if (GetFileAttributes(exePath) == INVALID_FILE_ATTRIBUTES)
		return false;
	else
		return true;
}

bool Checker::CompileCSharp( const char *path )
{
	char command[MAX_PATH * 3];
	char csPath[MAX_PATH + 1];
	char exePath[MAX_PATH + 1];
	wsprintf(csPath, "%s.cs", path);
	CopyFile(path, csPath, FALSE);
	wsprintf(exePath, "%s.exe", path);
	wsprintf(command, "%s\\Compilers\\CS\\csc.exe /out:\"%s\" \"%s\"", workDirectory.c_str(), exePath, csPath);

	PROCESS_INFORMATION pinf;
	STARTUPINFO si;
	si.cb = sizeof(si);
	si.lpReserved = NULL;
	si.lpDesktop = NULL;
	si.lpTitle = NULL;
	si.cbReserved2 = 0;
	si.lpReserved2 = NULL;
	si.dwFlags = STARTF_USESHOWWINDOW;
	si.wShowWindow = SW_HIDE;

	CreateProcess(NULL, command, NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL, &si, &pinf);
	WaitForSingleObject(pinf.hProcess, INFINITE);
	CloseHandle(pinf.hThread);
	CloseHandle(pinf.hProcess);

	DeleteFile(csPath);
	if (GetFileAttributes(exePath) == INVALID_FILE_ATTRIBUTES)
		return false;
	else
		return true;
}

bool Checker::CheckTestOutput( const char *etalonPath, const char *resultPath )
{
	FILE *etalonFile;
	errno_t error = fopen_s(&etalonFile, etalonPath, "r");
	if (error != 0)
		return false;

	FILE *resultFile;
	error = fopen_s(&resultFile, resultPath, "r");
	if (error != 0)
	{
		fclose(etalonFile);
		return false;
	}
	
	while (true)
	{
		char etalonString[4096], resultString [4096];
		etalonString[0] = '\0';
		resultString[0] = '\0';
		fscanf(resultFile, "%s", resultString);
		fscanf(etalonFile, "%s", etalonString);

		bool etalonEof = feof(etalonFile) != 0;
		bool resultEof = feof(resultFile) != 0;
		if (etalonEof)
		{
			fclose(resultFile);
			fclose(etalonFile);	
			return resultEof;
		}

		if (resultEof)
		{
			fclose(resultFile);
			fclose(etalonFile);	
			return false;
		}


		if (lstrcmp(etalonString, resultString))
		{
			fclose(resultFile);
			fclose(etalonFile);	
			return false;
		}
	}
	
	fclose(resultFile);
	fclose(etalonFile);
	return true;
}

int Checker::AnsiToMultiByte(const char *str, char *outStr, int outSize, UINT encoding)
{
	int strLength = lstrlen(str);
	wchar_t *wstr = (wchar_t *)alloca((strLength + 1) * sizeof(wchar_t));
	int wstrWritten = MultiByteToWideChar(CP_ACP, 0, str, -1, wstr, strLength + 1);
	if (wstrWritten > 0)
		wstrWritten--;
	wstr[wstrWritten] = '\0';
	return WideCharToMultiByte(encoding, 0, wstr, -1, outStr, outSize, NULL, NULL);

}
