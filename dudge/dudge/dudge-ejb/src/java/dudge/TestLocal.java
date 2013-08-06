package dudge;

import dudge.db.Test;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface TestLocal {

	/**
	 * Возвращает тест для задачи
	 *
	 * @param testId Заданный идентификатор теста.
	 * @return запрошенный тест.
	 */
	public Test getTest(int testId);

	/**
	 * Добавляет в систему новый тест для некоторой задачи.
	 *
	 * @param test новый тест для добавления.
	 * @return добавленный тест.
	 */
	public Test addTest(Test test);

	/**
	 * Изменяет заданный тест.
	 *
	 * @param test тест для модификации с измененными полями.
	 */
	public void modifyTest(Test test);

	/**
	 * Удаляет из системы выбранный тест.
	 *
	 * @param testId идентификатор удаляемого теста.
	 */
	public void deleteTest(int testId);
}
