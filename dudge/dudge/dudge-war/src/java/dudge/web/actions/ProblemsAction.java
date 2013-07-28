/*
 * ProblemsAction.java
 *
 * Created on June 12, 2007, 4:15 PM
 */
package dudge.web.actions;

import dudge.ContestLocal;
import dudge.PermissionCheckerRemote;
import dudge.ProblemLocal;
import dudge.TestLocal;
import dudge.UserLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Problem;
import dudge.db.Test;
import dudge.web.AuthenticationObject;
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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

	/**
	 * Creates a new instance of ProblemsAction
	 */
	public ProblemsAction() {
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

	private ContestLocal lookupContestBean() {
		try {
			Context c = new InitialContext();
			return (ContestLocal) c.lookup("java:global/dudge/dudge-ejb/ContestBean");//java:comp/env/ejb/ContestBean
		} catch (NamingException ne) {
			logger.log(Level.ALL, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	private ProblemLocal lookupProblemBean() {
		try {
			Context c = new InitialContext();
			return (ProblemLocal) c.lookup("java:global/dudge/dudge-ejb/ProblemBean");//java:comp/env/ejb/ProblemBean
		} catch (NamingException ne) {
			logger.log(Level.ALL, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	private TestLocal lookupTestBean() {
		try {
			Context c = new InitialContext();
			return (TestLocal) c.lookup("java:global/dudge/dudge-ejb/TestBean");//java:comp/env/ejb/TestBean
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
	public ActionForward list(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		ProblemsForm pf = (ProblemsForm) af;
		pf.getProblems().clear();
		pf.getContestProblems().clear();

		//TODO: Проверять права пользователя на получение списка задач и их "скрытность".
		pf.getProblems().addAll(lookupProblemBean().getProblems());

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

		int problemId = Integer.parseInt((String) request.getParameter("problemId"));
		Problem problem = lookupProblemBean().getProblem(problemId);

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
		int start = Integer.parseInt((String) request.getParameter("start"));
		int limit = Integer.parseInt((String) request.getParameter("limit"));

		List<Problem> problems = lookupProblemBean().getProblems();
		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		// Отсеиваем из списка задач те, которые пользователь видеть в общем списке задач не должен.
		List<Problem> selectedProblems = new ArrayList<>();
		for (Problem problem : problems) {
			if (pcb.canViewProblem(ao.getUsername(), problem.getProblemId())) {
				selectedProblems.add(problem);
			}
		}

		int totalCount = selectedProblems.size();

		if (start + limit > selectedProblems.size()) {
			limit = selectedProblems.size() - start;
		}

		if (start < 0 || limit < 0) {
			return;
		}

		selectedProblems = selectedProblems.subList(start, start + limit);

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("totalCount", totalCount);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		Iterator<Problem> iter = selectedProblems.iterator();
		while (iter.hasNext()) {
			ja.put(this.getProblemJSONView(iter.next(), ao));
		}
		try {
			jo.put("problems", ja);
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
	 * @return
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;

		Problem problem = lookupProblemBean().getProblem(Integer.parseInt(request.getParameter("problemId")));

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

		problem.setOwner(lookupUserBean().getUser(ao.getUsername()));

		Date date = new Date();
		problem.setCreateTime(date);

		try {
			problem = lookupProblemBean().addProblem(problem);
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

		Problem problem = lookupProblemBean().getProblem(pf.getProblemId());

		problem.setTitle(importedProblem.getTitle());
		problem.setAuthor(importedProblem.getAuthor());
		problem.setDescription(importedProblem.getDescription());
		problem.setCpuTimeLimit((int) importedProblem.getLimits().getTime());
		problem.setMemoryLimit((int) importedProblem.getLimits().getMemory());
		problem.setOutputLimit((int) importedProblem.getLimits().getOutput());
		problem.setRealTimeLimit((int) importedProblem.getLimits().getTime());

		problem.setHidden(pf.isHidden());

		try {
			lookupProblemBean().modifyProblem(problem);
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
		problem.setOwner(lookupUserBean().getUser(ao.getUsername()));

		problem.setCreateTime(date);
		// Фиксируем изменения в БД.
		problem = lookupProblemBean().addProblem(problem);

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
		Problem problem = lookupProblemBean().getProblem(pf.getProblemId());

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

		lookupProblemBean().modifyProblem(problem);

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

		ProblemLocal problemBean = lookupProblemBean();
		TestLocal testBean = lookupTestBean();
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
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void getTestList(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyProblem(ao.getUsername(), pf.getProblemId())) {
			return;
		}

		List<Test> tests = new LinkedList<>(lookupProblemBean().getProblem(pf.getProblemId()).getTests());
		Collections.sort(tests);

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("totalCount", tests.size());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		Iterator<Test> iter = tests.iterator();
		while (iter.hasNext()) {
			ja.put(this.getTestJSONView(iter.next()));
		}
		try {
			jo.put("tests", ja);
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
	public void deleteTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);

		int testId = Integer.parseInt((String) request.getParameter("testId"));

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteTest(ao.getUsername(), new Test())) {
			return;
		}

		lookupTestBean().deleteTest(testId);
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
		ProblemLocal problemBean = lookupProblemBean();
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

		lookupTestBean().addTest(test);
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
		Test test = lookupTestBean().getTest(testId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canGetTest(ao.getUsername(), test)) {
			return;
		}

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		try {
			jo.put("totalCount", 1);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		// 0 соотв. входному тесту, 1 - выходному.
		int testType = Integer.parseInt((String) request.getParameter("testType"));

		if ((testType % 2) == 0) {
			try {
				JSONObject js = new JSONObject();
				js.put("data", test.getInputData());
				ja.put(js);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "exception caught", e);
			}
		}
		if ((testType % 2) == 1) {
			try {
				JSONObject js = new JSONObject();
				js.put("data", test.getOutputData());
				ja.put(js);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "exception caught", e);
			}
		}

		try {
			jo.put("testData", ja);
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
	public void commitTest(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		int testId = Integer.parseInt((String) request.getParameter("testId"));
		int testType = Integer.parseInt((String) request.getParameter("testType"));
		String data = (String) request.getParameter("data");
		TestLocal testBean = lookupTestBean();

		Test test = testBean.getTest(testId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyTest(ao.getUsername(), test)) {
			return;
		}

		if (testType % 2 == 0) {
			test.setInputData(data);
		}
		if (testType % 2 == 1) {
			test.setOutputData(data);
		}

		testBean.modifyTest(test);
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param problem
	 * @param ao
	 * @return
	 */
	private JSONObject getProblemJSONView(Problem problem, AuthenticationObject ao) {

		JSONObject json = new JSONObject();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		// Заполняем данными задачи созданный объект JSON.
		try {
			json.put("id", problem.getProblemId());
			json.put("title", problem.getTitle());
			json.put("owner", problem.getOwner().getLogin());
			json.put("create_time", sdf.format(problem.getCreateTime()));
			json.put("is_healthy", problem.isHealthy());

			if (pcb.canDeleteProblem(ao.getUsername(), problem.getProblemId())) {
				json.put("deletable", true);
			} else {
				json.put("deletable", false);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Trouble while creating JSON view of Problem object.", e);
		}
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
	public void delete(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);
		ProblemLocal problemBean = lookupProblemBean();
		int problemId = Integer.parseInt((String) request.getParameter("problemId"));
		Problem problem = problemBean.getProblem(problemId);

		// Проверяем право пользователя на удаление задачи из системы.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteProblem(ao.getUsername(), problemId)) {
			return;
		}

		// Задача не будет удалена, если она есть хотя бы в одном из соревнований, существующих в системе.
		List<Contest> contests = lookupContestBean().getContests();
		for (Contest contest : contests) {
			if (contest.getContestProblems().contains(new ContestProblem(contest, problem))) {
				return;
			}
		}

		problemBean.deleteProblem(problemId);
	}
}
