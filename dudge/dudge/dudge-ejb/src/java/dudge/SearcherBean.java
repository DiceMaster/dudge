package dudge;

import dudge.db.Problem;
import dudge.db.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vladimir Shabanov
 */
@Stateless
public class SearcherBean implements SearcherLocal {
	@PersistenceContext
	private EntityManager em;

	private void persist(Object object) {
		em.persist(object);
		em.flush();
	}
	
	public List<User> searchUsers(String query, int limit) {
		if(query.length() == 0)
			return new ArrayList<User>();
		
		return
		em.createNativeQuery(
			"SELECT * FROM users " +
			"WHERE login LIKE ?1 " +
			"OR real_name LIKE ?1 " +
			"OR organization LIKE ?1 " +
			"ORDER BY login"
			, User.class)
		.setParameter(1, "%" + query + "%")
		.setMaxResults(limit)
		.getResultList();
	}

	public List<Problem> searchProblems(String query, int limit) {
		if(query.length() == 0)
			return new ArrayList<Problem>();
		
		return
		em.createNativeQuery(
			"SELECT * FROM problems " +
			"WHERE title LIKE ?1 " +
			"OR description LIKE ?1 " +
			"OR author LIKE ?1 " +
			"OR description LIKE ?1 " +
			"ORDER BY title,problem_id"
			, Problem.class)
		.setParameter(1, "%" + query + "%")
		.setMaxResults(limit)
		.getResultList();
	}	
}
