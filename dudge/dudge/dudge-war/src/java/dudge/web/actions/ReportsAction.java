package dudge.web.actions;

import dudge.ReportingLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
 *
 * @author Mikhail
 */
public class ReportsAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(ReportsAction.class.toString());

	/**
	 *
	 * @return
	 */
	private ReportingLocal lookupReportingBean() {
		try {
			Context c = new InitialContext();
			return (ReportingLocal) c.lookup("java:global/dudge/dudge-ejb/ReportingBean");//java:comp/env/ReportingBean
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	/**
	 * Метод для перехода на страницу генерации отчётности
	 */
	public ActionForward list(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("reports");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward printCurrentContestProtocol(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		FileInputStream in = null;
		try {
			ReportingLocal reportingBean = lookupReportingBean();
			File report = reportingBean.printContestProtocol(Integer.parseInt(request.getSession().getAttribute("contestId").toString()));
			if (report != null) {
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment;" + " filename=" + new String(report.getName().getBytes("UTF-8"), "ISO-8859-1"));
				OutputStream out = response.getOutputStream();
				in = new FileInputStream(report);
				byte[] buffer = new byte[4096];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				out.flush();
				return null;
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, null, ex);
				}
			}
		}
		return mapping.findForward("reports");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward printCurrentContestParticipants(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		FileInputStream in = null;
		try {
			ReportingLocal reportingBean = lookupReportingBean();
			File report = reportingBean.printContestParticipants(Integer.parseInt(request.getSession().getAttribute("contestId").toString()));
			if (report != null) {
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment;" + " filename=" + new String(report.getName().getBytes("UTF-8"), "ISO-8859-1"));
				OutputStream out = response.getOutputStream();
				in = new FileInputStream(report);
				byte[] buffer = new byte[4096];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				out.flush();
				return null;
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, null, ex);
			}
		}
		return mapping.findForward("reports");
	}
}