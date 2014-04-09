package dudge;

import dudge.ifaces.SolutionRemote;
import dudge.db.Run;
import dudge.db.Solution;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mikhail Ivanov
 */
@Stateless
public class SolutionBean implements SolutionLocal, SolutionRemote {

	private static final Logger logger = Logger.getLogger(SolutionBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @param solutionId
	 * @return
	 */
	@Override
	public Solution getSolution(int solutionId) {
		Solution solution = em.find(Solution.class, solutionId);
		return solution;
	}

	/**
	 *
	 * @param solutionId
	 * @return
	 */
	@Override
	public Solution getSolutionEager(int solutionId) {
		Solution solution = getSolution(solutionId);
		// Инстанциируем отложенную загрузку для того, чтобы раб мог получать коллекции.
		solution.getRuns().size();
		solution.getContest().getContestProblems().size();
		solution.getContest().getContestLanguages().size();
		solution.getProblem().getTests().size();

		return solution;
	}

	/**
	 *
	 * @param login
	 * @param contestId
	 * @param problemId
	 * @return
	 */
	@Override
	public List<Solution> getSolutions(String login, int contestId, int problemId) {
		List<Solution> lcpSolutions = (List<Solution>) em.createNamedQuery("Solution.findByUserContestProblem")
				.setParameter("login", login.toLowerCase(Locale.ENGLISH))
				.setParameter("contestId", contestId).setParameter("problemId", problemId).getResultList();

		// Удаляем из полученного списка решений те, которые не попадают в интервал проведения соревнования.
		List<Solution> solutions = new ArrayList<>();
		for (Solution solution : lcpSolutions) {
			Date endTime = new Date(solution.getContest().getStartTime().getTime() + solution.getContest().getDuration() * (long) 1000);
			if ((solution.getSubmitTime().after(solution.getContest().getStartTime()))
					&& (solution.getSubmitTime().before(endTime) || solution.getContest().getDuration() == 0)) {
				solutions.add(solution);
			}
		}

		return solutions;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Solution> getPendingSolutions() {
		return (List<Solution>) em.createNamedQuery("Solution.getPendingSolutions").getResultList();
	}

	/**
	 * Возвращает список решений.
	 *
	 * @param start номер первого возвращяемого решения.
	 * @param count сколько решений возвращать.
	 * @return отсортированный по возрастанию давности список решений.
	 */
	@Override
	public List<Solution> getSolutions(int start, int count) {
		return (List<Solution>) em.createNamedQuery("Solution.getLastSolutions").setFirstResult(start).setMaxResults(count).getResultList();
	}
	
	/**
	 * Возвращает количество решений, отправленных в систему.
	 *
	 * @return Количество решений, отправленных в систему.
	 */
	@Override
	public long getSolutionsCount() {
		return (Long)em.createQuery("SELECT COUNT(s) FROM Solution s").getSingleResult();
	}
	
	/**
	 *
	 * @param count
	 * @return
	 */
	@Override
	public List<Solution> getLastSolutions(int count) {
		return (List<Solution>) em.createNamedQuery("Solution.getLastSolutions").setMaxResults(count).getResultList();
	}

	/**
	 *
	 * @param solution
	 */
	@Override
	public void modifySolution(Solution solution) {
		Solution merged = em.merge(solution);

		// Если решение прошло все тесты, то помечаем задачу как здоровую.
		if (merged.isAllTestsPassed()) {
			merged.getProblem().setHealthy(true);
		}

		em.flush();
	}

	/**
	 *
	 * @param solution
	 */
	@Override
	public void saveSolution(Solution solution) {

		Solution dbs = getSolution(solution.getSolutionId());

		dbs.setStatus(solution.getStatus());
		dbs.setStatusMessage(solution.getStatusMessage());
		dbs.setCompilationTime(solution.getCompilationTime());

		dbs.setStatus(solution.getStatus());
		dbs.setStatusMessage(solution.getStatusMessage());
		dbs.setCompilationTime(solution.getCompilationTime());

		for (Run run : dbs.getRuns()) {
			em.remove(run);
		}
		dbs.getRuns().clear();

		if (solution.getRuns() != null) {
			for (Run run : solution.getRuns()) {
				dbs.getRuns().add(em.merge(run));
			}
		}

		// Если решение прошло все тесты, то помечаем задачу как здоровую.
		if (dbs.isAllTestsPassed()) {
			if (dbs.getProblem() != null) {
				dbs.getProblem().setHealthy(true);
			} else {
				dbs.setProblem(solution.getProblem());
			}
		}
		em.flush();
	}
}
