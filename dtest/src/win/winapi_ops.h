#ifndef DTEST_WINAPI_OPS_H
#define DTEST_WINAPI_OPS_H

#include "stdafx.h"

/*��� ������� ������ ������� ����������� ����� � ������� ��������.*/
HANDLE make_inheritable(HANDLE hnd);

/*���������� �������� ��������� ������ WinAPI.*/
std::string get_last_error(unsigned int errcode);
std::string get_last_error();

/*���������� ������� ����� � ��� �� ����������.*/
int recur_rmdir(const tstring& refcstrRootDirectory);

char* enc_conv(const char* src,
				int src_len,
				unsigned int enc_from,
				unsigned int enc_to,
				int *len);

std::string enc_conv(const std::string& s, unsigned int enc_from, unsigned int enc_to);

std::wstring to_utf16(const std::string& s, unsigned int enc_from);
std::string from_utf16(const std::wstring& ws, unsigned int enc_to);

tstring to_winapi(const std::string& s, unsigned int enc_from);
std::string from_winapi(const tstring& s, unsigned int enc_to);

#endif
