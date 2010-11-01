#include <algorithm>
#include "User.h"
#include "Task.h"

std::vector<User *> User::smUsers;
CRITICAL_SECTION User::smUserListAccessCriticalSection;
int User::smNextID = 1;

User::User(int id, const std::string &name, const std::string &password, PacketSocket *connection) :
	mID(id), mName(name), mPassword(password), mpConnection(connection), mTime(0), mSolved(0) 
{
	InitializeCriticalSection(&mLock);
	int nTasks = Task::GetTasksCount();
	for (int iTask = 0; iTask < nTasks; iTask++)
		mTasks.push_back(0);

	GenerateFolderName();
}

User::~User()
{
	DeleteCriticalSection(&mLock);
}

void User::GenerateFolderName()
{
	mFolder = "";
	char hex[8];
	size_t iFolderChar = 0;
	for (size_t iChar = 0; iChar < mName.size(); iChar++)
	{
		if (mName[iChar] > 0)
		{
			if ((mName[iChar] == '<') || (mName[iChar] == '>') || (mName[iChar] == ':') || (mName[iChar] == '\"') || 
				(mName[iChar] == '/') || (mName[iChar] == '\\') || (mName[iChar] == '|') || (mName[iChar] == '*') || (mName[iChar] == '?'))
				mFolder += '_';
			else
				mFolder += mName[iChar];
			iFolderChar++;
		}
		else
		{
			wsprintf(hex, "%02X", (unsigned char)mName[iChar]);
			mFolder += hex;
		}
	}
}

void User::Lock()
{
	EnterCriticalSection(&mLock);
}

void User::Unlock()
{
	LeaveCriticalSection(&mLock);
}

const std::string & User::GetName()
{
	return mName;
}

const std::string & User::GetFolder()
{
	return mFolder;
}

const std::string & User::GetPassword()
{
	return mPassword;
}

int User::GetTime()
{
	return mTime;
}

void User::SetTime( int time )
{
	mTime = time;
}

int User::GetSolved()
{
	return mSolved;
}

void User::SetSolved( int solved )
{
	mSolved = solved;
}

PacketSocket * User::GetConnection()
{
	return mpConnection;
}

void User::SetConnection( PacketSocket *pConnection )
{
	mpConnection = pConnection;
}

int User::GetID()
{
	return mID;
}

User * User::AddUser( const std::string &name, const std::string &password, PacketSocket *connection )
{
	EnterCriticalSection(&smUserListAccessCriticalSection);
	User *pNewUser = new User(smNextID++, name, password, connection);
	smUsers.push_back(pNewUser);
	LeaveCriticalSection(&smUserListAccessCriticalSection);
	return pNewUser;
}

User * User::FindUser( const std::string &name )
{
	EnterCriticalSection(&smUserListAccessCriticalSection);
	for (size_t iUser = 0; iUser < smUsers.size(); iUser++)
		if (smUsers[iUser]->GetName() == name)
		{
			User *foundUser = smUsers[iUser];
			LeaveCriticalSection(&smUserListAccessCriticalSection);
			return foundUser;
		}

	LeaveCriticalSection(&smUserListAccessCriticalSection);
	return NULL;
}

User * User::FindUser( PacketSocket *connection )
{
	EnterCriticalSection(&smUserListAccessCriticalSection);
	for (size_t iUser = 0; iUser < smUsers.size(); iUser++)
		if (smUsers[iUser]->GetConnection() == connection)
		{
			User *foundUser = smUsers[iUser];
			LeaveCriticalSection(&smUserListAccessCriticalSection);
			return foundUser;
		}

	LeaveCriticalSection(&smUserListAccessCriticalSection);
	return NULL;
}

void User::StaticInitialize()
{
	InitializeCriticalSection(&smUserListAccessCriticalSection);
}

void User::SortUsers()
{
	EnterCriticalSection(&smUserListAccessCriticalSection);
	std::sort(smUsers.begin(), smUsers.end(), &User::SortCompare);
	LeaveCriticalSection(&smUserListAccessCriticalSection);
}

bool User::SortCompare(User *user1, User *user2)
{
	if (user1->GetSolved() < user2->GetSolved())
		return false;
	else
		if (user1->GetSolved() > user2->GetSolved())
			return true;
		else
			return user1->GetTime() < user2->GetTime();
}

int User::EnumerateUsers()
{
	EnterCriticalSection(&smUserListAccessCriticalSection);
	return (int)smUsers.size();
}

User * User::GetUser( int iUser )
{
	if (iUser < 0 || iUser >= (int)smUsers.size())
	{
		return NULL;
	}
	User *user = smUsers[iUser];
	return user;
}

void User::EndEnumerateUsers()
{
	LeaveCriticalSection(&smUserListAccessCriticalSection);
}

int User::GetTask( int iTask )
{
	if (iTask < 0 || iTask >= Task::GetTasksCount())
		return 0;

	return mTasks[iTask];
}

void User::SetTask( int iTask, int task )
{
	if (iTask < 0 || iTask >= Task::GetTasksCount())
		return;

	mTasks[iTask] = task;
}
