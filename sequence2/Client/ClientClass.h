#pragma once

#include <string>
#include "IClientWindowListener.h"
#include "../SocketStreamLib/IPacketListener.h"
#include "../SocketStreamLib/PacketSocket.h"
#include "../SocketStreamLib/MemoryReader.h"
#include "ClientWindow.h"

class ClientClass : public IPacketListener, public IClientWindowListener
{
public:
	ClientClass();
	~ClientClass();

	void Run(HINSTANCE hInst);

	void OnPacketReceived(PacketSocket *from, void *data, int size);
	void OnSendTask(const char *task, int iTask, int iLang);
	void OnConnect();

private:
	enum ConnectionStatus
	{
		csConnected,
		csConnecting,
		csDisconnected
	};

private:
	void Init();
	void Connect();
	bool FindServer(sockaddr_in &serverAddr);
	static unsigned WINAPI ConnectionThread(void *param);

	ConnectionStatus GetConnectedStatus();
	void SetConnectedStatus(ConnectionStatus status);

private:
	// Обработчики различных сетевых сообщений
	void OnMessage(PacketSocket *from, MemoryReader *data); 
	void OnTasks(PacketSocket *from, MemoryReader *data); 
	void OnMonitor(PacketSocket *from, MemoryReader *data); 

private:
	ClientWindow *mpWindow;
	HINSTANCE mInst;
	uintptr_t connectionThread;
	PacketSocket *mpConnection;
	ConnectionStatus mbConnected;
	CRITICAL_SECTION connectionStatusSection;
	bool mbSilentDisconnect;
	std::string mUserName;

};
