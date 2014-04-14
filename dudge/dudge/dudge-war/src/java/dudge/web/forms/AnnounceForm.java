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
	private Contest highlightedContest;
	private boolean noContests;
	private List<Contest> activeContests = Collections.synchronizedList(new ArrayList<Contest>());
	private List<Contest> pendingContests = Collections.synchronizedList(new ArrayList<Contest>());
	private List<Contest> recentlyFinishedContests = Collections.synchronizedList(new ArrayList<Contest>());
	private List<Contest> globalContests = Collections.synchronizedList(new ArrayList<Contest>());

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

	/**
	 * @return the highlightedContest
	 */
	public Contest getHighlightedContest() {
		return highlightedContest;
	}

	/**
	 * @param highlightedContest the highlightedContest to set
	 */
	public void setHighlightedContest(Contest highlightedContest) {
		this.highlightedContest = highlightedContest;
	}

	/**
	 * @return the globalContests
	 */
	public List<Contest> getGlobalContests() {
		return globalContests;
	}

	/**
	 * @param globalContests the globalContests to set
	 */
	public void setGlobalContests(List<Contest> globalContests) {
		this.globalContests = globalContests;
	}

	/**
	 * @return the noContests
	 */
	public boolean isNoContests() {
		return noContests;
	}

	/**
	 * @param noContests the noContests to set
	 */
	public void setNoContests(boolean noContests) {
		this.noContests = noContests;
	}
}
