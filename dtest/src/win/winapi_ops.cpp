#include "../sysconf.h"
#ifdef WINDOWS

#include "winapi_ops.h"

using namespace std;

HANDLE make_inheritable(HANDLE hnd)
{
    HANDLE copy;
    HANDLE self = GetCurrentProcess();

    if (! DuplicateHandle(
		self,
		hnd,
		self,
		&copy,
        0,
		TRUE,
        DUPLICATE_SAME_ACCESS | DUPLICATE_CLOSE_SOURCE)
		)
	{
		return INVALID_HANDLE_VALUE;
	}

    return copy;
}

std::string get_last_error(unsigned int errcode)
{
	LPVOID lpMsgBuf;
	if (!FormatMessageA(
		FORMAT_MESSAGE_ALLOCATE_BUFFER | 
		FORMAT_MESSAGE_FROM_SYSTEM | 
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		errcode,
		0,
		(LPSTR) &lpMsgBuf,
		0,
		NULL ))
	{
		return "";
	}

	std::string error = (LPCSTR)lpMsgBuf;

	LocalFree( lpMsgBuf );

	return error;
}

std::string get_last_error()
{
	return get_last_error(GetLastError());
}

int recur_rmdir(const tstring& refcstrRootDirectory)
{
  tstring strFilePath;				// Path of file
  tstring strPattern;				// Pattern
  HANDLE hFile;						// Handle to file
  WIN32_FIND_DATA FileInformation;	// File information

  strPattern = refcstrRootDirectory + _T("\\*.*");
  hFile = ::FindFirstFile(strPattern.c_str(), &FileInformation);
  if(hFile != INVALID_HANDLE_VALUE)
  {
    do
    {
      if(FileInformation.cFileName[0] != _T('.'))
      {
        strFilePath.erase();
        strFilePath = tstring(refcstrRootDirectory + _T("\\")) + FileInformation.cFileName;

        if(FileInformation.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
        {
          // Delete subdirectory
          int iRC = recur_rmdir(strFilePath);
          if(iRC)
            return iRC;
        }
        else
        {
          // Set file attributes
          if(::SetFileAttributes(strFilePath.c_str(),
                                 FILE_ATTRIBUTE_NORMAL) == FALSE)
            return ::GetLastError();

          // Delete file
          if(::DeleteFile(strFilePath.c_str()) == FALSE)
            return ::GetLastError();
        }
      }
    } while(::FindNextFile(hFile, &FileInformation) == TRUE);

    // Close handle
    ::FindClose(hFile);

    DWORD dwError = ::GetLastError();
    if(dwError != ERROR_NO_MORE_FILES)
      return dwError;
    else
    {
      // Set directory attributes
      if(::SetFileAttributes(refcstrRootDirectory.c_str(),
                             FILE_ATTRIBUTE_NORMAL) == FALSE)
        return ::GetLastError();

      // Delete directory
      if(::RemoveDirectory(refcstrRootDirectory.c_str()) == FALSE)
        return ::GetLastError();
    }
  }

  return 0;
}

char* enc_conv(const char* src,
				int src_len,
				unsigned int enc_from,
				unsigned int enc_to,
				int *len)
{
	int nlen;

	//������ ����������� ������ ������.
	nlen = 	MultiByteToWideChar(enc_from, 0, src, src_len, NULL, 0);

	wchar_t *wstr = new wchar_t[nlen];

	MultiByteToWideChar(
		enc_from,
		0,
		src,
		src_len,
		wstr,
		nlen);

	//������ ����������� ������ ������.
	int nlen2 = WideCharToMultiByte(enc_to, 0, wstr, nlen, NULL, 0, NULL, NULL);

	char *buf = new char[nlen2];

	WideCharToMultiByte(
		enc_to,
		0,
		wstr,
		nlen,
		buf,
		nlen2,
		NULL,
		NULL);

	delete [] wstr;

	*len = nlen2;

	return buf;
}

string enc_conv(const string& s, unsigned int enc_from, unsigned int enc_to)
{
	int len;
	char *buf = enc_conv(s.c_str(), (int) s.length(), enc_from, enc_to, &len);

	string res;

	int i;
	for(i = 0; i < len; ++i)
	{
		res += buf[i];
	}

	delete [] buf;

	return res;
}

wstring to_utf16(const string& s, unsigned int enc_from)
{
	int nlen;

	//������ ����������� ������ ������.
	nlen = 	MultiByteToWideChar(enc_from, 0, s.c_str(), -1, NULL, 0);

	wchar_t *buf = new wchar_t[nlen];

	MultiByteToWideChar(
		enc_from,
		0,
		s.c_str(),
		-1,
		buf,
		nlen);

	wstring ws = buf;
	delete [] buf;

	return ws;
}

string from_utf16(const wstring& ws, unsigned int enc_to)
{
	//������ ����������� ������ ������.
	int nlen = WideCharToMultiByte(enc_to, 0, ws.c_str(), -1, NULL, 0, NULL, NULL);

	char *buf = new char[nlen];

	WideCharToMultiByte(
		enc_to,
		0,
		ws.c_str(),
		-1,
		buf,
		nlen,
		NULL,
		NULL);

	string s = buf;
	delete [] buf;

	return s;
}

tstring to_winapi(const string& s, unsigned int enc_from)
{
#ifdef _UNICODE
	return to_utf16(s, enc_from);
#else
	return enc_conv(s, CP_UTF8, CP_THREAD_ACP);
#endif
}

string from_winapi(const tstring& ts, unsigned int enc_to)
{
#ifdef _UNICODE
	return from_utf16(ts, enc_to);
#else
	return enc_conv(ts, CP_THREAD_ACP, CP_UTF8);
#endif
}

#endif
