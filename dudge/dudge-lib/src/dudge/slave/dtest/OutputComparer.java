package dudge.slave.dtest;

import java.io.IOException;
import java.io.InputStream;

/**
 * Интерфейс сравнивателя вывода решения с эталонным выводом.
 *
 * @author Vladimir Shabanov
 */
public interface OutputComparer {

	/**
	 * Метод сравнения вывода решения с эталонным.
	 *
	 * @param referenceOutput эталонный вывод
	 * @param solutionOutput фактический вывод решения задачи
	 * @return true если фактический вывод эквивалентен эталонному выводу (т.е. если тест пройдет), иначе false
	 */
	boolean compare(InputStream referenceOutput, InputStream solutionOutput) throws IOException;
}
