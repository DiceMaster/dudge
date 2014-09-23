/*
 * ProblemsAction.java
 *
 * Created on June 12, 2007, 4:15 PM
 */
package dudge.web.actions;

import dudge.PermissionCheckerRemote;
import dudge.ProblemLocal;
import dudge.TestLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Problem;
import dudge.db.Test;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.ProblemsForm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Vladimir Shabanov
 */
public class ProblemsAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(ProblemsAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	private static final String[] columns = {
		"problemId",
		"title",
		"author",
		"createTime",
		"healthy"
	};
	
	/**
	 * Creates a new instance of ProblemsAction
	 */
	public ProblemsAction() {
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward list(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;
		pf.getProblems().clear();
		pf.getContestProblems().clear();

		//TODO: Проверять права пользователя на получение списка задач и их "скрытность".
		pf.getProblems().addAll(serviceLocator.lookupProblemBean().getProblems());

		return mapping.findForward("problems");
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

		ProblemsForm pf = (ProblemsForm) af;

		int problemId = Integer.parseInt(request.getParameter("problemId"));
		Problem problem = serviceLocator.lookupProblemBean().getProblem(problemId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewProblem(ao.getUsername(), problemId)) {
			return mapping.findForward("accessDenied");
		}

		pf.setTitle(problem.getTitle());
		pf.setDescription(problem.getDescription());
		pf.setCpuTimeLimit(problem.getCpuTimeLimit());
		pf.setRealTimeLimit(problem.getRealTimeLimit());
		pf.setMemoryLimit(problem.getMemoryLimit());
		pf.setOutputLimit(problem.getOutputLimit());

		pf.setOwner(problem.getOwner().getLogin());
		pf.setAuthor(problem.getAuthor());
		pf.setCreateTime(problem.getCreateTime());

		try {
			pf.setExampleInputTest(((List<Test>) problem.getTests()).get(0).getInputData());
			pf.setExampleOutputTest(((List<Test>) problem.getTests()).get(0).getOutputData());
		} catch (IndexOutOfBoundsException ex) {
		}

		return mapping.findForward("viewProblem");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getProblemList(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {		
		//  Получаем из запроса, какие данные требуются клиенту.
		String iDisplayStartString = (String) request.getParameter("iDisplayStart");
		String iDisplayLengthString = (String) request.getParameter("iDisplayLength");
		int iDisplayStart = iDisplayStartString == null ? -1 : Integer.parseInt(iDisplayStartString);
		int iDisplayLength = iDisplayLengthString == null ? -1 : Integer.parseInt(iDisplayLengthString);

		String searchString = (String) request.getParameter("sSearch");
		if (searchString != null && searchString.isEmpty()) {
		   searchString = null;
		}

		String order = null;
		boolean descending = false;
		if (request.getParameter("iSortCol_0") != null)
		{
			int iColumn = Integer.parseInt(request.getParameter("iSortCol_0"));
			if (request.getParameter("bSortable_" + iColumn).equals("true"))
			{
					order = columns[iColumn];
					descending = request.getParameter("sSortDir_0").equals("desc");
			}
		}

		List<Problem> problems = serviceLocator.lookupProblemBean().getProblems(
			order,
			descending
		);
		
		// Проверяем право пользователя.
		AuthenticationObject ao = AuthenticationObject.extract(request);
		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		// Отсеиваем из списка задач те, которые пользователь видеть в общем списке задач не должен.
		List<Problem> visibleProblems = new ArrayList<>();
		for (Problem problem : problems) {
			if (pcb.canViewProblem(ao.getUsername(), problem.getProblemId())) {
				visibleProblems.add(problem);
			}
		}

		int totalCount = visibleProblems.size();

		List<Problem> filteredProblems = visibleProblems;
		
		if (searchString != null) {
			filteredProblems = new ArrayList<>();
			for (Problem problem : visibleProblems) {
				if (Integer.toString(problem.getProblemId()).contains(searchString) ||
					problem.getTitle().toLowerCase().contains(searchString.toLowerCase()) ||
					problem.getAuthor().toLowerCase().contains(searchString.toLowerCase())) {
					
					filteredProblems.add(problem);
				}
			}
		}
		
		int filteredCount = filteredProblems.size();
		
		if (iDisplayStart + iDisplayLength > filteredProblems.size()) {
			iDisplayLength = filteredProblems.size() - iDisplayStart;
		}

		List<Problem> selectedProblems;
		if (iDisplayStart >= 0 && iDisplayLength > 0) {
			selectedProblems = filteredProblems.subList(iDisplayStart, iDisplayStart + iDisplayLength);
		} else {
			selectedProblems = filteredProblems;
		}
		
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		
		long totalProblemsCount = serviceLocator.lookupProblemBean().getProblemsCount();
		try {
			jo.put("sEcho", request.getParameter("sEcho"));
			jo.put("iTotalRecords", totalCount);
			jo.put("iTotalDisplayRecords", filteredCount);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (Problem problem : selectedProblems) {
			ja.put(this.getProblemJSONView(problem, ao));
		}
		
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
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getProblem(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {		
		String problemIdString = (String) request.getParameter("problemId");
		int problemId = problemIdString == null ? -1 : Integer.parseInt(problemIdString);

		Problem problem = serviceLocator.lookupProblemBean().getProblem(problemId);
		if (problem == null) {
			return;
		}
		
		// Проверяем право пользователя.
		AuthenticationObject ao = AuthenticationObject.extract(request);
		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		if (!pcb.canViewProblem(ao.getUsername(), problem.getProblemId())) {
			return;
		}
		
		JSONArray ja = this.getProblemJSONView(problem, ao);

		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(ja);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "exception caught", e);
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
	public ActionForward edit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;

		Problem problem = serviceLocator.lookupProblemBean().getProblem(Integer.parseInt(request.getParameter("problemId")));

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyProblem(ao.getUsername(), problem.getProblemId())) {
			return mapping.findForward("accessDenied");
		}

		pf.reset(mapping, request);

		// Выставляем значения соотв. полей информации пользователя.
		pf.setTitle(problem.getTitle());
		pf.setDescription(problem.getDescription());
		pf.setCpuTimeLimit(problem.getCpuTimeLimit());
		pf.setRealTimeLimit(problem.getRealTimeLimit());
		pf.setMemoryLimit(problem.getMemoryLimit());
		pf.setOutputLimit(problem.getOutputLimit());

		pf.setOwner(problem.getOwner().getLogin());
		pf.setCreateTime(problem.getCreateTime());
		pf.setHidden(problem.isHidden());
		
		pf.setNewProblem(false);
		return mapping.findForward("editProblem");
	}
	
	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward editTests(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;

		Problem problem = serviceLocator.lookupProblemBean().getProblem(Integer.parseInt(request.getParameter("problemId")));

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyProblem(ao.getUsername(), problem.getProblemId())) {
			return mapping.findForward("accessDenied");
		}

		pf.reset(mapping, request);

		// Выставляем значения соотв. полей информации пользователя.
		pf.setTitle(problem.getTitle());

		pf.setOwner(problem.getOwner().getLogin());
		pf.setCreateTime(problem.getCreateTime());
		pf.setHidden(problem.isHidden());
		
		pf.setEncodedTestList(encodeProblemTestsToJSON(problem.getProblemId()));

		pf.setNewProblem(false);
		return mapping.findForward("editTests");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward create(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя добавлять задачи.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddProblem(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		pf.reset(mapping, request);

		Problem problem = new Problem();

		// Выставляем значения соответствующих полей.
		pf.setTitle(problem.getTitle());
		pf.setDescription(problem.getDescription());
		pf.setCpuTimeLimit(problem.getCpuTimeLimit());
		pf.setRealTimeLimit(problem.getRealTimeLimit());
		pf.setMemoryLimit(problem.getMemoryLimit());
		pf.setOutputLimit(problem.getOutputLimit());
		pf.setHidden(problem.isHidden());

		pf.setNewProblem(true);

		return mapping.findForward("editProblem");
	}

	/**
	 *
	 * @param file
	 * @param request
	 * @return
	 */
	private dudge.problemc.binding.Problem importProblem(FormFile file, HttpServletRequest request) {

		dudge.problemc.binding.Problem importedProblem = null;
		try {
			javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(dudge.problemc.binding.Problem.class.getPackage().getName());
			javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
			try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getFileData())) {
				importedProblem = (dudge.problemc.binding.Problem) unmarshaller.unmarshal(inputStream);
			}
		} catch (JAXBException | IOException ex) {
			request.setAttribute("exception", ex);
			logger.log(Level.SEVERE, "exception", ex);
		}

		return importedProblem;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward importCreate(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;

		dudge.problemc.binding.Problem importedProblem = importProblem(pf.getFile(), request);
		if (importedProblem == null) {
			return mapping.findForward("importError");
		}

		Problem problem = new Problem(
				importedProblem.getTitle(),
				importedProblem.getDescription(),
				(int) importedProblem.getLimits().getMemory(),
				(int) importedProblem.getLimits().getTime(),
				(int) importedProblem.getLimits().getTime(),
				(int) importedProblem.getLimits().getOutput());

		problem.setHidden(pf.isHidden());
		problem.setAuthor(pf.getAuthor());

		AuthenticationObject ao = AuthenticationObject.extract(request);

		problem.setOwner(serviceLocator.lookupUserBean().getUser(ao.getUsername()));

		Date date = new Date();
		problem.setCreateTime(date);

		try {
			problem = serviceLocator.lookupProblemBean().addProblem(problem);
		} catch (Exception e) {
			request.setAttribute("exception", e);
			return mapping.findForward("importError");
		}

		importTests(problem.getProblemId(), importedProblem);

		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward importEdit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;

		dudge.problemc.binding.Problem importedProblem = importProblem(pf.getFile(), request);
		if (importedProblem == null) {
			return mapping.findForward("importError");
		}

		Problem problem = serviceLocator.lookupProblemBean().getProblem(pf.getProblemId());

		problem.setTitle(importedProblem.getTitle());
		problem.setAuthor(importedProblem.getAuthor());
		problem.setDescription(importedProblem.getDescription());
		problem.setCpuTimeLimit((int) importedProblem.getLimits().getTime());
		problem.setMemoryLimit((int) importedProblem.getLimits().getMemory());
		problem.setOutputLimit((int) importedProblem.getLimits().getOutput());
		problem.setRealTimeLimit((int) importedProblem.getLimits().getTime());

		problem.setHidden(pf.isHidden());

		try {
			serviceLocator.lookupProblemBean().modifyProblem(problem);
		} catch (Exception e) {
			request.setAttribute("exception", e);
			return mapping.findForward("importError");
		}

		pf.reset(mapping, request);

		importTests(problem.getProblemId(), importedProblem);

		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submitCreate(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddProblem(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		if (pf.getFile() != null && pf.getFile().getFileName() != null && !pf.getFile().getFileName().isEmpty()) {
			return importCreate(mapping, af, request, response);
		}

		// Создаем новый экземпляр задачи, и передаем ей извлеченные из формы данные.
		Date date = new Date();
		Problem problem = new Problem(
				pf.getTitle(),
				pf.getDescription(),
				pf.getMemoryLimit(),
				pf.getCpuTimeLimit(),
				pf.getRealTimeLimit(),
				pf.getOutputLimit());

		problem.setHidden(pf.isHidden());

		problem.setAuthor(pf.getAuthor());
		problem.setOwner(serviceLocator.lookupUserBean().getUser(ao.getUsername()));

		problem.setCreateTime(date);
		// Фиксируем изменения в БД.
		problem = serviceLocator.lookupProblemBean().addProblem(problem);

		//Редирект на страницу новосозданной задачи.
		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submitEdit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;
		// Получение контеста, который требуется отредактировать.
		Problem problem = serviceLocator.lookupProblemBean().getProblem(pf.getProblemId());

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyProblem(ao.getUsername(), problem.getProblemId())) {
			return mapping.findForward("accessDenied");
		}


		if (pf.getFile() != null && pf.getFile().getFileName() != null && !pf.getFile().getFileName().isEmpty()) {
			return importEdit(mapping, af, request, response);
		}

		problem.setTitle(pf.getTitle());
		problem.setAuthor(pf.getAuthor());
		problem.setDescription(pf.getDescription());
		problem.setCpuTimeLimit(pf.getCpuTimeLimit());
		problem.setMemoryLimit(pf.getMemoryLimit());
		problem.setOutputLimit(pf.getOutputLimit());
		problem.setRealTimeLimit(pf.getRealTimeLimit());

		problem.setHidden(pf.isHidden());

		serviceLocator.lookupProblemBean().modifyProblem(problem);

		pf.reset(mapping, request);

		//Редирект на страницу просмотра задачи.
		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 *
	 * @param problemId
	 * @param problem
	 */
	@SuppressWarnings("unchecked")
	private void importTests(int problemId, dudge.problemc.binding.Problem problem) {

		ProblemLocal problemBean = serviceLocator.lookupProblemBean();
		TestLocal testBean = serviceLocator.lookupTestBean();
		List<Test> tests = new LinkedList<>(problemBean.getProblem(problemId).getTests());
		Collections.sort(tests);

		// замена существующих тестов на новые
		Iterator<Test> testsIterator = tests.iterator();
		Iterator<dudge.problemc.binding.Problem.Tests.Test> problemTestIterator = problem.getTests().getTest().iterator();
		while (testsIterator.hasNext() && problemTestIterator.hasNext()) {
			Test nextTest = testsIterator.next();
			dudge.problemc.binding.Problem.Tests.Test problemTest = problemTestIterator.next();

			nextTest.setInputData(problemTest.getInput());
			nextTest.setOutputData(problemTest.getOutput());
			nextTest.setTestNumber(problemTest.getNumber());

			testBean.modifyTest(nextTest);
		}

		// удаление лишних тестов
		while (testsIterator.hasNext()) {
			testBean.deleteTest(testsIterator.next().getTestId());
		}

		// добавление новых тестов
		while (problemTestIterator.hasNext()) {
			dudge.problemc.binding.Problem.Tests.Test problemTest = problemTestIterator.next();
			int numberOfNewTest = problemBean.getProblem(problemId).getTests().size() + 1;

			Test test = new Test(problemTest.getInput(), problemTest.getOutput());
			test.setProblem(problemBean.getProblem(problemId));
			test.setTestNumber(numberOfNewTest);

			testBean.addTest(test);
		}
	}
	
	/**
	 * Метод возвращает список тестов задачи, закодированный как JSON-String.
	 *
	 * @param problemId - идентификатор задачи.
	 * @return
	 */
	private String encodeProblemTestsToJSON(int problemId) {
		ProblemLocal problemBean = serviceLocator.lookupProblemBean();
		Problem problem = problemBean.getProblem(problemId);
		if (problem == null) {
			return "[]";
		}
		List<Test> tests = new LinkedList<>(problem.getTests());
		Collections.sort(tests);

		JSONArray ja = new JSONArray();

		Iterator<Test> iter = tests.iterator();
		while (iter.hasNext()) {
			ja.put(this.getTestJSONView(iter.next()));
		}

		return ja.toString();
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void deleteTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);

		int testId = Integer.parseInt((String) request.getParameter("testId"));

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteTest(ao.getUsername(), new Test())) {
			return;
		}

		serviceLocator.lookupTestBean().deleteTest(testId);
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void addTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		AuthenticationObject ao = AuthenticationObject.extract(request);
		ProblemLocal problemBean = serviceLocator.lookupProblemBean();
		ProblemsForm pf = (ProblemsForm) af;

		int numberOfNewTest = problemBean.getProblem(pf.getProblemId()).getTests().size() + 1;

		Test test = new Test("", "");
		test.setProblem(problemBean.getProblem(pf.getProblemId()));
		test.setTestNumber(numberOfNewTest);

		// Проверяем права пользователя добавление нового теста для задачи.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddTest(ao.getUsername(), test)) {
			return;
		}

		serviceLocator.lookupTestBean().addTest(test);
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		int testId = Integer.parseInt((String) request.getParameter("testId"));
		Test test = serviceLocator.lookupTestBean().getTest(testId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canGetTest(ao.getUsername(), test)) {
			return;
		}

		JSONObject jo = new JSONObject();

		try {
			jo.put("inputData", test.getInputData());
			jo.put("outputData", test.getOutputData());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
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
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void commitTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		int testId = Integer.parseInt((String) request.getParameter("testId"));
		String inputData = (String) request.getParameter("inputData");
		String outputData = (String) request.getParameter("outputData");
		TestLocal testBean = serviceLocator.lookupTestBean();

		Test test = testBean.getTest(testId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyTest(ao.getUsername(), test)) {
			return;
		}

		test.setInputData(inputData);
		test.setOutputData(outputData);

		testBean.modifyTest(test);
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param problem
	 * @param ao
	 * @return
	 */
	private JSONArray getProblemJSONView(Problem problem, AuthenticationObject ao) {
		JSONArray json = new JSONArray();

		json.put(problem.getProblemId());
		json.put(problem.getTitle());
		json.put(problem.getAuthor());
		json.put(problem.getCreateTime().getTime());
		json.put(problem.isHealthy());

		return json;
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param test
	 * @return
	 */
	private JSONObject getTestJSONView(Test test) {

		JSONObject json = new JSONObject();
		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("testId", test.getTestId());
			json.put("number", test.getTestNumber());
			json.put("input", "");
			json.put("output", "");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Trouble while creating JSON view of Test object.", e);
		}
		return json;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);
		ProblemLocal problemBean = serviceLocator.lookupProblemBean();
		int problemId = Integer.parseInt((String) request.getParameter("problemId"));
		Problem problem = problemBean.getProblem(problemId);

		// Проверяем право пользователя на удаление задачи из системы.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteProblem(ao.getUsername(), problemId)) {
			return mapping.findForward("accessDenied");
		}

		// Задача не будет удалена, если она есть хотя бы в одном из соревнований, существующих в системе.
		List<Contest> contests = serviceLocator.lookupContestBean().getContests();
		for (Contest contest : contests) {
			if (contest.getContestProblems().contains(new ContestProblem(contest, problem))) {
				ActionForward forward = new ActionForward();
				forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
				forward.setRedirect(true);
				return forward;			
			}
		}

		problemBean.deleteProblem(problemId);
		
		return mapping.findForward("redirectProblemsList");
	}	

}
