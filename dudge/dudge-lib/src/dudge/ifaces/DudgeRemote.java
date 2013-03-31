
package dudge.ifaces;

import dudge.db.Solution;
import javax.ejb.Remote;

/**
 * Удаленный интерфейс DudgeBean'а, содержащий все методы
 * для работы с ним по сети в пределах системы.
 */
@Remote
public interface DudgeRemote {
	/**
	 * Позволяет получить решение по его идентификатору,
	 * при этом все поля решения и подобъектов будут инстанциированы
	 * и его можно без опаски сериализовать.
	 * @param solutionId идентификатор решения.
	 * @return решение в системе.
	 */
	Solution getSolutionEager(int solutionId);
	
	/**
	 * Сохраняет сделанные в решении изменения.
	 * @param solution измененное решение.
	 */
	void saveSolution(Solution solution);
}
