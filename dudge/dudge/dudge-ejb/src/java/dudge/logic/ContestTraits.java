/*
 * ContestTraits.java
 *
 * Created on September 26, 2007, 6:28 PM
 */

package dudge.logic;

import dudge.db.Contest;
import java.io.Serializable;

/**
 * Класс характерных свойств соревнования, определяющих взаимодействие
 * с ним остальных частей системы.
 * @author Vladimir Shabanov
 */
public interface ContestTraits extends Serializable {

	/**
	 * Возвращает соревнование, с которым связаны данные свойства.
	 * @return соревнование.
	 */
	Contest getContest();
	
	/**
	 * Определяет, нужно ли для этого соревнования запускать все тесты.
	 * @return true если все тесты должны быть запущены, false - если
	 * до первого неудачного.
	 */
	boolean isRunAllTests();
	
	/**
	 * Возвращает суффикс страницы с монитором соревнования.
	 * @return строка-суффикс адреса страницы монитора (бес расширения).
	 */
	String getMonitorSuffix();
}
