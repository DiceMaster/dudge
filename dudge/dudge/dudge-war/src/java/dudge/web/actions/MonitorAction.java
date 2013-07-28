package dudge.web.actions;

import dudge.ContestLocal;
import dudge.DudgeLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.RoleType;
import dudge.logic.ContestTraits;
import dudge.monitor.AcmMonitorRecord;
import dudge.monitor.GlobalMonitorRecord;
import dudge.PermissionCheckerRemote;
import dudge.UserLocal;
import dudge.monitor.SchoolMonitorRecord;
import dudge.web.AuthenticationObject;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author virl
 */
public class MonitorAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(MonitorAction.class.toString());

	public MonitorAction() {
	}

	/**
	 *
	 * @return
	 */
	private DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:global/dudge/dudge-ejb/DudgeBean");//java:comp/env/ejb/DudgeBean
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	/**
	 *
	 * @return
	 */
	private UserLocal lookupUserBean() {
		try {
			Context c = new InitialContext();
			return (UserLocal) c.lookup("java:global/dudge/dudge-ejb/UserBean");//java:comp/env/ejb/UserBean
		} catch (NamingException ne) {
			logger.log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	/**
	 *
	 * @return
	 */
	private ContestLocal lookupContestBean() {
		try {
			Context c = new InitialContext();
			return (ContestLocal) c.lookup("java:global/dudge/dudge-ejb/ContestBean");//java:comp/env/ejb/ContestBean
		} catch (NamingException ne) {
			logger.log(Level.ALL, "exception caught", ne);
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
	public ActionForward showStatus(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("status");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ContestLocal contestBean = lookupContestBean();
		AuthenticationObject ao = AuthenticationObject.extract(request);

		int contestId;
		// Получаем идентификатор соревнования.
		String param = request.getParameter("contestId");
		if (param != null) {
			contestId = Integer.parseInt(param);
		} else // Если нам не послали идентификатор, то используем идентификатор
		// текущего соревнования.
		{
			contestId = contestBean.getDefaultContest().getContestId();
		}

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewMonitor(ao.getUsername(), contestId)) {
			return mapping.findForward("accessDenied");
		}

		// Получаем свойства соревнования.
		ContestTraits traits = contestBean.getContest(contestId).getTraits();

		//Редирект на страницу монитора.
		return mapping.findForward("monitor" + traits.getMonitorSuffix());
	}

	/**
	 * Метод для получения через AJAX данных монитора глобального соревнования. Возвращает в response нужные данные. Само соревнование задается через параметр
	 * contestId.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getGlobalMonitorData(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		//  Получаем идентификатор соревнования.
		int contestId = Integer.parseInt(request.getParameter("contestId"));
		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewMonitor(ao.getUsername(), contestId)) {
			return;
		}

		Contest contest = lookupContestBean().getContest(contestId);
		//GlobalTraits traits = (GlobalTraits) contest.getTraits();

		JSONArray jaRows = new JSONArray();

		// Администраторы соревнования видят монитор размороженным.
		boolean userIsContestAdmin = lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		List<GlobalMonitorRecord> monitorRows = lookupDudgeBean().getGlobalMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000)));

		for (GlobalMonitorRecord row : monitorRows) {
			JSONObject joRow = new JSONObject();

			try {
				joRow.put("user", row.getUser());
				joRow.put("place", row.getPlace());
				joRow.put("solvedProblems", row.getSolvedProblemsCount());
				joRow.put("rating", row.getRating());
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Creating JSON view of Solution object failed.", e);
				return;
			}

			jaRows.put(joRow);
		} // for row

		JSONObject joRoot = new JSONObject();
		try {
			joRoot.put("totalCount", monitorRows.size());
		} catch (JSONException e) {
			logger.log(Level.ALL, "exception caught", e);
			return;
		}

		try {
			joRoot.put("rows", jaRows);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
			return;
		}

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(joRoot);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Exception occured.", ex);
		}
	}

	/**
	 * Метод для получения через AJAX данных монитора соревнования ACM. Возвращает в response нужные данные. Само соревнование задается через параметр
	 * contestId.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getAcmMonitorData(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		//  Получаем идентификатор соревнования.
		int contestId = Integer.parseInt(request.getParameter("contestId"));
		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewMonitor(ao.getUsername(), contestId)) {
			return;
		}

		Contest contest = lookupContestBean().getContest(contestId);
		//AcmTraits traits = (AcmTraits) contest.getTraits();

		JSONArray jaRows = new JSONArray();

		// Администраторы соревнования видят монитор размороженным.
		boolean userIsContestAdmin = lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		List<AcmMonitorRecord> monitorRows = lookupDudgeBean().getAcmMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000)));

		for (AcmMonitorRecord row : monitorRows) {
			JSONObject joRow = new JSONObject();

			try {
				joRow.put("user", row.getUser());
				joRow.put("place", row.getPlace());

				for (ContestProblem contestProblem : contest.getContestProblems()) {
					// Данные ячейки в мониторе.
					String cellData = "";

					if (row.isSolved(contestProblem.getProblemMark())) {
						// Выводим в ячейку "плюс";
						cellData += "+";
					} else {
						cellData += "-";
					}

					// Выводим количество неуспешных попыток,
					// если таковые были.
					int unsucAttempts = row.getProblemTriesCount(contestProblem.getProblemMark());
					if (unsucAttempts != 0) {
						cellData += Integer.toString(unsucAttempts);
					}
					joRow.put("problem" + contestProblem.getProblemMark(), cellData);
				} // for contestProblem

				joRow.put("solvedProblems", row.getSolvedProblemsCount());
				// Выводим время в минутах.
				joRow.put("time", row.getTime() / (60 * 1000));
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Creating JSON view of Solution object failed.", e);
				return;
			}

			jaRows.put(joRow);
		} // for row

		JSONObject joRoot = new JSONObject();
		try {
			joRoot.put("totalCount", monitorRows.size());
		} catch (JSONException e) {
			logger.log(Level.ALL, "exception caught", e);
			return;
		}

		try {
			joRoot.put("rows", jaRows);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
			return;
		}

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(joRoot);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
		}
	}

	/**
	 * Метод для получения через AJAX данных монитора школьного соревнования. Возвращает в response нужные данные. Само соревнование задается через параметр
	 * contestId.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getSchoolMonitorData(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		//  Получаем идентификатор соревнования.
		int contestId = Integer.parseInt(request.getParameter("contestId"));

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewMonitor(ao.getUsername(), contestId)) {
			return;
		}

		Contest contest = lookupContestBean().getContest(contestId);

		JSONArray jaRows = new JSONArray();

		// Администраторы соревнования видят монитор размороженным.
		boolean userIsContestAdmin = lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		List<SchoolMonitorRecord> monitorRows = lookupDudgeBean().getSchoolMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000)));

		for (SchoolMonitorRecord row : monitorRows) {
			JSONObject joRow = new JSONObject();

			try {
				joRow.put("user", row.getUser());
				joRow.put("place", row.getPlace());

				for (ContestProblem contestProblem : contest.getContestProblems()) {
					// Данные ячейки в мониторе.
					String cellData = "";

					if (row.isSolved(contestProblem.getProblemMark())) {
						// Выводим в ячейку "плюс";
						cellData += "+";
					} else {
						cellData += "-";
					}

					// Выводим количество неуспешных попыток,
					// если таковые были.
					int unsucAttempts = row.getProblemTriesCount(contestProblem.getProblemMark());
					if (unsucAttempts != 0) {
						cellData += Integer.toString(unsucAttempts);
					}
					joRow.put("problem" + contestProblem.getProblemMark(), cellData);
				} // for contestProblem

				joRow.put("solvedProblems", row.getSolvedProblemsCount());
				// Выводим время в минутах.
				joRow.put("time", row.getTime() / (60 * 1000));
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "Creating JSON view of Solution object failed.", e);
				return;
			}

			jaRows.put(joRow);
		} // for row

		JSONObject joRoot = new JSONObject();
		try {
			joRoot.put("totalCount", monitorRows.size());
		} catch (JSONException e) {
			logger.log(Level.ALL, "exception caught", e);
			return;
		}

		try {
			joRoot.put("rows", jaRows);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
			return;
		}

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(joRoot);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
		}
	}
}
