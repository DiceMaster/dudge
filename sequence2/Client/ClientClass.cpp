#include <WinSock2.h>
#include <process.h>
#include "ClientClass.h"
#include "EnterDialog.h"
#include "RegDialog.h"
#include "../Server/Commands.h"
#include "../SocketStreamLib/PacketSocket.h"
#include "../SocketStreamLib/MemoryReader.h"
#include "../SocketStreamLib/MemoryWriter.h"


ClientClass::ClientClass() : mpWindow(NULL), mbConnected(csDisconnected)
{
	InitializeCriticalSection(&connectionStatusSection);
}

ClientClass::~ClientClass()
{
	DeleteCriticalSection(&connectionStatusSection);

	if (mpWindow)
		delete mpWindow;
}

void ClientClass::Run(HINSTANCE hInst)
{
	mInst = hInst;
	Init();

	mpWindow->Show();
}

void ClientClass::Init()
{
	mpWindow = new ClientWindow(mInst);
	mpWindow->SetListener(this);

	WSADATA wsad; 
	if (WSAStartup(MAKEWORD(2, 2), &wsad))
	{
		MessageBox(mpWindow->GetHandle(), "Ошибка инициализации сети", "Ошибка!", MB_ICONSTOP);
		ExitProcess(0);
	}

	Connect();
}

void ClientClass::Connect()
{
	if (GetConnectedStatus() != csDisconnected)
		return;

	SetConnectedStatus(csConnecting);

	connectionThread = _beginthreadex(NULL, 0, &ConnectionThread, this, 0, NULL);
}

unsigned WINAPI ClientClass::ConnectionThread(void *param)
{
	ClientClass *pClient = (ClientClass *)param;
	pClient->mbSilentDisconnect = false;

	SOCKET brSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (brSocket == INVALID_SOCKET)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1001)", "Ошибка!", MB_ICONERROR);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	BOOL bSOargument = TRUE;
	if (setsockopt(brSocket, SOL_SOCKET, SO_BROADCAST, (char *)&bSOargument, sizeof(BOOL)) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1002)", "Ошибка!", MB_ICONERROR);
		shutdown(brSocket, SD_BOTH);
		closesocket(brSocket);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	sockaddr_in clientAddr;
	clientAddr.sin_family = AF_INET;	
	clientAddr.sin_port = 0;
	clientAddr.sin_addr.s_addr = INADDR_ANY;

	if (bind(brSocket, (SOCKADDR *)&clientAddr, sizeof(sockaddr_in)) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1002)", "Ошибка!", MB_ICONERROR);
		shutdown(brSocket, SD_BOTH);
		closesocket(brSocket);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	sockaddr_in serverAddr;
	serverAddr.sin_family = AF_INET;
	serverAddr.sin_port = htons(PORT_BROADCAST);
	serverAddr.sin_addr.s_addr = INADDR_BROADCAST;

	DWORD buf = 1;
	if (sendto(brSocket, (char *)&buf, 4, 0, (SOCKADDR *)&serverAddr, sizeof(sockaddr_in)) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1003)", "Ошибка!", MB_ICONERROR);
		shutdown(brSocket, SD_BOTH);
		closesocket(brSocket);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	fd_set brSet;
	brSet.fd_count = 1;
	brSet.fd_array[0] = brSocket;
	TIMEVAL maxWaitTime;
	maxWaitTime.tv_sec = 3;
	maxWaitTime.tv_usec = 0;
	int selected = select(0, &brSet, NULL, NULL, &maxWaitTime);
	if (selected == 0)
	{
		shutdown(brSocket, SD_BOTH);
		closesocket(brSocket);
		MessageBox(pClient->mpWindow->GetHandle(), "Сервер не найден!", "Внимание!", MB_ICONEXCLAMATION);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	// Делать с таймаутом
	int len = sizeof(sockaddr_in);
	if (recvfrom(brSocket, (char *)&buf, 4, 0, (SOCKADDR *)&serverAddr, &len) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1004)", "Ошибка!", MB_ICONERROR);
		shutdown(brSocket, SD_BOTH);
		closesocket(brSocket);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	shutdown(brSocket, SD_BOTH);
	closesocket(brSocket);

	if (buf != 23)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1005)", "Ошибка!", MB_ICONERROR);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	SOCKET client = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (client == INVALID_SOCKET)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1006)", "Ошибка!", MB_ICONERROR);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	if (bind(client, (SOCKADDR *)&clientAddr, sizeof(sockaddr_in)) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1007)", "Ошибка!", MB_ICONERROR);
		shutdown(client, SD_BOTH);
		closesocket(client);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	serverAddr.sin_port = htons(PORT_SERVER);
	if (connect(client, (SOCKADDR *)&serverAddr, sizeof(sockaddr_in)) == SOCKET_ERROR)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Ошибка соединения (#1008)", "Ошибка!", MB_ICONERROR);
		char error[256];
		wsprintf(error, "%d", WSAGetLastError());
		MessageBox(pClient->mpWindow->GetHandle(), error, "Код", MB_ICONERROR);
		shutdown(client, SD_BOTH);
		closesocket(client);
		pClient->SetConnectedStatus(csDisconnected);
		return 0;
	}

	LINGER linger;
	int size = sizeof(linger);
	getsockopt(client, SOL_SOCKET, SO_LINGER, (char *)&linger, &size);
	pClient->mpConnection = new PacketSocket(client, serverAddr);
	pClient->mpConnection->SetPacketListener(pClient);

	pClient->SetConnectedStatus(csConnected);

	EnterDialog *pDialog = new EnterDialog(pClient->mpWindow->GetHandle());
	EnterDialog::EDialogResult result = pDialog->ShowModal();
	if (result == EnterDialog::enterDialogCancel)
	{
		pClient->mpConnection->Close();
		delete pClient->mpConnection;
		pClient->SetConnectedStatus(csDisconnected);
		delete pDialog;
		return 0;
	}

	MemoryWriter *writer = new MemoryWriter();
	int cmd;
	if (result == EnterDialog::enterDialogEnter)
		cmd = CMD_CONNECT;
	else
		cmd = CMD_REG;

	pClient->mUserName = pDialog->GetLogin();
	writer->Write(cmd);
	writer->WriteString(pDialog->GetLogin().c_str());
	writer->WriteString(pDialog->GetPassword().c_str());
	pClient->mpConnection->Send(writer->GetData(), writer->GetSize());

	delete writer;

	delete pDialog;

	pClient->mpConnection->ProcessConnection();

	pClient->mpConnection->Close();
	delete pClient->mpConnection;

	if (!pClient->mbSilentDisconnect)
	{
		MessageBox(pClient->mpWindow->GetHandle(), "Соединение с сервером разорвано!", "Внимание!", MB_ICONEXCLAMATION);
		pClient->mpWindow->SetUserName("нет участника");
	}

	pClient->SetConnectedStatus(csDisconnected);

	return 0;
}

ClientClass::ConnectionStatus ClientClass::GetConnectedStatus()
{
	EnterCriticalSection(&connectionStatusSection);
	ClientClass::ConnectionStatus status = mbConnected;
	LeaveCriticalSection(&connectionStatusSection);
	return status;
}

void ClientClass::SetConnectedStatus(ClientClass::ConnectionStatus status)
{
	EnterCriticalSection(&connectionStatusSection);
	mbConnected = status;
	mpWindow->SetConnectedStatus(status != csDisconnected);
	LeaveCriticalSection(&connectionStatusSection);
}

void ClientClass::OnPacketReceived(PacketSocket *from, void *data, int size)
{
	MemoryReader *reader = new MemoryReader(data, size);
	int command;
	reader->Read(command);
	switch (command)
	{
	case CMD_MSG:
		OnMessage(from, reader);
		break;
	case CMD_TASK:
		OnTasks(from, reader);
		break;
	case CMD_MONITOR:
		OnMonitor(from, reader);
		break;
	}

	delete reader;
}

void ClientClass::OnSendTask(const char *task, int iTask, int iLang)
{
	MemoryWriter writer;
	int command = CMD_TASK;
	writer.Write(command);
	writer.Write(iTask);
	writer.WriteString(task);
	writer.Write(iLang);

	mpConnection->Send(writer.GetData(), writer.GetSize());
}

void ClientClass::OnConnect()
{
	Connect();
}

void ClientClass::OnMessage( PacketSocket *from, MemoryReader *data )
{
	char message[1024];
	data->ReadString(message);
	EClientServerMessage messageType;
	data->Read(messageType);
	MessageBox(mpWindow->GetHandle(), message, "Сообщение от сервера", MB_ICONINFORMATION);
	switch (messageType)
	{
	case eMessageLogin:
		mpWindow->SetUserName(mUserName.c_str());
		break;
	case eMessageDisconnect:
		mpWindow->SetUserName("нет участника");
		mbSilentDisconnect = true;
		from->Close();
		break;
	case eMessageTask:
		mpWindow->EnableSend();
		break;
	}
}

void ClientClass::OnTasks( PacketSocket *from, MemoryReader *data )
{
	char tasks[32][512];
	int count;
	data->Read(count);
	for (int iTask = 0; iTask < count; iTask++)
	{
		data->ReadString(tasks[iTask]);
	}

	mpWindow->SetLoginStatus(true);
	mpWindow->SetTasks(count, tasks);
}

void ClientClass::OnMonitor( PacketSocket *from, MemoryReader *data )
{
	mpWindow->SetMonitor(data);
}