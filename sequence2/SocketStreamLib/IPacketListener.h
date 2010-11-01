#pragma once

class PacketSocket;

class IPacketListener
{
public:
	virtual void OnPacketReceived(PacketSocket *from, void *data, int size) = 0;
};
