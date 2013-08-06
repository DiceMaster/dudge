package dudge.web.actions;

import dudge.LanguageLocal;
import dudge.db.Language;
import dudge.PermissionCheckerRemote;
import dudge.web.AuthenticationObject;
import dudge.web.ServiceLocator;
import dudge.web.forms.LanguagesForm;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

	private static final Logger logger = Logger.getLogger(LanguagesAction.class.toString());
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();

	/**
	 * Creates a new instance of ContestsAction
	 */
	public LanguagesAction() {
	}

	/**
	 * Метод для перехода на страницу списка языков программирования.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward list(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("languages");
	}

	/**
	 * Метод, возвращающий асинхронному запросу клиента данные о языках программирования в формате JSON.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void getLanguagesList(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		List<Language> languages = serviceLocator.lookupLanguageBean().getLanguages();

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		try {
			jo.put("totalCount", languages.size());
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		AuthenticationObject ao = AuthenticationObject.extract(request);
		for (Iterator<Language> iter = languages.iterator(); iter.hasNext();) {
			ja.put(this.getLanguageJSONView(iter.next(), ao));
		}
		try {
			jo.put("languages", ja);
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
	 * Метод для перехода на страницу просмотра контеста.
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		// Получаем идентификатор редактируемого языка, чтобы по нему найти объект нужного языка
		// и выставить текущие значения как значения по умолчанию для полей на странице просмотра языка.

		String languageId = request.getParameter("languageId");
		Language language = serviceLocator.lookupLanguageBean().getLanguage(languageId);

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

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		LanguagesForm lf = (LanguagesForm) af;

		// Находим язык с указанным логином.
		String languageId = request.getParameter("languageId");
		Language language = serviceLocator.lookupLanguageBean().getLanguage(languageId);
		lf.reset(mapping, request);

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем права пользователя на редактирование языков.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyLanguage(ao.getUsername())) {
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

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward create(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {

		LanguagesForm lf = (LanguagesForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddLanguage(ao.getUsername())) {
			return mapping.findForward("accessDenied");

		}

		lf.reset(mapping, request);

		lf.setNewLanguage(true);

		return mapping.findForward("editLanguage");
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
		LanguagesForm lf = (LanguagesForm) af;

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canAddLanguage(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		Language addingLanguage = new Language();

		addingLanguage.setLanguageId(lf.getLanguageId());
		addingLanguage.setName(lf.getTitle());
		addingLanguage.setDescription(lf.getDescription());

		addingLanguage.setFileExtension(lf.getFileExtension());
		addingLanguage.setCompilationCommand(lf.getCompilationCommand());
		addingLanguage.setExecutionCommand(lf.getExecutionCommand());

		// Костыль, необходимо осуществлять валидацию по нормальному.
		try {
			serviceLocator.lookupLanguageBean().addLanguage(addingLanguage);
		} catch (EntityExistsException ex) {
			return mapping.findForward("accessDenied");
		}
		//Редирект на страницу новосозданного языка.
		ActionForward forward = new ActionForward();
		forward.setPath("languages.do?reqCode=view&languageId=" + addingLanguage.getLanguageId());
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
		LanguagesForm lf = (LanguagesForm) af;

		LanguageLocal languageBean = serviceLocator.lookupLanguageBean();

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canModifyLanguage(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}

		Language modifiedLanguage = languageBean.getLanguage(lf.getLanguageId());

		modifiedLanguage.setName(lf.getTitle());
		modifiedLanguage.setDescription(lf.getDescription());

		modifiedLanguage.setFileExtension(lf.getFileExtension());
		modifiedLanguage.setCompilationCommand(lf.getCompilationCommand());
		modifiedLanguage.setExecutionCommand(lf.getExecutionCommand());

		languageBean.modifyLanguage(modifiedLanguage);

		//Редирект на страницу отредактированного языка.
		ActionForward forward = new ActionForward();
		forward.setPath("languages.do?reqCode=view&languageId=" + modifiedLanguage.getLanguageId());
		forward.setRedirect(true);
		return forward;
	}

	/**
	 *
	 * @param mapping
	 * @param af
	 * @param request
	 * @param response
	 */
	public void delete(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		String languageId = request.getParameter("id");

		AuthenticationObject ao = AuthenticationObject.extract(request);

		// Проверяем право пользователя.
		PermissionCheckerRemote pcb = ao.getPermissionChecker();
		if (!pcb.canDeleteLanguage(ao.getUsername())) {
			return;
		}

		serviceLocator.lookupLanguageBean().deleteLanguage(languageId);
	}

	/**
	 * Метод возвращает представления объекта Language в формата JSON - это нужно для его отображение на стороне клиента через JavaScript/AJAX.
	 *
	 * @param language
	 * @param ao
	 * @return
	 */
	private JSONObject getLanguageJSONView(Language language, AuthenticationObject ao) {

		JSONObject json = new JSONObject();

		PermissionCheckerRemote pcb = ao.getPermissionChecker();

		// Заполняем данными задачи созданный объект JSON.
		try {
			json.put("id", language.getLanguageId());
			json.put("title", language.getName());
			json.put("description", language.getDescription());
			json.put("editable", pcb.canModifyLanguage(ao.getUsername()));
			json.put("deletable", pcb.canDeleteLanguage(ao.getUsername()));
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Failed creation of JSON view of Language object.", e);
		}
		return json;
	}
}
