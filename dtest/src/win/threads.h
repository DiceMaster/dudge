#ifndef DTEST_THREADS_H
#define DTEST_THREADS_H

#include "stdafx.h"

extern const DWORD pipe_buffer_size;

//Параметры потока чтения
struct output_thread_args_t
{
	std::ostream *out;
	HANDLE hRead;		//HANDLE выходного потока решения.
	HANDLE hProcess;	//HANDLE процесса решения.
	unsigned int output_limit;
	bool *p_is_limit;
};

//Поток для чтения выходных данных
DWORD WINAPI output_thread_func(LPVOID param);

//Параметры потока записи
struct input_thread_args_t
{
	std::istream *inp;
	HANDLE hWrite;
};

//Поток для записи входных данных
DWORD WINAPI input_thread_func(LPVOID param);

//Параметры потока наблюдения
struct monitor_thread_args_t
{
	HANDLE hProcess;
	unsigned char *presult;
	DWORD timeout_msec;
#ifndef LEGACY_OS
	HANDLE hPort;
#endif
};

//Поток для наблюдения за процессом решения
DWORD WINAPI monitor_thread_func(LPVOID param);

#endif
