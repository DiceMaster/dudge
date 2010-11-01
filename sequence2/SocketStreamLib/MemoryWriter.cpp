#include <malloc.h>
#include <string.h>
#include "MemoryWriter.h"

MemoryWriter::MemoryWriter(void) : mpBuffer(malloc(512)), mSize(512), mPosition(0)
{
}

MemoryWriter::~MemoryWriter(void)
{
	free(mpBuffer);
}


void MemoryWriter::WriteBytes(const void *buf, int count)
{
	if (mPosition + count > mSize)
	{
		while (mPosition + count > mSize)
			mSize *= 2;

		mpBuffer = realloc(mpBuffer, mSize);
	}

	memcpy((char *)mpBuffer + mPosition, buf, count);
	mPosition += count;
}

void MemoryWriter::WriteString(const char *str)
{
	int len = (int)strlen(str);
	Write(len);
	WriteBytes(str, len);
}

int MemoryWriter::GetSize()
{
	return mPosition;
}

void *MemoryWriter::GetData()
{
	return mpBuffer;
}
