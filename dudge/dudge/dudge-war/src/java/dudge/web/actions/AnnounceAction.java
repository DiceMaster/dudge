/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.actions;

import dudge.DudgeLocal;
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

	protected static final Logger logger = Logger.getLogger(AnnounceAction.class.toString());

	public AnnounceAction() {
	}

	private DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:comp/env/ejb/DudgeBean");
		} catch (NamingException ne) {
			logger.log(Level.ALL, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	public ActionForward makeAnnounce(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		DudgeLocal dudgeBean = lookupDudgeBean();
		AnnounceForm anf = (AnnounceForm) af;

		anf.setDudgeBean(dudgeBean);

		List<Contest> activeContests = dudgeBean.getActiveContests();
		Collections.sort(activeContests);
		anf.setActiveContests(activeContests);
		List<Contest> pendingContests = dudgeBean.getPendingContests();
		Collections.sort(pendingContests);
		anf.setPendingContests(pendingContests);
		List<Contest> recentlyFinishedContests = dudgeBean.getRecentlyFinishedContests();
		Collections.sort(recentlyFinishedContests);
		anf.setRecentlyFinishedContests(recentlyFinishedContests);

		return mapping.findForward("announce");
	}
}
