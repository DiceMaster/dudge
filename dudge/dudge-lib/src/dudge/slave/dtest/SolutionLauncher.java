/*
 * SolutionLauncher.java
 *
 * Created on 29 Октябрь 2005 г., 20:49
 */

package dudge.slave.dtest;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, содержащий методы для создания и удаления
 * временных папок, запуска решения и установки
 * ему ограничений.
 * Большинство методов этого класса реализовано
 * в платформозависимой тестирующей библиотеке dtest.
 * @author Vladimir Shabanov
 */
public class SolutionLauncher
{
	private final static Logger logger = Logger.getLogger(SolutionLauncher.class.toString());
	
	static {
		try
		{
			System.loadLibrary("dtest");
		}
		catch(UnsatisfiedLinkError ex)
		{
			logger.log(Level.SEVERE,
					"Failed to load library dtest/libdtest. java.library.path" + System.getProperty("java.library.path"),
					ex);

			throw ex;
		}
		
		if(!init())
			throw new RuntimeException("Library initialization failed.");
	}
	
	private int contextID;
	private Properties props;
	private String testDir;

	// Используется в нативной библиотеке.
	private byte[] buffer = new byte[64*1024];

	/**
	 * Создает новый экземпляр SolutionLauncher.
	 */
	public SolutionLauncher()
	{
		this(null);
	}
	
	/**
	 * Создает новый экземпляр SolutionLauncher.
	 * 
	 * @param properties настройки динамической библиотеки.
	 */
	public SolutionLauncher(Properties properties)
	{
		super();
		
		if(properties == null)
		{
			props = new Properties();
		}
		else
		{
			props = properties;
		}
		
		contextID = createContext();
		testDir = createTemporaryDirectory();
	}

	/**
	 * Освобождает ресурсы, занятые объектом этого класса.
	 */
	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			deleteTemporaryDirectory(testDir);
			freeContext(contextID);
		}
		finally
		{
			logger.finer("SolutionLauncher object is finalized.");
			super.finalize();
		}
	}
	
	/**
	 * Возвращает путь к созданной временной папке.
	 * @return путь ко временной папке.
	 */
	public String getTemporaryDirectory()
	{
		return testDir;
	}

	private static native boolean init();
	
	private static native int createContext();
	
	private static native void freeContext(int id);
	
	/**
	 * Запускает откомпилированное решение с ограничениями.
	 * @param limits ограничения на процессорное время, память и прочее.
	 * @param command команда запуска решения.
	 * @param ins входной тест.
	 * @param outs куда писать вывод.
	 * @param errorStream поток отладочных данных.
	 * @return результат проверки данного теста.
	 */
	public native CheckingResult checkSolution(
			CheckingLimits limits,
			String command,
			InputStream ins,
			OutputStream outs,
			Writer errorStream
			);
	
	/**
	 * Генерирует случайное платформеннозависимое имя
	 * временной директории, создает ее, устанавливает
	 * на нее правильные права (для возможности компиляции)
	 * и возвращает абсолютный путь к ней.
	 */
	private native String createTemporaryDirectory();
	
	/**
	 * Удаляет временную папку и все ее содержимое.
	 */
	private native boolean deleteTemporaryDirectory(String path);
}
