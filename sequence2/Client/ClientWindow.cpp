#include "ClientWindow.h"
#include "resource.h"
#include <CommCtrl.h>
#include <windowsx.h>
#include <stdio.h>
#include <io.h>

ClientWindow::ClientWindow(HINSTANCE hInst) : mInst(hInst), mpListener(NULL), numTasks(-1)
{
	InitCommonControls();

	mWindow = CreateDialog(hInst, MAKEINTRESOURCE(IDD_MAIN), NULL, &MessagesProc);
	SetWindowLongPtr(mWindow, GWLP_USERDATA, (LONG_PTR)this);

	Init();

	ShowWindow(mWindow, SW_SHOW);
}

ClientWindow::~ClientWindow()
{
}

HWND ClientWindow::GetHandle()
{
	return mWindow;
}

void ClientWindow::SetListener(IClientWindowListener *pListener)
{
	mpListener = pListener;
}

void ClientWindow::Init()
{
	mTab = GetDlgItem(mWindow, IDC_TAB);
	mSrc = GetDlgItem(mWindow, IDC_SRC);
	mList = GetDlgItem(mWindow, IDC_LIST);
	mExit = GetDlgItem(mWindow, IDC_EXIT);
	mOpen = GetDlgItem(mWindow, IDC_OPEN);
	mConnect = GetDlgItem(mWindow, IDC_CONNECT);
	mSend = GetDlgItem(mWindow, IDC_SEND);
	mTask = GetDlgItem(mWindow, IDC_TASK);
	mlTask = GetDlgItem(mWindow, IDC_LTASK);
	mLang = GetDlgItem(mWindow, IDC_LANG);
	mlLang = GetDlgItem(mWindow, IDC_LLANG);
	mlSrc = GetDlgItem(mWindow, IDC_LSRC);

	SetWindowLong(mlSrc, GWL_STYLE, GetWindowStyle(mlSrc) | SS_OWNERDRAW);
	SetWindowLong(mlTask, GWL_STYLE, GetWindowStyle(mlSrc) | SS_OWNERDRAW);
	SetWindowLong(mlLang, GWL_STYLE, GetWindowStyle(mlSrc) | SS_OWNERDRAW);

	TCITEM itm;
	itm.mask = TCIF_TEXT;
	itm.pszText = "Задания";
	SendMessage(mTab,  TCM_INSERTITEM,  0, (LPARAM)&itm);
	itm.pszText = "Рейтинг";
	SendMessage(mTab,  TCM_INSERTITEM,  1, (LPARAM)&itm);

	TabCtrl_SetCurSel(mTab, 0);
	ShowWindow(mList, SW_HIDE);
	ShowWindow(mSrc, SW_SHOW);
	ShowWindow(mExit, SW_SHOW);
	ShowWindow(mOpen, SW_SHOW);
	ShowWindow(mConnect, SW_SHOW);
	ShowWindow(mSend, SW_SHOW);
	ShowWindow(mTask, SW_SHOW);
	ShowWindow(mlTask, SW_SHOW);
	ShowWindow(mLang, SW_SHOW);
	ShowWindow(mlLang, SW_SHOW);
	ShowWindow(mlSrc, SW_SHOW);

	RECT RC;
	GetClientRect(mWindow, &RC);
	Resize(RC.right - RC.left, RC.bottom - RC.top);

	Edit_LimitText(mSrc, -1);
	ComboBox_AddString(mLang, "Pascal");
	ComboBox_AddString(mLang, "C++");
	ComboBox_AddString(mLang, "C#");
	ComboBox_SetCurSel(mLang, 0);

	DWORD style = GetWindowLong(mList, GWL_STYLE); 
	if ((style & LVS_TYPEMASK) != LVSIL_SMALL  ) 
		SetWindowLong(mList, GWL_STYLE, (style & ~LVS_TYPEMASK) | LVSIL_SMALL); 

	DWORD exStyle = ListView_GetExtendedListViewStyle(mList);
	exStyle |= LVS_EX_FULLROWSELECT;
	ListView_SetExtendedListViewStyle(mList, exStyle);

	//Добавляем колонки
	LV_COLUMN lvC;
	lvC.mask = LVCF_FMT | LVCF_WIDTH | LVCF_TEXT | LVCF_SUBITEM;
	lvC.fmt = LVCFMT_LEFT;
	lvC.cx = 55;
	lvC.pszText = "Решено";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 140;
	lvC.pszText = "Имя";
	ListView_InsertColumn(mList, 0, &lvC);
	lvC.cx = 50;
	lvC.pszText = "Место";
	ListView_InsertColumn(mList, 0, &lvC);
}

void ClientWindow::SetConnectedStatus(bool bConnected)
{
	EnableWindow(mConnect, !bConnected);
	if (!bConnected)
		SetLoginStatus( false );
}

void ClientWindow::SetLoginStatus( bool bLogined )
{
	EnableWindow(mSend, bLogined);
	EnableWindow(mTask, bLogined);
}

void ClientWindow::LoadFile()
{
	char path[1024];
	path[0] = '\0';
	OPENFILENAME ofn = {
		sizeof(OPENFILENAME), 
		mWindow, 
		NULL,
		"Pascal (*.pas)\0*.pas\0C++ (*.c;*.cpp;*.h;*.hpp)\0*.c;*.cpp;*.h;*.hpp\0Any file\0*\0\0", 
		NULL,
		0,
		1,
		path,
		MAX_PATH,
		NULL,
		MAX_PATH, 
		NULL,
		"Выбор файла", 
		OFN_FILEMUSTEXIST,
		0,
		1,
		NULL, 
		0,
		NULL,
		NULL 
	};

	if (GetOpenFileName(&ofn))
	{
		FILE *f = fopen(path, "rb");
		long fileLength = _filelength(_fileno(f));
		char *buf = new char[fileLength + 1];
		fread(buf, 1, fileLength, f);
		fclose(f);
		buf[fileLength] = '\0';
		SetWindowText(mSrc, buf);
		delete [] buf;
	}
}

int WINAPI ClientWindow::MessagesProc(HWND mWindow, UINT msg, WPARAM wParam, LPARAM lParam)
{
	ClientWindow *pWindow = (ClientWindow *)GetWindowLongPtr(mWindow, GWLP_USERDATA);

	switch (msg)
	{
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDC_SEND:
			if (pWindow->mpListener)
			{
				EnableWindow(pWindow->mSend, FALSE);

				int length = GetWindowTextLength(pWindow->mSrc);
				char *buf = new char[length + 1];
				GetWindowText(pWindow->mSrc, buf, length + 1);

				int iTask = ComboBox_GetCurSel(pWindow->mTask);
				int iLang = ComboBox_GetCurSel(pWindow->mLang);
				pWindow->mpListener->OnSendTask(buf, iTask, iLang);
				delete [] buf;
			}
			return 0;
		case IDC_CONNECT:
			if (pWindow->mpListener)
				pWindow->mpListener->OnConnect();
			return 0;
		case IDC_OPEN:
			pWindow->LoadFile();
			return 0;
		case IDC_EXIT:
			EndDialog(pWindow->mWindow, 0);
			PostQuitMessage(0);
			return 0;
		}
		break;

	case WM_DRAWITEM:
		{
			LPDRAWITEMSTRUCT pdi = (LPDRAWITEMSTRUCT)lParam;
			char str[256];
			GetWindowText(pdi->hwndItem, str, 256);

			SetBkMode(pdi->hDC, TRANSPARENT); 
			TextOut(pdi->hDC, 0, 0, str, lstrlen(str));
		}
		return TRUE;

	case WM_SIZE:
		pWindow->Resize(LOWORD(lParam), HIWORD(lParam));
		return 0;

	case WM_NOTIFY:
		{
		LPNMHDR hdr = (LPNMHDR)lParam;
		if (hdr->code == TCN_SELCHANGE)
		{
			int tab1Show, tab2Show;
			if (TabCtrl_GetCurSel(pWindow->mTab))
			{
				tab1Show = SW_HIDE;
				tab2Show = SW_SHOW;
			}
			else
			{
				tab2Show = SW_HIDE;
				tab1Show = SW_SHOW;
			}

			ShowWindow(pWindow->mList, tab2Show);
			ShowWindow(pWindow->mSrc, tab1Show);
			ShowWindow(pWindow->mExit, tab1Show);
			ShowWindow(pWindow->mOpen, tab1Show);
			ShowWindow(pWindow->mConnect, tab1Show);
			ShowWindow(pWindow->mSend, tab1Show);
			ShowWindow(pWindow->mTask, tab1Show);
			ShowWindow(pWindow->mlTask, tab1Show);
			ShowWindow(pWindow->mLang, tab1Show);
			ShowWindow(pWindow->mlLang, tab1Show);
			ShowWindow(pWindow->mlSrc, tab1Show);
		}
		}
		break;

	case WM_CLOSE:
		EndDialog(pWindow->mWindow, 0);
		PostQuitMessage(0);
		return 0;

	case WM_INITDIALOG:
		return FALSE;
	}

	return FALSE;
}

void ClientWindow::Resize(int w, int h)
{
	RECT RC;
	RECT TRC;
	RECT CRC;
	SetWindowPos(mTab, HWND_BOTTOM, 0, 0, w, h, SWP_NOZORDER);
	GetClientRect(mTab, &TRC);
	TabCtrl_AdjustRect(mTab, FALSE, &TRC);
	SetWindowPos(mList, HWND_TOP, TRC.left, TRC.top, TRC.right-TRC.left, TRC.bottom-TRC.top, 0);
	GetWindowRect(mExit, &RC);
	GetWindowRect(mLang, &CRC);

	SetWindowPos(mConnect,	HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 0*(RC.bottom - RC.top + 5) + 23, 0, 0, SWP_NOSIZE);
	SetWindowPos(mOpen,		HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 1*(RC.bottom - RC.top + 5) + 23, 0, 0, SWP_NOSIZE);
	SetWindowPos(mSend,		HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 2*(RC.bottom - RC.top + 5) + 2*(CRC.bottom - CRC.top) + 67, 0, 0, SWP_NOSIZE);
	SetWindowPos(mExit,		HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.bottom - (RC.bottom - RC.top + 5), 0, 0, SWP_NOSIZE);
	
	SetWindowPos(mlLang,	HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 2*(RC.bottom - RC.top + 5) + 23, 0, 0, SWP_NOSIZE);
	SetWindowPos(mLang,		HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 2*(RC.bottom - RC.top + 5) + 41, 0, 0, SWP_NOSIZE);
	SetWindowPos(mlTask,	HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 2*(RC.bottom - RC.top + 5) + CRC.bottom - CRC.top + 44, 0, 0, SWP_NOSIZE);
	SetWindowPos(mTask,		HWND_TOP, TRC.right - RC.right + RC.left - 5, TRC.top + 2*(RC.bottom - RC.top + 5) + CRC.bottom - CRC.top + 62, 0, 0, SWP_NOSIZE);

	SetWindowPos(mlSrc,		HWND_TOP, TRC.left + 5, TRC.top + 5, 0, 0, SWP_NOSIZE);
	SetWindowPos(mSrc,		HWND_TOP, TRC.left + 5, TRC.top + 23, TRC.right - TRC.left - RC.right + RC.left - 20, TRC.bottom - TRC.top - 28, 0);
}

void ClientWindow::SetTasks(int count, const char tasks[][512])
{
	char taskDesc[512];
	ComboBox_ResetContent(mTask);
	for (int iTask = 0; iTask < count; iTask++)
	{

		wsprintf(taskDesc, "%c - %s", 'A' + iTask, tasks[iTask]);
		ComboBox_AddString(mTask, taskDesc);
	}
	ComboBox_SetCurSel(mTask, 0);
}

void ClientWindow::Show()
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

void ClientWindow::SetMonitor( MemoryReader *pReader )
{
	int userCount, taskCount;
	pReader->Read(userCount);
	pReader->Read(taskCount);
	ListView_DeleteAllItems(mList);
	if (taskCount != numTasks)
	{
		while (ListView_DeleteColumn(mList, 3) == TRUE);
		char taskLetter[8];
		taskLetter[1] = '\0';
		LV_COLUMN lvC;
		lvC.mask = LVCF_FMT | LVCF_WIDTH | LVCF_TEXT | LVCF_SUBITEM;
		lvC.fmt = LVCFMT_LEFT;
		for (int iTask = 0; iTask < taskCount; iTask++)
		{
			lvC.cx = 45;
			taskLetter[0] = 'A' + iTask;
			lvC.pszText = taskLetter;
			ListView_InsertColumn(mList, 3 + iTask, &lvC);
		}
		lvC.cx = 120;
		lvC.pszText = "Штрафное время";
		ListView_InsertColumn(mList, 3 + taskCount, &lvC);
		numTasks = taskCount;
	}
	for (int iUser = 0; iUser < userCount; iUser++)
	{
		char userName[256];
		pReader->ReadString(userName);

		LVITEM lvI;
		lvI.mask = LVIF_TEXT | LVIF_STATE;
		lvI.state = 0;
		lvI.stateMask = 0;
		lvI.iItem = iUser;
		lvI.iSubItem = 0;
		char tempStr[256];
		int place;
		pReader->Read(place);
		wsprintf(tempStr, "%d", place + 1);
		lvI.pszText = tempStr;
		lvI.iItem = ListView_InsertItem(mList, &lvI);

		lvI.mask = LVIF_TEXT;
		lvI.iSubItem = 1;
		lvI.pszText = userName;
		ListView_SetItem(mList, &lvI);

		int solved;
		pReader->Read(solved);
		wsprintf(tempStr, "%d", solved);
		lvI.iSubItem = 2;
		lvI.pszText = tempStr;
		ListView_SetItem(mList, &lvI);

		for (int iTask = 0; iTask < taskCount; iTask++)
		{
			int task;
			pReader->Read(task);
			lvI.iSubItem = 3 + iTask;
			if (task > 0)
				wsprintf(tempStr, "+%d", task);
			else
				wsprintf(tempStr, "%d", task);
			lvI.pszText = tempStr;
			ListView_SetItem(mList, &lvI);
		}

		lvI.iSubItem = 3 + taskCount;
		int time;
		pReader->Read(time);
		wsprintf(tempStr, "%d", time);
		lvI.pszText = tempStr;
		ListView_SetItem(mList, &lvI);
	}
}

void ClientWindow::SetUserName( const char *name )
{
	char title[256];
	wsprintf(title, "Соревнование (%s)", name);
	SetWindowText(mWindow, title);
}

void ClientWindow::EnableSend()
{
	EnableWindow(mSend, TRUE);
}
