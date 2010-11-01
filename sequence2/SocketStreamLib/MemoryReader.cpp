#include <memory.h>
#include "MemoryReader.h"

MemoryReader::MemoryReader(const void *buffer, int size) : mpBuffer(buffer), mSize(size), mPosition(0)
{
}

MemoryReader::~MemoryReader()
{
}

void MemoryReader::ReadBytes(void *buf, int count)
{
	if (mPosition + count > mSize)
		return;

	memcpy(buf, (const char *)mpBuffer + mPosition, count);
	mPosition += count;
}

void MemoryReader::ReadString(char *str)
{
	int size;
	Read(size);
	ReadBytes(str, size);
	str[size] = '\0';
}

void MemoryReader::Seek(int position)
{
	mPosition = position;
}
