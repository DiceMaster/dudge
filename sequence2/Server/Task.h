#pragma once

#include <Windows.h>
#include <string>
#include <vector>

class Task
{
public:
	const std::string &GetName();
	DWORD GetTimeout();
	const std::string &GetDescription();

private:
	Task(const std::string &name, DWORD timeout, const std::string &desc);
	~Task();

public:
	// статические функции класса
	static void LoadTasks(const char *path);
	static int GetTasksCount();
	static Task *GetTask(int id);

private:
	// статические данные
	static std::vector<Task *> smTasks;

private:
	std::string mName;
	DWORD mTimeout;
	std::string mDescription;

};
