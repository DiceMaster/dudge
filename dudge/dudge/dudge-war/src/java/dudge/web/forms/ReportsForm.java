package dudge.web.forms;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mikhail Ivanov
 */
public class ReportsForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	private int contestId;

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
}
