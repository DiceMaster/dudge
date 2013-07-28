package dudge.web.actions;

import dudge.ContestLocal;
import dudge.db.Contest;
import dudge.web.forms.AnnounceForm;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

	public AnnounceAction() {
	}
	
	private ContestLocal lookupContestBean() {
		try {
			Context c = new InitialContext();
			return (ContestLocal) c.lookup("java:global/dudge/dudge-ejb/ContestBean");//java:comp/env/ejb/ContestBean
		} catch (NamingException ne) {
			logger.log(Level.ALL, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	public ActionForward makeAnnounce(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ContestLocal contestBean = lookupContestBean();
		AnnounceForm anf = (AnnounceForm) af;

		anf.setContestBean(contestBean);

		List<Contest> activeContests = contestBean.getActiveContests();
		Collections.sort(activeContests);
		anf.setActiveContests(activeContests);
		List<Contest> pendingContests = contestBean.getPendingContests();
		Collections.sort(pendingContests);
		anf.setPendingContests(pendingContests);
		List<Contest> recentlyFinishedContests = contestBean.getRecentlyFinishedContests();
		Collections.sort(recentlyFinishedContests);
		anf.setRecentlyFinishedContests(recentlyFinishedContests);

		return mapping.findForward("announce");
	}
}
