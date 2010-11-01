#pragma once

class IClientWindowListener
{
public:
	virtual void OnSendTask(const char *task, int iTask, int iLang) = 0;
	virtual void OnConnect() = 0;

};
