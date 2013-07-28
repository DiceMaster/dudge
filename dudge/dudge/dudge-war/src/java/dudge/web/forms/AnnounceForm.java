package dudge.web.forms;

import dudge.ContestLocal;
import dudge.db.Contest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mikhail_Ivanov
 */
public class AnnounceForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	private ContestLocal contestBean;
	private List<Contest> activeContests = Collections.synchronizedList(new ArrayList<Contest>());
	private List<Contest> pendingContests = Collections.synchronizedList(new ArrayList<Contest>());
	private List<Contest> recentlyFinishedContests = Collections.synchronizedList(new ArrayList<Contest>());

	public AnnounceForm() {
	}

	/**
	 *
	 * @return
	 */
	public ContestLocal getContestBean() {
		return contestBean;
	}

	/**
	 *
	 * @param contestBean
	 */
	public void setContestBean(ContestLocal contestBean) {
		this.contestBean = contestBean;
	}

	/**
	 *
	 * @return
	 */
	public List<Contest> getActiveContests() {
		return activeContests;
	}

	/**
	 *
	 * @param activeContests
	 */
	public void setActiveContests(List<Contest> activeContests) {
		this.activeContests = activeContests;
	}

	/**
	 *
	 * @return
	 */
	public List<Contest> getPendingContests() {
		return pendingContests;
	}

	/**
	 *
	 * @param pendingContests
	 */
	public void setPendingContests(List<Contest> pendingContests) {
		this.pendingContests = pendingContests;
	}

	/**
	 *
	 * @return
	 */
	public List<Contest> getRecentlyFinishedContests() {
		return recentlyFinishedContests;
	}

	/**
	 *
	 * @param recentlyFinishedContests
	 */
	public void setRecentlyFinishedContests(List<Contest> recentlyFinishedContests) {
		this.recentlyFinishedContests = recentlyFinishedContests;
	}
}
