package dudge;

import dudge.db.Problem;
import dudge.db.Test;
import java.util.Collections;
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
public class TestBean implements TestLocal {

	private static final Logger logger = Logger.getLogger(TestBean.class.toString());
	@PersistenceContext(unitName = "dudge-ejbPU")
	private EntityManager em;

	/**
	 *
	 * @param testId
	 * @return
	 */
	@Override
	public Test getTest(int testId) {
		return (Test) em.createNamedQuery("Test.findByTestId").setParameter("testId", testId).getSingleResult();
	}

	/**
	 *
	 * @param test
	 * @return
	 */
	@Override
	public Test addTest(Test test) {
		Problem problem = em.find(Problem.class, test.getProblem().getProblemId());
		problem.getTests().add(test);

		// Помечаем задачу как нездоровую.
		problem.setHealthy(false);
		em.flush();

		return test;
	}

	/**
	 *
	 * @param test
	 */
	@Override
	public void modifyTest(Test test) {
		Test merged = em.merge(test);

		// Помечаем задачу как нездоровую.
		merged.getProblem().setHealthy(false);

		em.flush();
		em.refresh(merged.getProblem());
	}

	/**
	 *
	 * @param testId
	 */
	@Override
	public void deleteTest(int testId) {

		Problem problem = em.find(Test.class, testId).getProblem();
		int problemId = problem.getProblemId();
		int testNumber = em.find(Test.class, testId).getTestNumber();
		em.remove(em.find(Test.class, testId));

		problem.setTests((List<Test>) em.createNamedQuery("Test.findByProblemId").setParameter("problemId", problemId).getResultList());
		Collections.sort((List<Test>) problem.getTests());

		int testCount = problem.getTests().size();
		for (int i = testNumber + 1; i <= testCount + 1; i++) {
			int index = ((List<Test>) problem.getTests()).indexOf((Test) em.createNamedQuery("Test.findByNumberAndProblemId")
					.setParameter("testNumber", i).setParameter("problemId", problemId).getSingleResult());
			((List<Test>) problem.getTests()).get(index).setTestNumber(i - 1);
		}

		em.merge(problem);
		em.flush();
	}
}
