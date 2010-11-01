#include "SetPasswordDialog.h"
#include "resource.h"

SetPasswordDialog::SetPasswordDialog(HWND hParent)
{
	mDialog = CreateDialog(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_ENTPAS), hParent, &PasProc);
	SetWindowLongPtr(mDialog, GWLP_USERDATA, (LONG_PTR)this);
}

SetPasswordDialog::~SetPasswordDialog()
{

}

int WINAPI SetPasswordDialog::PasProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	SetPasswordDialog *pDialog = (SetPasswordDialog *)GetWindowLongPtr(hWnd, GWLP_USERDATA);

	char str1[256], str2[256];
	switch (msg)
	{
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDC_OK:
			GetWindowText(GetDlgItem(hWnd, IDC_P1), str1, 256);
			GetWindowText(GetDlgItem(hWnd, IDC_P2), str2, 256);
			if (!lstrcmp(str1, str2))
			{
				pDialog->mResult = true;
				pDialog->mbLoop = false;
				pDialog->mPassword = str1;
			}
			else
			{
				MessageBox(hWnd, "", "Îøèáêà!", MB_ICONSTOP);
				SetWindowText(GetDlgItem(hWnd, IDC_P1), "");
				SetWindowText(GetDlgItem(hWnd, IDC_P2), "");
			}
			return 0;
		case IDC_CANCEL:
			pDialog->mResult = false;
			pDialog->mbLoop = false;
			return 0;
		}
		break;
	case WM_INITDIALOG:
		return FALSE;
	}
	return FALSE;
}

bool SetPasswordDialog::ShowModal()
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

std::string SetPasswordDialog::GetPassword()
{
	return mPassword;
}