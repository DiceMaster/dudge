package dudge;

import dudge.db.Solution;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface SolutionLocal {

	/**
	 * Позволяет получить решение по его идентификатору.
	 *
	 * @param solutionId идентификатор решения.
	 * @return решение в системе.
	 */
	Solution getSolution(int solutionId);

	/**
	 * Возвращает список решений задачи, отправленных определенным пользователем в конкретное соревнование. При формировании списка учитывается время отправки
	 * решений, время начала соревнования, а также его длительность - так, что решения, отправленные после окончания или до начала соревнования, не попадут в
	 * результат.
	 *
	 * @param login имя пользователя-автора решений.
	 * @param contestId идентификатор соревнования, куда решения были отправлены.
	 * @param problemId идентификатор задачи, для которой были отправлены решения.
	 * @return отсортированный по убыванию давности (т.е. сначала старые) список решений.
	 */
	List<Solution> getSolutions(String login, int contestId, int problemId);

	/**
	 * Возвращает список решений, ожидающих проверки.
	 *
	 * @return решения, ожидающие проверки.
	 */
	List<Solution> getPendingSolutions();

	/**
	 * Возвращает список решений.
	 *
	 * @param start номер первого возвращяемого решения.
	 * @param count сколько решений возвращать.
	 * @return отсортированный по возрастанию давности список решений.
	 */
	List<Solution> getSolutions(int start, int count);
	
	/**
	 * Возвращает количество решений, отправленных в систему.
	 *
	 * @return Количество решений, отправленных в систему.
	 */
	long getSolutionsCount();
	
	/**
	 * Возвращает список последних поступивших в систему решений.
	 *
	 * @param count сколько решений возвращать в списке.
	 * @return отсортированный по возрастанию давности список из последних count поступивших решений.
	 */
	List<Solution> getLastSolutions(int count);

	/**
	 * Изменяет данные решения.
	 *
	 * @param solution решение, данные которого нужно изменить.
	 */
	void modifySolution(Solution solution);
}
