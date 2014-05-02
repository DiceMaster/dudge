package dudge.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Arengor
 */
public class AuthenticationCookies {

	private static final int COOKIE_EXPIRATION_TIME = 6 * 60 * 60;
	private static final int COOKIE_KEY_CODE = 123456789;
	public static final String COOKIE_USER_NAME = "UserName";
	public static final String COOKIE_EXPIRATION = "Expiration";
	public static final String COOKIE_KEY = "Key";

	public static String getCookieKey(String userName, long expiration) {
		int key = COOKIE_KEY_CODE;

		if (userName != null) {
			key += userName.hashCode();
		}

		key += Long.valueOf(expiration).hashCode();

		return Integer.toString(key);
	}

	public static void setCookies(String userName, HttpServletResponse response) {
		Cookie userCookie = new Cookie(COOKIE_USER_NAME, userName);
		userCookie.setMaxAge(COOKIE_EXPIRATION_TIME);
		response.addCookie(userCookie);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, COOKIE_EXPIRATION_TIME);
		long expirationTime = calendar.getTime().getTime();
				
		Cookie expirationCookie = new Cookie(COOKIE_EXPIRATION, String.valueOf(expirationTime));
		expirationCookie.setMaxAge(COOKIE_EXPIRATION_TIME);
		response.addCookie(expirationCookie);

		String key = getCookieKey(userName, expirationTime);
		Cookie keyCookie = new Cookie(COOKIE_KEY, key);
		keyCookie.setMaxAge(COOKIE_EXPIRATION_TIME);
		response.addCookie(keyCookie);
	}

	public static void removeCookies(HttpServletResponse response) {
		Cookie userCookie = new Cookie(COOKIE_USER_NAME, null);
		userCookie.setMaxAge(0);
		response.addCookie(userCookie);
		Cookie expirationCookie = new Cookie(COOKIE_EXPIRATION, null);
		expirationCookie.setMaxAge(0);
		response.addCookie(expirationCookie);
		Cookie keyCookie = new Cookie(COOKIE_KEY, null);
		keyCookie.setMaxAge(0);
		response.addCookie(keyCookie);
	}
}
