/*
 * ContestType.java
 *
 * Created on May 8, 2007, 8:05 PM
 */

package dudge.db;

/**
 * Определяет тип соревнования.
 * @author Vladimir Shabanov
 */
public enum ContestType {
	/**
	 * Глобальное соревнование - не требуется регистрация и нет монитора.
	 */
	GLOBAL,
	
	/**
	 * Олимпиада по программированию.
	 */
	ACM,
	
	/**
	 * Лабораторная работа по программированию.
	 */
	LAB,

	/**
	 * Школьное соревнование.
	 */
	SCHOOL
}
