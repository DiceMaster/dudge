/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.forms;

/**
 *
 * @author Mikhail
 */
public class ReportsForm extends org.apache.struts.action.ActionForm {

	public static final long serialVersionUID = 1L;
	private int contestId;

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
}
