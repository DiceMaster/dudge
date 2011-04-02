#pragma once

#include <string>

class Settings
{
public:
	static Settings *GetInstance();
	int GetTimeout();
	std::string GetServerAddr();

private:
	Settings();

	void ReadSettings();

private:
	static Settings* msInstance;

	int mTimeout;
	std::string mServerAddr;

};
