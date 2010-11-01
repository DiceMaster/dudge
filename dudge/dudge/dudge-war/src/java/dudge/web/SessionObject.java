/*
 * SessionObject.java
 *
 * Created on June 15, 2007, 10:31 PM
 */

package dudge.web;

import dudge.PermissionCheckerRemote;
import dudge.DudgeLocal;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vladimir Shabanov
 */
public class SessionObject {
	
	/**
	 * Получает SessionObject из сессии.
	 * @param session http-сессия.
	 * @return SessionObject, хранящийся в сессии, или новый если такового
	 * еще нет.
	 */
	public static SessionObject extract(HttpSession session) {
		SessionObject so;
		if((so = (SessionObject) session.getAttribute("sessionObject")) == null)
		{
			so = new SessionObject();
			session.setAttribute("sessionObject", so);
		}

		return so;
	}
	
	/**
	 * Имя аутентифицированного пользователя
	 * или null, если такового нет.
	 */
	private String username = null;
	
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
	
	/** Creates a new instance of SessionObject */
	public SessionObject() {
	}

	public DudgeLocal getDudge() {
		return ServiceLocator.getInstance().lookupDudge();
	}

	public PermissionCheckerRemote getPermissionChecker() {
		return ServiceLocator.getInstance().lookupPermissionChecker();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAuthenticated() {
		return this.username != null;
	}

	public void extractCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return;
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
			return;
		}

		if (key.equals(AuthenticationCookies.getCookieKey(userName, expirationDate))) {
			Date currentDate = new Date();
			if (currentDate.before(expirationDate)) {
				username = userName;
			}
		}

		this.getDudge().joinAllOpenContests(username);
	} // extractCookies()
}
