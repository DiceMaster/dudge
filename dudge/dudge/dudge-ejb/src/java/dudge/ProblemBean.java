package dudge;

import dudge.db.Problem;
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
	 * Возвращает упорядоченный список задач.
	 *
	 * @param orderBy сортируемая колонка.
	 * @param descending обратный порядок сортировки.
	 * @return список адач.
	 */
	@Override
	public List<Problem> getProblems(String orderBy, boolean descending) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery problemsCriteriaQuery = builder.createQuery();
		Root problemsRoot = problemsCriteriaQuery.from(Problem.class);

		if (orderBy != null) {
			problemsCriteriaQuery.orderBy(descending ? builder.desc(problemsRoot.get(orderBy)) : builder.asc(problemsRoot.get(orderBy)));
		}

		Query problemsQuery = em.createQuery(problemsCriteriaQuery);
		
		return (List<Problem>) problemsQuery.getResultList();
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
