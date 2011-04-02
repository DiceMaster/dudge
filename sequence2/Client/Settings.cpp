#include "Settings.h"
#include <windows.h>

Settings* Settings::msInstance = NULL;

#define SETTINGS_FILE_PATH "settings.ini"

Settings *Settings::GetInstance()
{
	if (msInstance == NULL)
	{
		msInstance = new Settings();
	}

	return msInstance;
}

int Settings::GetTimeout()
{
	return mTimeout;
}

std::string Settings::GetServerAddr()
{
	return mServerAddr;
}

Settings::Settings() : mTimeout(0), mServerAddr("")
{
	ReadSettings();
}

void Settings::ReadSettings()
{
	char iniPath[MAX_PATH+1];
	int written = GetCurrentDirectory(MAX_PATH, iniPath);
	iniPath[written] = '\\';
	iniPath[written + 1] = '\0';
	strcat(iniPath, SETTINGS_FILE_PATH);
	mTimeout = GetPrivateProfileInt("connection", "timeout", 3, iniPath);
	char addrString[MAX_PATH+1];
	GetPrivateProfileString("connection", "addr", NULL, addrString, MAX_PATH, iniPath);
}