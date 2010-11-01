#pragma once

#include <windows.h>
#include <string>

class CheckPasswordDialog
{
public:
	CheckPasswordDialog(HWND hParent, const std::string &password);
	~CheckPasswordDialog();

	bool ShowModal();

private:
	static int WINAPI CheckProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
	bool mbLoop;
	HWND mDialog;
	std::string mPassword;
	bool mResult;

};
