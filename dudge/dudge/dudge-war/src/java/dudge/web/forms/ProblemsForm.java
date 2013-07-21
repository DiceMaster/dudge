/*
 * ProblemsForm.java
 *
 * Created on June 12, 2007, 4:08 PM
 */
package dudge.web.forms;

import dudge.db.ContestProblem;
import dudge.db.Problem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Vladimir Shabanov
 */
public class ProblemsForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	// Список задач, полученных от сервера.
	private List<Problem> problems = Collections.synchronizedList(new ArrayList<Problem>());
	private List<ContestProblem> contestProblems = Collections.synchronizedList(new ArrayList<ContestProblem>());
	//  Действие над текущей задачей.
	private boolean newProblem = false;
	private boolean canEdit = false;
	// Просматриваемая или редактируемая задача.
	private int problemId;
	private String title;
	private String description;
	private Date createTime;
	private int memoryLimit;
	private int cpuTimeLimit;
	private int realTimeLimit;
	private int outputLimit;
	private boolean isHidden = false;
	private String owner;
	private String author;
	private String exampleInputTest = "&nbsp;";
	private String exampleOutputTest = "&nbsp;";
	private FormFile file;

	/**
	 * Creates a new instance of ProblemsForm
	 */
	public ProblemsForm() {
	}

	/**
	 *
	 * @param mapping
	 * @param request
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		problems.clear();
		contestProblems.clear();
		title = "";
		description = "";
		isHidden = false;
		exampleInputTest = "&nbsp;";
		exampleOutputTest = "&nbsp;";
		file = null;
	}

	public List<Problem> getProblems() {
		return problems;
	}

	public int getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.setProblemId(Integer.parseInt(problemId));
	}

	public List<ContestProblem> getContestProblems() {
		return contestProblems;
	}

	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public int getCpuTimeLimit() {
		return cpuTimeLimit;
	}

	public void setCpuTimeLimit(int cpuTimeLimit) {
		this.cpuTimeLimit = cpuTimeLimit;
	}

	public int getRealTimeLimit() {
		return realTimeLimit;
	}

	public void setRealTimeLimit(int realTimeLimit) {
		this.realTimeLimit = realTimeLimit;
	}

	public int getOutputLimit() {
		return outputLimit;
	}

	public void setOutputLimit(int outputLimit) {
		this.outputLimit = outputLimit;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public boolean isNewProblem() {
		return newProblem;
	}

	public void setNewProblem(boolean newProblem) {
		this.newProblem = newProblem;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getExampleInputTest() {
		return exampleInputTest;
	}

	public void setExampleInputTest(String exampleEInputTest) {
		this.exampleInputTest = exampleEInputTest;
	}

	public String getExampleOutputTest() {
		return exampleOutputTest;
	}

	public void setExampleOutputTest(String exampleOutputTest) {
		this.exampleOutputTest = exampleOutputTest;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
