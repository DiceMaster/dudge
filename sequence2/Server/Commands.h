#pragma once

#define PORT_SERVER		12311
#define PORT_BROADCAST	12312

#define CMD_CONNECT	1
#define CMD_REG		2
#define CMD_TASK	3
#define CMD_MONITOR	4
#define CMD_MSG		5

enum EClientServerMessage
{
	eMessageAny,
	eMessageInfo,
	eMessageLogin,
	eMessageDisconnect,
	eMessageTask,
};

struct SResult
{
	char Name[256];
	int Tasks[100];
	int Place;
	int Time;
};

struct SSendTask
{
	int nTask;		//����� ������
	int Lang;		//����
	int NameLen;	//����� ����� ���������
	int PasLen;		//����� ������ ���������
	int bytes;		//����� ���������� ����������
};
