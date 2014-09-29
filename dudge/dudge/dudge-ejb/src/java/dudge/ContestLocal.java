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


	public interface FilteredContests {
		public long getFilteredTotal();
        public List<Contest> getFilteredContests();
    }
    
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
	 * Возвращает диапазон соревнований в соответствии с заданными критериями.
	 *
	 * @param searchCriteria строка поиска.
	 * @param orderBy сортируемая колонка.
	 * @param descending обратный порядок сортировки.
	 * @param start Начало диапазона.
	 * @param length Длина диапазона. 
	 * @return Диапазон соревнований системы.
	 */
	FilteredContests getContests(String searchCriteria, String orderBy, boolean descending, int start, int length);
        
        /**
	 * Возвращает количество соревнований, зарегестрированных в системе.
	 *
	 * @return Количество соревнований в системе.
	 */
	long getContestsCount();

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
	 * Возвращает глобальные соревнования.
	 *
	 * @return список глобальных соревнований.
	 */
	List<Contest> getGlobalContests();

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
