#include <WinSock2.h>
#include <process.h>
#include <locale.h>
#include "Server.h"
#include "Task.h"
#include "User.h"
#include "../SocketStreamLib/MemoryReader.h"
#include "../SocketStreamLib/MemoryWriter.h"


Server::Server() : mpWindow(NULL), mpChecker(NULL)
{
	InitializeCriticalSection(&mLogLock);
}

Server::~Server()
{
	if (mpWindow)
		delete mpWindow;
}

void Server::Run(HINSTANCE hInst)
{
	mInst = hInst;
	Init();

	mpWindow->Show();
}

void Server::Init()
{
	setlocale( LC_ALL, ".ACP" );

	GetCurrentDirectory(MAX_PATH, mWorkDirectory);
	Task::LoadTasks(mWorkDirectory);
	User::StaticInitialize();
	RegisterHandlers();

	mpWindow = new MainWindow(mInst);
	mpWindow->SetListener(this);
	mpWindow->UpdateTaskList();

	mpChecker = new Checker(mWorkDirectory);
	mpChecker->SetListener(this);

	WSADATA wsad; 
	if (WSAStartup(MAKEWORD(2, 2), &wsad))
	{
		MessageBox(mpWindow->GetHandle(), "Ошибка инициализации сети", "Ошибка!", MB_ICONSTOP);
		ExitProcess(0);
	}
}

void Server::RegisterHandlers()
{
	mMessageHandlers[CMD_REG] = &Server::OnRegister;
	mMessageHandlers[CMD_CONNECT] = &Server::OnLogin;
	mMessageHandlers[CMD_TASK] = &Server::OnTask;
}

unsigned WINAPI Server::BroadcastThread(void *data)
{
	Server *pServer = (Server *)data;

	SOCKET brSocket;
	brSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (brSocket == INVALID_SOCKET)
		return 0;

	BOOL bSOargument = TRUE;
	if (setsockopt(brSocket, SOL_SOCKET, SO_BROADCAST, (char *)&bSOargument, sizeof(BOOL)) == SOCKET_ERROR)
		return 0;

	sockaddr_in sin;
	sin.sin_family = AF_INET;
	sin.sin_port = htons(PORT_BROADCAST);
	sin.sin_addr.s_addr = INADDR_ANY;

	if (bind(brSocket, (SOCKADDR *)&sin, sizeof(sockaddr_in)) == SOCKET_ERROR)
		return 0;

	DWORD buf;
	int len = sizeof(sockaddr_in);
	sockaddr_in from;
	while (true)
	{
		recvfrom(brSocket, (char *)&buf, 4, 0, (SOCKADDR *)&from, &len);
		buf = 23;
		sendto(brSocket, (char *)&buf, 4, 0, (SOCKADDR *)&from, len);
		len = sizeof(sockaddr_in);
	}

	return 0;
}

unsigned WINAPI Server::NetworkThread(void *data)
{
	Server *pServer = (Server *)data;

	SOCKET listenSocket;
	listenSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (listenSocket == INVALID_SOCKET)
		return 0;

	sockaddr_in sin;
	sin.sin_family = AF_INET;
	sin.sin_port = htons(PORT_SERVER);
	sin.sin_addr.s_addr = INADDR_ANY;

	if (bind(listenSocket, (SOCKADDR *)&sin, sizeof(sockaddr_in)) == SOCKET_ERROR)
		return 0;

	if (listen(listenSocket, 0) == SOCKET_ERROR)
		return 0;

	fd_set socketsSet;
	socketsSet.fd_count = 1;
	socketsSet.fd_array[0] = listenSocket;
	
	fd_set selectSet;
	while (true)
	{
		memcpy(&selectSet, &socketsSet, sizeof(fd_set));
		select(0, &selectSet, NULL, NULL, NULL);

		for (unsigned int iSocket = 0; iSocket < selectSet.fd_count; iSocket++)
		{
			if (selectSet.fd_array[iSocket] == listenSocket)
			{
				sockaddr_in sockaddr;
				int len = sizeof(sockaddr);
				SOCKET acceptedSocket = accept(listenSocket, (SOCKADDR *)&sockaddr, &len);

				socketsSet.fd_array[socketsSet.fd_count++] = acceptedSocket;

				PacketSocket *packetSocket = new PacketSocket(acceptedSocket, sockaddr);
				packetSocket->SetBlocking(false);
				packetSocket->SetPacketListener(pServer);
				pServer->mConnections[acceptedSocket] = packetSocket;

				continue;
			}

			SOCKET sock = selectSet.fd_array[iSocket];
			PacketSocket *packetSocket = pServer->mConnections[sock];
			if (!packetSocket->ProcessConnection())
			{
				User *user = User::FindUser(packetSocket);
				if (user != NULL)
				{
					user->Lock();
					user->SetConnection(NULL);
					user->Unlock();
				}

				packetSocket->Close();
				delete packetSocket;
				pServer->mConnections.erase(sock);
				
				u_int iRemove;
				for (iRemove = 0; iRemove < socketsSet.fd_count; iRemove++)
					if (socketsSet.fd_array[iRemove] == sock)
						break;

				for (; iRemove < socketsSet.fd_count - 1; iRemove++)
					socketsSet.fd_array[iRemove] = socketsSet.fd_array[iRemove + 1];

				socketsSet.fd_count--;
			}
		}
	}
	
	return 0;
}


void Server::SendMonitor()
{
	MemoryWriter writer;
	int taskCount = Task::GetTasksCount();
	int command = CMD_MONITOR;
	writer.Write(command);
	int usersCount = User::EnumerateUsers();
	writer.Write(usersCount);
	writer.Write(taskCount);
	for (int iUser = 0; iUser < usersCount; iUser++)
	{
		User *pUser = User::GetUser(iUser);
		pUser->Lock();
		writer.WriteString(pUser->GetName().c_str());
		writer.Write(iUser);
		int solved = pUser->GetSolved();
		writer.Write(solved);

		for (int iTask = 0; iTask < taskCount; iTask++)
		{
			int task = pUser->GetTask(iTask);
			writer.Write(task);
		}

		int time = pUser->GetTime();
		writer.Write(time);
		pUser->Unlock();
	}

	for (int iUser = 0; iUser < usersCount; iUser++)
	{
		User *pUser = User::GetUser(iUser);
		pUser->Lock();
		PacketSocket *pConnection = pUser->GetConnection();
		if (pConnection != NULL)
			pConnection->Send(writer.GetData(), writer.GetSize());

		pUser->Unlock();
	}

	User::EndEnumerateUsers();
}

void Server::OnPacketReceived(PacketSocket *from, void *data, int size)
{
	MemoryReader *reader = new MemoryReader(data, size);
	int command;
	reader->Read(command);
	MessageHandler handler = mMessageHandlers[command];
	if (handler != NULL)
		(this->*handler)(from, reader);

	delete reader;
}

void Server::OnTimer()
{
	char timeStr[256];
	SYSTEMTIME time;
	GetLocalTime(&time);
	DWORD CtTime = time.wHour * 3600 + time.wMinute * 60 + time.wSecond;
	CtTime -= mStartTime;
	wsprintf(timeStr, "Прошло времени: %02d:%02d:%02d", CtTime / 3600, (CtTime / 60) % 60, CtTime % 60);
	mpWindow->SetElapsed(timeStr);

	if ((CtTime % 5) == 0)
		SendMonitor();

	if ((CtTime % 30) == 0)
		MakeBackup();
}

void Server::OnStart()
{
	SYSTEMTIME time;
	GetLocalTime(&time);
	mStartTime = time.wHour * 3600 + time.wMinute * 60 + time.wSecond;

	Start();
}

void Server::Start()
{
	mpWindow->EnableCompetition(true);
	mpWindow->StartTimer();
	AddLog("Соревнование запущено");
	mBrodcastThread = _beginthreadex(NULL, 0, &BroadcastThread, this, 0, NULL);
	mNetworkThread = _beginthreadex(NULL, 0, &NetworkThread, this, 0, NULL);
}

void Server::OnRestore()
{
	char path[MAX_PATH + 1];
	path[0] = '\0';
	OPENFILENAME ofn = {
		sizeof(OPENFILENAME), 
		NULL, 
		NULL,
		"Backup (*.tmp)\0*.tmp\0Any file\0*\0\0", 
		NULL,
		0,
		1,
		path,
		MAX_PATH,
		NULL,
		MAX_PATH, 
		NULL,
		"Восстановление соревнования", 
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
		RestoreBackup(path);
	}
}

void Server::RestoreBackup( const char *path )
{
	FILE *backupFile;
	errno_t error = fopen_s(&backupFile, path, "r");
	if (error != 0)
	{
		AddLog("Не удается открыть файл с резервной информацией");
		return;
	}

	int taskCount, userCount;
	User *pUser;
	char password[512], name[512];
	fscanf(backupFile, "%d", &mStartTime);
	fscanf(backupFile, "%d", &userCount);
	fscanf(backupFile, "%d", &taskCount);
	if (taskCount != Task::GetTasksCount())
	{
		fclose(backupFile);
		AddLog("не совпадает количество задач");
		return;
	}

	for (int iUser = 0; iUser < userCount; iUser++)
	{
		fgetc(backupFile);
		fgets(name, 512, backupFile);
		if (name[lstrlen(name)-1] == '\n')
			name[lstrlen(name)-1] = '\0';

		fgets(password, 512, backupFile);
		if (password[lstrlen(password)-1] == '\n')
			password[lstrlen(password)-1] = '\0';

		pUser = User::AddUser(name, password, NULL);

		int solved, time;
		fscanf(backupFile, "%d", &solved);
		fscanf(backupFile, "%d", &time);
		pUser->SetSolved(solved);
		pUser->SetTime(time);

		int task;
		for (int iTask = 0; iTask < taskCount; iTask++)
		{
			fscanf(backupFile, "%d", &task);
			pUser->SetTask(iTask, task);
		}
		fgetc(backupFile);
	}

	fclose(backupFile);

	mpWindow->RefreshUserList();

	Start();
}

void Server::OnDel(int id)
{

}

void Server::SendMessage(PacketSocket *to, const char *message, EClientServerMessage messageType)
{
	MemoryWriter writer;
	int command = CMD_MSG;
	writer.Write(command);
	writer.WriteString(message);
	writer.Write(messageType);
	to->Send(writer.GetData(), writer.GetSize());
}

void Server::MakeBackup()
{
	char path[MAX_PATH + 1];
	wsprintf(path, "%s\\backup.tmp", mWorkDirectory);
	FILE *file;
	errno_t error = fopen_s(&file, path, "w");
	if (error == 0)
	{
		int usersCount = User::EnumerateUsers();
		fprintf(file, "%d\n", mStartTime);
		fprintf(file, "%d ", usersCount);
		int tasksCount = Task::GetTasksCount();
		fprintf(file, "%d\n", tasksCount);
		for (int  iUser = 0; iUser < usersCount; iUser++)
		{
			User *pUser = User::GetUser(iUser);
			pUser->Lock();

			fprintf(file, "%s\n", pUser->GetName().c_str());
			fprintf(file, "%s\n", pUser->GetPassword().c_str());
			fprintf(file, "%d ", pUser->GetSolved());
			fprintf(file, "%d ", pUser->GetTime());
			for (int iTask = 0; iTask < tasksCount; iTask++)
			{
				fprintf(file, "%d ", pUser->GetTask(iTask));
			}

			pUser->Unlock();
			fprintf(file, "\n");
		}

		User::EndEnumerateUsers();
		fclose(file);
	}
}

void Server::OnCheckResult(User *from, int iTask, ECheckResult result, int iTest, int error)
{
	std::string message;
	SYSTEMTIME time;
	DWORD currentTime;
	char testStr[32];

	switch (result)
	{
	case eCheckInternalError:
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nВозникла внутренняя ошибка в системе проверки";
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckOk:
		from->SetTask(iTask, -from->GetTask(iTask) + 1);
		GetLocalTime(&time);
		currentTime = time.wHour * 3600 + time.wMinute * 60 + time.wSecond;
		currentTime -= mStartTime;
		from->SetTime(from->GetTime() + currentTime / 60 + (from->GetTask(iTask) - 1)* 20);
		from->SetSolved(from->GetSolved() + 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" принята!";
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckCompileError:
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: ошибка компиляции";
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckWrongAnswer:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: неправильный ответ на тесте ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckTimeLimit:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: превышение времени на тесте ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckMemoryLimit:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: превышение лимита на объем используемой памяти. Тест ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckOutputLimit:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: превышение лимита на объем выводимых данных. Тест ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckProcessLimit:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: попытка запустить дополнительный процесс. Тест ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;
	case eCheckRuntimeError:
		from->SetTask(iTask, from->GetTask(iTask) - 1);
		message = "Задача \"";
		message += Task::GetTask(iTask)->GetName();
		message += "\" участника \"";
		message += from->GetName();
		message += "\" не принята\r\nПричина: ошибка времени выполнения на тесте ";
		wsprintf(testStr, "%d", iTest);
		message += testStr;
		if (from->GetConnection() != NULL)
			SendMessage(from->GetConnection(), message.c_str(), eMessageTask);
		AddLog(message.c_str());
		break;

	}
}

void Server::AddLog( const char *message )
{
	EnterCriticalSection(&mLogLock);

	char mesageWithTime[1024];
	SYSTEMTIME time;
	GetLocalTime(&time);
	wsprintf(mesageWithTime, "%02d:%02d:%02d - %s\r\n", time.wHour, time.wMinute, time.wSecond, message);
	mpWindow->AddLog(mesageWithTime);
	LeaveCriticalSection(&mLogLock);
}
