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

	/**
	 *
	 * @return
	 */
	public List<Problem> getProblems() {
		return problems;
	}

	/**
	 *
	 * @return
	 */
	public int getProblemId() {
		return problemId;
	}

	/**
	 *
	 * @param problemId
	 */
	public void setProblemId(String problemId) {
		this.setProblemId(Integer.parseInt(problemId));
	}

	/**
	 *
	 * @return
	 */
	public List<ContestProblem> getContestProblems() {
		return contestProblems;
	}

	/**
	 *
	 * @param problemId
	 */
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 *
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 *
	 * @return
	 */
	public int getMemoryLimit() {
		return memoryLimit;
	}

	/**
	 *
	 * @param memoryLimit
	 */
	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	/**
	 *
	 * @return
	 */
	public int getCpuTimeLimit() {
		return cpuTimeLimit;
	}

	/**
	 *
	 * @param cpuTimeLimit
	 */
	public void setCpuTimeLimit(int cpuTimeLimit) {
		this.cpuTimeLimit = cpuTimeLimit;
	}

	/**
	 *
	 * @return
	 */
	public int getRealTimeLimit() {
		return realTimeLimit;
	}

	/**
	 *
	 * @param realTimeLimit
	 */
	public void setRealTimeLimit(int realTimeLimit) {
		this.realTimeLimit = realTimeLimit;
	}

	/**
	 *
	 * @return
	 */
	public int getOutputLimit() {
		return outputLimit;
	}

	/**
	 *
	 * @param outputLimit
	 */
	public void setOutputLimit(int outputLimit) {
		this.outputLimit = outputLimit;
	}

	/**
	 *
	 * @return
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 *
	 * @param owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 *
	 * @return
	 */
	public FormFile getFile() {
		return file;
	}

	/**
	 *
	 * @param file
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}

	/**
	 *
	 * @return
	 */
	public boolean isNewProblem() {
		return newProblem;
	}

	/**
	 *
	 * @param newProblem
	 */
	public void setNewProblem(boolean newProblem) {
		this.newProblem = newProblem;
	}

	/**
	 *
	 * @return
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	/**
	 *
	 * @param canEdit
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 *
	 * @return
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 *
	 * @param isHidden
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 *
	 * @return
	 */
	public String getExampleInputTest() {
		return exampleInputTest;
	}

	/**
	 *
	 * @param exampleEInputTest
	 */
	public void setExampleInputTest(String exampleEInputTest) {
		this.exampleInputTest = exampleEInputTest;
	}

	/**
	 *
	 * @return
	 */
	public String getExampleOutputTest() {
		return exampleOutputTest;
	}

	/**
	 *
	 * @param exampleOutputTest
	 */
	public void setExampleOutputTest(String exampleOutputTest) {
		this.exampleOutputTest = exampleOutputTest;
	}

	/**
	 *
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 *
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
}
