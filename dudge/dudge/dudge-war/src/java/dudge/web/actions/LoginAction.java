/*
 * LoginAction.java
 *
 * Created on June 10, 2007, 10:31 PM
 */
package dudge.web.actions;

import dudge.web.AuthenticationCookies;
import dudge.web.ServiceLocator;
import dudge.web.forms.LoginForm;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Экшн для обработки входа и выхода пользователей системы.
 *
 * @author Vladimir Shabanov
 */
public class LoginAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(LoginAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	/**
	 * Creates a new instance of LoginAction
	 */
	public LoginAction() {
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward login(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		LoginForm lf = (LoginForm) af;
		if (serviceLocator.lookupUserBean().authenticate(lf.getUsername(), lf.getPassword())) {
			lf.setPassword("");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, 6);
			Date expirationTime = calendar.getTime();

			AuthenticationCookies.setCookies(lf.getUsername(), expirationTime, response);

			ActionForward forward = new ActionForward();
			forward.setPath(request.getHeader("referer"));
			forward.setRedirect(true);

			return forward;
		}

		return mapping.findForward("fail");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		AuthenticationCookies.removeCookies(response);

		ActionForward forward = new ActionForward();
		forward.setPath(request.getHeader("referer"));
		forward.setRedirect(true);
		return forward;
	}
}
