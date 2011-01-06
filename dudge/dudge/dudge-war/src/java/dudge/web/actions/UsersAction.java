/*
 * UsersAction.java
 *
 * Created on 15 Август 2007 г., 23:30
 */
package dudge.web.actions;

import dudge.DudgeLocal;
import dudge.db.Problem;
import dudge.PermissionCheckerRemote;
import dudge.db.User;
import dudge.web.forms.UsersForm;
import dudge.web.SessionObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import json.JSONObject;
import json.JSONArray;
import json.JSONException;

/**
 *
 * @author olorin
 * @version
 */
public class UsersAction extends DispatchAction {

	protected static Logger logger = Logger.getLogger(Problem.class.toString());

	/** Creates a new instance of RegistrationAction */
	public UsersAction() {
	}

	private DudgeLocal lookupDudgeBean() {
		try {
			Context c = new InitialContext();
			return (DudgeLocal) c.lookup("java:comp/env/ejb/DudgeBean");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	public ActionForward list(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		DudgeLocal dudgeBean = lookupDudgeBean();
		UsersForm rf = (UsersForm) af;

		return mapping.findForward("users");
	}

	public ActionForward view(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		UsersForm uf = (UsersForm) af;
		// Получаем логин просматриваемого пользователя.
		String login = uf.getLogin();

		SessionObject so = SessionObject.extract(request.getSession());
		// Находим пользователя с указанным логином.
		User user = lookupDudgeBean().getUser(login);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canGetUser(
				so.getUsername(),
				user)) {
			return mapping.findForward("accessDenied");
		}

		uf.reset(mapping, request);

		// Выставляем значения соотв. полей информации пользователя.
		uf.setLogin(user.getLogin());
		uf.setRealName(user.getRealName());
		uf.setEmail(user.getEmail());
		uf.setOrganization(user.getOrganization());
		uf.setRegDate(user.getRegDate());
		if (user.getAge() != null) {
			uf.setAge(String.valueOf(user.getAge()));
		}
		uf.setJabberId(String.valueOf(user.getJabberId()));
		if (user.getIcqNumber() != null) {
			uf.setIcqNumber(String.valueOf(user.getIcqNumber()));
		}
		uf.setAdmin(user.isAdmin());
		uf.setContestCreator(user.canCreateContest());
		uf.setProblemCreator(user.canCreateProblem());

		return mapping.findForward("viewUser");
	}

	public ActionForward edit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		UsersForm uf = (UsersForm) af;

		// Находим пользователя с указанным логином.
		User user = lookupDudgeBean().getUser(uf.getLogin());
		uf.reset(mapping, request);

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем права пользователя на редактирование выбранного профиля.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyUser(
				so.getUsername(),
				user.getLogin())) {
			return mapping.findForward("accessDenied");
		}

		// Выставляем значения соотв. полей информации пользователя.
		uf.setLogin(user.getLogin());
		uf.setRealName(user.getRealName());
		uf.setEmail(user.getEmail());
		uf.setOrganization(user.getOrganization());
		if (user.getAge() != null) {
			uf.setAge(String.valueOf(user.getAge()));
		}

		uf.setJabberId(user.getJabberId());

		if (user.getIcqNumber() != null) {
			uf.setIcqNumber(String.valueOf(user.getIcqNumber()));
		}

		uf.setAdmin(user.isAdmin());
		uf.setContestCreator(user.canCreateContest());
		uf.setProblemCreator(user.canCreateProblem());
		uf.setNewUser(false);
		return mapping.findForward("editUser");
	}

	public ActionForward register(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		UsersForm uf = (UsersForm) af;
		uf.reset(mapping, request);

		uf.setNewUser(true);
		return mapping.findForward("editUser");
	}

	public ActionForward submitRegister(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		UsersForm uf = (UsersForm) af;
		SessionObject so = SessionObject.extract(request.getSession());
		PermissionCheckerRemote pcb = so.getPermissionChecker();

		if (!uf.getPassword().equals(uf.getPasswordConfirm())) {
			throw new RuntimeException("Passwords do not match.");
		}

		dudge.db.User user = null;
		try {
			user = lookupDudgeBean().registerUser(uf.getLogin(), uf.getPassword(), uf.getEmail());
		} catch (EntityExistsException ex) {
			return mapping.findForward("accessDenied");
		}
		user.setRealName(uf.getRealName());
		user.setOrganization(uf.getOrganization());

		try {
			if (!uf.getAge().equals("")) {
				user.setAge(Integer.parseInt(uf.getAge()));
			}
		} catch (NumberFormatException ex) {
		}

		user.setJabberId(uf.getJabberId());

		try {
			if (!uf.getIcqNumber().equals("")) {
				user.setIcqNumber(Integer.parseInt(uf.getIcqNumber()));
			}
		} catch (NumberFormatException ex) {
		}


		if (pcb.canDeepModifyUser(so.getUsername(), user.getLogin())) {
			user.setAdmin(uf.isAdmin());
			user.setCreateContest(uf.isContestCreator());
			user.setCreateProblem(uf.isProblemCreator());
		}

		lookupDudgeBean().modifyUser(user);
		return mapping.findForward("registrationSuccess");
	}

	public ActionForward submitEdit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		UsersForm uf = (UsersForm) af;

		dudge.db.User user = lookupDudgeBean().getUser(uf.getLogin());

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyUser(
				so.getUsername(),
				user.getLogin())) {
			return mapping.findForward("accessDenied");
		}

		user.setEmail(uf.getEmail());
		user.setRealName(uf.getRealName());
		user.setOrganization(uf.getOrganization());

		try {
			if (!uf.getAge().equals("")) {
				user.setAge(Integer.parseInt(uf.getAge()));
			}
		} catch (NumberFormatException ex) {
		}

		user.setJabberId(uf.getJabberId());

		try {
			if (!uf.getIcqNumber().equals("")) {
				user.setIcqNumber(Integer.parseInt(uf.getIcqNumber()));
			}
		} catch (NumberFormatException ex) {
		}

		if (pcb.canDeepModifyUser(so.getUsername(), user.getLogin())) {
			user.setAdmin(uf.isAdmin());
			user.setCreateContest(uf.isContestCreator());
			user.setCreateProblem(uf.isProblemCreator());
		}

		lookupDudgeBean().modifyUser(user);

		// Редирект на страницу просмотра профиля отредактированного пользователя.
		ActionForward forward = new ActionForward();
		forward.setPath("users.do?reqCode=view&login=" + user.getLogin());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 * Возвращает AJAX-клиенту очередную порцию из списка пользователей.
	 */
	public void getUserList(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {

		//  Получаем из запроса, какие данные требуются клиенту.
		int start = Integer.parseInt((String) request.getParameter("start"));
		int limit = Integer.parseInt((String) request.getParameter("limit"));

		List<User> users = lookupDudgeBean().getUsers();
		List<User> selectedUsers;
		try {
			selectedUsers = users.subList(start, start + limit);
		} catch (IndexOutOfBoundsException e) {
			selectedUsers = users.subList(start, users.size());
		}
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("totalCount", users.size());
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}

		for (Iterator<User> iter = selectedUsers.iterator(); iter.hasNext();) {
			ja.put(this.getUserJSONView(iter.next()));
		}
		try {
			jo.put("users", ja);
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

	/**
	 * Удаляет выбранного пользователя из системы. Доступен только администраторам.
	 */
	public ActionForward delete(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем права пользователя на удаление пользователей из системы.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canDeleteUser(
				so.getUsername())) {
			return mapping.findForward("accessDenied");
		}
		String deletedUser = (String) request.getParameter("login");
		lookupDudgeBean().deleteUser(deletedUser);

		return mapping.findForward("users");
	}

	/**
	 * Метод возвращает представления объекта в формата JSON - это нужно
	 * для его отображение на стороне клиента через JavaScript/AJAX.
	 */
	public JSONObject getUserJSONView(User user) {

		JSONObject json = new JSONObject();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		// Заполняем данными пользователя созданный объект JSON.
		try {
			json.put("login", user.getLogin());
			json.put("realname", user.getRealName());
			json.put("regdate", sdf.format(user.getRegDate()));
			json.put("organization", user.getOrganization());
		} catch (JSONException Je) {
			this.logger.severe("Truble in creating JSON view of User object.");
		}
		return json;
	}

	public void changePassword(ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");

		SessionObject so = SessionObject.extract(request.getSession());
		DudgeLocal dudgeBean = lookupDudgeBean();

		// Передаем клиенту информацию о результате смены пароля.
		response.setContentType("application/x-json");
		
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONObject record = new JSONObject();

		try {
			jo.put("totalCount", 1);
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}

		// Проверяем, что старый пароль верен.
		User currentUser = dudgeBean.getUser(so.getUsername());
		
		// DBG
		this.logger.severe(oldPassword);
		this.logger.severe(newPassword);
		this.logger.severe(currentUser.getPwdHash());
		
		
		if (!dudgeBean.calcHash(oldPassword).equals(currentUser.getPwdHash())) {
			try {
				record.put("result", "0");
				ja.put(record);
				jo.put("password", ja);
			} catch (JSONException Je) {
				this.logger.severe("Truble in creating JSON view of status for password changing operation.");
				return;
			}
			try {
				response.getWriter().print(jo);
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
			return;
		}

		currentUser.setPwdHash(dudgeBean.calcHash(newPassword));
		dudgeBean.modifyUser(currentUser);


		// Отсылаем на сервер ответ о результате смены пароля.

		try {
			record.put("result", "1");
			ja.put(record);
			jo.put("password", ja);
		} catch (JSONException Je) {
			this.logger.severe("Truble in creating JSON view of status for password changing operation.");
			return;
		}
		try {
			response.getWriter().print(jo);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
}