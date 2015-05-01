package dudge.web.actions;

import dudge.ContestLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.RoleType;
import dudge.logic.ContestTraits;
import dudge.monitor.AcmMonitorRecord;
import dudge.monitor.GlobalMonitorRecord;
import dudge.PermissionCheckerRemote;
import dudge.monitor.SchoolMonitorRecord;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.MonitorForm;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	public MonitorAction() {
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

		AuthenticationObject ao = AuthenticationObject.extract(request);
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAdmin(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}
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

		ContestLocal contestBean = serviceLocator.lookupContestBean();
		AuthenticationObject ao = AuthenticationObject.extract(request);
		MonitorForm mf = (MonitorForm) af;

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

		Contest contest = contestBean.getContest(contestId);

		mf.reset(mapping, request);
		mf.setContestId(contestId);
		mf.setContestCaption(contest.getCaption());
		for (ContestProblem contestProblem : contest.getContestProblems()) {
			mf.getProblems().add(contestProblem);
		}

		// Получаем свойства соревнования.
		ContestTraits traits = contest.getTraits();

		//Редирект на страницу монитора.
		return mapping.findForward("monitor" + traits.getMonitorSuffix());
	}

	/**
	 * Метод для получения через AJAX данных монитора глобального соревнования.
	 * Возвращает в response нужные данные. Само соревнование задается через
	 * параметр contestId.
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

		Contest contest = serviceLocator.lookupContestBean().getContest(contestId);
		//GlobalTraits traits = (GlobalTraits) contest.getTraits();

		boolean userIsContestAdmin = serviceLocator.lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		Date freezeTime = new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000));
		List<GlobalMonitorRecord> monitorRows = serviceLocator.lookupDudgeBean().getGlobalMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: freezeTime);
	
		boolean isFrozen = !userIsContestAdmin && !contest.isInfinite() && new Date().after(freezeTime);
		Date updateTime = isFrozen ? freezeTime : new Date();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("sEcho", request.getParameter("sEcho"));
			jo.put("iTotalRecords", monitorRows.size());
			jo.put("iTotalDisplayRecords", monitorRows.size());
			jo.put("frozen", isFrozen);
			jo.put("updateTime", updateTime.getTime());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (GlobalMonitorRecord row : monitorRows) {
			JSONArray jRow = new JSONArray();
			
			jRow.put(row.getPlace());
			jRow.put(row.getUser());
			jRow.put(row.getSolvedProblemsCount());
			jRow.put(row.getRating());

			ja.put(jRow);
		} // for row

		try {
			jo.put("aaData", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Exception occured.", ex);
		}
	}

	/**
	 * Метод для получения через AJAX данных монитора соревнования ACM.
	 * Возвращает в response нужные данные. Само соревнование задается через
	 * параметр contestId.
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

		Contest contest = serviceLocator.lookupContestBean().getContest(contestId);
		//AcmTraits traits = (AcmTraits) contest.getTraits();

		// Администраторы соревнования видят монитор размороженным.
		boolean userIsContestAdmin = serviceLocator.lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		Date freezeTime = new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000));
		List<AcmMonitorRecord> monitorRows = serviceLocator.lookupDudgeBean().getAcmMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: freezeTime);
	
		boolean isFrozen = !userIsContestAdmin && !contest.isInfinite() && new Date().after(freezeTime);
		Date updateTime = isFrozen ? freezeTime : new Date();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("sEcho", request.getParameter("sEcho"));
			jo.put("iTotalRecords", monitorRows.size());
			jo.put("iTotalDisplayRecords", monitorRows.size());
			jo.put("frozen", isFrozen);
			jo.put("updateTime", updateTime.getTime());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (AcmMonitorRecord row : monitorRows) {
			JSONArray json = new JSONArray();

			// Заполняем данными пользователя созданный JSON массив.

			json.put(row.getPlace());
			json.put(row.getUser());
			json.put(row.getSolvedProblemsCount());

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
				json.put(cellData);
			} // for contestProblem

			// Выводим время в минутах.
			json.put(row.getTime() / (60 * 1000));
			
			ja.put(json);
		} // for row

		try {
			jo.put("aaData", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}
	}

	/**
	 * Метод для получения через AJAX данных монитора школьного соревнования.
	 * Возвращает в response нужные данные. Само соревнование задается через
	 * параметр contestId.
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

		Contest contest = serviceLocator.lookupContestBean().getContest(contestId);

		// Администраторы соревнования видят монитор размороженным.
		boolean userIsContestAdmin = serviceLocator.lookupUserBean().isInRole(ao.getUsername(), contest.getContestId(), RoleType.ADMINISTRATOR);

		Date freezeTime = new Date(Long.valueOf(contest.getEndTime().getTime() - contest.getFreezeTime() * 1000));
		List<SchoolMonitorRecord> monitorRows = serviceLocator.lookupDudgeBean().getSchoolMonitorRecords(
				contest, (userIsContestAdmin || contest.isInfinite()) ? null
				: freezeTime);
	
		boolean isFrozen = !userIsContestAdmin && !contest.isInfinite() && new Date().after(freezeTime);
		Date updateTime = isFrozen ? freezeTime : new Date();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("sEcho", request.getParameter("sEcho"));
			jo.put("iTotalRecords", monitorRows.size());
			jo.put("iTotalDisplayRecords", monitorRows.size());
			jo.put("frozen", isFrozen);
			jo.put("updateTime", updateTime.getTime());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (SchoolMonitorRecord row : monitorRows) {
			JSONArray jRow = new JSONArray();

			jRow.put(row.getPlace());
			jRow.put(row.getUser());			
			jRow.put(row.getSolvedProblemsCount());
			
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
				jRow.put(cellData);
			} // for contestProblem
			
			// Выводим время в минутах.
			jRow.put(row.getTime() / (60 * 1000));

			ja.put(jRow);
		} // for row

		try {
			jo.put("aaData", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}
		
		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
		}
	}
}
