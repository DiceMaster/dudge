package dudge.web.actions;

import dudge.PermissionCheckerRemote;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.RulesForm;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Aleksandr Tsyganov
 */
public class RulesAction extends DispatchAction {
	private final ServiceLocator serviceLocator = ServiceLocator.getInstance();
	
	public RulesAction() {
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		RulesForm rf = (RulesForm) af;

		String rules = serviceLocator.lookupDudgeBean().getRules();

		rf.reset(mapping, request);
		rf.setRules(rules);
		if (rules == null || rules.isEmpty()) {
			rf.setNotFilled(true);
		}

		return mapping.findForward("viewRules");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);

		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canEditRules(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}
		
		RulesForm rf = (RulesForm) af;

		String rules = serviceLocator.lookupDudgeBean().getRules();

		rf.reset(mapping, request);
		rf.setRules(rules);
		if (rules == null || rules.isEmpty()) {
			rf.setNotFilled(true);
		}
		
		return mapping.findForward("editRules");
	}
	
	public ActionForward submitEdit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);

		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canEditRules(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}
		
		RulesForm rf = (RulesForm) af;
		serviceLocator.lookupDudgeBean().setRules(rf.getRules());
		
		ActionForward forward = new ActionForward();
		forward.setPath("rules.do?reqCode=view");
		forward.setRedirect(true);
		return forward;
	}
}
