package dudge.slave.dtest;

/**
 *  !!!Этот класс используется в JNI-части системы!!!
 *  Класс результата проверки одного теста.
 *  Объект этого класса возвращается
 *  JNI-методом динамической библиотеки.
 *  @author Vladimir Shabanov
 */
public class CheckingResult
{
	public int returnedValue;
	
	/**
	 * resultType - результат запуска решения.
	 * 0 - все нормально.
	 * 1 - превышен лимит CPU.
	 * 2 - превышен лимит RAM.
	 * 3 - превышен лимит вывода.
	 * 4 - runtime error.
	 * 5 - превышен лимит количества процессов.
	 *
	 * -1 - внутренняя ошибка.
	 */
	public byte resultType = -1;
	
	public long maxMemUsed = 0; // bytes
	public long cpuTimeUsed = 0; // milliseconds
	
	public CheckingResult()
	{
	}
}
