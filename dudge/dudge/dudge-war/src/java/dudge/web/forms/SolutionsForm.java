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
	// Идентификатор соревнования.
	private int contestId;
	// Исходник решения.
	private String sourceCode;
	// Статус решения.
	private String status;
	// Сообщение к статусу решения.
	private String statusMessage;
	// Номер текущего теста.
	private int currentTestNumber;
	// Время отправки решения.
	private long submitTime;
	// Автор решения.
	private String userId;
	// Название задачи.
	private String problemName;
	// Метка задачи.
	private String problemMark;
	// Название соревнования.
	private String contestName;

	public SolutionsForm(int solutionId, String languageId, int problemId, String sourceCode, String status, String statusMessage, int currentTestNumber, long submitTime) {
		this.solutionId = solutionId;
		this.languageId = languageId;
		this.problemId = problemId;
		this.sourceCode = sourceCode;
		this.status = status;
		this.statusMessage = statusMessage;
		this.currentTestNumber = currentTestNumber;
		this.submitTime = submitTime;
	}

	/**
	 * Creates a new instance of SolutionsForm
	 */
	public SolutionsForm() {
	}

	/**
	 *
	 * @param mapping
	 * @param request
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		contestLanguages.clear();
		contestProblems.clear();
		setSolutionId(0);
		languageId = "";
		sourceCode = "";
		status = null;
		statusMessage = "";
		submitTime = 0L;
		userId = null;
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

	public void setProblemId(int problemId) {
		this.problemId = problemId;
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

	public void setSolutionId(int solutionId) {
		this.solutionId = solutionId;
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

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}

	public String getProblemMark() {
		return problemMark;
	}

	public void setProblemMark(String problemMark) {
		this.problemMark = problemMark;
	}

	public String getContestName() {
		return contestName;
	}

	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
}
