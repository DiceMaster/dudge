#include "DataPacket.h"

DataPacket::DataPacket(void *pData, int size) : mpData(pData), mSize(size)
{
}

DataPacket::DataPacket(const DataPacket &other) : mpData(other.mpData), mSize(other.mSize)
{
}

DataPacket::~DataPacket()
{
}

void *DataPacket::GetData()
{
	return mpData;
}

int DataPacket::GetSize()
{
	return mSize;
}