/*
 * PermissionCheckerBean.java
 */

package dudge;

import dudge.db.RoleType;
import dudge.db.Solution;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.User;
import dudge.db.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Класс, осуществляющий проверку прав пользователей.
 * @see PermissionCheckerRemote.
 *
 * @author Michael Antonov
 */
@Stateless
public class PermissionCheckerBean implements PermissionCheckerRemote {
	@EJB
	private DudgeLocal dudgeBean;
	
	protected Logger logger = Logger.getLogger(PermissionCheckerBean.class.toString());
	
	@PersistenceContext
	private EntityManager em;

	public void persist(Object object) {
		em.persist(object);
	}
		
	/** Creates a new instance of PermissionCheckerBean */
	public PermissionCheckerBean() {
	}
	
	
	/**
	 * true, если пользователь аутентифицирован, иначе false.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param user Пользователь, данные которого запрашиваются.
	 */
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
	 * true, если пользователь пытается редактировать свой профиль или является
	 * администратором системы, иначе false.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param user Имя пользователя, данные которого надо модифицировать.
	 */
	public boolean canModifyUser(String principal, String user) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Если пользователь пытается редактировать не свой профиль
				principal.equals(user)
				||
				//Проверяем, является ли он Администратором Системы
				princ.isAdmin()
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь является администратором, иначе false.
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param user имя пользователя, данные которого надо модифицировать.
	 */
	public boolean canDeepModifyUser(String principal, String user) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор , иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param user Удаляемый пользователь.
	 */
	public boolean canDeleteUser(String principal) {
		if(principal == null)
			return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * true, если пользователь аутентифицирован, иначе false.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param language Язык, который запрашивается.
	 */
	public boolean canGetLanguage(String principal, Language language) {
		if (principal == null)
			return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		return true;
	}
		
	/**
	 * true, если пользователь администратор, иначе false.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 */
	public boolean canAddLanguage(String principal) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				) {
			return true;
		}
		
		return false;
	}

	/**
	 * true, если пользователь администратор системы, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param language Язык, который модифицируется.
	 */
	public boolean canModifyLanguage(String principal) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				) {
			return true;
		}
		
		return false;
	}
	
	public boolean canDeleteLanguage(String principal){
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(princ.isAdmin()) return true;
		return false;
		
	}

	/**
	 * Проверяет право на присоединение к соревнованию.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, к которому пользователь пытается присоединиться.
	 * @return true.
	 */
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
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param contest Соревнование, которое надо получить.
	 */
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
	 * true, если пользователь администратор имеет привилегию создавать
	 * соревнования, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 */
	public boolean canAddContest(String principal) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if (
				//Если пользователь является Администратором системы
				princ.isAdmin()
				||
				// Или если он имеет право создавать контесты
				princ.canCreateContest()
				) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * true, если пользователь администратор или администратор соревнования,
	 * иначе false.
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, которое изменяют.
	 */
	public boolean canModifyContest(String principal, int contestId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if (
				// Проверяем, является ли пользователь глобальным администратором.
				princ.isAdmin()
				||
				//Проверяем, является ли пользователь администратором совернования
				dudgeBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или соревнования,
	 * иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId ID удаляемого соревнования.
	 */
	public boolean canDeleteContest(String principal, int contestId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				||
				dudgeBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)
				) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * В общем случае выбор зависит от того, скрытая или нет задача.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId Идентификатор задачи, которую запрашивают.
	 */
	public boolean canViewProblem(String principal, int problemId) {
		// Если задача открытая, то ее может видеть любой пользователь.
		if (!dudgeBean.getProblem(problemId).isHidden()) {
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

		List<Contest> contests = dudgeBean.getContests();
		List<Contest> selectedContests = new ArrayList<Contest>();

		Problem problem = dudgeBean.getProblem(problemId);

		for(Contest contest: contests) {
			if( contest.getContestProblems().contains(new ContestProblem(contest, problem))) {
				selectedContests.add(contest);
			}
		}

		for (Contest contest: selectedContests) {
                    if (contest.isOpen() && (contest.isInProgress() || contest.isFinished()) ) {
                        return true;
                    }
		}

		if (principal == null) {
                    return false;
                }
                
		User princ = dudgeBean.getUser(principal);		
		if (princ == null) {
                    return false;
                }
                if (princ.isAdmin()) {
                    return true;
                }
		if(dudgeBean.getProblem(problemId).getOwner().equals(princ)) {
                    return true;
                }
		
		for (Contest contest: selectedContests) {
                    if(dudgeBean.isInRole(principal , contest.getContestId() , RoleType.ADMINISTRATOR)) {
                        return true;
                    }
		}
		
		for (Contest contest: selectedContests) {
                    if ( (dudgeBean.isInRole(principal , contest.getContestId() , RoleType.USER) ||
                                    dudgeBean.isInRole(principal , contest.getContestId() , RoleType.OBSERVER))
                                    && (contest.isInProgress() || contest.isFinished())
                                    ) {
                        return true;
                    }
		}

		/*for (Contest contest: selectedContests) {
			if (contest.isOpen() && contest.getStartTime().before(new Date())) return true;
		}*/
		
		return false;
	}
	
	/**
	 * Пользователь может добавить задачу, если он администратор или имеет соответствующую
	 * привелегию.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param problem задача, которую добавляют.
	 * @return true, если пользователь администратор или имеет привелегию добавления задач,
	 * иначе false.
	 */
	public boolean canAddProblem(String principal) {
		if (principal == null)
			return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if(princ.isAdmin() || princ.canCreateProblem())
			return true;
		
		return false;
	}
	
	/**
	 * Проверяет право пользователя добавить задачу в свой контест.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор контеста, в который пытаются добавить задачу.
	 * @param problemId Идентификатор задачи, которую пользователь пытается добавить в свой контест.
	 * @return true если задача не скрытая и пользователь имеет права админа или создателя задач.
	 * Если задача скрытая, то true, если пользователь автор задачи, или администратор системы.
	 */
	public boolean canAddProblemToContest(String principal, int contestId, int problemId) {
		User princ = dudgeBean.getUser(principal);
		if (princ.isAdmin()) return true;
		
		if(!dudgeBean.getProblem(problemId).isHidden()) {
			if( princ.canCreateContest()) return true;
		} else {
			if( princ.canCreateContest()
			&& dudgeBean.getProblem(problemId).getOwner().equals(princ)) return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор или владелец запрошенной задачи,
	 * иначе false.
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param problem идентификатор задачи, которую изменяют.
	 */
	public boolean canModifyProblem(String principal, int problemId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if (
				//Если пользователь не является Администратором системы
				princ.isAdmin()
				||
				//Если пользователь не владелец данной задачи
				dudgeBean.getProblem(problemId).getOwner().equals(princ)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * True, если пользователь администратор или владелец задачи,
	 * иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param problemId ID удаляемой задачи.
	 */
	public boolean canDeleteProblem(String principal, int problemId) {
		if(principal == null)
			return false;
		
		User princ = (dudgeBean.getUser(principal));
		
		if(princ == null)
			return false;
		
		if (
				//Проверяем, является ли пользователь Администратором Системы
				princ.isAdmin()
				||
				dudgeBean.getProblem(problemId).getOwner().equals(princ)
				) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или соревнования или
	 * владелец решения, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param solutionId идентификатор просматриваемого решения.
	 */
	public boolean canViewSolution(String principal, int solutionId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		Solution solution = dudgeBean.getSolution(solutionId);
		
		if(
				//Если пользователь является Администратором системы
				princ.isAdmin()
				||
				//Если пользователь является админитратором данного соревнования
				dudgeBean.isInRole(
				principal,
				solution.getContest().getContestId(),
				RoleType.ADMINISTRATOR
				)
				||
				//Если пользователь является автором решения
				solution.getUser().equals(princ)
				) {
			return true;
		}
		
		return false;
	}

	/**
	 * true, если пользователь администратор системы или соревнования
	 * или участник данного соревнования и в соревновании есть задачи, иначе false.
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 */
	public boolean canSubmitSolution(String principal, int contestId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		Contest contest = dudgeBean.getContest(contestId);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		//Если пользователь является администратором системы
		if(princ.isAdmin() ) return true;
		
		//Или если является админстратором соревнования
		if(dudgeBean.isInRole(principal, contestId, RoleType.ADMINISTRATOR)) return true;
		
		//Или если пользователь является участником соревнования, и это соревнование сейчас идет.
		if(dudgeBean.isInRole(principal, contestId , RoleType.USER) && contest.isInProgress())
			return true;
		
		// Если соревнование открытое  и в настоящее время идет.
		//if(contest.isOpen() && contest.isInProgress())
			//return true;
			
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или соревнования
	 * или участник данного соревнования и задача принадлежит данному соревнованию, иначе false.
	 * @param principal имя пользователя, для которого проверяется право.
	 * @param contestId идентификатор соревнования, куда отправляется решение.
	 * @param problemId идентификатор задачи, решение которой отправляется.
	 */
	public boolean canSubmitSolution(String principal, int contestId, int problemId) {
		Contest contest = dudgeBean.getContest(contestId);
		Problem problem = dudgeBean.getProblem(problemId);
		
		if(canSubmitSolution(principal, contestId)
				&& contest != null
				&& problem != null
				&& contest.getContestProblems().contains(new ContestProblem(contest, problem))
				)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы.
	 * @param principal  Имя пользователя, для которого проверяется право.
	 * @param solution Решение, которое модифицируют.
	 */
	public boolean canModifySolution(String principal, Solution solution) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(
				//Если пользователь является Администратором системы
				princ.isAdmin()
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который запрашивается.
	 */
	public boolean canGetTest(String principal, Test test) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(
				// Проверяем, что пользователь является администратором системы
				princ.isAdmin()
				||
				// Проверяем, что пользователь является владельцем задачи
				test.getProblem().getOwner().equals(princ)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который добавляется.
	 */
	public boolean canAddTest(String principal, Test test) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(
				// Проверяем, что пользователь является администратором системы
				princ.isAdmin()
				||
				// Проверяем, что пользователь является владельцем задачи
				test.getProblem().getOwner().equals(princ)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который модифицируется.
	 */
	public boolean canModifyTest(String principal, Test test) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(
				// Проверяем, что пользователь является администратором системы
				princ.isAdmin()
				||
				// Проверяем, что пользователь является владельцем задачи
				test.getProblem().getOwner().equals(princ)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * true, если пользователь администратор системы или владелец задачи, иначе false.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param test Тест, который модифицируется.
	 */
	public boolean canDeleteTest(String principal, Test test) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		if(
				// Проверяем, что пользователь является администратором системы
				princ.isAdmin()
				||
				// Проверяем, что пользователь является владельцем задачи
				test.getProblem().getOwner().equals(princ)
				) {
			return true;
		}
		return false;
	}
	
	/**
	 * Проверяет право на просмотр монитора соревнования.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, монитор которого просматривается.
	 * @return true всегда.
	 */
	public boolean canViewMonitor(String principal, int contestId) {
		return true;
	}
	
	/**
	 * Проверяет право на отправку заявки на участие данным пользователем
	 * в данное соревнование.
	 * @param principal Имя пользователя, для которого проверяется право.
	 * @param contestId Идентификатор соревнования, в которое заявка отправляется.
	 * @return true если пользователь залогинен и не имеет ролей в данном соревновании,
	 * иначе false.
	 */
	public boolean canSendApplication(String principal, int contestId) {
		if(principal == null) return false;
		User princ = dudgeBean.getUser(principal);
		if(princ == null) {
			logger.warning("Nonexistent user " + principal);
			return false;
		}
		
		if(dudgeBean.haveNoRoles(principal, contestId)) {
			return true;
		}
				
		return false;
	}
	
	/* Проверяет право на просмотр конкретной новости.
	 * @principal пользователь, для которого проверяется право;
	 * @newsId идентификатор новости;
	 * @return всегда true;
	 */
	public boolean canViewNews(String principal, int newsId) {
		return (java.lang.Boolean)
		em.createNativeQuery("SELECT can_view_news(:principal, :news_id)", java.lang.Boolean.class)
		.setParameter("principal", principal)
		.setParameter("newsId", newsId)
		.getSingleResult();
	}
}
