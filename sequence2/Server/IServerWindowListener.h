#pragma once

class IServerWindowListener
{
public:
	virtual void OnTimer() = 0;
	virtual void OnStart() = 0;
	virtual void OnRestore() = 0;
	virtual void OnDel(int id) = 0;

};
