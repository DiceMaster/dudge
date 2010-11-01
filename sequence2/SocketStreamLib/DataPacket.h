#pragma once

class DataPacket
{
public:
	DataPacket(void *pData, int size);
	DataPacket(const DataPacket &other);
	~DataPacket();

	void *GetData();
	int GetSize();

private:
	void *mpData;
	int mSize;
};
