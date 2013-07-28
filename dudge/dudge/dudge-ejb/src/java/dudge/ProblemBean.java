package dudge;

import dudge.db.Problem;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mikhail Ivanov
 */
@Stateless
public class ProblemBean implements ProblemLocal {

	private static final Logger logger = Logger.getLogger(ProblemBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @param problemId
	 * @return
	 */
	@Override
	public Problem getProblem(int problemId) {
		return (Problem) em.find(Problem.class, problemId);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public long getProblemsCount() {
		return (Long) em.createQuery("SELECT COUNT(p) FROM Problem p").getSingleResult();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Problem> getProblems() {
		return (List<Problem>) em.createNamedQuery("Problem.getProblems").getResultList();
	}

	/**
	 *
	 * @param start
	 * @param limit
	 * @return
	 */
	@Override
	public List<Problem> getProblems(int start, int limit) {
		return (List<Problem>) em.createQuery("SELECT p FROM Problem p ORDER BY p.problemId")
				.setFirstResult(start).setMaxResults(limit).getResultList();
	}

	/**
	 *
	 * @param problem
	 * @return
	 */
	@Override
	public Problem addProblem(Problem problem) {
		em.persist(problem);
		em.flush();
		return problem;
	}

	/**
	 *
	 * @param problem
	 */
	@Override
	public void modifyProblem(Problem problem) {
		em.merge(problem);
	}

	/**
	 *
	 * @param problemId
	 */
	@Override
	public void deleteProblem(int problemId) {
		em.remove((Problem) em.find(Problem.class, problemId));
	}
}
