#pragma once

#include <map>
#include "../SocketStreamLib/IPacketListener.h"
#include "../SocketStreamLib/PacketSocket.h"
#include "../SocketStreamLib/MemoryReader.h"
#include "MainWindow.h"
#include "Checker.h"
#include "Commands.h"

class Server : public IPacketListener, public IServerWindowListener, public ICheckerListener
{
public:
	Server();
	~Server();

	void Run(HINSTANCE hInst);

	void OnPacketReceived(PacketSocket *from, void *data, int size);

	void OnTimer();
	void OnStart();
	void OnRestore();
	void OnCheckResult(User *from, int iTask, ECheckResult result, int iTest, int error);
	void OnDel(int id);

private:
	void Init();
	void RegisterHandlers();

	static unsigned WINAPI BroadcastThread(void *param);
	static unsigned WINAPI NetworkThread(void *param);
	void SendMessage(PacketSocket *to, const char *message, EClientServerMessage messageType = eMessageInfo);
	void SendMonitor();
	void AddLog(const char *message);

	void Start();
	void MakeBackup();
	void RestoreBackup(const char *path);

private:
	// Обработчики различных сетевых сообщений
	void OnRegister(PacketSocket *from, MemoryReader *data); 
	void OnLogin(PacketSocket *from, MemoryReader *data);
	void OnTask(PacketSocket *from, MemoryReader *data);

private:
	MainWindow *mpWindow;
	Checker *mpChecker;
	uintptr_t mBrodcastThread;
	uintptr_t mNetworkThread;
	HINSTANCE mInst;
	char mWorkDirectory[MAX_PATH + 1];
	std::map<SOCKET, PacketSocket *> mConnections;
	DWORD mStartTime;
	CRITICAL_SECTION mLogLock;
	
	typedef void (Server::*MessageHandler)(PacketSocket *from, MemoryReader *data);
	std::map<int, MessageHandler> mMessageHandlers;
};
