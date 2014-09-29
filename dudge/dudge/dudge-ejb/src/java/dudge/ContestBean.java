package dudge;

import dudge.db.Contest;
import dudge.db.ContestLanguage;
import dudge.db.ContestProblem;
import dudge.db.Param;
import dudge.db.Role;
import dudge.db.Solution;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mikhail Ivanov
 */
@Stateless
public class ContestBean implements ContestLocal {

	private static final Logger logger = Logger.getLogger(ContestBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @return
	 */
	@Override
	public Contest getDefaultContest() {
		Param param = (Param) em.find(Param.class, "default_contest");
		if (param == null) {
			logger.severe("Database 'params' table doesn't contain 'default_contest' parameter.");
			throw new NullPointerException("Database 'params' table doesn't contain 'default_contest' parameter.");
		}

		return getContest(Integer.parseInt(param.getValue()));
	}

	/**
	 *
	 * @param contestId
	 * @return
	 */
	@Override
	public Contest getContest(int contestId) {
		Contest contest = (Contest) em.find(Contest.class, contestId);

		return contest;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Contest> getContests() {
		List<Contest> contests = (List<Contest>) em.createNamedQuery("Contest.getContests").getResultList();

		return contests;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Contest> getPendingContests() {
		List<Contest> contests = (List<Contest>) em.createNamedQuery("Contest.getPendingContests").getResultList();

		List<Contest> pendingContests = new ArrayList<>();

		for (Contest contest : contests) {
			if (contest.isPending()) {
				pendingContests.add(contest);
			}
		}

                Collections.sort(pendingContests);
                
		return pendingContests;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Contest> getActiveContests() {
		List<Contest> contests = (List<Contest>) em.createNamedQuery("Contest.getActiveContests").getResultList();

		List<Contest> activeContests = new ArrayList<>();

		for (Contest contest : contests) {
			if (contest.isInProgress()) {
				activeContests.add(contest);
			}
		}

		Collections.sort(activeContests);
                
		return activeContests;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Contest> getRecentlyFinishedContests() {
		List<Contest> contests = (List<Contest>) em.createNamedQuery("Contest.getRecentlyFinishedContests").getResultList();

		List<Contest> recentlyFinishedContests = new ArrayList<>();

		for (Contest contest : contests) {
			java.util.Calendar c = java.util.Calendar.getInstance();
			c.setTime(contest.getEndTime());
			c.add(java.util.Calendar.DAY_OF_MONTH, 7);

			if (contest.isFinished() && c.after(java.util.Calendar.getInstance())) {
				recentlyFinishedContests.add(contest);
			}
		}

		Collections.sort(recentlyFinishedContests);
                
		return recentlyFinishedContests;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Contest> getGlobalContests() {
		List<Contest> contests = (List<Contest>) em.createNamedQuery("Contest.getGlobalContests").getResultList();

		Collections.sort(contests);
                
		return contests;
	}
	
	/**
	 *
	 * @param contest
	 * @return
	 */
	@Override
	public Contest addContest(Contest contest) {
		em.persist(contest);
		em.flush();
		return contest;
	}

	/**
	 * ВНИМАНИЕ! Данный код написан с учетом того, что в спецификации EJB 3.0 не предусмотрено автоматическое каскадное удаление сущностей при
	 *
	 * @OneToMany-merge.
	 *
	 * @param contest
	 */
	@Override
	public void modifyContest(Contest contest) {
		// Получаем старые значения, сохраненные в коллекциях.

		List<Solution> oldSolutions = (List<Solution>) em.createNamedQuery("Solution.findByContestId")
				.setParameter("contestId", contest.getContestId()).getResultList();
		List<Role> oldRoles = (List<Role>) em.createNamedQuery("Role.findByContestId")
				.setParameter("contestId", contest.getContestId()).getResultList();
		List<ContestProblem> oldContestProblems = (List<ContestProblem>) em.createNamedQuery("ContestProblem.findByContestId")
				.setParameter("contestId", contest.getContestId()).getResultList();
		List<ContestLanguage> oldContestLanguages = (List<ContestLanguage>) em.createNamedQuery("ContestLanguage.findByContestId")
				.setParameter("contestId", contest.getContestId()).getResultList();

		// Если некоторая сущность более не присутствует в БД, она удаляется.
		List<Solution> newSolutions = (List<Solution>) contest.getSolutions();
		for (Iterator<Solution> iter = oldSolutions.iterator(); iter.hasNext();) {
			Solution s = iter.next();
			if (!newSolutions.contains(s)) {
				iter.remove();
				em.remove(s);
			}
		}

		// Если некоторая сущность более не присутствует в БД, она удаляется.
		Collection<Role> newRoles = contest.getRoles();
		for (Iterator<Role> iter = oldRoles.iterator(); iter.hasNext();) {
			Role r = iter.next();
			if (!newRoles.contains(r)) {
				iter.remove();
				em.remove(r);
			}
		}

		// Если некоторая сущность более не присутствует в БД, она удаляется.
		List<ContestLanguage> newContestLanguages = (List<ContestLanguage>) contest.getContestLanguages();
		for (Iterator<ContestLanguage> iter = oldContestLanguages.iterator(); iter.hasNext();) {
			ContestLanguage cl = iter.next();
			if (!newContestLanguages.contains(cl)) {
				iter.remove();
				em.remove(cl);
			}
		}

		// Если некоторая сущность более не присутствует в БД, она удаляется.
		List<ContestProblem> newContestProblems = (List<ContestProblem>) contest.getContestProblems();
		for (Iterator<ContestProblem> iter = oldContestProblems.iterator(); iter.hasNext();) {
			ContestProblem cp = iter.next();
			if (!newContestProblems.contains(cp)) {
				iter.remove();
				em.remove(cp);
			}
		}

		em.merge(contest);
	}

	/**
	 *
	 * @param contestId
	 */
	@Override
	public void deleteContest(int contestId) {
		em.remove((Contest) em.find(Contest.class, contestId));
	}

	@Override
	public FilteredContests getContests(String searchCriteria, String orderBy, boolean descending, int start, int length) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery contestsCriteriaQuery = builder.createQuery();
		Root contestsRoot = contestsCriteriaQuery.from(Contest.class);

		CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		Root countRoot = countCriteriaQuery.from(Contest.class);
		countCriteriaQuery.select(builder.count(countRoot));

		if (searchCriteria != null) {
			contestsCriteriaQuery.where(
					builder.like(contestsRoot.get("caption"), "%" + searchCriteria + "%")
				);
			countCriteriaQuery.where(
					builder.like(contestsRoot.get("caption"), "%" + searchCriteria + "%")
				);
		}

		final long count = em.createQuery(countCriteriaQuery).getSingleResult();

		if (orderBy != null) {
			contestsCriteriaQuery.orderBy(descending ? builder.desc(contestsRoot.get(orderBy)) : builder.asc(contestsRoot.get(orderBy)));
		}

		Query contestsQuery = em.createQuery(contestsCriteriaQuery);
		if (start >= 0) {
			contestsQuery = contestsQuery.setFirstResult(start);
		}
		if (length > 0) {
			contestsQuery = contestsQuery.setMaxResults(length);
		}
		final List<Contest> filteredContests = (List<Contest>) contestsQuery.getResultList();

		return new FilteredContests() {

			@Override
			public long getFilteredTotal() {
				return count;
			}

			@Override
			public List<Contest> getFilteredContests() {
				return filteredContests;
			}
		};

	}

	@Override
	public long getContestsCount() {
		return (Long) em.createQuery("SELECT COUNT(c) FROM Contest c").getSingleResult();
	}
}
