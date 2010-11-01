#include "RegDialog.h"
#include "resource.h"

RegDialog::RegDialog(HWND hParent)
{
	mDialog = CreateDialog(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_REG), hParent, &RegProc);
	SetWindowLongPtr(mDialog, GWLP_USERDATA, (LONG_PTR)this);
}

RegDialog::~RegDialog()
{
}

int WINAPI RegDialog::RegProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	RegDialog *pDialog = (RegDialog *)GetWindowLongPtr(hWnd, GWLP_USERDATA);

	char str[256], tmp[256];
	switch (msg)
	{
	case WM_COMMAND:
		switch (LOWORD(wParam))
		{
		case IDC_PASREG:
			GetWindowText(GetDlgItem(hWnd, IDC_PAS), str, 256);
			if (str[0])
			{
				GetWindowText(GetDlgItem(hWnd, IDC_PAS2), tmp, 256);
				if (!lstrcmp(str, tmp))
				{
					pDialog->mPassword = str;

					GetWindowText(GetDlgItem(hWnd, IDC_NAME), str, 256);
					if (str[0])
					{
						pDialog->mLogin = str;
						pDialog->mbLoop = false;
						pDialog->mResult = true;
						return 0;
					}

					MessageBox(hWnd, "Отсутствует имя пользователя\nНельзя зарегистрироваться без имени!", "Ошибка!", MB_ICONSTOP);
				}
				else
				{
					SetWindowText(GetDlgItem(hWnd, IDC_PAS), "");
					SetWindowText(GetDlgItem(hWnd, IDC_PAS2), "");
					MessageBox(hWnd, "Неверно введён пароль\nПопробуйте ещё раз", "Ошибка!", MB_ICONSTOP);
				}
			}
			else
			{
				SetWindowText(GetDlgItem(hWnd, IDC_PAS2), "");
				MessageBox(hWnd, "Отсуттвует пароль\nНельзя зарегистрироваться без пароля!", "Ошибка!", MB_ICONSTOP);
			}
			return 0;
		case IDC_PASCANSEL:
			pDialog->mbLoop = false;
			pDialog->mResult = false;
			return 0;
		}
		break;
	case WM_CLOSE:
		pDialog->mbLoop = false;
		pDialog->mResult = false;
		return 0;
	case WM_INITDIALOG:
		return FALSE;
	case WM_NOTIFY:
		return 0;
	}
	return FALSE;
}

bool RegDialog::ShowModal()
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

std::string RegDialog::GetLogin()
{
	return mLogin;
}

std::string RegDialog::GetPassword()
{
	return mPassword;
}
