package dudge;

import dudge.db.Language;
import dudge.db.Solution;
import dudge.db.Test;
import dudge.db.User;
import javax.ejb.Remote;

/**
 * Интерфейс для проверяльщиков прав доступа пользователя к системе. Объявляет методы класса PermissionCheckerBean, выполняющие проверку разрешенности
 * элементарных операций для конечного пользователя, по запросам удаленных клиентов.
 *
 * @see PermissionCheckerBean
 */
@Remote
public interface PermissionCheckerRemote {

	/**
	 * Проверяет право получения информации о пользователе.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user пользователь, данные которого надо модифицировать.
	 */
	boolean canGetUser(String principal, User user);

	/**
	 * Проверяет право модификации данных определенного пользователя.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Имя пользователь, данные которого надо модифицировать.
	 */
	boolean canModifyUser(String principal, String user);

	/**
	 * Проверяет право модификации привилегий (администратор, создатель соревнований) для данного пользователя.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user имя пользователя, данные которого надо модифицировать.
	 */
	boolean canDeepModifyUser(String principal, String user);

	/**
	 * Проверяет право на удаление пользователя из системы.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	boolean canDeleteUser(String principal);

	/**
	 * Проверяет право получения определенного языка программирования.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param language Язык, который запрашивается.
	 */
	boolean canGetLanguage(String principal, Language language);

	/**
	 * Проверяет право добавить в систему новый язык программирования.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	boolean canAddLanguage(String principal);

	/**
	 * Проверяет право модификации определенного языка программирования.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	boolean canModifyLanguage(String principal);

	/**
	 * Проверяет право пользователя удалить язык из системы.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @return true если пользователь может удалить язык, и false в противном случае.
	 */
	boolean canDeleteLanguage(String principal);

	/**
	 * Проверяет право присоединиться к выбранному соревнованию.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, монитор которого просматривается.
	 * @return true если пользователь может присоединиться, иначе false.
	 */
	boolean canJoinContest(String principal, int contestId);

	/**
	 * Проверяет право получить соревнование по его идентификатору.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, которое запрашивают.
	 */
	boolean canViewContest(String principal, int contestId);

	/**
	 * Проверяет право добавить новое соревнование некоторого типа в систему.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	boolean canAddContest(String principal);

	/**
	 * Проверяет право модифицировать данное соревнование соревнование.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, которое изменяют.
	 */
	boolean canModifyContest(String principal, int contestId);

	/**
	 * Проверяет право на удаление соревнования из системы.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId ID удаляемого соревнования.
	 */
	boolean canDeleteContest(String principal, int contestId);

	/**
	 * Проверяет право посмотреть задачу по ее идентификатору.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId Идентификатор задачи, которую запрашивают.
	 */
	boolean canViewProblem(String principal, int problemId);

	/**
	 * Проверяет право добавления новой задачу в систему.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	boolean canAddProblem(String principal);

	/**
	 * Проверяет право пользователя добавить задачу в свой контест.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор контеста, в который пытаются добавить задачу.
	 * @param problemId Идентификатор задачи, которую пользователь пытается добавить в свой контест.
	 * @return true если пользователь может использовать эту задачу в своеи контесте, иначе false.
	 */
	boolean canAddProblemToContest(String principal, int contestId, int problemId);

	/**
	 * Проверяет право модифицировать задачу в системе.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param problem идентификатор задачи, которую изменяют.
	 */
	boolean canModifyProblem(String principal, int problemId);

	/**
	 * Проверяет право удалять задачи из системы.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId ID удаляемой задачи.
	 */
	boolean canDeleteProblem(String principal, int problemId);

	/**
	 * Проверяет право просмотра решения.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param solutionId идентификатор запрашиваемого решения.
	 */
	boolean canViewSolution(String principal, int solutionId);

	/**
	 * Проверяет право на отправку решений в соревнование.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 */
	public boolean canSubmitSolution(String principal, int contestId);

	/**
	 * Проверяет право отправить решение на проверку.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 * @param problemId идентификатор задачи, решение которой отправляется.
	 */
	public boolean canSubmitSolution(String principal, int contestId, int problemId);

	/**
	 * Изменяет данные решения.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param solution решение, которое изменяют.
	 */
	boolean canModifySolution(String principal, Solution solution);

	/**
	 * Проверяет право получить тест для задачи
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который запрашивается.
	 */
	boolean canGetTest(String principal, Test test);

	/**
	 * Добавляет в систему новый тест для некоторой задачи.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который запрашивается.
	 */
	boolean canAddTest(String principal, Test test);

	/**
	 * Проверяет право изменить заданный тест.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который запрашивается.
	 */
	boolean canModifyTest(String principal, Test test);

	/**
	 * Проверяет право удалить данный тест.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который удаляется.
	 */
	boolean canDeleteTest(String principal, Test test);

	/**
	 * Проверяет право на просмотр монитора соревнования.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, монитор которого просматривается.
	 * @return true если пользователь может просмотреть монитор, иначе false.
	 */
	boolean canViewMonitor(String principal, int contestId);

	/**
	 * Проверяет право на отправку заявки на участие данным пользователем в данное соревнование.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, в которое заявка отправляется.
	 * @return true если пользователь может отправить заявку, иначе false.
	 */
	public boolean canSendApplication(String principal, int contestId);

	/* Проверяет право на просмотр конкретной новости.
	 * @principal пользователь, для которого проверяется право;
	 * @newsId идентификатор новости;
	 * @return true если пользователь может просматривать данную новость.
	 */
	public boolean canViewNews(String principal, int newsId);
}
