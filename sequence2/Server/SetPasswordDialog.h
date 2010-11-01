#pragma once

#include <windows.h>
#include <string>

class SetPasswordDialog
{
public:
	SetPasswordDialog(HWND hParent);
	~SetPasswordDialog();

	bool ShowModal();

	std::string GetPassword();

private:
	static int WINAPI PasProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
	bool mbLoop;
	HWND mDialog;
	std::string mPassword;
	bool mResult;

};
