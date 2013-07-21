/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.actions;

import dudge.SearcherLocal;
import dudge.db.User;
import dudge.db.Problem;
import dudge.web.ServiceLocator;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vladimir Shabanov
 */
public class SearchAction extends DispatchAction {

	protected static final Logger logger = Logger.getLogger(SearchAction.class.toString());

	public void search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String query = request.getParameter("query");

		SearcherLocal searcher = ServiceLocator.getInstance().getSearcher();

		List<User> users = searcher.searchUsers(query, 20);
		List<Problem> problems = searcher.searchProblems(query, 20);

		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();

		int totalCount = users.size() + problems.size();

		try {
			jo.put("totalCount", totalCount);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "exception caught", e);
			return;
		}

		for (Problem problem : problems) {
			JSONObject jor = new JSONObject();
			jor.put("obtype", "problem");
			jor.put("obid", Integer.toString(problem.getProblemId()));
			jor.put("caption", problem.getTitle());
			jor.put("addInfo", "");
			ja.put(jor);
		}

		for (User user : users) {
			JSONObject jor = new JSONObject();
			jor.put("obtype", "user");
			jor.put("obid", user.getLogin());
			jor.put("caption", user.getLogin());
			jor.put("addInfo", user.getRealName());
			ja.put(jor);
		}

		try {
			jo.put("results", ja);
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
}