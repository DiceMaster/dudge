#include "EnterDialog.h"
#include "RegDialog.h"
#include "resource.h"

EnterDialog::EnterDialog(HWND hParent)
{
	mDialog = CreateDialog(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_ENTER), hParent, &EnterProc);
	SetWindowLongPtr(mDialog, GWLP_USERDATA, (LONG_PTR)this);
}

EnterDialog::~EnterDialog()
{
}

int WINAPI EnterDialog::EnterProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	EnterDialog *pDialog = (EnterDialog *)GetWindowLongPtr(hWnd, GWLP_USERDATA);
	RegDialog *registerDialog;
	char str[256];
	switch (msg)
	{
		case WM_COMMAND:
			switch (LOWORD(wParam))
			{
				case IDC_ENTER:
					GetWindowText(GetDlgItem(hWnd, IDC_ENTNAME), str, 256);
					pDialog->mLogin = str;
					GetWindowText(GetDlgItem(hWnd, IDC_ENTPAS), str, 256);
					pDialog->mPassword = str;
					pDialog->mbLoop = false;
					pDialog->mResult = enterDialogEnter;
					return 0;
				case IDC_ENTREG:
					registerDialog = new RegDialog(hWnd);
					if (registerDialog->ShowModal())
					{
						pDialog->mLogin = registerDialog->GetLogin();
						pDialog->mPassword = registerDialog->GetPassword();
						pDialog->mbLoop = false;
						pDialog->mResult = enterDialogRegister;
					}
					return 0;
				case IDC_ENTCANSEL:
					pDialog->mbLoop = false;
					pDialog->mResult = enterDialogCancel;
					return 0;
			}
			break;
		case WM_CLOSE:
			pDialog->mbLoop = false;
			pDialog->mResult = enterDialogCancel;
			return 0;
		case WM_INITDIALOG:
			return FALSE;
	}
	return FALSE;
}

EnterDialog::EDialogResult EnterDialog::ShowModal()
{
	mbLoop = true;
	EnableWindow(GetParent(mDialog), FALSE);
	ShowWindow(mDialog, SW_SHOW);

	while (mbLoop)
	{
		MSG msg;
		GetMessage(&msg, NULL, 0, 0);
		if (!IsDialogMessage(mDialog, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	EnableWindow(GetParent(mDialog), TRUE);
	DestroyWindow(mDialog);

	return mResult;
}

std::string EnterDialog::GetLogin()
{
	return mLogin;
}

std::string EnterDialog::GetPassword()
{
	return mPassword;
}
