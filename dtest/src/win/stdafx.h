#ifndef DTEST_STDAFX_H
#define DTEST_STDAFX_H

//����������� ��� Windows 2000
//#define _WIN32_WINNT 0x0500
//#define WINVER 0x0500

//Windows 98
//#define _WIN32_WINDOWS 0x0410
//#define WINVER 0x0410

#include <windows.h>
#include <tchar.h>
#include <direct.h>

/*#ifdef MINGW
#include <ddk/ntapi.h>
#define SetInformationJobObject(arg1, arg2, arg3, arg4) ZwSetInformationJobObject((arg1), (arg2), (arg3), (arg4))
#endif*/

#include <string>
//#include <xstring>
#include <iostream>
#include <list>
#include <sstream>
#include <map>

#ifdef _UNICODE
typedef std::wstring tstring;
#define tcout std::wcout
#define tcerr std::wcerr
#define tcin std::wcin
#else
typedef std::string tstring;
#define tcout cout
#define tcerr cerr
#define tcin cin
#endif

#include "../sysconf.h"

#endif
