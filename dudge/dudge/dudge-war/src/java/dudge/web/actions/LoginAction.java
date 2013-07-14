/*
 * LoginAction.java
 *
 * Created on June 10, 2007, 10:31 PM
 */

package dudge.web.actions;

import dudge.DudgeLocal;
import dudge.web.AuthenticationCookies;
import dudge.web.forms.LoginForm;
import java.util.Calendar;
import java.util.Date;
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
 * Экшн для обработки входа и выхода пользователей системы.
 * @author Vladimir Shabanov
 */
public class LoginAction extends DispatchAction {

	/** Creates a new instance of LoginAction */
	public LoginAction() {
	}

	/**
         * 
         * @return 
         */
	private DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:comp/env/ejb/DudgeBean");
		}
		catch(NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
			throw new RuntimeException(ne);
		}
	}

	/**
         * 
         * @param mapping
         * @param af
         * @param request
         * @param response
         * @return 
         */
	public ActionForward login(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LoginForm lf = (LoginForm) af;
		if(lookupDudgeBean()
			.authenticate(lf.getUsername(), lf.getPassword()) )
		{
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
	public ActionForward logout(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {

		AuthenticationCookies.removeCookies(response);

		ActionForward forward = new ActionForward();
		forward.setPath(request.getHeader("referer"));
		forward.setRedirect(true);
		return forward;
	}	
	
}
