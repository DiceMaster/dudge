#include "User.h"
#include "Task.h"
#include "MainWindow.h"
#include "SetPasswordDialog.h"
#include "CheckPasswordDialog.h"
#include "resource.h"
#include <CommCtrl.h>
#include <windowsx.h>

#define TYM_ICON	22333

MainWindow::MainWindow(HINSTANCE hInst) : mInst(hInst), mpListener(NULL)
{
	InitCommonControls();

	mWindow = CreateDialog(hInst, MAKEINTRESOURCE(IDD_MAIN), NULL, &MessagesProc);
	SetWindowLongPtr(mWindow, GWLP_USERDATA, (LONG_PTR)this);

	Init();

	ShowWindow(mWindow, SW_SHOW);
}

MainWindow::~MainWindow()
{
}

HWND MainWindow::GetHandle()
{
	return mWindow;
}

void MainWindow::SetListener(IServerWindowListener *pListener)
{
	mpListener = pListener;
}

void MainWindow::AddLog(const char *str)
{
	char *strCopy = new char[lstrlen(str) + 1];
	lstrcpy(strCopy, str);
	PostMessage(mWindow, WM_ADD_LOG, 0,(LPARAM)strCopy);
}

void MainWindow::Init()
{
	SetPasswordDialog *dialog = new SetPasswordDialog(mWindow);
	if (!dialog->ShowModal())
		ExitProcess(0);

	mPassword = dialog->GetPassword();
	delete dialog;

	mLog = GetDlgItem(mWindow, IDC_LOG);
	mTasks = GetDlgItem(mWindow, IDC_TASKS);
	mList = GetDlgItem(mWindow, IDC_USERS);
	mElapced = GetDlgItem(mWindow, IDC_TIME);
	mStart = GetDlgItem(mWindow, IDC_START);
	mRestore = GetDlgItem(mWindow, IDC_RESTORE);

	DWORD dwStyle = GetWindowLong(mList, GWL_STYLE); 
	if ((dwStyle & LVS_TYPEMASK) != LVSIL_SMALL)
		SetWindowLong(mList, GWL_STYLE, (dwStyle & ~LVS_TYPEMASK) | LVSIL_SMALL);

	DWORD exStyle = ListView_GetExtendedListViewStyle(mList);
	exStyle |= LVS_EX_FULLROWSELECT;
	ListView_SetExtendedListViewStyle(mList, exStyle);

	//Добавляем колонки
	LV_COLUMN lvC;
	lvC.mask = LVCF_FMT | LVCF_WIDTH | LVCF_TEXT | LVCF_SUBITEM;
	lvC.fmt = LVCFMT_LEFT;
	lvC.cx = 65;
	lvC.pszText = "Штраф";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 60;
	lvC.pszText = "Решено";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 140;
	lvC.pszText = "IP - Адрес";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 140;
	lvC.pszText = "Имя";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 50;
	lvC.pszText = "Место";
	ListView_InsertColumn(mList, 0, &lvC);
}

void MainWindow::EnableCompetition(bool bEnable)
{
	EnableWindow(mStart, !bEnable);
	EnableWindow(mRestore, !bEnable);
}

int WINAPI MainWindow::MessagesProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	MainWindow *pWindow = (MainWindow *)GetWindowLongPtr(hWnd, GWLP_USERDATA);
	return pWindow->ProcessMessage(msg, wParam, lParam);
}

int MainWindow::ProcessMessage(UINT msg, WPARAM wParam, LPARAM lParam)
{
	switch (msg)
	{
	case WM_TIMER:
		if (mpListener != NULL)
			mpListener->OnTimer();
		return 0;

	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDC_DEL:
			if (mpListener != NULL)
				mpListener->OnDel(ListView_GetSelectionMark(mList));
			return 0;

		case IDC_RESTORE:
			if (mpListener != NULL)
				mpListener->OnRestore();
			return 0;

		case IDC_START:
			if (mpListener != NULL)
				mpListener->OnStart();
			return 0;

		case IDC_EXIT:
			EndDialog(mWindow, 0);
			PostQuitMessage(0);
			return 0;
		}
		break;

	case TYM_ICON:
		if (lParam == WM_LBUTTONDOWN)
		{
			if (mPassword.size() > 0)
			{
				CheckPasswordDialog *dialog = new CheckPasswordDialog(mWindow, mPassword);
				if (dialog->ShowModal())
				{
					ShowWindow(mWindow, SW_SHOW);
					ShowWindow(mWindow, SW_RESTORE);
				}

				delete dialog;
			}
			else
			{
				ShowWindow(mWindow, SW_SHOW);
				ShowWindow(mWindow, SW_RESTORE);
			}
		}
		return 0;
			
	case WM_INITDIALOG:
			return FALSE;

	case WM_SIZE:
		if (wParam == SIZE_MINIMIZED)
		{
			NOTIFYICONDATA icd;
			icd.cbSize = sizeof(icd);
			icd.hWnd = mWindow;
			icd.uID = 0;
			icd.uFlags = NIF_MESSAGE | NIF_ICON | NIF_TIP; 
			icd.uCallbackMessage = TYM_ICON;
			icd.hIcon = LoadIcon(mInst, MAKEINTRESOURCE(IDI_TRAY));
			lstrcpy(icd.szTip, "Сервер проверки задач");

			Shell_NotifyIcon(NIM_ADD, &icd);

			DestroyIcon(icd.hIcon);

			ShowWindow(mWindow, SW_HIDE);
		}
		else
			if (wParam == SIZE_RESTORED)
			{
				NOTIFYICONDATA icd;
				icd.cbSize = sizeof(icd);
				icd.hWnd = mWindow;
				icd.uID = 0;
				icd.uFlags = 0; 
				Shell_NotifyIcon(NIM_DELETE, &icd);
			}
			return 0;
	case WM_ADD_LOG:
		_AddLog((char  *)lParam);
		return 0;

	case WM_REFRESH_USERS:
		_RefreshUserList();
		return 0;

	case WM_SET_ELAPSED:
		_SetElapsed((char  *)lParam);
		return 0;

	case WM_CLOSE:
		EndDialog(mWindow, 0);
		PostQuitMessage(0);
		return 0;
	}
	return FALSE;	
}

void MainWindow::Show()
{
	MSG msg;
	while(GetMessage(&msg, NULL, 0, 0))
	{
		if (!IsDialogMessage(mWindow, &msg))
		{
			TranslateMessage(&msg); 
			DispatchMessage(&msg); 
		}
	}
}

void MainWindow::RefreshUserList()
{
	PostMessage(mWindow, WM_REFRESH_USERS, 0, 0);
}

void MainWindow::SetElapsed( const char *elapsed )
{
	char *strCopy = new char[lstrlen(elapsed) + 1];
	lstrcpy(strCopy, elapsed);
	PostMessage(mWindow, WM_SET_ELAPSED, 0,(LPARAM)strCopy);
}

void MainWindow::StartTimer()
{
	SetTimer(mWindow, 1, 1000, NULL);
}

void MainWindow::UpdateTaskList()
{
	ListBox_ResetContent(mTasks);
	int taskCount = Task::GetTasksCount();
	for (int iTask = 0; iTask < taskCount; iTask++)
	{
		Task *pTask = Task::GetTask(iTask);
		ListBox_AddString(mTasks, pTask->GetName().c_str());
	}
}

void MainWindow::_AddLog( char *str )
{
	SendMessage(mLog, EM_REPLACESEL, FALSE,(LPARAM)str);
	delete []str;
}

void MainWindow::_RefreshUserList()
{
	ListView_DeleteAllItems(mList);

	int usersCount = User::EnumerateUsers();
	for (int iUser = 0; iUser < usersCount; iUser++)
	{
		User *user = User::GetUser(iUser);
		user->Lock();

		char tempStr[256];
		LVITEM lvI;
		lvI.mask = LVIF_TEXT | LVIF_STATE;
		lvI.state = 0;
		lvI.stateMask = 0;
		lvI.iItem = iUser;
		lvI.iSubItem = 0;
		wsprintf(tempStr, "%d", iUser + 1);
		lvI.pszText = tempStr;
		lvI.iItem = ListView_InsertItem(mList, &lvI);

		lvI.mask = LVIF_TEXT;
		lvI.iSubItem = 1;
		lvI.pszText = const_cast<char *>(user->GetName().c_str());
		ListView_SetItem(mList, &lvI);

		lvI.iSubItem = 2;
		if (user->GetConnection() == NULL)
			lvI.pszText = "";
		else
			lvI.pszText = inet_ntoa(user->GetConnection()->GetAddress().sin_addr);
		ListView_SetItem(mList, &lvI);

		lvI.iSubItem = 3;
		wsprintf(tempStr, "%d", user->GetSolved());
		lvI.pszText = tempStr;
		ListView_SetItem(mList, &lvI);

		lvI.iSubItem = 4;
		wsprintf(tempStr, "%d", user->GetTime());
		lvI.pszText = tempStr;
		ListView_SetItem(mList, &lvI);

		user->Unlock();
	}

	User::EndEnumerateUsers();
}

void MainWindow::_SetElapsed( const char *elapsed )
{
	SetWindowText(mElapced, elapsed);
	delete [] elapsed;
}