#include "Server.h"
#include <windows.h>

int WINAPI WinMain(HINSTANCE hInst, HINSTANCE, LPSTR, int)
{
	Server *server = new Server();
	server->Run(hInst);
	delete server;

	return 0;
}
