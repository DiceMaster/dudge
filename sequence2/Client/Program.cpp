//#include <windows.h>
#include "ClientClass.h"

int WINAPI WinMain(HINSTANCE hInst, HINSTANCE, char *, int)
{
	ClientClass *client = new ClientClass();
	client->Run(hInst);
	delete client;

	return 0;
}
