/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.actions;

import dudge.DudgeLocal;
import dudge.db.Contest;
import dudge.db.Language;
import dudge.PermissionCheckerRemote;
import dudge.web.SessionObject;
import dudge.web.forms.LanguagesForm;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
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
 * @author olorin
 */
public class LanguagesAction extends DispatchAction {

	protected static Logger logger = Logger.getLogger(LanguagesAction.class.toString());

	/** Creates a new instance of ContestsAction */
	public LanguagesAction() {
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

	/**
	 * Метод для перехода на страницу списка языков программирования.
	 */
	public ActionForward list(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		return mapping.findForward("languages");
	}

	/**
	 * Метод, возвращающий асинхронному запросу клиента данные о языках программирования
	 * в формате JSON. 
	 */
	public void getLanguagesList(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {

		List<Language> languages = lookupDudgeBean().getLanguages();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("totalCount", languages.size());
		} catch (JSONException ex) {
			ex.printStackTrace();
			return;
		}

		SessionObject so = SessionObject.extract(request.getSession());
		for (Iterator<Language> iter = languages.iterator(); iter.hasNext();) {
			ja.put(this.getLanguageJSONView(iter.next(), so));
		}
		try {
			jo.put("languages", ja);
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

	/**
	 * Метод для перехода на страницу просмотра контеста.  
	 */
	public ActionForward view(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		// Получаем идентификатор редактируемого языка, чтобы по нему найти объект нужного языка
		// и выставить текущие значения как значения по умолчанию для полей на странице просмотра языка.

		String languageId = request.getParameter("languageId");
		Language language = lookupDudgeBean().getLanguage(languageId);

		lf.reset(mapping, request);

		// Выставляем значения для полей, соотв. текущим значениям редактируемого контеста.
		lf.setLanguageId(languageId);
		lf.setTitle(language.getName());
		lf.setDescription(language.getDescription());
		lf.setFileExtension(language.getFileExtension());
		lf.setCompilationCommand(language.getCompilationCommand());
		lf.setExecutionCommand(language.getExecutionCommand());

		return mapping.findForward("viewLanguage");

	}

	public ActionForward edit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		// Находим язык с указанным логином.
		String languageId = request.getParameter("languageId");
		Language language = lookupDudgeBean().getLanguage(languageId);
		lf.reset(mapping, request);

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем права пользователя на редактирование языков.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyLanguage(so.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		// Выставляем значения соотв. полей информации языка программирования.
		lf.setLanguageId(languageId);
		lf.setTitle(language.getName());
		lf.setDescription(language.getDescription());
		lf.setFileExtension(language.getFileExtension());
		lf.setCompilationCommand(language.getCompilationCommand());
		lf.setExecutionCommand(language.getExecutionCommand());


		lf.setNewLanguage(false);
		return mapping.findForward("editLanguage");
	}

	public ActionForward create(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {

		LanguagesForm lf = (LanguagesForm) af;

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canAddLanguage(so.getUsername())) {
			return mapping.findForward("accessDenied");

		}

		lf.reset(mapping, request);

		lf.setNewLanguage(true);

		return mapping.findForward("editLanguage");
	}

	public ActionForward submitCreate(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		DudgeLocal dudgeBean = lookupDudgeBean();

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canAddLanguage(so.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		Language addingLanguage = new Language();

		addingLanguage.setLanguageId(lf.getLanguageId());
		addingLanguage.setName(lf.getTitle());
		addingLanguage.setDescription(lf.getDescription());

		addingLanguage.setFileExtension(lf.getFileExtension());
		;
		addingLanguage.setCompilationCommand(lf.getCompilationCommand());
		addingLanguage.setExecutionCommand(lf.getExecutionCommand());

		// Костыль, необходимо осуществлять валидацию по нормальному.
		try {
			dudgeBean.addLanguage(addingLanguage);
		} catch (EntityExistsException ex) {
			return mapping.findForward("accessDenied");
		}
		//Редирект на страницу новосозданного языка.
		ActionForward forward = new ActionForward();
		forward.setPath("languages.do?reqCode=view&languageId=" + addingLanguage.getLanguageId());
		forward.setRedirect(true);
		return forward;
	}

	public ActionForward submitEdit(
			ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		DudgeLocal dudgeBean = lookupDudgeBean();

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canModifyLanguage(so.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		Language modifiedLanguage = dudgeBean.getLanguage(lf.getLanguageId());

		modifiedLanguage.setName(lf.getTitle());
		modifiedLanguage.setDescription(lf.getDescription());

		modifiedLanguage.setFileExtension(lf.getFileExtension());
		modifiedLanguage.setCompilationCommand(lf.getCompilationCommand());
		modifiedLanguage.setExecutionCommand(lf.getExecutionCommand());

		dudgeBean.modifyLanguage(modifiedLanguage);

		//Редирект на страницу отредактированного языка.
		ActionForward forward = new ActionForward();
		forward.setPath("languages.do?reqCode=view&languageId=" + modifiedLanguage.getLanguageId());
		forward.setRedirect(true);
		return forward;
	}

	public void delete(ActionMapping mapping,
			ActionForm af,
			HttpServletRequest request,
			HttpServletResponse response) {
		String languageId = request.getParameter("id");

		DudgeLocal dudgeBean = lookupDudgeBean();

		SessionObject so = SessionObject.extract(request.getSession());

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = so.getPermissionChecker();
		if (!pcb.canDeleteLanguage(so.getUsername())) {
			return;
		}
		dudgeBean.deleteLanguage(languageId);
	}

	/**
	 * Метод возвращает представления объекта Language в формата JSON - это нужно
	 * для его отображение на стороне клиента через JavaScript/AJAX.
	 */
	private JSONObject getLanguageJSONView(Language language, SessionObject so) {

		JSONObject json = new JSONObject();

		PermissionCheckerRemote pcb = so.getPermissionChecker();

		// Заполняем данными задачи созданный объект JSON.
		try {
			json.put("id", language.getLanguageId());
			json.put("title", language.getName());
			json.put("description", language.getDescription());

			json.put("editable",
					pcb.canModifyLanguage(so.getUsername()));

			json.put("deletable",
					pcb.canDeleteLanguage(so.getUsername()));

		} catch (JSONException je) {
			logger.log(Level.SEVERE, "Failed creation of JSON view of Language object.", je);
		}
		return json;
	}
}
