#pragma once

#include <windows.h>
#include <string>
#include "IServerWindowListener.h"

#define WM_ADD_LOG			WM_APP + 1
#define WM_REFRESH_USERS	WM_APP + 2
#define WM_SET_ELAPSED		WM_APP + 3

class MainWindow
{
public:
	MainWindow(HINSTANCE hInst);
	~MainWindow();

public:
	void SetListener(IServerWindowListener *pListener);
	void EnableCompetition(bool bEnable);

	void StartTimer();
	void Show();
	HWND GetHandle();
	void UpdateTaskList();

	// Thread safe
	void AddLog(const char *str);
	void RefreshUserList();
	void SetElapsed(const char *elapsed);

private:
	void Init();
	static int WINAPI MessagesProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);
	static int WINAPI AdmProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

	int ProcessMessage(UINT msg, WPARAM wParam, LPARAM lParam);
	void _AddLog(char *str);
	void _RefreshUserList();
	void _SetElapsed(const char *elapsed);

private:
	IServerWindowListener *mpListener;

	RECT mRC;
	HWND mWindow;
	HWND mLog;
	HWND mTasks;
	HWND mList;
	HWND mElapced;
	HWND mStart;
	HWND mRestore;
	HINSTANCE mInst;
	std::string mPassword;

};
