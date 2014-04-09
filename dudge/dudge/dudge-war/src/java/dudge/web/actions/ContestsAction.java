/*
 * ContestsAction.java
 *
 * Created on June 10, 2007, 11:51 PM
 */
package dudge.web.actions;

import dudge.ContestLocal;
import dudge.PermissionCheckerRemote;
import dudge.UserLocal;
import dudge.db.Application;
import dudge.db.ApplicationStatus;
import dudge.db.Contest;
import dudge.db.ContestLanguage;
import dudge.db.ContestProblem;
import dudge.db.ContestType;
import dudge.db.Language;
import dudge.db.Problem;
import dudge.db.Role;
import dudge.db.RoleType;
import dudge.db.User;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.ContestsForm;
import dudge.web.forms.ContestsForm.ContestRole;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
 * @author Vladimir Shabanov
 */
public class ContestsAction extends DispatchAction {

	private static final Logger logger = Logger.getLogger(ContestsAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

        private static final String[] columns = {
          "contestId",
          "caption",
          "type",
          "open",
          "startTime"
        };
        
	/**
	 * Creates a new instance of ContestsAction
	 */
	public ContestsAction() {
	}

	public ActionForward list(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("contests");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward rules(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ContestsForm cf = (ContestsForm) af;

		int contestId = Integer.parseInt((String) request.getParameter("contestId"));
		Contest contest = serviceLocator.lookupContestBean().getContest(contestId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewContest(ao.getUsername(), contestId)) {
			return mapping.findForward("accessDenied");
		}

		cf.reset(mapping, request);
		cf.getContestTypes().addAll(Arrays.asList(ContestType.values()));
		cf.getRoleTypes().addAll(Arrays.asList(RoleType.values()));

		// Выставляем значения для полей, соотв. текущим значениям редактируемого контеста.
		cf.setContestId(String.valueOf(contestId));
		cf.setCaption(contest.getCaption());
		cf.setDescription(contest.getDescription());
		cf.setContestType(contest.getType().name());
		cf.setRules(contest.getRules());

		return mapping.findForward("contestRules");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getContestList(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
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

                ContestLocal.FilteredContests contests = serviceLocator.lookupContestBean().getContests(
                    searchString,
                    order,
                    descending,
                    iDisplayStart,
                    iDisplayLength
                );
                
                long totalContestsCount = serviceLocator.lookupContestBean().getContestsCount();
                
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
                    jo.put("sEcho", request.getParameter("sEcho"));
                    jo.put("iTotalRecords", totalContestsCount);
                    jo.put("iTotalDisplayRecords", contests.getFilteredTotal());
		} catch (JSONException e) {
                    logger.log(Level.SEVERE, "exception caught", e);
                    return;
		}

		AuthenticationObject ao = AuthenticationObject.extract(request);
		for (Iterator<Contest> iter = contests.getFilteredContests().iterator(); iter.hasNext();) {
                    ja.put(this.getContestJSONView(iter.next(), ao));
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
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param contest
	 * @param ao
	 * @return
	 */
	private JSONArray getContestJSONView(Contest contest, AuthenticationObject ao) {

		JSONArray json = new JSONArray();

		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		// Заполняем данными задачи созданный объект JSON.
                json.put(contest.getContestId());

                json.put(contest.getCaption());
                json.put(contest.getType().toString());
                json.put(contest.isOpen());
                json.put(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(contest.getStartTime()));
                
                json.put(contest.getDuration());
                json.put(pcb.canJoinContest(ao.getUsername(), contest.getContestId()));
                json.put(pcb.canModifyContest(ao.getUsername(), contest.getContestId()));
                json.put(pcb.canDeleteContest(ao.getUsername(), contest.getContestId()));
		return json;
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

		ContestsForm cf = (ContestsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddContest(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		cf.reset(mapping, request);
		cf.getContestTypes().addAll(Arrays.asList(ContestType.values()));
		cf.getRoleTypes().addAll(Arrays.asList(RoleType.values()));

		//  Выставляем дефолтные значения полей.
		cf.setCaption("");
		cf.setDescription("");
		cf.setRules("");
		cf.setContestType("ACM");
		cf.setOpen(false);

		cf.setStartDate(new SimpleDateFormat("yyyy/MM/dd").format(java.util.Calendar.getInstance().getTime()));
		cf.setStartHour(String.valueOf(12));
		cf.setStartMinute(String.valueOf(00));
		cf.setDurationHours(String.valueOf(5));
		cf.setDurationMinutes(String.valueOf(00));
		cf.setFreezeTime(String.valueOf(0));

		// Устанавливаем закодированный в формате JSON значения коллекций в пустые значения
		// (кроме коллекции языков, которая все равно присылается, со всеми 'false'),
		// используемые в отображении их на странице средствами JavaScript/ExtJS.

		cf.setEncodedContestLanguages(this.encodeContestLanguagesToJSON(0));
		cf.setEncodedContestProblems("");
		cf.setEncodedRoles("");
		cf.setEncodedApplications("");

		cf.setNewContest(true);

		return mapping.findForward("createContest");
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

		ContestsForm cf = (ContestsForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Получаем идентификатор редактируемого контеста, чтобы по нему найти объект нужного контеста
		// и выставить текущие значения как значения по умолчанию для полей на странице редактирования.
		int contestId = Integer.parseInt((String) request.getParameter("contestId"));
		Contest modifiedContest = serviceLocator.lookupContestBean().getContest(contestId);

		cf.setContestId(Integer.toString(contestId));

		// Проверка прав отредактировать данный контест.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyContest(ao.getUsername(), contestId)) {
			return mapping.findForward("accessDenied");
		}

		cf.reset(mapping, request);
		cf.getContestTypes().addAll(Arrays.asList(ContestType.values()));
		cf.getRoleTypes().addAll(Arrays.asList(RoleType.values()));

		// Выставляем значения для полей, соотв. текущим значениям редактируемого контеста.
		cf.setContestId(String.valueOf(contestId));
		cf.setCaption(modifiedContest.getCaption());
		cf.setDescription(modifiedContest.getDescription());
		cf.setRules(modifiedContest.getRules());
		cf.setContestType(modifiedContest.getType().name());
		cf.setOpen(modifiedContest.isOpen());
		cf.setFreezeTime(String.valueOf(modifiedContest.getFreezeTime() / 60));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(modifiedContest.getStartTime());

		cf.setStartDate(new SimpleDateFormat("yyyy/MM/dd").format(modifiedContest.getStartTime()));
		cf.setStartHour(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		cf.setStartMinute(String.valueOf(calendar.get(Calendar.MINUTE)));
		cf.setDurationHours(String.valueOf(modifiedContest.getDuration() / 3600));
		cf.setDurationMinutes(String.valueOf((modifiedContest.getDuration() % 3600) / 60));

		// Устанавливаем закодированный в формате JSON значения коллекций,
		// используемые в отображении их на странице средствами JavaScript/ExtJS.

		cf.setEncodedContestLanguages(encodeContestLanguagesToJSON(contestId));
		cf.setEncodedContestProblems(encodeContestProblemsToJSON(contestId));
		cf.setEncodedRoles(encodeRolesToJSON(contestId));
		cf.setEncodedApplications(encodeApplicationsToJSON(contestId));

		cf.setNewContest(false);
		return mapping.findForward("editContest");
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void sendApplication(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем, что заявку подал залогинившийся пользователь.
		if (!ao.isAuthenticated()) {
			return;
		}

		ContestLocal contestBean = serviceLocator.lookupContestBean();
		UserLocal userBean = serviceLocator.lookupUserBean();
		int contestId = Integer.parseInt(request.getParameter("contestId"));
		String message = request.getParameter("message");
		Contest contest = contestBean.getContest(contestId);
		String login = ao.getUsername();

		// Если пользователь уже является участником этого контеста, 
		// то нет смысла обрабатывать его заявку.
		if (!userBean.haveNoRoles(login, contestId)) {
			return;
		}

		// Если соревнование открытое, то пользователь сразу добавляется в него.
		if (contest.isOpen()) {
			Role r = new Role(contest, userBean.getUser(login), RoleType.USER);
			contest.getRoles().add(r);
			contestBean.modifyContest(contest);
			return;
		}

		Application ap = new Application(contest, userBean.getUser(login));
		ap.setFilingTime(new Date());
		ap.setMessage(message);
		ap.setStatus(ApplicationStatus.NEW.toString());

		contest.getApplications().add(ap);
		contestBean.modifyContest(contest);
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

		ContestsForm cf = (ContestsForm) af;
		UserLocal userBean = serviceLocator.lookupUserBean();

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddContest(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		// Создаем новый экземпляр контеста, и передаем ему извлеченные из формы данные.
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy/MM/dd").parse(cf.getStartDate());
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cf.getStartHour()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(cf.getStartMinute()));

		Contest contest = new Contest(
				cf.getCaption(),
				cf.getDescription(),
				cf.getRules(),
				ContestType.valueOf(cf.getContestType()),
				calendar.getTime(),
				Integer.parseInt(cf.getDurationHours()) * 3600 + Integer.parseInt(cf.getDurationMinutes()) * 60);
		contest.setFreezeTime(Integer.parseInt(cf.getFreezeTime()) * 60);

		contest.setOpen(cf.isOpen());

		// Устанавливаем значения коллекций (раскодированных из JSON данных).
		List<ContestLanguage> allLanguages = (List<ContestLanguage>) this.decodeContestLanguagesFromJSON(
				cf.getEncodedContestLanguages(), contest);
		if (allLanguages != null) {
			contest.getContestLanguages().addAll(allLanguages);
		}

		// При задании коллекции задач соревнования, удаляем из нее те скрытые задачи,
		// добавлять которые создатель контеста не имеет прав.
		List<ContestProblem> allContestProblems = (List<ContestProblem>) this.decodeContestProblemsFromJSON(
				cf.getEncodedContestProblems(), contest);

		if (allContestProblems != null) {
			contest.getContestProblems().addAll(allContestProblems);
		}

		// При добавлении роли проверяем, что пользователь с таким логином существует в БД.
		Collection<Role> allRoles = (Collection<Role>) decodeRolesFromJSON(cf.getEncodedRoles(), contest);
		if (allRoles != null) {
			contest.getRoles().addAll(allRoles);
		}

		// Добавляем в список ролей создателя контеста с правами администрирования.
		Role creator = new Role(contest, userBean.getUser(ao.getUsername()), RoleType.ADMINISTRATOR);

		if (!contest.getRoles().contains(creator)) {
			contest.getRoles().add(creator);
		}

		contest = serviceLocator.lookupContestBean().addContest(contest);
		cf.setContestId(String.valueOf(contest.getContestId()));

		//Редирект на страницу новосозданного контеста.
		ActionForward forward = new ActionForward();
		forward.setPath("contests.do?reqCode=view&contestId=" + contest.getContestId());
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
	 * @throws ParseException
	 */
	public ActionForward submitEdit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) throws ParseException {

		ContestsForm cf = (ContestsForm) af;
		ContestLocal contestBean = serviceLocator.lookupContestBean();

		// Получение контеста, который требуется отредактировать.
		int contestId = Integer.parseInt(request.getParameter("contestId"));
		Contest modifiedContest = contestBean.getContest(contestId);

		cf.setContestId(Integer.toString(contestId));

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyContest(ao.getUsername(), modifiedContest.getContestId())) {
			return mapping.findForward("accessDenied");
		}

		//  Редактируем существующий контест.
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy/MM/dd").parse(cf.getStartDate());
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cf.getStartHour()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(cf.getStartMinute()));

		modifiedContest.setCaption(cf.getCaption());
		modifiedContest.setDescription(cf.getDescription());
		modifiedContest.setRules(cf.getRules());
		modifiedContest.setType(ContestType.valueOf(cf.getContestType()));
		modifiedContest.setOpen(cf.isOpen());
		modifiedContest.setStartTime(calendar.getTime());
		modifiedContest.setDuration(Integer.parseInt(cf.getDurationHours()) * 3600 + Integer.parseInt(cf.getDurationMinutes()) * 60);
		modifiedContest.setFreezeTime(Integer.parseInt(cf.getFreezeTime()) * 60);

		// Устанавливаем значения коллекций (раскодированных из JSON данных).
		List<ContestLanguage> allLanguages = (List<ContestLanguage>) this.decodeContestLanguagesFromJSON(
				cf.getEncodedContestLanguages(), contestBean.getContest(contestId));
		modifiedContest.setContestLanguages(allLanguages);

		// При задании коллекции задач соревнования, удаляем из нее те скрытые задачи,
		// добавлять которые создатель контеста не имеет прав.
		List<ContestProblem> allContestProblems = (List<ContestProblem>) this.decodeContestProblemsFromJSON(
				cf.getEncodedContestProblems(), contestBean.getContest(contestId));

		modifiedContest.setContestProblems(allContestProblems);

		// При добавлении роли проверяем, что пользователь с таким логином существует в БД.
		Collection<Role> allRoles = (Collection<Role>) decodeRolesFromJSON(cf.getEncodedRoles(), modifiedContest);

		modifiedContest.setRoles(allRoles);

		List<Application> allApplications = (List<Application>) this.decodeApplicationsFromJSON(cf.getEncodedApplications(), modifiedContest);
		modifiedContest.setApplications(allApplications);

		contestBean.modifyContest(modifiedContest);
		cf.reset(mapping, request);

		//Редирект на страницу отредактированного контеста.
		ActionForward forward = new ActionForward();
		forward.setPath("contests.do?reqCode=view&contestId=" + modifiedContest.getContestId());
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
	public ActionForward view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		ContestsForm cf = (ContestsForm) af;

		// Получаем идентификатор редактируемого контеста, чтобы по нему найти объект нужного контеста
		// и выставить текущие значения как значения по умолчанию для полей на странице редактирования.

		int contestId = Integer.parseInt((String) request.getParameter("contestId"));
		Contest contest = serviceLocator.lookupContestBean().getContest(contestId);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canViewContest(ao.getUsername(), contestId)) {
			return mapping.findForward("accessDenied");
		}

		cf.reset(mapping, request);
		cf.getContestTypes().addAll(Arrays.asList(ContestType.values()));
		cf.getRoleTypes().addAll(Arrays.asList(RoleType.values()));
                
                for (Role role : contest.getRoles()) {
                    cf.getRoles().add(cf.new ContestRole(role.getUser().getLogin(), role.getUser().getRealName(), role.getRoleType()));
                }

		// Выставляем значения для полей, соотв. текущим значениям редактируемого контеста.
		cf.setContestId(String.valueOf(contestId));
		cf.setCaption(contest.getCaption());
		cf.setDescription(contest.getDescription());
		cf.setContestType(contest.getType().name());
		cf.setOpen(contest.isOpen());
		cf.setFreezeTime(String.valueOf(contest.getFreezeTime() / 60));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contest.getStartTime());

		cf.setStartDate(new SimpleDateFormat("yyyy/MM/dd").format(contest.getStartTime()));
		cf.setStartHour(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		cf.setStartMinute(String.valueOf(calendar.get(Calendar.MINUTE)));
		cf.setDurationHours(String.valueOf(contest.getDuration() / 3600));
		cf.setDurationMinutes(String.valueOf((contest.getDuration() % 3600) / 60));

		return mapping.findForward("viewContest");
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

		int contestId = Integer.parseInt((String) request.getParameter("contestId"));

		// Проверяем право пользователя на удаление задачи из системы.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteContest(ao.getUsername(), contestId)) {
			return;
		}

		serviceLocator.lookupContestBean().deleteContest(contestId);
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward listOfProblems(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("contestProblems");
	}

	/**
	 * Перепроверяет все принадлежащие некоторому контесту решения определенной задачи.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void resubmitAll(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		//  Получаем из запроса, какие данные требуются клиенту.
		int problemId = Integer.parseInt(request.getParameter("problemId"));

		ContestLocal contestBean = serviceLocator.lookupContestBean();

		//FIXME: Проверять здесь права.
		//AuthenticationObject ao = AuthenticationObject.extract(request);

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

		Contest currentContest = contestBean.getContest(contestId);

		serviceLocator.lookupDudgeBean().resubmitSolutions(currentContest.getContestId(), problemId);
	}

	////////////////////////////////////////////////////////////////////////////
	// Группа закрытых методов для обработки данных для обмена информации с клиентом.
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Метод возвращает коллекцию ролей данного соревнования, раскодированную из JSON-String.
	 *
	 * @param jsonEncodedRoles
	 * @param contest
	 * @return
	 */
	private Collection<Role> decodeRolesFromJSON(String jsonEncodedRoles, Contest contest) {

		JSONArray jsonRoles;
		Collection<Role> roles = new TreeSet<>(new Comparator<Role>() {
			@Override
			public int compare(Role a, Role b) {
				if (!a.getUser().getLogin().equals(b.getUser().getLogin())) {
					return a.getUser().getLogin().compareTo(b.getUser().getLogin());
				}

				if (a.getContest().getContestId() != b.getContest().getContestId()) {
					return a.getContest().getContestId() - b.getContest().getContestId();
				}

				if (!a.getRoleType().equals(b.getRoleType())) {
					return a.getRoleType().compareTo(b.getRoleType());
				}

				return 0;
			}
		});
		try {
			jsonRoles = new JSONArray(jsonEncodedRoles);

			UserLocal userBean = serviceLocator.lookupUserBean();
			for (int i = 0; i < jsonRoles.length(); i++) {
				String login = jsonRoles.getJSONObject(i).getString("login");
				User user = userBean.getUser(login);

				if (user != null) {
					Role currentRole = new Role(contest, user, RoleType.valueOf(jsonRoles.getJSONObject(i).getString("role")));
					roles.add(currentRole);
				}

			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		// Отуникаливаем список ролей, сформированный пользователем.
		return roles;
	}

	/**
	 * Метод возвращает коллекцию обработанных заявок данного соревнования, раскодированную из JSON-String.
	 *
	 * @param jsonEncodedApplications
	 * @param contest
	 * @return
	 * @throws ParseException
	 */
	private Collection<Application> decodeApplicationsFromJSON(String jsonEncodedApplications, Contest contest) throws ParseException {

		JSONArray jsonApplications;
		Collection<Application> applications = null;
		try {
			jsonApplications = new JSONArray(jsonEncodedApplications);
			applications = new ArrayList<>();
			UserLocal userBean = serviceLocator.lookupUserBean();
			for (int i = 0; i < jsonApplications.length(); i++) {
				String login = jsonApplications.getJSONObject(i).getString("login");
				User user = userBean.getUser(login);

				if (user != null) {
					Application currentApplication = new Application(contest, user);
					currentApplication.setStatus(jsonApplications.getJSONObject(i).getString("status"));
					currentApplication.setFilingTime(
							new SimpleDateFormat("yyyy.MM.dd HH:mm").parse(jsonApplications.getJSONObject(i).getString("filing_time")));
					currentApplication.setMessage(jsonApplications.getJSONObject(i).getString("message"));
					applications.add(currentApplication);
				}

			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return applications;
	}

	/**
	 * Метод возвращает коллекцию включенных в соревнование задач , раскодированных из JSON-String.
	 *
	 * @param jsonEncodedProblems
	 * @param contest
	 * @return
	 */
	private Collection<ContestProblem> decodeContestProblemsFromJSON(String jsonEncodedProblems, Contest contest) {

		JSONArray jsonProblems;
		Collection<ContestProblem> problems = null;
		try {
			jsonProblems = new JSONArray(jsonEncodedProblems);
			problems = new ArrayList<>();

			for (int i = 0; i < jsonProblems.length(); i++) {
				int problemId = Integer.parseInt(jsonProblems.getJSONObject(i).getString("problemId"));
				Problem problem = serviceLocator.lookupProblemBean().getProblem(problemId);

				if (problem != null) {
					ContestProblem currentProblem = new ContestProblem(contest, problem);

					currentProblem.setProblemOrder(Integer.parseInt(jsonProblems.getJSONObject(i).getString("order")));
					currentProblem.setProblemMark(jsonProblems.getJSONObject(i).getString("mark"));
					currentProblem.setProblemCost(Integer.parseInt(jsonProblems.getJSONObject(i).getString("cost")));

					problems.add(currentProblem);
				}

			}

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return problems;
	}

	/**
	 * Метод возвращает коллекцию поддерживаемых соревнованием языков, раскодированных из JSON-String.
	 *
	 * @param jsonEncodedLanguages
	 * @param contest
	 * @return
	 */
	private Collection<ContestLanguage> decodeContestLanguagesFromJSON(String jsonEncodedLanguages, Contest contest) {

		JSONArray jsonLanguages;
		Collection<ContestLanguage> languages = null;

		try {
			jsonLanguages = new JSONArray(jsonEncodedLanguages);
			languages = new ArrayList<>();

			// Заново создаем список языков, разрешенных в данном соревновании, по полученным от пользователя данным.
			for (int i = 0; i
					< jsonLanguages.length(); i++) {
				if (jsonLanguages.getJSONObject(i).getBoolean("enabled") == true) {
					String languageId = jsonLanguages.getJSONObject(i).getString("id");
					Language language = serviceLocator.lookupLanguageBean().getLanguage(languageId);
					ContestLanguage conLan = new ContestLanguage(contest, language);
					languages.add(conLan);
				}

			}

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return languages;
	}

	/**
	 * Метод возвращает список пользователей, допущенных под разными ролями к данному соревнованию, закодированный как JSON-String.
	 *
	 * @param contestId
	 * @return
	 */
	private String encodeRolesToJSON(int contestId) {
		Collection<Role> roles = serviceLocator.lookupContestBean().getContest(contestId).getRoles();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("rolesTotalCount", roles.size());

			for (Iterator<Role> iter = roles.iterator(); iter.hasNext();) {
				ja.put(this.getRoleJsonView(iter.next()));
			}

			jo.put("roles", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return jo.toString();
	}

	/**
	 * Метод возвращает список заявок, поданных в данное соревнование, закодированный как JSON-String.
	 *
	 * @param contestId
	 * @return
	 */
	private String encodeApplicationsToJSON(int contestId) {

		List<Application> applications = (List<Application>) serviceLocator.lookupContestBean().getContest(contestId).getApplications();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("applicationsTotalCount", applications.size());

			for (Iterator<Application> iter = applications.iterator(); iter.hasNext();) {
				ja.put(this.getApplicationJsonView(iter.next()));
			}

			jo.put("applications", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return jo.toString();
	}

	/**
	 * Метод возвращает список языков, поддерживаемых данным контестов, закодированный как JSON-String.
	 *
	 * @param contestId - идентификатор контеста, или 0 - если вызывается для нового контеста.
	 * @return
	 */
	private String encodeContestLanguagesToJSON(int contestId) {

		ContestLocal contestBean = serviceLocator.lookupContestBean();
		List<Language> languages = serviceLocator.lookupLanguageBean().getLanguages();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		// Если вызывается для нового контеста.
		if (contestId == 0) {
			try {
				jo.put("languagesTotalCount", languages.size());

				for (Language language : languages) {
					JSONObject languageView = new JSONObject();

					languageView.put("enabled", false);
					languageView.put("id", language.getLanguageId());
					languageView.put("title", language.getName());
					languageView.put("description", language.getDescription());

					ja.put(languageView);
				}

				jo.put("languages", ja);
			} catch (JSONException e) {
				logger.log(Level.SEVERE, "exception caught", e);
			}

			return jo.toString();
		}


		// Иначе:
		// Просматриваем все языки, и для выяснения их допустимости провереем,
		// есть ли данный язык в списке языков данноого контеста.
		try {
			jo.put("languagesTotalCount", languages.size());

			for (Language language : languages) {
				JSONObject languageView = new JSONObject();
				ContestLanguage conLanguage = new ContestLanguage(contestBean.getContest(contestId), language);

				if (contestBean.getContest(contestId).getContestLanguages().contains(conLanguage)) {
					languageView.put("enabled", true);
				} else {
					languageView.put("enabled", false);
				}

				languageView.put("id", language.getLanguageId());
				languageView.put("title", language.getName());
				languageView.put("description", language.getDescription());

				ja.put(languageView);
			}

			jo.put("languages", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return jo.toString();
	}

	/**
	 * Метод возвращает список задач данного соревнования, закодированный как JSON-String.
	 *
	 * @param contestId
	 * @return
	 */
	private String encodeContestProblemsToJSON(int contestId) {
		List<ContestProblem> problems = (List<ContestProblem>) serviceLocator.lookupContestBean().getContest(contestId).getContestProblems();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("problemsTotalCount", problems.size());

			for (Iterator<ContestProblem> iter = problems.iterator(); iter.hasNext();) {
				ja.put(this.getContestProblemJsonView(iter.next()));
			}

			jo.put("problems", ja);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
		}

		return jo.toString();
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param role
	 * @return
	 */
	private JSONObject getRoleJsonView(Role role) {
		JSONObject json = new JSONObject();

		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("login", role.getUser().getLogin());
			json.put("role", role.getRoleType().toString());

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Failed creation of JSON view of Role object.", e);
		}

		return json;
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param app
	 * @return
	 */
	private JSONObject getApplicationJsonView(Application app) {
		JSONObject json = new JSONObject();

		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("login", app.getOwner().getLogin());
			json.put("filing_time", new SimpleDateFormat("yyyy.MM.dd HH:mm").format(app.getFilingTime()));
			json.put("message", app.getMessage());
			json.put("status", app.getStatus());

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Failed creation of JSON view of Application object.", e);
		}

		return json;
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param problem
	 * @return
	 */
	private JSONObject getContestProblemJsonView(ContestProblem problem) {
		JSONObject json = new JSONObject();

		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("problemId", problem.getProblem().getProblemId());
			json.put("order", problem.getProblemOrder());
			json.put("mark", problem.getProblemMark());
			json.put("cost", problem.getProblemCost());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Failed creation of JSON view of ContestProblem object.", e);
		}

		return json;
	}
}
