/*
 * CompilerHangedException.java
 *
 * Created on 14 Май 2007 г., 18:43
 */
package dudge.slave;

/**
 * Класс исключения, выкидывающегося классом SourceCompiler в случае, если компилятор повис при компиляции решения.
 *
 * @author Vladimir Shabanov
 */
public class CompilerHangedException extends Exception {

	/**
	 * Creates a new instance of CompilerHangedException
	 */
	public CompilerHangedException() {
		super();
	}
}
