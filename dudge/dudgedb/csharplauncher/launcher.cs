using System;

public class DudgeProgram
{
public static int Main(string[] args)
{
	try
	{
		Program.Main(args);
	}
	catch(Exception)
	{
		return 1;
	}

	return 0;
}
}