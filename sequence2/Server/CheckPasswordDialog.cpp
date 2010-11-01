#include "CheckPasswordDialog.h"
#include "resource.h"

CheckPasswordDialog::CheckPasswordDialog(HWND hParent, const std::string &password)
{
	mPassword = password;
	mDialog = CreateDialog(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_ADMPAS), hParent, &CheckProc);
	SetWindowLongPtr(mDialog, GWLP_USERDATA, (LONG_PTR)this);
}

CheckPasswordDialog::~CheckPasswordDialog()
{

}

int WINAPI CheckPasswordDialog::CheckProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	CheckPasswordDialog *pDialog = (CheckPasswordDialog *)GetWindowLongPtr(hWnd, GWLP_USERDATA);

	char str[256];
	switch (msg)
	{
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDC_YES:
			GetWindowText(GetDlgItem(hWnd, IDC_ADMPAS), str, 256);
			SetWindowText(GetDlgItem(hWnd, IDC_ADMPAS), "");
			if (!lstrcmp(str, pDialog->mPassword.c_str()))
			{
				pDialog->mResult = true;
				pDialog->mbLoop = false;
			}
			return 0;
		case IDC_NOT:
			pDialog->mResult = false;
			pDialog->mbLoop = false;
			return 0;
		}
		break;
	case WM_INITDIALOG:
		SetFocus(GetDlgItem(hWnd, IDC_ADMPAS));
		return FALSE;
	}
	return FALSE;
}

bool CheckPasswordDialog::ShowModal()
{
	mbLoop = true;
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

	DestroyWindow(mDialog);

	return mResult;
}
