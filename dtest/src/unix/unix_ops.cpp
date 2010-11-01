#include "../sysconf.h"
#ifdef UNIX

#include "unix_ops.h"

#include <errno.h>         /* for errno */
#include <stdio.h>         /* for NULL */
#include <stdlib.h>        /* for malloc() */
#include <string.h>        /* for string function */
#include <limits.h>        /* for PATH_MAX */
#include <sys/types.h>     /* for opendir(), readdir(), closedir(), stat() */
#include <sys/stat.h>      /* for [l]stat() */
#include <dirent.h>        /* for opendir(), readdir(), closedir() */
#include <unistd.h>        /* for rmdir(), unlink() */

#define SYSERR strerror(errno)

int recur_rmdir(const char *obj, char **errmsg)
{
	int ret_val = 0;       /* return value from this routine */
	DIR *dir;              /* pointer to a directory */
	struct dirent *dir_ent;          /* pointer to directory entries */
	char          dirobj[PATH_MAX];  /* object inside directory to modify */
	struct stat   statbuf;           /* used to hold stat information */
	static char   err_msg[1024];     /* error message */

	/* Determine the file type */
	if ( lstat(obj, &statbuf) < 0 )
	{
		if ( errmsg != NULL )
		{
			sprintf(err_msg, "lstat(%s) failed; errno=%d: %s",
					obj, errno, SYSERR);
			*errmsg = err_msg;
		}
		return -1;
	}
	
	/* Take appropriate action, depending on the file type */
	if ( (statbuf.st_mode & S_IFMT) == S_IFDIR )
	{
		/* object is a directory */
	
		/* Do NOT perform the request if the directory is "/" */
		if ( !strcmp(obj, "/") )
		{
			if ( errmsg != NULL )
			{
				sprintf(err_msg, "Cannot remove /");
				*errmsg = err_msg;
			}
			return -1;
		}
	
		/* Open the directory to get access to what is in it */
		if ( (dir = opendir(obj)) == NULL )
		{
			if ( rmdir(obj) != 0 )
			{
				if ( errmsg != NULL )
				{
				sprintf(err_msg, "rmdir(%s) failed; errno=%d: %s",
						obj, errno, SYSERR);
				*errmsg = err_msg;
				}
				return -1;
			}
			else
			{
				return 0;
			}
		}
	
		/* Loop through the entries in the directory, removing each one */
		for ( dir_ent = (struct dirent *)readdir(dir);
				dir_ent != NULL;
				dir_ent = (struct dirent *)readdir(dir))
		{
			/* Don't remove "." or ".." */
			if ( !strcmp(dir_ent->d_name, ".") || !strcmp(dir_ent->d_name, "..") )
				continue;
	
			/* Recursively call this routine to remove the current entry */
			sprintf(dirobj, "%s/%s", obj, dir_ent->d_name);
			if ( recur_rmdir(dirobj, errmsg) != 0 )
				ret_val = -1;
		}
	
		/* Close the directory */
		closedir(dir);
	
		/* If there were problems removing an entry, don't attempt to
			remove the directory itself */
		if ( ret_val == -1 )
			return -1;
	
		/* Get the link count, now that all the entries have been removed */
		if ( lstat(obj, &statbuf) < 0 )
		{
			if ( errmsg != NULL )
			{
				sprintf(err_msg, "lstat(%s) failed; errno=%d: %s",
						obj, errno, SYSERR);
				*errmsg = err_msg;
			}
			return -1;
		}
	
		/* Remove the directory itself */
		if ( statbuf.st_nlink >= 3 )
		{
			/* The directory is linked; unlink() must be used */
			if ( unlink(obj) < 0 )
			{
				if ( errmsg != NULL )
				{
				sprintf(err_msg, "unlink(%s) failed; errno=%d: %s",
						obj, errno, SYSERR);
				*errmsg = err_msg;
				}
				return -1;
			}
		}
		else
		{
			/* The directory is not linked; remove() can be used */
			if ( remove(obj) < 0 )
			{
				if ( errmsg != NULL )
				{
				sprintf(err_msg, "remove(%s) failed; errno=%d: %s",
						obj, errno, SYSERR);
				*errmsg = err_msg;
				}
				return -1;
			}
		}
	}
	else
	{
		/* object is not a directory; just use unlink() */
		if ( unlink(obj) < 0 )
		{
			if ( errmsg != NULL )
			{
				sprintf(err_msg, "unlink(%s) failed; errno=%d: %s",
						obj, errno, SYSERR);
				*errmsg = err_msg;
			}
			return -1;
		}
	}  /* if obj is a directory */
	
	/*
		* Everything must have went ok.
		*/
	return 0;
}

#endif
