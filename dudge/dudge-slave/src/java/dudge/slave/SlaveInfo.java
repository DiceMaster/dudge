/*
 * SlaveInfo.java
 *
 * Created on May 7, 2007, 8:48 PM
 *
 */
package dudge.slave;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс с информацией о проверяющей части. Передается серверу системы при запросе очередной задачи на компиляцию и проверку.
 *
 * @author Vladimir Shabanov
 */
public class SlaveInfo implements Serializable {

	public static final long serialVersionUID = 1L;
	private List<String> supportedLanguages = new ArrayList();

	/**
	 * Creates a new instance of SlaveInfo
	 */
	public SlaveInfo(String[] languages) {
		supportedLanguages.addAll(Arrays.asList(languages));
	}

	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}
}
