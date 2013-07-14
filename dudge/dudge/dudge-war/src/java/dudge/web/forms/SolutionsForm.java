/*
 * SubmitForm.java
 *
 * Created on June 12, 2007, 9:10 PM
 */

package dudge.web.forms;

import dudge.db.ContestLanguage;
import dudge.db.ContestProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vladimir Shabanov
 */
public class SolutionsForm extends ActionForm {
        public static final long serialVersionUID = 1L;
	
	private List<ContestLanguage> contestLanguages = Collections.synchronizedList(new ArrayList<ContestLanguage>());
	
	private List<ContestProblem> contestProblems = Collections.synchronizedList(new ArrayList<ContestProblem>());
	
	// Идентификатор решения.
	private int solutionId;
	
	// Идентификатор языка решения.
	private String languageId;
	
	// Идентификатор задачи решения.
	private int problemId;
	
	// Исходник решения.
	private String sourceCode;
	
	// Статус решения.
	private String status;
	
	// Сообщение к статусу решения.
	private String statusMessage;
	
	private int currentTestNumber;
	
	/** Creates a new instance of SolutionsForm */
	public SolutionsForm() {
	}

        /**
         * 
         * @param mapping
         * @param request 
         */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		contestLanguages.clear();
		contestProblems.clear();
		solutionId = 0;
		languageId = "";
		sourceCode = "";
		status = null;
		statusMessage = "";
	}
	
	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getProblemId() {
		return Integer.toString(problemId);
	}

	public void setProblemId(String problemId) {
		this.problemId = Integer.parseInt(problemId);
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public List<ContestLanguage> getContestLanguages() {
		return contestLanguages;
	}	

	public List<ContestProblem> getContestProblems() {
		return contestProblems;
	}	
	
	public int getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = Integer.parseInt(solutionId);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public int getCurrentTestNumber() {
		return currentTestNumber;
	}

	public void setCurrentTestNumber(int currentTestNumber) {
		this.currentTestNumber = currentTestNumber;
	}
}
