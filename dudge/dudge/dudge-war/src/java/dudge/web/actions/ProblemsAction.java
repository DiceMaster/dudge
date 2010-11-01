/*
 * ProblemsAction.java
 *
 * Created on June 12, 2007, 4:15 PM
 */

package dudge.web.actions;

import dudge.DudgeLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.PermissionCheckerRemote;
import dudge.db.Problem;
import dudge.db.Test;

import dudge.web.SessionObject;
import dudge.web.forms.ProblemsForm;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import json.JSONObject;
import json.JSONArray;
import json.JSONException;

/**
 *
 * @author Vladimir Shabanov
 */
public class ProblemsAction extends DispatchAction {
	
	protected static Logger logger = Logger.getLogger(Problem.class.toString());
	
	/** Creates a new instance of ProblemsAction */
	public ProblemsAction() {
	}
	
	private DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:comp/env/ejb/DudgeBean");
		} catch(NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
			throw new RuntimeException(ne);
		}
	}
	
	public ActionForward list(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ProblemsForm pf = (ProblemsForm) af;
		pf.getProblems().clear();
		pf.getContestProblems().clear();
		
		//TODO: Проверять права пользователя на получение списка задач и их "скрытность".
		
		DudgeLocal dudgeBean = lookupDudgeBean();
		pf.getProblems().addAll(dudgeBean.getProblems());
		
		return mapping.findForward("problems");
	}
	
	public ActionForward view(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ProblemsForm pf = (ProblemsForm) af;
		
		
		int problemId = Integer.parseInt( (String)request.getParameter("problemId") );
		Problem problem = lookupDudgeBean().getProblem(problemId);
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canViewProblem(
				so.getUsername(),
				problemId)
				) {
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
			pf.setExampleInputTest( ((List<Test>) problem.getTests()).get(0).getInputData());
			pf.setExampleOutputTest( ((List<Test>) problem.getTests()).get(0).getOutputData());
		} catch (IndexOutOfBoundsException ex) {
		}
		
		return mapping.findForward("viewProblem");
	}
	
	public void getProblemList( ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		//  Получаем из запроса, какие данные требуются клиенту.
		int start = Integer.parseInt( (String) request.getParameter("start"));
		int limit = Integer.parseInt( (String) request.getParameter("limit"));
		
		DudgeLocal dudgeBean = lookupDudgeBean();
		
		List<Problem> problems = dudgeBean.getProblems();
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		
		// Отсеиваем из списка задач те, которые пользователь видеть в общем списке задач не должен.
		List<Problem> selectedProblems = new ArrayList<Problem>();
		for (Problem problem: problems){
			if(pcb.canViewProblem(so.getUsername() , problem.getProblemId()))
				selectedProblems.add(problem);
		}
		
		int totalCount = selectedProblems.size();
		
		if(start + limit > selectedProblems.size())
			limit = selectedProblems.size() - start;
		
		if(start < 0 || limit < 0)
			return;
		
		selectedProblems = selectedProblems.subList(start, start + limit);
		
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("totalCount", totalCount);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		for (Iterator<Problem> iter = selectedProblems.iterator() ; iter.hasNext() ; ) {
			ja.put(this.getProblemJSONView(iter.next() , so));
		}
		try {
			jo.put("problems" ,ja);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public ActionForward edit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response){
		ProblemsForm pf = (ProblemsForm) af;
		
		Problem problem = lookupDudgeBean().getProblem(Integer.parseInt(request.getParameter("problemId")));
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyProblem(
				so.getUsername(),
				problem.getProblemId())
				) {
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
	
	public ActionForward create(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ProblemsForm pf = (ProblemsForm) af;
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя добавлять задачи.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canAddProblem(
				so.getUsername())
				) { return mapping.findForward("accessDenied");
				
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
	
	public ActionForward submitCreate(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ProblemsForm pf = (ProblemsForm) af;
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canAddProblem(
				so.getUsername())
				) {
			return mapping.findForward("accessDenied");
		}
		
		// Создаем новый экземпляр задачи, и передаем ей извлеченные из формы данные.
		Date date = new Date();
		Problem problem = new Problem(
				-1,
				pf.getTitle(),
				pf.getDescription(),
				pf.getMemoryLimit(),
				pf.getCpuTimeLimit(),
				pf.getRealTimeLimit(),
				pf.getOutputLimit()
				);
		
		problem.setHidden(pf.isHidden());
		
		
		problem.setOwner(
				lookupDudgeBean().getUser(
				SessionObject.extract(request.getSession()).getUsername())
				);
		
		problem.setCreateTime(date);
		// Фиксируем изменения в БД.
		problem = lookupDudgeBean().addProblem(problem);
		
		//Редирект на страницу новосозданной задачи.
		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}
	
	public ActionForward submitEdit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		ProblemsForm pf = (ProblemsForm) af;
		
		// Получение контеста, который требуется отредактировать.
		Problem problem = lookupDudgeBean().getProblem(pf.getProblemId());
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyProblem(
				so.getUsername() , problem.getProblemId())
				) {
			return mapping.findForward("accessDenied");
		}
		
		problem.setTitle(pf.getTitle());
		problem.setAuthor(pf.getAuthor());
		problem.setDescription(pf.getDescription());
		problem.setCpuTimeLimit(pf.getCpuTimeLimit());
		problem.setMemoryLimit(pf.getMemoryLimit());
		problem.setOutputLimit(pf.getOutputLimit());
		problem.setRealTimeLimit(pf.getRealTimeLimit());
		
		problem.setHidden(pf.isHidden());
		
		lookupDudgeBean().modifyProblem(problem);
		
		pf.reset(mapping , request);
		
		//Редирект на страницу просмотра задачи.
		ActionForward forward = new ActionForward();
		forward.setPath("problems.do?reqCode=view&problemId=" + problem.getProblemId());
		forward.setRedirect(true);
		return forward;
	}
	
	public void getTestList(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyProblem(
				so.getUsername(),
				pf.getProblemId())
				) {
			return;
		}
		
		List<Test> tests = new LinkedList<Test>( lookupDudgeBean().getProblem(pf.getProblemId()).getTests() );
		Collections.sort(tests);
		
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("totalCount", tests.size());
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		for (Iterator<Test> iter = tests.iterator() ; iter.hasNext() ; ) {
			ja.put(this.getTestJSONView(iter.next()));
		}
		try {
			jo.put("tests" ,ja);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	public void deleteTest(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		SessionObject so = SessionObject.extract(request.getSession());
		ProblemsForm pf = (ProblemsForm) af;
		int testId = Integer.parseInt( (String) request.getParameter("testId"));
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (
				!pcb.canDeleteTest(
				so.getUsername(),
				new Test(testId))
				
				) {
			
			return ;
		}
		lookupDudgeBean().deleteTest(testId);
	}
	
	public void addTest(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		SessionObject so = SessionObject.extract(request.getSession());
		
		ProblemsForm pf = (ProblemsForm) af;
		
		int numberOfNewTest  = lookupDudgeBean().getProblem(pf.getProblemId()).getTests().size() + 1;
		
		Test test = new Test(-1 , "" , "");
		test.setProblem(lookupDudgeBean().
				getProblem(pf.getProblemId()));
		test.setTestNumber(numberOfNewTest);
		
		// Проверяем права пользователя добавление нового теста для задачи.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (
				!pcb.canAddTest(
				so.getUsername(),
				test
				)
				) {
			return ;
		}
		
		lookupDudgeBean().addTest(test);
	}
	
	public void getTest(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		ProblemsForm pf = (ProblemsForm) af;
		
		int testId = Integer.parseInt( (String) request.getParameter("testId"));
		Test test =  lookupDudgeBean().getTest(testId);
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canGetTest(
				so.getUsername(),
				test)
				) {
			return;
		}
		
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();try {
			jo.put("totalCount", 1);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		// 0 соотв. входному тесту, 1 - выходному.
		int testType  =  Integer.parseInt( (String) request.getParameter("testType"));
		
		if ((testType % 2)  == 0) {
			try {
				JSONObject js = new JSONObject();
				js.put("data" , test.getInputData() );
				ja.put(js);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		if ( (testType % 2)  == 1) {
			try {
				JSONObject js = new JSONObject();
				js.put("data" ,  test.getOutputData() );
				ja.put(js);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		
		try {
			jo.put("testData" ,ja);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}
		
		// Устанавливаем тип контента
		response.setContentType("application/x-json");
		try {
			response.getWriter().print(jo);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	public void commitTest(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		int testId = Integer.parseInt( (String) request.getParameter("testId"));
		int testType = Integer.parseInt( (String) request.getParameter("testType"));
		String data = (String) request.getParameter("data");
		Test test = lookupDudgeBean().getTest(testId);
		
		SessionObject so = SessionObject.extract(request.getSession());
		
		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyTest(
				so.getUsername(),
				test)
				) {
			return;
		}
		
		if (testType % 2 == 0) {
			test.setInputData(data);
		}
		if (testType % 2 == 1) {
			test.setOutputData(data);
		}
		
		lookupDudgeBean().modifyTest(test);
	}
	
	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно
	 * для его отображение на стороне клиента через JavaScript/AJAX.
	 */
	private JSONObject getProblemJSONView(Problem problem , SessionObject so) {
		
		JSONObject json = new JSONObject();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		
		// Заполняем данными задачи созданный объект JSON.
		try {
			json.put("id", problem.getProblemId());
			json.put("title", problem.getTitle());
			json.put("owner", problem.getOwner().getLogin());
			json.put("create_time", sdf.format(problem.getCreateTime()));
			json.put("is_healthy", problem.isHealthy());

			if(pcb.canDeleteProblem(so.getUsername() , problem.getProblemId())) {
				json.put("deletable" , true);
			} else{
				json.put("deletable" , false);
			}
		} catch (JSONException Je ) {
			this.logger.severe("Truble in creating JSON view of Problem object.");
		}
		return json;
	}
	
	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно
	 * для его отображение на стороне клиента через JavaScript/AJAX.
	 */
	private JSONObject getTestJSONView(Test test) {
		
		JSONObject json = new JSONObject();
		
		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("testId" , test.getTestId());
			json.put("number" , test.getTestNumber());
			json.put("input" , "");
			json.put("output" , "");
			
		} catch (JSONException Je ) {
			this.logger.severe("Truble in creating JSON view of User object.");
		}
		return json;
	}
	
	public void delete(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		SessionObject so = SessionObject.extract(request.getSession());
		ProblemsForm pf = (ProblemsForm) af;
		
		int problemId = Integer.parseInt( (String) request.getParameter("problemId"));
		Problem problem = lookupDudgeBean().getProblem(problemId);
		
		// Проверяем право пользователя на удаление задачи из системы.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (
				!pcb.canDeleteProblem(
				so.getUsername(),
				problemId)
				
				) {
			
			return ;
		}
		
		// Задача не будет удалена, если она есть хотя бы в одном из соревнований, существующих в системе.
		List<Contest> contests = lookupDudgeBean().getContests();
		for(Contest contest : contests) {
			if(contest.getContestProblems().contains(new ContestProblem(contest , problem))) return;
		}
		
		lookupDudgeBean().deleteProblem(problemId);
	}
}
