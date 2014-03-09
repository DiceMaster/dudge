/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.web.forms;

import dudge.db.ContestProblem;
import dudge.monitor.AcmMonitorRecord;
import dudge.monitor.GlobalMonitorRecord;
import dudge.monitor.SchoolMonitorRecord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Arengor
 */
public class MonitorForm extends ActionForm {

	private int contestId;
	private List<ContestProblem> problems = Collections.synchronizedList(new ArrayList<ContestProblem>());

	public MonitorForm() {

	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);

		problems.clear();
	}

	public List<ContestProblem> getProblems() {
		return problems;
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
}
