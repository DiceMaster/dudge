package dudge;

import dudge.db.Problem;
import dudge.db.User;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author virl
 */
@Local
public interface SearcherLocal {

	public List<User> searchUsers(String query, int limit);

	public List<Problem> searchProblems(String query, int limit);
}
