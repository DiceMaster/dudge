/*
 * LocaleAction.java
 *
 * Created on 08.11.2007, 18:20:52
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.actions;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Michael Antonov
 */
public class LocalesAction extends DispatchAction {

	protected static final Logger logger = Logger.getLogger(LocalesAction.class.toString());

	/**
	 * Устанавливает нужную локаль для пользовательского интерфейса.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward setLocale(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		String localeName = (String) request.getParameter("locale_name");
		Locale locale = new Locale(localeName);
		setLocale(request, locale);

		final String referrer = request.getHeader("referer");
		if (referrer != null && referrer.length() > 0) {
			try {
				//У нас есть информация о том, с какой страницы юзер пришёл. Посылаем его на неё обратно.
				response.sendRedirect(referrer);
				return null;
			} catch (IOException ex) {
				logger.severe(ex.getMessage());
				return null;
			}
		} else {
			//Непонятно откуда пришёл юзер. Отправляем его на главную страницу.
			return new ActionForward("/index.do", true);
		}

		/*ActionForward forward = new ActionForward();
		 forward.setPath(request.getHeader("referer"));
		 forward.setRedirect(true);
		 return forward;*/
	}
}