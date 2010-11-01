#include "Task.h"

std::vector<Task *> Task::smTasks;

Task::Task( const std::string &name, DWORD timeout, const std::string &desc ) :
	mName(name), mTimeout(timeout), mDescription(desc)
{
}

Task::~Task()
{
}

const std::string & Task::GetName()
{
	return mName;
}

DWORD Task::GetTimeout()
{
	return mTimeout;
}

const std::string & Task::GetDescription()
{
	return mDescription;
}

void Task::LoadTasks( const char *path )
{
	char taskPath[MAX_PATH + 1], descriptionPath[MAX_PATH + 1];
	char taskName[1024], taskDesc[1024];

	lstrcpyn(taskPath, path, MAX_PATH);
	lstrcat(taskPath, "\\Tasks\\A");
	int pathLength = lstrlen(taskPath);

	smTasks.clear();
	int iTask = 0;
	while(GetFileAttributes(taskPath) != INVALID_FILE_ATTRIBUTES)
	{
		wsprintf(descriptionPath, "%s\\desc", taskPath);
		FILE *file;
		errno_t error = fopen_s(&file, descriptionPath, "r");
		if (error == 0)
		{
			fgets(taskName, 1024, file);
			if (taskName[lstrlen(taskName)-1] == '\n')
				taskName[lstrlen(taskName)-1] = '\0';
			int timeout;
			fscanf_s(file, "%d", &timeout);
			taskDesc[0] = '\0';
			fclose(file);

			smTasks.push_back(new Task(taskName, timeout, taskDesc));

			iTask++;
			taskPath[pathLength - 1] = 'A' + iTask;
		}
		else
			return;
	}
}

int Task::GetTasksCount()
{
	return (int)smTasks.size();
}

Task * Task::GetTask( int id )
{
	if (id < 0 || id >= (int)smTasks.size())
		return NULL;

	return smTasks[id];
}
