#pragma once

#include <winsock2.h>
#include "IPacketListener.h"

class PacketSocket
{
public:
	PacketSocket(SOCKET sock, const sockaddr_in &address);
	~PacketSocket();

	sockaddr_in GetAddress();

	void SetBlocking(bool blocking);

	void Send(void *pData, int length);
	void AssyncSend(void *pData, int length);

	void SetPacketListener(IPacketListener *pListener);

	bool ProcessConnection();

	void Close();
	
private:
	struct SendingData
	{
		PacketSocket *mpThis;
		void *mpData;
		int mSize;
	};

	static DWORD WINAPI SendThreadProc(void *pData);
	void SendFunction(void *pData, int length);
	void ParseData();

private:
	SOCKET mSock;
	IPacketListener *mpListener;
	sockaddr_in mAddress;

	CRITICAL_SECTION mLock;

	// Данные, необходимые для буферезации получаемой информации
	char *mpBuffer;
	int mBufferLength;
	int mPackageSize;
	int mReaded;
	bool mbOnHeader;
};
