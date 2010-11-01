#ifndef DTEST_THREADS_H
#define DTEST_THREADS_H

#include "stdafx.h"

extern const DWORD pipe_buffer_size;

//��������� ������ ������
struct output_thread_args_t
{
	std::ostream *out;
	HANDLE hRead;		//HANDLE ��������� ������ �������.
	HANDLE hProcess;	//HANDLE �������� �������.
	unsigned int output_limit;
	bool *p_is_limit;
};

//����� ��� ������ �������� ������
DWORD WINAPI output_thread_func(LPVOID param);

//��������� ������ ������
struct input_thread_args_t
{
	std::istream *inp;
	HANDLE hWrite;
};

//����� ��� ������ ������� ������
DWORD WINAPI input_thread_func(LPVOID param);

//��������� ������ ����������
struct monitor_thread_args_t
{
	HANDLE hProcess;
	unsigned char *presult;
	DWORD timeout_msec;
#ifndef LEGACY_OS
	HANDLE hPort;
#endif
};

//����� ��� ���������� �� ��������� �������
DWORD WINAPI monitor_thread_func(LPVOID param);

#endif
