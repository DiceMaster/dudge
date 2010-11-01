#pragma once

#include <windows.h>
#include <string>

class EnterDialog
{
public:
	enum EDialogResult
	{
		enterDialogCancel,
		enterDialogEnter,
		enterDialogRegister,
	};

public:
	EnterDialog(HWND hParent);
	~EnterDialog();

	EDialogResult ShowModal();
	std::string GetLogin();
	std::string GetPassword();

private:
	static int WINAPI EnterProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
	bool mbLoop;
	HWND mDialog;
	std::string mLogin;
	std::string mPassword;
	EDialogResult mResult;

};
