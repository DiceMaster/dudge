/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.web;

import dudge.DudgeLocal;
import dudge.PermissionCheckerRemote;
import dudge.ReportingLocal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Arengor
 */
public class AuthenticationObject {
	/**
	 * Получает AuthenticationObject из сессии.
	 * @param session http-сессия.
	 * @return AuthenticationObject, хранящийся в сессии, или новый если такового
	 * еще нет.
	 */
	public static AuthenticationObject extract(HttpServletRequest request) {
		AuthenticationObject ao = new AuthenticationObject();

		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return ao;
		}

		String userName = null;
		Date expirationDate = null;
		String key = null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(AuthenticationCookies.COOKIE_USER_NAME)) {
				userName = cookie.getValue();
			}

			if (cookie.getName().equals(AuthenticationCookies.COOKIE_EXPIRATION)) {
				DateFormat dateFormat = DateFormat.getInstance();

				try {
					expirationDate = dateFormat.parse(cookie.getValue());
				}
				catch (ParseException ex) {
				}
			}

			if (cookie.getName().equals(AuthenticationCookies.COOKIE_KEY)) {
				key = cookie.getValue();
			}
		}

		if (userName == null || expirationDate == null || key == null) {
			return ao;
		}

		if (key.equals(AuthenticationCookies.getCookieKey(userName, expirationDate))) {
			Date currentDate = new Date();
			if (currentDate.before(expirationDate)) {
				ao.setUsername(userName);
				DudgeLocal dudgeLocal = ServiceLocator.getInstance().lookupDudge();
				dudgeLocal.joinAllOpenContests(userName);
			}
		}

		return ao;
	}

	/**
	 * Имя аутентифицированного пользователя
	 * или null, если такового нет.
	 */
	private String username = null;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAuthenticated() {
		return this.username != null;
	}

	public PermissionCheckerRemote getPermissionChecker() {
		return ServiceLocator.getInstance().lookupPermissionChecker();
	}
        
        public ReportingLocal getReporting() {
		return ServiceLocator.getInstance().getReporter();
	}
}
