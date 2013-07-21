package dudge.slave;

import dudge.db.Solution;
import javax.ejb.Local;

/**
 * This is the business interface for Slave enterprise bean.
 */
@Local
public interface SlaveLocal {

	void testSolution(Solution solution) throws SlaveException;
}
