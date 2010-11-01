/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.web;

import java.text.DateFormat;
import java.text.ParseException;
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

    public static String getCookieKey(String userName, Date expiration) {
        int key = COOKIE_KEY_CODE;

        if (userName != null) {
            key += userName.hashCode();
        }

        if (expiration != null) {
            key += expiration.hashCode();
        }
        
        return Integer.toString(key);
    }

    private static void setCookies(
            String userName,
            Date expirationTime,
            int maxAge,
            HttpServletResponse response) {
        Cookie userCookie = new Cookie(COOKIE_USER_NAME, userName);
        userCookie.setMaxAge(maxAge);
        response.addCookie(userCookie);

        Cookie expirationCookie;
        if (expirationTime != null)
        {
            DateFormat dateFormat = DateFormat.getInstance();
            String expirationString = dateFormat.format(expirationTime);
            try {
                expirationTime = dateFormat.parse(expirationString);
            }
            catch (ParseException ex) {
            }
            expirationCookie = new Cookie(COOKIE_EXPIRATION, expirationString);
        }
        else
            expirationCookie = new Cookie(COOKIE_EXPIRATION, null);
        expirationCookie.setMaxAge(maxAge);
        response.addCookie(expirationCookie);

        String key = getCookieKey(userName, expirationTime);
        Cookie keyCookie = new Cookie(COOKIE_KEY, key);
        keyCookie.setMaxAge(maxAge);
        response.addCookie(keyCookie);
    }

    public static void setCookies(
            String userName,
            Date expirationTime,
            HttpServletResponse response) {
        setCookies(userName, expirationTime, COOKIE_EXPIRATION_TIME, response);
    }

    public static void removeCookies(
            HttpServletResponse response) {
        setCookies(null, null, 0, response);
    }
}
