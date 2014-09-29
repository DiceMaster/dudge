/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.web;

import dudge.db.RunResultType;
import dudge.db.SolutionStatus;
import dudge.solution.SolutionMessageSource;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 *
 * @author Arengor
 */
public class SolutionMessageProvider implements SolutionMessageSource {
	MessageResources messageResources;
	Locale locale;
	
	public SolutionMessageProvider(HttpServletRequest request) {
		messageResources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
		locale = RequestUtils.getUserLocale(request, null);
	}
	
	public static SolutionMessageProvider getInstance(HttpServletRequest request) {
		return new SolutionMessageProvider(request);
	}
	
	@Override
	public String getSolutionStatusMessage(SolutionStatus status) {
		return messageResources.getMessage(locale, "solution.status." + status.toString());
	}

	@Override
	public String getRunResultMessage(RunResultType runResult) {
		return messageResources.getMessage(locale, "solution.status." + runResult.toString());
	}

	@Override
	public String getOnTestMessage() {
		return messageResources.getMessage(locale, "solution.onTest");
	}
	
}
