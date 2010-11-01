#pragma once

#include <windows.h>
#include "IClientWindowListener.h"
#include "../SocketStreamLib/MemoryReader.h"


class ClientWindow
{
public:
	ClientWindow(HINSTANCE hInst);
	~ClientWindow();

	void Show();
	HWND GetHandle();
	void SetListener(IClientWindowListener *pListener);

	// thread safe
	void SetUserName(const char *name);
	void SetConnectedStatus(bool bConnected);
	void SetLoginStatus(bool bLogined);
	void SetTasks(int count, const char tasks[][512]);
	void SetMonitor(MemoryReader *pReader);
	void EnableSend();

private:
	void Init();
	void Resize(int w, int h);
	void LoadFile();

	static int WINAPI MessagesProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);

private:
	IClientWindowListener *mpListener;

	HWND mWindow;
	HINSTANCE mInst;
	int numTasks;

	HWND mMain;
	HWND mTab;
	HWND mSrc;
	HWND mList;
	HWND mExit;
	HWND mOpen;
	HWND mConnect;
	HWND mSend;
	HWND mTask;
	HWND mlTask;
	HWND mLang;
	HWND mlLang;
	HWND mlSrc;

};
