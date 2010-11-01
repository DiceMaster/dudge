#pragma once

#include "../SocketStreamLib/PacketSocket.h"
#include <string>
#include <vector>

class User
{
public:
	void Lock();
	void Unlock();

	int GetID();
	const std::string &GetName();
	const std::string &GetFolder();
	const std::string &GetPassword();
	int GetTime();
	void SetTime(int time);
	int GetSolved();
	void SetSolved(int solved);
	PacketSocket *GetConnection();
	void SetConnection(PacketSocket *pConnection);
	int GetTask(int iTask);
	void SetTask(int iTask, int task);

private:
	User(int id, const std::string &name, const std::string &password, PacketSocket *connection);
	~User();

	void GenerateFolderName();

public:
	// статические функции
	static void StaticInitialize();

	static User *FindUser(PacketSocket *connection);
	static User *FindUser(const std::string &name);
	static User *AddUser(const std::string &name, const std::string &password, PacketSocket *connection);
	static void SortUsers();
	static bool SortCompare(User *user1, User *user2);
	static int EnumerateUsers();
	static User *GetUser(int iUser);
	static void EndEnumerateUsers();

private:
	// статические данные
	static std::vector<User *> smUsers;
	static int smNextID;
	static CRITICAL_SECTION smUserListAccessCriticalSection;

private:
	int mID;
	std::string mName;
	std::string mFolder;
	std::string mPassword;
	int mTime;
	int mSolved;
	std::vector<int> mTasks;
	PacketSocket *mpConnection;
	CRITICAL_SECTION mLock;

};
