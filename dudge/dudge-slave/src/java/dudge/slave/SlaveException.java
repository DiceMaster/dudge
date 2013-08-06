/*
 * SlaveException.java
 *
 * Created on June 2, 2007, 11:36 PM
 */
package dudge.slave;

/**
 *
 * @author Vladimir Shabanov
 */
public class SlaveException extends Exception {

	/**
	 * Creates a new instance of SlaveException
	 */
	public SlaveException(String message) {
		super(message);
	}

	public SlaveException(String message, Throwable cause) {
		super(message, cause);
	}
}
