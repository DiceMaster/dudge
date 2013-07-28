package dudge;

import java.io.File;
import java.io.IOException;
import javax.ejb.Local;

/**
 *
 * @author Mikhail Ivanov
 */
@Local
public interface ReportingLocal {

	public File printContestProtocol(int contestId) throws IOException;

	public File printContestParticipants(int contestId) throws IOException;
}
