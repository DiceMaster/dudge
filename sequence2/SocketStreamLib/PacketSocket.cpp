#include <memory.h>
#include "PacketSocket.h"

PacketSocket::PacketSocket(SOCKET sock, const sockaddr_in &address) : 
	mSock(sock),
	mpListener(NULL),
	mBufferLength(1024),
	mpBuffer((char *)HeapAlloc(GetProcessHeap(), 0, 1024)),
	mPackageSize(0),
	mReaded(0),
	mbOnHeader(true),
	mAddress(address)
{
	InitializeCriticalSection(&mLock);
}

PacketSocket::~PacketSocket()
{
	DeleteCriticalSection(&mLock);
	HeapFree(GetProcessHeap(), 0, mpBuffer);
}

sockaddr_in PacketSocket::GetAddress()
{
	return mAddress;
}

void PacketSocket::SetBlocking(bool blocking)
{
	unsigned long value = !blocking;
	ioctlsocket(mSock, FIONBIO, &value);
}

void PacketSocket::Send(void *pData, int length)
{
	int packetLength = length + sizeof(packetLength);
	EnterCriticalSection(&mLock);
	SendFunction(&packetLength, sizeof(packetLength));
	SendFunction(pData, length);
	LeaveCriticalSection(&mLock);
}

void PacketSocket::AssyncSend(void *pData, int length)
{
	void *dataCopy = HeapAlloc(GetProcessHeap(), 0, length);
	CopyMemory(dataCopy, pData, length);
	SendingData *sendingData = (SendingData *)HeapAlloc(GetProcessHeap(), 0, sizeof(SendingData));
	sendingData->mpData = dataCopy;
	sendingData->mpThis = this;
	sendingData->mSize = length;
	DWORD threadId;
	CreateThread(NULL, 0, &SendThreadProc, &sendingData, 0, &threadId);
}

void PacketSocket::SendFunction(void *pData, int length)
{
	int pos = 0;
	fd_set fdSet;
	fdSet.fd_count = 1;
	fdSet.fd_array[0] = mSock;
	
	do
	{
		int retval = send(mSock, (const char *)pData + pos, length - pos, 0);

		if (retval == SOCKET_ERROR)
		{
			int error = WSAGetLastError();
			if (error == WSAEWOULDBLOCK)
			{
				select(0, NULL, &fdSet, NULL, NULL); 
				continue;
			}

			// Обрабатывать ошибку
			return;
		}
		else
		{
			pos += retval;
		}
	}
	while (pos < length);

}

DWORD WINAPI PacketSocket::SendThreadProc(void *pData)
{
	SendingData *pSendingData = (SendingData *)pData;
	int packetLength = pSendingData->mSize + sizeof(packetLength);
	EnterCriticalSection(&pSendingData->mpThis->mLock);
	pSendingData->mpThis->SendFunction(&packetLength, sizeof(packetLength));
	pSendingData->mpThis->SendFunction(pSendingData->mpData, pSendingData->mSize);
	LeaveCriticalSection(&pSendingData->mpThis->mLock);
	
	HeapFree(GetProcessHeap(), 0, pSendingData->mpData);
	HeapFree(GetProcessHeap(), 0, pData);
	return 0;
}

void PacketSocket::SetPacketListener(IPacketListener *pListener)
{
	mpListener = pListener;
}

bool PacketSocket::ProcessConnection()
{
	int bytes = 0;
	bool firstReadAttempt = true;

	while (true)
	{
		ParseData();

		int result = recv(mSock, mpBuffer + mReaded, mBufferLength - mReaded, 0);

		if (result <= 0 || result == SOCKET_ERROR)
		{
			if (firstReadAttempt)
				return false;

			break;
		}

		mReaded += result;

		if (firstReadAttempt)
			firstReadAttempt = false;
	}

	return true;
}

void PacketSocket::ParseData()
{
	// Пока прочитали больше заголовка (длина пакета есть)
	while (mReaded >= sizeof(int))
	{
		if (mbOnHeader)
		{// Если пока еще на заголовке, то получаем длину пакета (первые 4 байта)
			mPackageSize = *((int *)mpBuffer);
			mbOnHeader = false;
		}
		else
		{
			if (mPackageSize == 0)
				return;

			if (mPackageSize > mBufferLength)
			{// Если места в буфере не хватает на все сообщение - то ресайзим его до нужного разера
				mpBuffer = (char *)HeapReAlloc(GetProcessHeap(), 0, mpBuffer, mPackageSize);
				mBufferLength = mPackageSize;
				break;
			}

			if (mReaded >= mPackageSize)
			{
				// Оповещаем владельца о пришедших данных
				mpListener->OnPacketReceived(this, mpBuffer + sizeof(int), mPackageSize - sizeof(int));

				mReaded -= mPackageSize;
				// Теперь урезаем начало буфера путем копирования в бругой буфер
				if (mReaded > 0)
					MoveMemory(mpBuffer, mpBuffer + mPackageSize, mReaded);

				mbOnHeader = true;
			}
			else
				break;
		}
	}
}

void PacketSocket::Close()
{
	shutdown(mSock, SD_BOTH);
	closesocket(mSock);
}
