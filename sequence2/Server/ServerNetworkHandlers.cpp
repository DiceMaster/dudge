#include "Server.h"
#include "Task.h"
#include "User.h"
#include "../SocketStreamLib/MemoryWriter.h"

void Server::OnRegister(PacketSocket *from, MemoryReader *data)
{
	char name[512];
	char password[512];
	data->ReadString(name);
	data->ReadString(password);

	User *user = User::FindUser(name);
	if (user != NULL)
	{
		SendMessage(from, "Такой пользователь уже зарегистрирован!", eMessageDisconnect);
		return;
	}

	User *newUser = User::AddUser(name, password, from);
	User::SortUsers();
	mpWindow->RefreshUserList();

	std::string message = "Участник \"";
	message += name;
	message += "\" зарегистрировался в сисеме, адрес \"";
	message += inet_ntoa(from->GetAddress().sin_addr);
	message += "\"";
	AddLog(message.c_str());

	char userDirectory[MAX_PATH + 1];
	wsprintf(userDirectory, "%s\\Users\\%s", mWorkDirectory, newUser->GetFolder().c_str());
	CreateDirectory(userDirectory, NULL);
	SendMessage(from, "Сервер подтвердил соединение", eMessageLogin);

	MemoryWriter tasksMessageWriter;
	int command = CMD_TASK;
	tasksMessageWriter.Write(command);
	int taskCount = Task::GetTasksCount();
	tasksMessageWriter.Write(taskCount);
	for (int iTask = 0; iTask < taskCount; iTask++)
	{
		Task *pTask = Task::GetTask(iTask);
		tasksMessageWriter.WriteString(pTask->GetName().c_str());
	}
	from->Send(tasksMessageWriter.GetData(), tasksMessageWriter.GetSize());
}

void Server::OnLogin(PacketSocket *from, MemoryReader *data)
{
	char name[512];
	char password[512];
	data->ReadString(name);
	data->ReadString(password);

	User *user = User::FindUser(name);
	if (user == NULL)
	{
		SendMessage(from, "Сперва зарегистрируйтесь!", eMessageDisconnect);
		return;
	}

	user->Lock();
	if (user->GetPassword() != password)
	{
		SendMessage(from, "Неправильный пароль!", eMessageDisconnect);
		user->Unlock();
		return;
	}

	user->SetConnection(from);
	user->Unlock();

	std::string message = "Участник \"";
	message += name;
	message += "\" вошел в сисему, адрес \"";
	message += inet_ntoa(from->GetAddress().sin_addr);
	message += "\"";
	AddLog(message.c_str());

	SendMessage(from, "Сервер подтвердил соединение", eMessageLogin);

	MemoryWriter tasksMessageWriter;
	int command = CMD_TASK;
	tasksMessageWriter.Write(command);
	int taskCount = Task::GetTasksCount();
	tasksMessageWriter.Write(taskCount);
	for (int iTask = 0; iTask < taskCount; iTask++)
	{
		Task *pTask = Task::GetTask(iTask);
		tasksMessageWriter.WriteString(pTask->GetName().c_str());
	}
	from->Send(tasksMessageWriter.GetData(), tasksMessageWriter.GetSize());
}

void Server::OnTask(PacketSocket *from, MemoryReader *data)
{
	User *user = User::FindUser(from);
	if (user == NULL)
	{
		return;
	}

	int iTask, iLang;
	data->Read(iTask);
	user->Lock();
	if (user->GetTask(iTask) > 0)
	{
		SendMessage(from, "Задача уже сдана!", eMessageTask);
		user->Unlock();
		return;
	}

	int taskTextlength;
	data->Read(taskTextlength);
	char *pTaskText = new char[taskTextlength + 1];
	data->ReadBytes(pTaskText, taskTextlength);
	pTaskText[taskTextlength] = '\0';
	data->Read(iLang);
	user->Unlock();

	mpChecker->AddTask(user, iTask, pTaskText, iLang);

	delete [] pTaskText;
}

