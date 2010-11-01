#pragma once

#include <windows.h>
#include <string>

class RegDialog
{
public:
	RegDialog(HWND hParent);
	~RegDialog();

	bool ShowModal();

	std::string GetLogin();
	std::string GetPassword();

private:
	static int WINAPI RegProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
	bool mbLoop;
	HWND mDialog;
	std::string mLogin;
	std::string mPassword;
	bool mResult;

};
