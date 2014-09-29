/*
 * PermissionCheckerBean.java
 */
package dudge;

import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.RoleType;
import dudge.db.Solution;
import dudge.db.Test;
import dudge.db.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Класс, осуществляющий проверку прав пользователей.
 *
 * @see PermissionCheckerRemote.
 *
 * @author Michael Antonov
 */
@Stateless
public class PermissionCheckerBean implements PermissionCheckerRemote {

	private static final Logger logger = Logger.getLogger(PermissionCheckerBean.class.toString());
	@EJB
	private ContestLocal contestBean;
	@EJB
	private ProblemLocal problemBean;
	@EJB
	private SolutionLocal solutionBean;
	@EJB
	private UserLocal userBean;
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	public void persist(Object object) {
		em.persist(object);
	}

	/**
	 * Creates a new instance of PermissionCheckerBean
	 */
	public PermissionCheckerBean() {
	}

	/**
	 * true, если пользователь аутентифицирован, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Пользователь, данные которого запрашиваются.
	 */
	@Override
	public boolean canGetUser(String principal, User user) {
		/*		if (principal == null)
		 return false;
		 User princ = dudgeBean.getUser(principal);
		 if(princ == null) {
		 logger.warning("Nonexistent user " + principal);
		 return false;
		 }*/

		return true;
	}

        /**
	 * true, если пользователь - администратор, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Пользователь, данные которого запрашиваются.
	 */
        @Override
	public boolean canViewUsersList(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь пытается редактировать свой профиль или является администратором системы, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Имя пользователя, данные которого надо модифицировать.
	 */
	@Override
	public boolean canModifyUser(String principal, String user) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь пытается редактировать свой профиль или является Администратором Системы
		if (principal.equals(user) || princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь является администратором, иначе false.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param user имя пользователя, данные которого надо модифицировать.
	 */
	@Override
	public boolean canDeepModifyUser(String principal, String user) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор , иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Удаляемый пользователь.
	 */
	@Override
	public boolean canDeleteUser(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь аутентифицирован, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param language Язык, который запрашивается.
	 */
	@Override
	public boolean canGetLanguage(String principal, Language language) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		return true;
	}

	/**
	 * true, если пользователь администратор, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	@Override
	public boolean canAddLanguage(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор системы, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param language Язык, который модифицируется.
	 */
	@Override
	public boolean canModifyLanguage(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 *
	 * @param principal
	 * @return
	 */
	@Override
	public boolean canDeleteLanguage(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, является ли пользователь Администратором Системы
		if (princ.isAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * Проверяет право на присоединение к соревнованию.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, к которому пользователь пытается присоединиться.
	 * @return true.
	 */
	@Override
	public boolean canJoinContest(String principal, int contestId) {
		return true;

		/*		DudgeLocal dudge = dudgeBean;
		 if(principal == null) return false;
		 User princ = dudgeBean.getUser(principal);
		 if (dudge.isInRole(principal , contestId , RoleType.ADMINISTRATOR)) return true;
		 if (dudge.isInRole(principal , contestId , RoleType.USER)) return true;
		 if (dudge.isInRole(principal , contestId , RoleType.OBSERVER)) return true;
		 if(
		 // Проверяем, что пользователь является администратором системы
		 princ.isAdmin()
		 ) return true;
		 return false;*/
	}

	/**
	 * true, если пользователь аутентифицирован, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contest Соревнование, которое надо получить.
	 */
	@Override
	public boolean canViewContest(String principal, int contestId) {
		/*		if (principal == null)
		 return false;
		 User princ = dudgeBean.getUser(principal);
		 if(princ == null) {
		 logger.warning("Nonexistent user " + principal);
		 return false;
		 }*/

		return true;
	}

	/**
	 * true, если пользователь администратор имеет привилегию создавать соревнования, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	@Override
	public boolean canAddContest(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь является администратором системы или он имеет право создавать соревнования
		if (princ.isAdmin() || princ.canCreateContest()) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор или администратор соревнования, иначе false.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, которое изменяют.
	 */
	@Override
	public boolean canModifyContest(String principal, int contestId) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь является администратором системы или администратором соревнования
		if (princ.isAdmin() || userBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор системы или соревнования, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId ID удаляемого соревнования.
	 */
	@Override
	public boolean canDeleteContest(String principal, int contestId) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь является администратором системы или администратором соревнования
		if (princ.isAdmin() || userBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)) {
			return true;
		}

		return false;
	}

	/**
	 * Нельзя просматривать задачи до начала соревнования и в закрытых 
	 * соревнованиях, к которым пользователь не присоединился.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования.
	 */
	@Override
	public boolean canViewContestProblems(String principal, int contestId) {
		Contest contest = contestBean.getContest(contestId);
		if (contest == null) {
			return false;
		}
		
		if (contest.isOpen() && (contest.isInProgress() || contest.isFinished())) {
			return true;
		}
		
		User princ = userBean.getUser(principal);
		if (princ == null) {
			return false;
		}
		if (princ.isAdmin()) {
			return true;
		}
		
		if (userBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)) {
			return true;
		}
		
		if ((userBean.isInRole(principal, contestId, RoleType.USER)
			|| userBean.isInRole(principal, contestId, RoleType.OBSERVER))
			&& (contest.isInProgress() || contest.isFinished())) {
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * В общем случае выбор зависит от того, скрытая или нет задача.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId Идентификатор задачи, которую запрашивают.
	 */
	@Override
	public boolean canViewProblem(String principal, int problemId) {
		// Если задача открытая, то ее может видеть любой пользователь.
		if (!problemBean.getProblem(problemId).isHidden()) {
			return true;
		}

		/* Если задача скрытая, то ее можно видеть при выполнении
		 * хотя бы одного из условий.
		 *  - Пользователь - администратор системы.
		 *  - Пользователь - автор задачи.
		 *  - Пользователь - администратор хотя бы одного из тех соревнований, где она используется.
		 *  - Пользователь имеет роль участника или наблюдателя хотя бы в одном из
		 *    тех соревнований, где используется эта задача, и которое уже началось.
		 */

		List<Contest> contests = contestBean.getContests();
		List<Contest> selectedContests = new ArrayList<>();

		Problem problem = problemBean.getProblem(problemId);

		for (Contest contest : contests) {
			if (contest.getContestProblems().contains(new ContestProblem(contest, problem))) {
				selectedContests.add(contest);
			}
		}

		for (Contest contest : selectedContests) {
			if (contest.isOpen() && (contest.isInProgress() || contest.isFinished())) {
				return true;
			}
		}

		if (principal == null) {
			return false;
		}

		User princ = userBean.getUser(principal);
		if (princ == null) {
			return false;
		}
		if (princ.isAdmin()) {
			return true;
		}
		if (problemBean.getProblem(problemId).getOwner().equals(princ)) {
			return true;
		}

		for (Contest contest : selectedContests) {
			if (userBean.isInRole(principal, contest.getContestId(), RoleType.ADMINISTRATOR)) {
				return true;
			}
		}

		for (Contest contest : selectedContests) {
			if ((userBean.isInRole(principal, contest.getContestId(), RoleType.USER)
					|| userBean.isInRole(principal, contest.getContestId(), RoleType.OBSERVER))
					&& (contest.isInProgress() || contest.isFinished())) {
				return true;
			}
		}

		/*for (Contest contest: selectedContests) {
		 if (contest.isOpen() && contest.getStartTime().before(new Date())) return true;
		 }*/

		return false;
	}

	/**
	 * Пользователь может добавить задачу, если он администратор или имеет соответствующую привелегию.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problem задача, которую добавляют.
	 * @return true, если пользователь администратор или имеет привелегию добавления задач, иначе false.
	 */
	@Override
	public boolean canAddProblem(String principal) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		if (princ.isAdmin() || princ.canCreateProblem()) {
			return true;
		}

		return false;
	}

	/**
	 * Проверяет право пользователя добавить задачу в свой контест.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор контеста, в который пытаются добавить задачу.
	 * @param problemId Идентификатор задачи, которую пользователь пытается добавить в свой контест.
	 * @return true если задача не скрытая и пользователь имеет права админа или создателя задач. Если задача скрытая, то true, если пользователь автор задачи,
	 * или администратор системы.
	 */
	@Override
	public boolean canAddProblemToContest(String principal, int contestId, int problemId) {
		User princ = userBean.getUser(principal);
		if (princ.isAdmin()) {
			return true;
		}

		if (!problemBean.getProblem(problemId).isHidden()) {
			if (princ.canCreateContest()) {
				return true;
			}
		} else {
			if (princ.canCreateContest() && problemBean.getProblem(problemId).getOwner().equals(princ)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * true, если пользователь администратор или владелец запрошенной задачи, иначе false.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param problem идентификатор задачи, которую изменяют.
	 */
	@Override
	public boolean canModifyProblem(String principal, int problemId) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || problemBean.getProblem(problemId).getOwner().equals(princ)) {
			return true;
		}
		return false;
	}

	/**
	 * True, если пользователь администратор или владелец задачи, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId ID удаляемой задачи.
	 */
	@Override
	public boolean canDeleteProblem(String principal, int problemId) {
		if (principal == null) {
			return false;
		}
		User princ = (userBean.getUser(principal));
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || problemBean.getProblem(problemId).getOwner().equals(princ)) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор системы или соревнования или владелец решения, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param solutionId идентификатор просматриваемого решения.
	 */
	@Override
	public boolean canViewSolution(String principal, int solutionId) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		Solution solution = solutionBean.getSolution(solutionId);

		//Если пользователь является Администратором системы, админитратором данного соревнования или автором решения
		if (princ.isAdmin()
				|| userBean.isInRole(principal, solution.getContest().getContestId(), RoleType.ADMINISTRATOR)
				|| solution.getUser().equals(princ)) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор системы или соревнования или участник данного соревнования и в соревновании есть задачи, иначе false.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 */
	@Override
	public boolean canSubmitSolution(String principal, int contestId) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		Contest contest = contestBean.getContest(contestId);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		//Если пользователь является администратором системы
		if (princ.isAdmin()) {
			return true;
		}

		//Или если является админстратором соревнования
		if (userBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)) {
			return true;
		}

		//Или если пользователь является участником соревнования и это соревнование сейчас идет.
		if (userBean.isInRole(principal, contestId, RoleType.USER) && contest.isInProgress()) {
			return true;
		}

		// Если соревнование открытое  и в настоящее время идет.
		if(contest.isOpen() && contest.isInProgress())
			return true;

		return false;
	}

	/**
	 * true, если пользователь администратор системы или соревнования или участник данного соревнования и задача принадлежит данному соревнованию, иначе false.
	 *
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 * @param problemId идентификатор задачи, решение которой отправляется.
	 */
	@Override
	public boolean canSubmitSolution(String principal, int contestId, int problemId) {
		Contest contest = contestBean.getContest(contestId);
		Problem problem = problemBean.getProblem(problemId);

		if (canSubmitSolution(principal, contestId) && contest != null && problem != null
				&& contest.getContestProblems().contains(new ContestProblem(contest, problem))) {
			return true;
		}

		return false;
	}

	/**
	 * true, если пользователь администратор системы.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param solution Решение, которое модифицируют.
	 */
	@Override
	public boolean canModifySolution(String principal, Solution solution) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		//Если пользователь является Администратором системы
		if (princ.isAdmin()) {
			return true;
		}
		return false;
	}

	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который запрашивается.
	 */
	@Override
	public boolean canGetTest(String principal, Test test) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || test.getProblem().getOwner().equals(princ)) {
			return true;
		}
		return false;
	}

	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который добавляется.
	 */
	@Override
	public boolean canAddTest(String principal, Test test) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || test.getProblem().getOwner().equals(princ)) {
			return true;
		}
		return false;
	}

	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который модифицируется.
	 */
	@Override
	public boolean canModifyTest(String principal, Test test) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || test.getProblem().getOwner().equals(princ)) {
			return true;
		}
		return false;
	}

	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который модифицируется.
	 */
	@Override
	public boolean canDeleteTest(String principal, Test test) {
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		// Проверяем, что пользователь является администратором системы или владельцем задачи
		if (princ.isAdmin() || test.getProblem().getOwner().equals(princ)) {
			return true;
		}
		return false;
	}

	/**
	 * Проверяет право на просмотр монитора соревнования.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, монитор которого просматривается.
	 * @return true всегда.
	 */
	@Override
	public boolean canViewMonitor(String principal, int contestId) {
		return true;
	}

	/**
	 * Проверяет право на отправку заявки на участие данным пользователем в данное соревнование.
	 *
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, в которое заявка отправляется.
	 * @return true если пользователь залогинен и не имеет ролей в данном соревновании, иначе false.
	 */
	@Override
	public boolean canSendApplication(String principal, int contestId) {
		if (contestBean.getContest(contestId).isOpen()) {
			return false;
		}
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}

		if (userBean.haveNoRoles(principal, contestId)) {
			return true;
		}

		return false;
	}

	/**
	 * Проверяет право на просмотр конкретной новости.
	 *
	 * @principal пользователь, для которого проверяется право;
	 * @newsId идентификатор новости;
	 * @return всегда true;
	 */
	@Override
	public boolean canViewNews(String principal, int newsId) {
		return (java.lang.Boolean) em.createNativeQuery("SELECT can_view_news(:principal, :news_id)", java.lang.Boolean.class)
				.setParameter("principal", principal).setParameter("newsId", newsId).getSingleResult();
	}
	
	/* Проверяет наличие каких-либо прав на управление объектами системы.
	 * @principal пользователь, для которого проверяются права;
	 * @return true если пользователь может чем-то управлять.
	 */
	@Override
	public boolean canAdmin(String principal)
	{
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		//Если пользователь является Администратором системы
		if (princ.isAdmin()) {
			return true;
		}
		return false;
	}
	
	/* Проверяет наличие прав на редактирование глобальной страницы с правилами.
	 * @principal пользователь, для которого проверяются права;
	 * @return true если пользователь может редактировать глобальные правила.
	 */
	@Override
	public boolean canEditRules(String principal)
	{
		if (principal == null) {
			return false;
		}
		User princ = userBean.getUser(principal);
		if (princ == null) {
			logger.log(Level.WARNING, "Nonexistent user {0}", principal);
			return false;
		}
		//Если пользователь является Администратором системы
		if (princ.isAdmin()) {
			return true;
		}
		return false;
	}
}
