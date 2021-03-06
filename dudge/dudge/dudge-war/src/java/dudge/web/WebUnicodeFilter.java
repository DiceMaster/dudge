/*
 * WebUnicodeFilter.java
 *
 * Created on 05.11.2007, 18:37:19
 *
 */
package dudge.web;

import java.io.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Michael Antonov
 */
public class WebUnicodeFilter implements Filter {

	private static final String defaultEncoding = "UTF-8";

	public WebUnicodeFilter() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(defaultEncoding);
		response.setCharacterEncoding(defaultEncoding);
		chain.doFilter(request, response);
	}

	/**
	 * Destroy method for this filter
	 *
	 */
	@Override
	public void destroy() {
	}

	/**
	 * Init method for this filter
	 *
	 */
	@Override
	public void init(FilterConfig filterConfig) {
	}
}