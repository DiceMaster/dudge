/*
 * DudgeBean.java
 *
 * Created on April 14, 2007, 2:49 PM
 */
package dudge;

import dudge.ifaces.SolutionRemote;
import dudge.db.Contest;
import dudge.db.Param;
import dudge.db.Role;
import dudge.db.RoleType;
import dudge.db.Run;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import dudge.db.User;
import dudge.monitor.AcmMonitorRecord;
import dudge.monitor.GlobalMonitorRecord;
import dudge.monitor.SchoolMonitorRecord;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Класс основного Bean'а системы. Реализует локальный и/или удаленный интерфейсы системы, содержит методы для работы с объектами системы и выполнения действий
 * в ней.
 *
 * @author Vladimir Shabanov
 */
@Stateless
public class DudgeBean implements DudgeLocal {

	private static final Logger logger = Logger.getLogger(DudgeBean.class.toString());
	@EJB
	private ContestLocal contestBean;
	@EJB
	private SolutionLocal solutionLocalBean;
	@EJB
	private SolutionRemote solutionRemoteBean;
	@EJB
	private UserLocal userBean;
	@Resource(mappedName = "jms/solutionsQueueFactory")
	private ConnectionFactory solutionsQueueFactory;
	@Resource(mappedName = "jms/solutionsQueue")
	private Queue solutionsQueue;
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;
	private final int minimumPasswordLength = 3;
	private final int maximumPasswordLength = 128;

	/**
	 * Creates a new instance of DudgeBean
	 */
	public DudgeBean() {
	}

	/**
	 *
	 * @param login
	 */
	@Override
	public void joinAllOpenContests(String login) {
		User user = userBean.getUser(login);

		boolean shouldFlush = false;
		for (Contest contest : contestBean.getContests()) {
			if (!contest.isOpen()) {
				continue;
			}

			if (userBean.haveNoRoles(login, contest.getContestId())) {
				Role role = new Role(contest, user, RoleType.USER);
				contest.getRoles().add(role);
				shouldFlush = true;
			}
		}
		if (shouldFlush) {
			em.flush();
		}
	}

	/**
	 *
	 * @param solution
	 * @return
	 */
	@Override
	public Solution submitSolution(Solution solution) {
		solution.setStatus(SolutionStatus.NEW);
		solution.setSubmitTime(new Date());

		User user = userBean.getUser(solution.getUser().getLogin());

		Contest contest = contestBean.getContest(solution.getContest().getContestId());
		contest.getSolutions().add(solution);

		if (contest.isOpen() && userBean.haveNoRoles(user.getLogin(), contest.getContestId())) {
			Role autoRegisteredUser = new Role(contest, user, RoleType.USER);
			contest.getRoles().add(em.merge(autoRegisteredUser));
		}

		em.flush();

		try {
			sendJMSMessageToSolutionsQueue(solutionRemoteBean.getSolutionEager(solution.getSolutionId()));
			logger.log(Level.INFO, "Solution {0} submitted to JMS queue.", solution.getSolutionId());
		} catch (JMSException | NamingException ex) {
			logger.log(Level.SEVERE, "Exception thrown when sending solution message.", ex);
			throw new RuntimeException(ex);
		}

		return solution;
	}

	/**
	 *
	 * @param contestId
	 * @param problemId
	 */
	@Override
	public void resubmitSolutions(int contestId, int problemId) {
		List<Solution> sols = (List<Solution>) em.createQuery(
				"SELECT s FROM Solution s WHERE s.contestId = :contestId AND s.problemId = :problemId AND s.status = 'PROCESSED'")
				.setParameter("contestId", contestId).setParameter("problemId", problemId).getResultList();

		for (Solution sol : sols) {
			this.resubmitSolution(sol.getSolutionId());
		}
	}

	/**
	 *
	 * @param solutionId
	 */
	@Override
	public void resubmitSolution(int solutionId) {
		Solution solution = solutionLocalBean.getSolution(solutionId);
		solution.setStatus(SolutionStatus.NEW);

		// Удаляем все запуски на тестах для данного решения.
		for (Run run : solution.getRuns()) {
			em.remove(run);
		}
		solution.getRuns().clear();

		try {
			sendJMSMessageToSolutionsQueue(solutionRemoteBean.getSolutionEager(solution.getSolutionId()));
			logger.log(Level.INFO, "Solution {0} submitted to JMS queue.", solution.getSolutionId());
		} catch (JMSException | NamingException ex) {
			logger.log(Level.SEVERE, "Exception thrown when sending solution message.", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int getMinimumPasswordLength() {
		return minimumPasswordLength;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int getMaximumPasswordLength() {
		return maximumPasswordLength;
	}

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	@Override
	public List<GlobalMonitorRecord> getGlobalMonitorRecords(Contest contest, Date when) {
		LinkedList<GlobalMonitorRecord> rows = new LinkedList<>();

		for (Role role : contest.getRoles()) {
			// Показываем в мониторе только пользователей, которые являются обычными участниками соревнования.
			if (role.getRoleType() != RoleType.USER) {
				continue;
			}

			rows.add(new GlobalMonitorRecord(solutionLocalBean, contest, role.getUser(), when));
		}

		Collections.sort(rows);
		Collections.reverse(rows);

		for (int i = 0; i < rows.size(); ++i) {
			rows.get(i).setPlace(i + 1);
		}

		return rows;
	}

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	@Override
	public List<AcmMonitorRecord> getAcmMonitorRecords(Contest contest, Date when) {
		LinkedList<AcmMonitorRecord> rows = new LinkedList<>();

		for (Role role : contest.getRoles()) {
			// Показываем в мониторе только пользователей, которые являются обычными участниками соревнования.
			if (role.getRoleType() != RoleType.USER) {
				continue;
			}

			rows.add(new AcmMonitorRecord(solutionLocalBean, contest, role.getUser(), when));
		}

		Collections.sort(rows);
		Collections.reverse(rows);

		for (int i = 0; i < rows.size(); ++i) {
			rows.get(i).setPlace(i + 1);
		}

		return rows;
	}

	/**
	 *
	 * @param contest
	 * @param when
	 * @return
	 */
	@Override
	public List<SchoolMonitorRecord> getSchoolMonitorRecords(Contest contest, Date when) {
		LinkedList<SchoolMonitorRecord> rows = new LinkedList<>();

		for (Role role : contest.getRoles()) {
			// Показываем в мониторе только пользователей, которые являются обычными участниками соревнования.
			if (role.getRoleType() != RoleType.USER) {
				continue;
			}

			rows.add(new SchoolMonitorRecord(solutionLocalBean, contest, role.getUser(), when));
		}

		Collections.sort(rows);
		Collections.reverse(rows);

		for (int i = 0; i < rows.size(); ++i) {
			rows.get(i).setPlace(i + 1);
		}

		return rows;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public URI getBugTrackingPath() {
		Param uriParam = em.find(Param.class, "bug_tracking_uri");
		URI bugURI = URI.create("https://www.glint.ru/dev/dudge/");

		try {
			if (uriParam != null) {
				bugURI = URI.create(uriParam.getValue());
			}
		} catch (IllegalArgumentException ex) {
			logger.log(Level.WARNING, "Wrong bugtracking URI parameter in database.", ex);
		}

		return bugURI;
	}

	/**
	 *
	 * @param session
	 * @param messageData
	 * @return
	 * @throws JMSException
	 */
	private Message createJMSMessageForSolutionsQueue(Session session, Object messageData) throws JMSException {
		ObjectMessage message = session.createObjectMessage((Solution) messageData);
		return message;
	}

	/**
	 *
	 * @param messageData
	 * @throws NamingException
	 * @throws JMSException
	 */
	private void sendJMSMessageToSolutionsQueue(Object messageData) throws NamingException, JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = solutionsQueueFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(solutionsQueue);
			messageProducer.send(createJMSMessageForSolutionsQueue(session, messageData));
		} finally {
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	/**
	 * Позволяет получить текст глобальных правил системы.
	 *
	 * @return текст глобальных правил.
	 */
	@Override
	public String getRules() {
		Param rulesParam = em.find(Param.class, "global_rules");

		if (rulesParam == null) {
			return null;
		}
		
		return rulesParam.getValue();
	}
	
	/**
	 * Позволяет задать текст глобальных правил системы.
	 *
	 * @param rules текст глобальных правил.
	 */
	public void setRules(String rules) {
		Param rulesParam = em.find(Param.class, "global_rules");

		if (rulesParam != null) {
			rulesParam.setValue(rules);
		} else {
			rulesParam = new Param("global_rules", rules);
		}
		
		em.merge(rulesParam);
	}
}
