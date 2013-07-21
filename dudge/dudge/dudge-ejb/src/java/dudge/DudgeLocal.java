package dudge;

import dudge.db.Contest;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.RoleType;
import dudge.db.Solution;
import dudge.db.Test;
import dudge.db.User;
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

	public String calcHash(String password);

	/**
	 * Аутентифицирует пользователя по его имени и паролю. При успешной аутентификации ее состояние будет запомнено на всю последующую сессию.
	 *
	 * @param login имя пользователя.
	 * @param password пароль пользователя.
	 * @return true при успешной аутентификации, иначе false.
	 */
	boolean authenticate(String login, String password);

	/**
	 * Проверяет наличие у пользователя права в соревновании.
	 *
	 * @param login имя пользователя.
	 * @contestId идентификатор соревнования.
	 * @roleType роль пользователя в соревновании.
	 * @return true при наличии данного права у пользователя в этом соревновании, иначе false.
	 */
	boolean isInRole(String login, int contestId, RoleType roleType);

	/**
	 * Проверяет отсутствие у пользователя каких-либо ролей в соревновании.
	 *
	 * @param login имя пользователя.
	 * @contestId идентификатор соревнования.
	 * @return true при наличии у пользователя какой-либо роли в этом соревновании, иначе false.
	 */
	boolean haveNoRoles(String login, int contestId);

	/**
	 * Позволяет получить пользователя по его имени.
	 *
	 * @param login имя пользователя.
	 * @return информация о пользователе.
	 */
	User getUser(String login);

	/**
	 * Регистрирует нового пользователя в системе.
	 *
	 * @param login имя нового пользователя (то же, что и в параметре user).
	 * @param password пароль пользователя.
	 * @param e-mail пользователя.
	 * @return созданный пользователь.
	 */
	public User registerUser(String login, String password, String email);

	/**
	 * Присоединяется пользователем ко всем открытым соревнованиям, т.е. тем, в которых можно участвовать без отдельной подачи заявки.
	 *
	 * @param login имя пользователя
	 */
	public void joinAllOpenContests(String login);

	/**
	 * Модифицирует данные пользователя.
	 *
	 * @param user пользователь с измененными полями.
	 */
	void modifyUser(User user);

	/**
	 * Удаляет из системы выбранного пользователя.
	 *
	 * @param login Имя удаляемого пользователя.
	 */
	public void deleteUser(String login);

	/**
	 * Позволяет получить язык по его идентификатору.
	 *
	 * @param languageId строка-идентификатор языка.
	 * @return язык программирования.
	 */
	Language getLanguage(String languageId);

	/**
	 * Возвращает все языки, поддерживаемые системой.
	 *
	 * @return список поддерживаемых системой языков программирования.
	 */
	List<Language> getLanguages();

	/**
	 * Добавляет новый язык в систему.
	 *
	 * @param language язык для добавления.
	 * @return добавленный язык программирования.
	 */
	Language addLanguage(Language language);

	/**
	 * Изменяет существующий в системе язык.
	 *
	 * @param language язык программирования с измененными полями.
	 */
	void modifyLanguage(Language language);

	/**
	 * Удаляет из системы выбранный язык программирования.
	 *
	 * @param languageId
	 */
	public void deleteLanguage(String languageId);

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

	/**
	 * Позволяет получить задачу по ее идентификатору.
	 *
	 * @param problemId идентификатор задачи.
	 * @return задача.
	 */
	Problem getProblem(int problemId);

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
	 * Возвращает задачи в определенном промежутке.
	 *
	 * @param start начальный id задачи;
	 * @param limit количество задач;
	 * @return список из limit задач системы с id, начинающимся со start.
	 */
	List<Problem> getProblems(int start, int limit);

	/**
	 * Возвращает всех пользователей, зарегестрированных в системе.
	 *
	 * @return Список пользователей системы.
	 */
	List<User> getUsers();

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
	 * Возвращает список последних поступивших в систему решений.
	 *
	 * @param count сколько решений возвращать в списке.
	 * @return отсортированный по возрастанию давности список из последних count поступивших решений.
	 */
	List<Solution> getLastSolutions(int count);

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
	 * Изменяет данные решения.
	 *
	 * @param solution решение, данные которого нужно изменить.
	 */
	void modifySolution(Solution solution);

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

	public List<AcmMonitorRecord> getAcmMonitorRecords(Contest contest, Date when);

	public List<GlobalMonitorRecord> getGlobalMonitorRecords(Contest contest, Date when);

	public List<SchoolMonitorRecord> getSchoolMonitorRecords(Contest contest, Date when);

	/**
	 * Позволяет получить путь к странице багрепорта системы контроля версий.
	 *
	 * @return путь, который хранится в параметре bug_tracking_path таблицы params базы данных или путь по умолчанию, если такого параметра нет.
	 */
	public URI getBugTrackingPath();
}
