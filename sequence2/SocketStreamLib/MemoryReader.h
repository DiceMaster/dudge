#pragma once

class MemoryReader
{
public:
	MemoryReader(const void *buffer, int size);
	~MemoryReader();

	void ReadBytes(void *buf, int count);
	template<typename T> void Read(T &data)
	{
		ReadBytes(&data, sizeof(T));
	}
	void ReadString(char *str);

	void Seek(int position);

private:
	const void *mpBuffer;
	int mPosition;
	int mSize;

};
