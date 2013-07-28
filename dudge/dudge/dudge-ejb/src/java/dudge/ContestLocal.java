package dudge;

import dudge.db.Contest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface ContestLocal {

	/**
	 * Возвращает соревнование по умолчанию.
	 *
	 * @return соревнование по умолчанию.
	 */
	Contest getDefaultContest();

	/**
	 * Позволяет получить соревнование по его идентификатору.
	 *
	 * @param contestId идентификатор соревнования.
	 * @return соревнование.
	 */
	Contest getContest(int contestId);

	/**
	 * Возвращает все присутствующие в системе соревнования.
	 *
	 * @return список всех соревнований.
	 */
	List<Contest> getContests();

	/**
	 * Возвращает соревнования на этапе регистраций.
	 *
	 * @return список соревнований на этапе регистраций.
	 */
	List<Contest> getPendingContests();

	/**
	 * Возвращает идущие в данный момент соревнования.
	 *
	 * @return список соревнований, идущих в данный момент.
	 */
	List<Contest> getActiveContests();

	/**
	 * Возвращает недавно законченные соревнования.
	 *
	 * @return список недавно законченных соревнований.
	 */
	List<Contest> getRecentlyFinishedContests();

	/**
	 * Добавляет новое соревнование в систему.
	 *
	 * @param contest Соревнование.
	 * @return добавленное соревнование.
	 */
	Contest addContest(Contest contest);

	/**
	 * Модифицирует соревнование.
	 *
	 * @param contest соревнование с измененными полями.
	 */
	void modifyContest(Contest contest);

	/**
	 * Удаляет из системы выбранное соревнование.
	 *
	 * @param contestId идектификатор удаляемого соревнования.
	 */
	public void deleteContest(int contestId);
}
