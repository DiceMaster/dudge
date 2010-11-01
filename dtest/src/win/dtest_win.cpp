#include "../sysconf.h"
#ifdef WINDOWS

#include "../dtest.h"

#include <iostream>

#include "stdafx.h"
#include "winapi_ops.h"
#include "threads.h"

#include <AccCtrl.h>
#include <Aclapi.h>
#include <Sddl.h>

using namespace std;

bool is_debug = true;

#if 0
/*
* Mingw headers are incomplete, and so are the libraries. So we have to load
* a whole lot of API functions dynamically. Since we have to do this anyway,
* also load the couple of functions that *do* exist in minwg headers but not
* on NT4. That way, we don't break on NT4.
*/
typedef WINAPI BOOL (*__CreateRestrictedToken)(HANDLE, DWORD, DWORD, PSID_AND_ATTRIBUTES, DWORD, PLUID_AND_ATTRIBUTES, DWORD, PSID_AND_ATTRIBUTES, PHANDLE);
typedef WINAPI BOOL (*__IsProcessInJob)(HANDLE, HANDLE, PBOOL);
typedef WINAPI HANDLE (*__CreateJobObject)(LPSECURITY_ATTRIBUTES, LPCTSTR);
typedef WINAPI BOOL (*__SetInformationJobObject)(HANDLE, JOBOBJECTINFOCLASS, LPVOID, DWORD);
typedef WINAPI BOOL (*__AssignProcessToJobObject)(HANDLE, HANDLE);
typedef WINAPI BOOL (*__QueryInformationJobObject)(HANDLE, JOBOBJECTINFOCLASS, LPVOID, DWORD, LPDWORD);
/* Windows API define missing from MingW headers */
#define DISABLE_MAX_PRIVILEGE   0x1

HANDLE Kernel32Handle = NULL;
HANDLE Advapi32Handle = NULL;

bool init_missing_mingw_functions() {
	if(Kernel32Handle != NULL)
		return true;
	
	/* Functions loaded dynamically */
	__CreateRestrictedToken _CreateRestrictedToken = NULL;
	__IsProcessInJob _IsProcessInJob = NULL;
	__CreateJobObject _CreateJobObject = NULL;
	__SetInformationJobObject _SetInformationJobObject = NULL;
	__AssignProcessToJobObject _AssignProcessToJobObject = NULL;
	__QueryInformationJobObject _QueryInformationJobObject = NULL;
	
	//ZeroMemory(&si, sizeof(si));
	//si.cb = sizeof(si);
	
	Advapi32Handle = LoadLibrary("ADVAPI32.DLL");
	if (Advapi32Handle != NULL) {
		_CreateRestrictedToken = (__CreateRestrictedToken) GetProcAddress(Advapi32Handle, "CreateRestrictedToken");
	}
	
/*	if (_CreateRestrictedToken == NULL) {
		// NT4 doesn't have CreateRestrictedToken, so just call ordinary CreateProcess
		write_stderr("WARNING: Unable to create restricted tokens on this platform\n");
		if (Advapi32Handle != NULL)
			FreeLibrary(Advapi32Handle);
		return CreateProcess(NULL, cmd, NULL, NULL, FALSE, 0, NULL, NULL, &si, processInfo);
	}
	
	// Open the current token to use as a base for the restricted one
	if (!OpenProcessToken(GetCurrentProcess(), TOKEN_ALL_ACCESS, &origToken)) {
		write_stderr("Failed to open process token: %lu\n", GetLastError());
		return 0;
	}
	
	// Allocate list of SIDs to remove
	ZeroMemory(&dropSids, sizeof(dropSids));
	if (!AllocateAndInitializeSid(&NtAuthority, 2,
			SECURITY_BUILTIN_DOMAIN_RID, DOMAIN_ALIAS_RID_ADMINS, 0, 0, 0, 0, 0,
			0, &dropSids[0].Sid) ||
			!AllocateAndInitializeSid(&NtAuthority, 2,
			SECURITY_BUILTIN_DOMAIN_RID, DOMAIN_ALIAS_RID_POWER_USERS, 0, 0, 0, 0, 0,
			0, &dropSids[1].Sid)) {
		write_stderr("Failed to allocate SIDs: %lu\n", GetLastError());
		return 0;
	}
	
	b = _CreateRestrictedToken(origToken,
			DISABLE_MAX_PRIVILEGE,
			sizeof(dropSids)/sizeof(dropSids[0]),
			dropSids,
			0, NULL,
			0, NULL,
			&restrictedToken);
	
	FreeSid(dropSids[1].Sid);
	FreeSid(dropSids[0].Sid);
	CloseHandle(origToken);*/
	
	if (!b) {
		write_stderr("Failed to create restricted token: %lu\n", GetLastError());
		return 0;
	}
	
	//r = CreateProcessAsUser(restrictedToken, NULL, cmd, NULL, NULL, TRUE, CREATE_SUSPENDED, NULL, NULL, &si, processInfo);
	
	Kernel32Handle = LoadLibrary("KERNEL32.DLL");
	if (Kernel32Handle != NULL) {
		_IsProcessInJob = (__IsProcessInJob) GetProcAddress(Kernel32Handle, "IsProcessInJob");
		_CreateJobObject = (__CreateJobObject) GetProcAddress(Kernel32Handle, "CreateJobObjectA");
		_SetInformationJobObject = (__SetInformationJobObject) GetProcAddress(Kernel32Handle, "SetInformationJobObject");
		_AssignProcessToJobObject = (__AssignProcessToJobObject) GetProcAddress(Kernel32Handle, "AssignProcessToJobObject");
		_QueryInformationJobObject = (__QueryInformationJobObject) GetProcAddress(Kernel32Handle, "QueryInformationJobObject");
	}
	
	// Verify that we found all functions
	if (_IsProcessInJob == NULL || _CreateJobObject == NULL || _SetInformationJobObject == NULL || _AssignProcessToJobObject == NULL || _QueryInformationJobObject == NULL) {
		error_stream << _T("WARNING: Unable to locate all job object functions in system API!") << endl;
	}
}

void deinit_missing_mingw_functions()
{
	FreeLibrary(Advapi32Handle);
	FreeLibrary(Kernel32Handle);
}

#endif

BOOLEAN WINAPI DllMain(
	HINSTANCE hDllHandle, 
	DWORD nReason, 
    LPVOID Reserved
	)
{
	//  Perform global initialization.
	switch ( nReason )
	{
	case DLL_PROCESS_ATTACH:

		//  For optimization.
		DisableThreadLibraryCalls( hDllHandle );
		break;
	case DLL_PROCESS_DETACH:
		break;
	}

	return true;
}
//  end DllMain

bool dtest_init()
{
	return true;
}

string create_test_dir()
{
	TCHAR *temp_path = new TCHAR[MAX_PATH];

	tstring prop_tempdir = _T("");
	bool using_priv_drop = true;
	if(!using_priv_drop)
	{
		GetTempPath(MAX_PATH, temp_path);
	}
	else
	{
		GetWindowsDirectory(temp_path, MAX_PATH);
		_tcscat(temp_path, _T("\\Temp"));
	}

#ifdef _UNICODE
	_tcscat(temp_path, prop_tempdir.c_str());
	_tcscat(temp_path, _wtmpnam(NULL));
#else
	_tcscat(temp_path, to_winapi(prop_tempdir, CP_UTF8).c_str());
	_tcscat(temp_path, tmpnam(NULL));
#endif

	tstring test_dir = temp_path;
	delete [] temp_path;

	CreateDirectory(test_dir.c_str(), NULL);

	return from_winapi(test_dir, CP_UTF8);
}

bool delete_test_dir(const string& path)
{
	recur_rmdir( to_winapi(path, CP_UTF8) );
	return true;
}

#include <fstream>

checking_result check_solution(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output
	)
{
	std::stringstream ss;
	return check_solution(limits, command, test_dir_path, input_test, output, ss);
}

checking_result check_solution(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output,
	std::ostream& error_stream
	)
{
	return check_solution_as_user(limits, command, test_dir_path, input_test, output, error_stream, "", "", "");
}

checking_result check_solution_as_user(
	const run_limits& limits,
	const std::string& command,
	const std::string& test_dir_path,
	std::istream& input_test,
	std::ostream& output,
	std::ostream& error_stream,
	const std::string& username,
    const std::string& domain,
    const std::string& password
	)
{
	checking_result res;
	res.res_type = DTEST_INTERNAL_ERROR;
	res.ret_value = 0;
    
	SECURITY_ATTRIBUTES sa;

    bool use_privelege_drop = !username.empty();

#ifndef LEGACY_OS
	SECURITY_DESCRIPTOR sd;        //��������� security ��� ������

	InitializeSecurityDescriptor(&sd,SECURITY_DESCRIPTOR_REVISION);
	SetSecurityDescriptorDacl(&sd, true, NULL, false);
	sa.lpSecurityDescriptor = &sd;
#else
        use_privelege_drop = false;
	sa.lpSecurityDescriptor = NULL;
#endif
	sa.nLength = sizeof(SECURITY_ATTRIBUTES);
	sa.bInheritHandle = true;       //��������� ������������ ������������


	HANDLE sol_stdin, sol_stdout, read_stdout, write_stdin;

	//C������ ����� ��� stdin
	if (!CreatePipe(&sol_stdin, &write_stdin, &sa, pipe_buffer_size)) 
	{
		error_stream<< "DTEST ERROR: stdin pipe not created: " << get_last_error() << endl;
		return res;
	}
	sol_stdin = make_inheritable(sol_stdin);

	//C������ ����� ��� stdout
	if (!CreatePipe(&read_stdout, &sol_stdout, &sa, pipe_buffer_size)) 
	{
		error_stream<< "DTEST ERROR: stdout pipe not created: " << get_last_error() << endl;
		CloseHandle(sol_stdin);
		CloseHandle(write_stdin);
		return res;
	}
	sol_stdout = make_inheritable(sol_stdout);

#ifndef LEGACY_OS
	//������ JobObject
	HANDLE hJob = CreateJobObject(NULL, NULL);
	if (hJob == INVALID_HANDLE_VALUE)
	{
		error_stream << "DTEST ERROR: " << get_last_error() << endl;
		CloseHandle(sol_stdin);
		CloseHandle(sol_stdout);
		CloseHandle(read_stdout);
		CloseHandle(write_stdin);
		return res;
	}

	//������� Completion Port.
	HANDLE compp = CreateIoCompletionPort(
		INVALID_HANDLE_VALUE,
		NULL,
		(ULONG_PTR) NULL,
		1);
	//END: ������� Completion Port.

	//����������� ��������.
	JOBOBJECT_EXTENDED_LIMIT_INFORMATION job_lim;
	ZeroMemory(&job_lim, sizeof(job_lim));
	job_lim.BasicLimitInformation.LimitFlags =
		JOB_OBJECT_LIMIT_JOB_TIME |
		JOB_OBJECT_LIMIT_JOB_MEMORY |
		JOB_OBJECT_LIMIT_ACTIVE_PROCESS |
		JOB_OBJECT_LIMIT_PRIORITY_CLASS |
		JOB_OBJECT_LIMIT_SCHEDULING_CLASS |
		JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION;
	job_lim.BasicLimitInformation.PriorityClass = ABOVE_NORMAL_PRIORITY_CLASS;

	//������������� ����������� �� ������.
	job_lim.JobMemoryLimit = limits.mem_bytes;

	unsigned int cpu_msec = limits.cpu_msec;
	
	//��������� ����� �� ����������� � 100-������������� ����.
	job_lim.BasicLimitInformation.PerJobUserTimeLimit.QuadPart = 10000 * cpu_msec;

	// �����
	int timeout_msec = int( cpu_msec * (-0.012 * cpu_msec + 144) / 100 );

	//������������� ������������ ���������� ���������.
	job_lim.BasicLimitInformation.ActiveProcessLimit = limits.proc_num;
	//END: ����������� ��������.

	//���������� � completion port.
	JOBOBJECT_ASSOCIATE_COMPLETION_PORT job_compp;
	ZeroMemory(&job_compp, sizeof(job_compp));
	job_compp.CompletionKey = NULL;
	job_compp.CompletionPort = compp;
	//END: ���������� � completion port.

	//��������� ����������� �� UI.
	JOBOBJECT_BASIC_UI_RESTRICTIONS job_uires;
	ZeroMemory(&job_uires, sizeof(job_uires));
	job_uires.UIRestrictionsClass = 
		JOB_OBJECT_UILIMIT_DESKTOP |
		JOB_OBJECT_UILIMIT_DISPLAYSETTINGS |
		JOB_OBJECT_UILIMIT_EXITWINDOWS |
		JOB_OBJECT_UILIMIT_GLOBALATOMS |
		JOB_OBJECT_UILIMIT_HANDLES |
		JOB_OBJECT_UILIMIT_READCLIPBOARD |
		JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS |
		JOB_OBJECT_UILIMIT_WRITECLIPBOARD;
	//END: ��������� ����������� �� UI.

	//����������� ����.
	JOBOBJECT_SECURITY_LIMIT_INFORMATION job_sec;
	ZeroMemory(&job_sec, sizeof(job_sec));
	job_sec.SecurityLimitFlags =
		JOB_OBJECT_SECURITY_NO_ADMIN |
		JOB_OBJECT_SECURITY_RESTRICTED_TOKEN;

/*	if(using_priv_drop)
	{
		job_sec.SecurityLimitFlags |= JOB_OBJECT_SECURITY_ONLY_TOKEN;
		job_sec.JobToken = launcher->getUserToken();
	}*/
	//END: ����������� ����.
#endif

	//��������� ������
	STARTUPINFO si;
	GetStartupInfo(&si);
	si.cb = sizeof(si);
	si.lpReserved = NULL;
	si.lpDesktop = NULL;
	si.lpTitle = NULL;
	si.cbReserved2 = 0;
	si.lpReserved2 = NULL;
	si.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
	si.hStdOutput = sol_stdout;
	si.hStdError = sol_stdout;
	si.hStdInput = sol_stdin;
	si.wShowWindow = SW_HIDE;

	if(is_debug)
	{
		error_stream << "DLL: Testdir path = " << enc_conv(test_dir_path, CP_UTF8, CP_THREAD_ACP) << endl;
		error_stream << "DLL: Command = " << enc_conv(command, CP_UTF8, CP_THREAD_ACP) << endl;
	}

	tstring cmdStr = to_winapi(command, CP_UTF8).c_str();
	TCHAR *cmdBuf = new TCHAR[cmdStr.length() + 1];
	_tcscpy(cmdBuf, cmdStr.c_str());

	PROCESS_INFORMATION pinf;

	HANDLE hToken;
	
	if(!use_privelege_drop)
	{
		error_stream << "DTEST: Not using privelege dropping." << endl;
		if(!CreateProcess(
				NULL,
				cmdBuf,
				NULL,
				NULL,
				TRUE,
				CREATE_SUSPENDED | CREATE_NEW_CONSOLE | CREATE_NO_WINDOW
					#ifndef LEGACY_OS
					| CREATE_BREAKAWAY_FROM_JOB
					#endif
					,
				NULL,
				to_winapi(test_dir_path, CP_UTF8).c_str(),
				&si,
				&pinf))
		{
				error_stream<< "DTEST ERROR: Process not created: " << get_last_error() << endl;

				delete [] cmdBuf;

#ifndef LEGACY_OS
				CloseHandle(hJob);
				CloseHandle(compp);
#endif
				CloseHandle(sol_stdin);
				CloseHandle(sol_stdout);
				CloseHandle(read_stdout);
				CloseHandle(write_stdin);

				return res;
		}
	}
	#ifndef LEGACY_OS
	else {
		//Using privelege drop.
		error_stream << "DTEST: Using privelege dropping with username "
			<< enc_conv(username, CP_UTF8, CP_THREAD_ACP) << endl;

		// Create a SID for the BUILTIN\Administrators group.
/*		BYTE sidBuffer[256];
		PSID pAdminSID = (PSID)sidBuffer;
		SID_IDENTIFIER_AUTHORITY SIDAuth = SECURITY_NT_AUTHORITY;

		if (!AllocateAndInitializeSid( &SIDAuth, 2,
									SECURITY_BUILTIN_DOMAIN_RID ,
									DOMAIN_ALIAS_RID_ADMINS, 0, 0, 0, 0, 0, 0,
									&pAdminSID) ) {
			terror_stream<<TEXT("DTEST ERROR: AllocateAndInitializeSid() failed: ") << get_last_error() << endl;
			return res;
		}

		// Change the local administrator's SID to a deny-only SID.
		SID_AND_ATTRIBUTES SidToDisable[1];
		SidToDisable[0].Sid = pAdminSID;
		SidToDisable[0].Attributes = 0;

		// Get the current process token.
		HANDLE hOldToken = NULL;
		if (!OpenProcessToken(
			GetCurrentProcess(),
			TOKEN_ASSIGN_PRIMARY | TOKEN_DUPLICATE |
			TOKEN_QUERY | TOKEN_ADJUST_DEFAULT | TOKEN_ADJUST,
			&hOldToken))
		{
			error_stream<<TEXT("DTEST ERROR: OpenProcessToken() failed: ") << get_last_error() << endl;
			return res;
		}

		HANDLE hNewToken = NULL;
		if (!CreateRestrictedToken(hOldToken,
			DISABLE_MAX_PRIVILEGE,
			1, SidToDisable,
			0, NULL,
			0, NULL,
			&hNewToken))
		{
			error_stream<<TEXT("DTEST ERROR: CreateRestrictedToken() failed: ") << get_last_error() << endl;
			CloseHandle(hOldToken);
			if (pAdminSID) FreeSid(pAdminSID);
			return res;
		}

		if (pAdminSID) FreeSid(pAdminSID);*/

		/*wchar_t* usernameBuf = new wchar_t[to_utf16(username, CP_UTF8).length() + 1];
		wcscpy(usernameBuf, to_utf16(username, CP_UTF8).c_str());
		wchar_t* domainBuf = new wchar_t[to_utf16(domain, CP_UTF8).length() + 1];
		wcscpy(domainBuf, to_utf16(domain, CP_UTF8).c_str());
		wchar_t* passwordBuf = new wchar_t[to_utf16(password, CP_UTF8).length() + 1];
		wcscpy(passwordBuf, to_utf16(password, CP_UTF8).c_str());

		if(!LogonUserW(
				usernameBuf,
				domainBuf,
				passwordBuf,
				LOGON32_LOGON_INTERACTIVE,
				LOGON32_PROVIDER_DEFAULT,
				&hToken
				))
		{
			delete [] usernameBuf;
			delete [] domainBuf;
			delete [] passwordBuf;
			error_stream<<_T("DTEST ERROR: LogonUser failed: ") << get_last_error() << endl;
			return res;
		}

		delete [] usernameBuf;
		delete [] domainBuf;
		delete [] passwordBuf;
		
		---------------------------------------------------------------
		*/

		//Setting directory permissions.
		PACL pOldDACL = NULL;
		PSECURITY_DESCRIPTOR pSD = NULL;
		ZeroMemory(&pSD, sizeof(PSECURITY_DESCRIPTOR));

		wstring objectNameString = /*L"\\\\?\\" +*/ to_utf16(test_dir_path, CP_UTF8);

		DWORD error_code;

		if( ERROR_SUCCESS != (error_code = GetNamedSecurityInfo(
			const_cast<wchar_t*>(objectNameString.c_str()),
			SE_FILE_OBJECT,
            DACL_SECURITY_INFORMATION,
            NULL,
            NULL,
            &pOldDACL,
            NULL,
            &pSD)) )
		{
			error_stream << "DTEST ERROR: GetNamedSecurityInfo() failed: " << get_last_error(error_code) << endl;
			return res;
		}
		
		//----------------------------------------------------------
		/*
		SID *pSid;
		DWORD sidSize = 0;
		wchar_t *domainName;
		DWORD domainNameSize = 0;
		SID_NAME_USE sidNameUse;

		LookupAccountNameW(
					NULL,
					const_cast<wchar_t*>( to_winapi(username, CP_UTF8).c_str() ),
					NULL,
					&sidSize,
					NULL,
					&domainNameSize,
					&sidNameUse
					);
		pSid = (SID*) new byte[sidSize];
		domainName = new wchar_t[domainNameSize + 1];

		error_stream << "sidSize = " << sidSize << " domainSize = " << domainNameSize << endl;

		if(!LookupAccountNameW(
			NULL,
			const_cast<wchar_t*>( to_winapi(username, CP_UTF8).c_str() ),
			pSid,
			&sidSize,
			domainName,
			&domainNameSize,
			&sidNameUse
			))
		{
			error_stream << "DTEST ERROR: LookupAccountName() failed: " << get_last_error() << endl;
			return res;
		}

		wchar_t *sidString;
		if(!ConvertSidToStringSidW(&pSid, &sidString))
		{
			error_stream << "DTEST ERROR: ConvertSidToStringSid() failed: " << get_last_error() << endl;
			return res;
		}
		error_stream << "DTEST: SID is " << from_winapi(sidString, CP_UTF8) << endl;
		LocalFree(sidString);

		EXPLICIT_ACCESS ea;
		ZeroMemory(&ea, sizeof(EXPLICIT_ACCESS));
		ea.grfAccessPermissions = GENERIC_ALL;
		ea.grfAccessMode = SET_ACCESS;
		ea.grfInheritance= CONTAINER_INHERIT_ACE | OBJECT_INHERIT_ACE;
		ea.Trustee.TrusteeType = TRUSTEE_IS_USER;
		ea.Trustee.TrusteeForm = TRUSTEE_IS_SID;
		ea.Trustee.ptstrName = (wchar_t*) pSid;

		*/
		//----------------------------------------------------------
		
		EXPLICIT_ACCESS ea;
		wstring usernameWinAPI = to_utf16(username, CP_UTF8);
		wchar_t* usernameBuf = new wchar_t[usernameWinAPI.length() + 1];
		wcscpy(usernameBuf, usernameWinAPI.c_str());

		BuildExplicitAccessWithNameW(
			&ea,
			usernameBuf,
			GENERIC_ALL,
			SET_ACCESS,
			CONTAINER_INHERIT_ACE | OBJECT_INHERIT_ACE
			);
		ea.Trustee.TrusteeType = TRUSTEE_IS_USER;

		PACL pNewDACL = NULL;
		if( ERROR_SUCCESS != (error_code = SetEntriesInAcl(1, &ea, pOldDACL, &pNewDACL)))
		{
			delete [] usernameBuf;
			error_stream << "DTEST ERROR: SetEntriesInAcl() failed: " << get_last_error(error_code) << endl;
			if(pSD!=NULL) LocalFree((HLOCAL) pSD);
			return res;
		}

		if( ERROR_SUCCESS != (error_code = SetNamedSecurityInfoW(
			const_cast<wchar_t*>(objectNameString.c_str()),
			SE_FILE_OBJECT,
            DACL_SECURITY_INFORMATION,
            NULL,
            NULL,
            pNewDACL,
            NULL)) )
		{
			delete [] usernameBuf;
			error_stream << "DTEST ERROR: SetNamedSecurityInfo() failed: " << get_last_error(error_code) << endl;
			if(pSD!=NULL) LocalFree((HLOCAL) pSD);
			if(pNewDACL != NULL) LocalFree((HLOCAL) pNewDACL);
			return res;
		}

		delete [] usernameBuf;
		if(pSD!=NULL) LocalFree((HLOCAL) pSD);
		if(pNewDACL != NULL) LocalFree((HLOCAL) pNewDACL);
		
		//Creating process as user.
		if(!CreateProcessWithLogonW(
				to_utf16(username, CP_UTF8).c_str(),
				to_utf16(domain, CP_UTF8).c_str(),
				to_utf16(password, CP_UTF8).c_str(),
				0,
				NULL,
				cmdBuf,
				CREATE_SUSPENDED | CREATE_NEW_CONSOLE | CREATE_NO_WINDOW | CREATE_BREAKAWAY_FROM_JOB,
				NULL,
				to_winapi(test_dir_path, CP_UTF8).c_str(),
				&si,
				&pinf))
		{
				error_stream<< "DTEST ERROR: Process not created: " << get_last_error() << endl;

				delete [] cmdBuf;

				CloseHandle(hJob);
				CloseHandle(compp);

				CloseHandle(sol_stdin);
				CloseHandle(sol_stdout);
				CloseHandle(read_stdout);
				CloseHandle(write_stdin);

				return res;
		}
	}
	#else
	{
		return DTEST_INTERNAL_ERROR;
	}
	#endif

	delete [] cmdBuf;

	CloseHandle(sol_stdin);
	CloseHandle(sol_stdout);

#ifndef LEGACY_OS
	//��������� ������� � JobObject'��.
	if(!AssignProcessToJobObject(hJob, pinf.hProcess))
	{
		error_stream << "DTEST ERROR: AssignProcessToJobObject() failed: "
			<< get_last_error() << endl;
		TerminateProcess(pinf.hProcess, 255);
		CloseHandle(hJob);
		CloseHandle(read_stdout);
		CloseHandle(write_stdin);
		CloseHandle(compp);
		return res;
	}

	if (!SetInformationJobObject(
			hJob,
			JobObjectExtendedLimitInformation,
			&job_lim,
			sizeof(job_lim)
			))
	{
		// ������ SetInformationJobObject �� ������������
	}
	
	if (!SetInformationJobObject(
			hJob,
			JobObjectAssociateCompletionPortInformation,
			&job_compp,
			sizeof(job_compp)
			))
	{
		// ������ SetInformationJobObject �� ������������
	}

	if (!SetInformationJobObject(
			hJob,
			JobObjectBasicUIRestrictions,
			&job_uires,
			sizeof(job_uires)
			))
	{
		// ������ SetInformationJobObject �� ������������
	}

	if (!SetInformationJobObject(
			hJob,
			JobObjectSecurityLimitInformation,
			&job_sec,
			sizeof(job_sec)
			))
	{
		// ������ SetInformationJobObject �� ������������
	}
#endif

	input_thread_args_t *iparam = new input_thread_args_t;
	iparam->inp = &input_test;
	iparam->hWrite = write_stdin;
	//������� ����� ��� ������ ������� ������.
	HANDLE hInput = CreateThread(NULL, 0, &input_thread_func, iparam, 0, NULL);

	bool out_is_limit = false;

	output_thread_args_t *oparam = new output_thread_args_t;
	oparam->out = &output;
	oparam->hRead = read_stdout;
	oparam->hProcess = pinf.hProcess;
	oparam->output_limit = limits.out_bytes;
	oparam->p_is_limit = &out_is_limit;
	//������� ����� ��� ������ �������� ������
	HANDLE hOutput = CreateThread(NULL, 0, &output_thread_func, oparam, 0, NULL);

	unsigned char mresult = DTEST_PASSED;

	monitor_thread_args_t *mparam = new monitor_thread_args_t;
	mparam->hProcess = pinf.hProcess;
	mparam->presult = &mresult;
#ifndef LEGACY_OS
	mparam->hPort = compp;
	mparam->timeout_msec = timeout_msec;
#else
	mparam->timeout_msec = limits.cpu_msec;
#endif
	//������� ����� ���������� �� ��������� �������.
	HANDLE hMonitor = CreateThread(NULL, 0, &monitor_thread_func, mparam, 0, NULL);

	//�������� ����� �������
	DWORD dur_time = GetTickCount();

	//��������� ������� ���������
	ResumeThread(pinf.hThread);

	//���� ���������� �������� �������
	WaitForSingleObject(pinf.hProcess, INFINITE);
	
	//��������� ������������ � ������� �� �� �����.
	dur_time = GetTickCount() - dur_time;
	if(is_debug)
		error_stream << "DLL: Real process time: " << dur_time << " msec." << endl;

	WaitForSingleObject(hInput, INFINITE);
	WaitForSingleObject(hOutput, INFINITE);
	WaitForSingleObject(hMonitor, INFINITE);

	//�������� ��� ��������.
	DWORD ex_code;
	GetExitCodeProcess(pinf.hProcess, &ex_code);

	if(out_is_limit)
	{
		res.res_type = DTEST_OUTPUT_LIMIT;
	}
	else if((mresult == DTEST_PASSED) && (ex_code !=0))
	{
		res.res_type = DTEST_RUNTIME_ERROR;
		res.ret_value = ex_code;
	}
	else
	{
		res.res_type = mresult;
		res.ret_value = ex_code;
	}

#ifndef LEGACY_OS
	CloseHandle(hJob);
#endif
	CloseHandle(hOutput);
	CloseHandle(hInput);
	CloseHandle(pinf.hProcess);
	CloseHandle(pinf.hThread);
	
	return res;
}

#endif
