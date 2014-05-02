/*
 * SolutionAction.java
 *
 * Created on June 19, 2007, 12:31 AM
 */
package dudge.web.actions;

import dudge.ContestLocal;
import dudge.PermissionCheckerRemote;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import dudge.db.User;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.SolutionsForm;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
 * Струтсовый экшен для операций с решениями.
 *
 * @author Vladimir Shabanov
 */
public class SolutionsAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(SolutionsAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	/**
	 * Creates a new instance of SolutionsAction
	 */
	public SolutionsAction() {
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		SolutionsForm sf = (SolutionsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);
		if (ao.getUsername() == null) {
			return mapping.findForward("loginRequired");
		}

		User user = serviceLocator.lookupUserBean().getUser(ao.getUsername());
		int solutionId;
		if (request.getParameter("solutionId") != null) {
			solutionId = Integer.parseInt(request.getParameter("solutionId"));
		} else {
			solutionId = sf.getSolutionId();
		}

		// Проверяем право пользователя на просмотр решения.
		if (!ao.getPermissionChecker().canViewSolution(user.getLogin(), solutionId)) {
			return mapping.findForward("accessDenied");
		}

		// TODO: нельзя доверять тому, что солюшен с таким id существует
		Solution solution = serviceLocator.lookupSolutionBean().getSolution(solutionId);

		sf.setSolutionId(solution.getSolutionId());
		sf.setContestId(solution.getContest().getContestId());
		sf.setContestName(solution.getContest().getCaption());
		sf.setProblemId(solution.getProblem().getProblemId());
		sf.setProblemName(solution.getProblem().getTitle());
		String mark = null;
		for (ContestProblem cp : solution.getContest().getContestProblems()) {
			if (cp.getProblem().getProblemId() == solution.getProblem().getProblemId()) {
				mark = cp.getProblemMark();
				break;
			}
		}
		sf.setProblemMark(mark);
		sf.setUserId(solution.getUser().getLogin());
		sf.setSubmitTime(solution.getSubmitTime().getTime());

		if (solution.getStatus() != SolutionStatus.PROCESSED || solution.getContest().getTraits().isRunAllTests() || solution.getLastRunResult() == null) {
			sf.setStatus(solution.getStatus().toString());
		} else {
			sf.setStatus(solution.getLastRunResult().toString());
		}

		try {
			sf.setCurrentTestNumber(Collections.max(solution.getRuns()).getRunNumber());
		} catch (NoSuchElementException e) {
			sf.setCurrentTestNumber(0);
		}

		sf.setStatusMessage(solution.getStatusMessage());
		sf.setSourceCode(solution.getSourceCode());

		return mapping.findForward("viewSolution");
	}
	
	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward mySolutions(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		AuthenticationObject ao = AuthenticationObject.extract(request);
		if (ao.getUsername() == null) {
			return mapping.findForward("loginRequired");
		}

		SolutionsForm sf = (SolutionsForm) af;
		ContestLocal contestBean = serviceLocator.lookupContestBean();
		Contest contest = contestBean.getContest(sf.getContestId());
		sf.setContestName(contest.getCaption());
		
		return mapping.findForward("mySolutions");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		SolutionsForm sf = (SolutionsForm) af;
		sf.reset(mapping, request);
		ContestLocal contestBean = serviceLocator.lookupContestBean();
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

		Contest contest = contestBean.getContest(contestId);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canSubmitSolution(ao.getUsername(), contest.getContestId())) {
			return mapping.findForward("accessDenied");
		}

		// Установление свойств, нужных для корректного отображение параметров языка соревнования.
		sf.getContestLanguages().addAll(contest.getContestLanguages());
		sf.getContestProblems().addAll(contest.getContestProblems());

		return mapping.findForward("submitSolution");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submitSubmit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		SolutionsForm sf = (SolutionsForm) af;
		ContestLocal contestBean = serviceLocator.lookupContestBean();
		AuthenticationObject ao = AuthenticationObject.extract(request);

		if (ao.getUsername() == null) {
			return mapping.findForward("loginRequired");
		}

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

		User user = serviceLocator.lookupUserBean().getUser(ao.getUsername());
		Contest contest = contestBean.getContest(contestId);
		Language language = serviceLocator.lookupLanguageBean().getLanguage(sf.getLanguageId());
		Problem problem = serviceLocator.lookupProblemBean().getProblem(sf.getProblemId());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (contest==null || problem==null || pcb==null ||
                    !pcb.canSubmitSolution(ao.getUsername(), contest.getContestId(), problem.getProblemId())) {
			return mapping.findForward("accessDenied");
		}

		Solution solution = new Solution();
		solution.setUser(user);
		solution.setContest(contest);
		solution.setLanguage(language);
		solution.setProblem(problem);
		solution.setSourceCode(sf.getSourceCode());

		solution = serviceLocator.lookupDudgeBean().submitSolution(solution);
		sf.setSolutionId(solution.getSolutionId());

		ActionForward viewRedirect = new ActionForward();
		viewRedirect.setPath("solutions.do?reqCode=view&solutionId=" + sf.getSolutionId());
		viewRedirect.setRedirect(true);
		return viewRedirect;
	}

	/**
	 * Метод для получения через AJAX статуса решения, где идентификатор решения задачется параметром solutionId.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void getSolutionStatus(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		int solutionId = Integer.parseInt((String) request.getParameter("solutionId"));
		Solution solution = serviceLocator.lookupSolutionBean().getSolution(solutionId);

		JSONObject jo = new JSONObject();
		String status = SolutionStatus.INTERNAL_ERROR.toString();
		int testNumber;
		try {
			testNumber = Collections.max(solution.getRuns()).getRunNumber();
		} catch (NoSuchElementException e) {
			testNumber = 0;
		}

		String statusMessage;
		if (solution != null) {
			if (solution.getStatus() != SolutionStatus.PROCESSED || solution.getContest().getTraits().isRunAllTests() || solution.getLastRunResult() == null) {
				status = solution.getStatus().toString();
			} else {
				status = solution.getLastRunResult().toString();
			}
			statusMessage = solution.getStatusMessage();
		} else {
			statusMessage = "Solution object is null";
		}
		try {
			jo.put("status", status);
			jo.put("currentTestNumber", testNumber);
			jo.put("statusMessage", statusMessage);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
		}
	}

	/**
	 * Метод для получения через AJAX списка отправленных в систему решений.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getLastSolutions(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		String iDisplayStartString = (String) request.getParameter("iDisplayStart");
		String iDisplayLengthString = (String) request.getParameter("iDisplayLength");
		int iDisplayStart = iDisplayStartString == null ? -1 : Integer.parseInt(iDisplayStartString);
		int iDisplayLength = iDisplayLengthString == null ? -1 : Integer.parseInt(iDisplayLengthString);


		List<Solution> solutions = serviceLocator.lookupSolutionBean().getSolutions(iDisplayStart, iDisplayLength);
		long solutionsCount = serviceLocator.lookupSolutionBean().getSolutionsCount();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("sEcho", request.getParameter("sEcho"));
			jo.put("iTotalRecords", solutionsCount);
			jo.put("iTotalDisplayRecords", solutionsCount);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (Solution sol : solutions) {
			JSONArray json = new JSONArray();

			// Заполняем данными пользователя созданный объект JSON.
			json.put(sol.getSolutionId());
			json.put(sol.getSubmitTime().getTime());
			json.put(sol.getUser().getLogin());
			json.put(sol.getContest().getContestId());
			json.put(sol.getContest().getCaption());
			
			String mark = null;
			for (ContestProblem cp : sol.getContest().getContestProblems()) {
				if (cp.getProblem().getProblemId() == sol.getProblem().getProblemId()) {
					mark = cp.getProblemMark();
					break;
				}
			}
			json.put(sol.getProblem().getProblemId());
			if (mark != null) {
				json.put(mark + " - " + sol.getProblem().getTitle());
			} else {
				json.put(sol.getProblem().getTitle());
			}

			json.put(sol.getLanguage().getLanguageId());
			if (sol.getStatus() != SolutionStatus.PROCESSED || sol.getLastRunResult() == null) {
				json.put(sol.getStatus().toString());
			} else {
				json.put(sol.getLastRunResult().toString());
			}
			
			int testNumber;
			try {
				testNumber = Collections.max(sol.getRuns()).getRunNumber();
			} catch (NoSuchElementException e) {
				testNumber = 0;
			}
			json.put(testNumber);

			ja.put(json);
		}
		
		try {
			jo.put("aaData", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Exception occured.", e);
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
