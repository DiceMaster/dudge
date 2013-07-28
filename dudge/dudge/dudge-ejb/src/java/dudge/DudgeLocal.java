package dudge;

import dudge.db.Contest;
import dudge.db.Solution;
import dudge.monitor.AcmMonitorRecord;
import dudge.monitor.GlobalMonitorRecord;
import dudge.monitor.SchoolMonitorRecord;
import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Удаленный интерфейс DudgeBean'а, содержащий все методы для работы с ним по сети в пределах системы.
 */
@Local
public interface DudgeLocal {

	/**
	 * Присоединяется пользователем ко всем открытым соревнованиям, т.е. тем, в которых можно участвовать без отдельной подачи заявки.
	 *
	 * @param login имя пользователя
	 */
	public void joinAllOpenContests(String login);

	/**
	 * Отправляет решение на проверку.
	 *
	 * @param solution решение.
	 * @return добавленное решение.
	 */
	Solution submitSolution(Solution solution);

	/**
	 * Отправляет решение на перепроверку.
	 *
	 * @param solutionId идентификатор решения.
	 */
	public void resubmitSolution(int solutionId);

	/**
	 * Перепроверяет группу решений в соревновании для данной задачи.
	 *
	 * @param contestId идентификатор соревнования;
	 * @param problemId идентификатор задачи.
	 */
	public void resubmitSolutions(int contestId, int problemId);

	/**
	 * Возвращает минимальную длину пароля пользователя.
	 *
	 * @return Минимальная длина пароля.
	 */
	public int getMinimumPasswordLength();

	/**
	 * Возвращает максимальную длину пароля пользователя.
	 *
	 * @return Максимальная длина пароля.
	 */
	public int getMaximumPasswordLength();

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	public List<AcmMonitorRecord> getAcmMonitorRecords(Contest contest, Date when);

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	public List<GlobalMonitorRecord> getGlobalMonitorRecords(Contest contest, Date when);

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	public List<SchoolMonitorRecord> getSchoolMonitorRecords(Contest contest, Date when);

	/**
	 * Позволяет получить путь к странице багрепорта системы контроля версий.
	 *
	 * @return путь, который хранится в параметре bug_tracking_path таблицы params базы данных или путь по умолчанию, если такого параметра нет.
	 */
	public URI getBugTrackingPath();
}
