package dudge.web.actions;

import dudge.ContestLocal;
import dudge.db.Contest;
import dudge.web.ServiceLocator;
import dudge.web.forms.AnnounceForm;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Mikhail_Ivanov
 */
public class AnnounceAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(AnnounceAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	public AnnounceAction() {
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward makeAnnounce(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ContestLocal contestBean = serviceLocator.lookupContestBean();
		AnnounceForm anf = (AnnounceForm) af;

		anf.setContestBean(contestBean);

		List<Contest> activeContests = contestBean.getActiveContests();
		List<Contest> pendingContests = contestBean.getPendingContests();
		List<Contest> finishedContests = contestBean.getRecentlyFinishedContests();

		anf.setNoContests(activeContests.isEmpty() &&
		                  pendingContests.isEmpty() &&
		                  finishedContests.isEmpty());
		
		if (activeContests.size() > 0) {
			anf.setHighlightedContest(activeContests.get(0));
		} else if (pendingContests.size() > 0) {
			anf.setHighlightedContest(pendingContests.get(0));
		} else if (finishedContests.size() > 0) {
			anf.setHighlightedContest(finishedContests.get(0));
		} else {
			anf.setHighlightedContest(null);
		}
		
		anf.setActiveContests(activeContests);
		anf.setPendingContests(pendingContests);
		anf.setRecentlyFinishedContests(finishedContests);
		anf.setGlobalContests(contestBean.getGlobalContests());
		
		return mapping.findForward("announce");
	}
}
