package dudge;

import dudge.db.Problem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface ProblemLocal {

	/**
	 * Позволяет получить задачу по ее идентификатору.
	 *
	 * @param problemId идентификатор задачи.
	 * @return задача.
	 */
	Problem getProblem(int problemId);

	/**
	 * Возвращает упорядоченный список задач.
	 *
	 * @param orderBy сортируемая колонка.
	 * @param descending обратный порядок сортировки.
	 * @return список задач.
	 */
	List<Problem> getProblems(String orderBy, boolean descending);
	
	/**
	 * @return общее количество задач в системе.
	 */
	long getProblemsCount();

	/**
	 * Возвращает все задачи, присутствующие в системе.
	 *
	 * @return Список задач в системе.
	 */
	List<Problem> getProblems();

	/**
	 * Добавляет новую задачу в систему.
	 *
	 * @param problem задача.
	 * @return добавленная задача.
	 */
	Problem addProblem(Problem problem);

	/**
	 * Модифицирует задачу в системе.
	 *
	 * @param problem задача для модификации с измененными полями.
	 */
	void modifyProblem(Problem problem);

	/**
	 * Удаляет из системы выбранную задачу.
	 *
	 * @param problemId идентификатор удаляемой задачи.
	 */
	public void deleteProblem(int problemId);
}
