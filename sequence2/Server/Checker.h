#pragma once

#include "User.h"
#include <string>
#include <queue>
#include <process.h>
#include "ICheckerListener.h"

class Checker
{
public:
	Checker(const char *pWorkDir);
	~Checker();
	void AddTask(User *from, int iTask, const char *text, int iLang);
	void SetListener(ICheckerListener *pListener);

private:
	struct STask
	{
		User *from;
		int iTask;
		std::string text;
		int iLang;

		STask() : from(0), iTask(-1), iLang(-1) {};
		STask(User *pFrom, int task, const char *pText, int lang) : 
		from(pFrom), iTask(task), text(pText), iLang(lang) {};
	};

	static unsigned WINAPI CheckThread(void *pData);
	void CheckTask(const STask &task);
	bool CheckTestOutput(const char *etalonPath, const char *resultPath);

	bool CompilePascal(const char *path);
	bool CompileCpp(const char *path);
	bool CompileCSharp(const char *path);
	int AnsiToMultiByte(const char *str, char *outStr, int outSize, UINT encoding);
	
private:
	ICheckerListener *mpListener;
	std::string workDirectory;
	CRITICAL_SECTION mQueueLock;
	HANDLE mNewTasksEvent;
	std::queue<STask> mTasks;

};
