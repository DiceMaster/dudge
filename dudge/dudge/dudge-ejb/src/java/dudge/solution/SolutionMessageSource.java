package dudge.solution;

import dudge.db.RunResultType;
import dudge.db.SolutionStatus;

/**
 *
 * @author Arengor
 */
public interface SolutionMessageSource {
	String getSolutionStatusMessage(SolutionStatus status);
	String getRunResultMessage(RunResultType runResult);
	String getOnTestMessage();
}
