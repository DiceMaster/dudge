#pragma once

class MemoryWriter
{
public:
	MemoryWriter();
	~MemoryWriter();

	void WriteBytes(const void *buf, int count);
	template<typename T> void Write(const T &data)
	{
		WriteBytes(&data, sizeof(T));
	}
	void WriteString(const char *str);

	int GetSize();
	void *GetData();

private:
	void *mpBuffer;
	int mPosition;
	int mSize;

};
