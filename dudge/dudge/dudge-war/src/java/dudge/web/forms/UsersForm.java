/*
 * UsersForm.java
 *
 * Created on 15 Август 2007 г., 23:36
 */
package dudge.web.forms;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author olorin
 * @version
 */
public class UsersForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	// Действие над текущим пользователем.
	private boolean newUser = false;
	// Информация о текущем просматриваемом/редактируемом пользователе.
	private String login = "";
	private String password = "";
	private String passwordConfirm = "";
	private String email = "";
	private String realName = "";
	private String organization = "";
	private String faculty = "";
	private String course = "";
	private String group = "";
	private String age = "";
	private String jabberId = "";
	private String icqNumber = "";
	private Date regDate;
	private String totalProblemsSolved = "0";
	private String totalContestsMember = "0";
	// Информация о правах просматриваемого/редактируемого пользователя.
	private boolean isAdmin = false;
	private boolean isContestCreator = false;
	private boolean isProblemCreator = false;
	// Ошибки валидации
	private boolean hasLoginError = false;
	private boolean hasRealNameError = false;
	private boolean hasPasswordError = false;
	private boolean hasEmailError = false;
	private String errorMessageKey = "";

	/**
	 *
	 * @return
	 */
	public boolean isHasEmailError() {
		return hasEmailError;
	}

	/**
	 *
	 * @param hasEmailError
	 */
	public void setHasEmailError(boolean hasEmailError) {
		this.hasEmailError = hasEmailError;
	}

	/**
	 *
	 * @return
	 */
	public boolean isHasLoginError() {
		return hasLoginError;
	}

	/**
	 *
	 * @param hasLoginError
	 */
	public void setHasLoginError(boolean hasLoginError) {
		this.hasLoginError = hasLoginError;
	}

	/**
	 *
	 * @return
	 */
	public boolean isHasPasswordError() {
		return hasPasswordError;
	}

	/**
	 *
	 * @param hasPasswordError
	 */
	public void setHasPasswordError(boolean hasPasswordError) {
		this.hasPasswordError = hasPasswordError;
	}

	/**
	 *
	 * @return
	 */
	public boolean isHasRealNameError() {
		return hasRealNameError;
	}

	/**
	 *
	 * @param hasRealNameError
	 */
	public void setHasRealNameError(boolean hasRealNameError) {
		this.hasRealNameError = hasRealNameError;
	}

	/**
	 *
	 * @return
	 */
	public String getErrorMessageKey() {
		return errorMessageKey;
	}

	/**
	 *
	 * @param errorMessageKey
	 */
	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}

	/**
	 *
	 * @param mapping
	 * @param request
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// Выставляем дефолтные значения соотв. полей информации пользователя.
		setLogin("");
		setPassword("");
		setPasswordConfirm("");
		setEmail("");
		setRealName("");
		setOrganization("");
		setFaculty("");
		setCourse("");
		setGroup("");
		setAge("");
		setTotalContestsMember("0");
		setTotalContestsMember("0");
		setJabberId("");
		setIcqNumber("");
		setProblemCreator(false);
		setAdmin(false);
		setContestCreator(false);
	}

	/**
	 *
	 * @return
	 */
	public String getLogin() {
		return login;
	}

	/**
	 *
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 *
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *
	 * @return
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 *
	 * @param passwordConfirm
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 *
	 * @param realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 *
	 * @return
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 *
	 * @param organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 *
	 * @return
	 */
	public String getFaculty() {
		return faculty;
	}

	/**
	 *
	 * @param faculty
	 */
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	/**
	 *
	 * @return
	 */
	public String getCourse() {
		return course;
	}

	/**
	 *
	 * @param course
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 *
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/**
	 *
	 * @param group
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 *
	 * @return
	 */
	public String getAge() {
		return age;
	}

	/**
	 *
	 * @param age
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 *
	 * @return
	 */
	public String getJabberId() {
		return jabberId;
	}

	/**
	 *
	 * @param jabberId
	 */
	public void setJabberId(String jabberId) {
		this.jabberId = jabberId;
	}

	/**
	 *
	 * @return
	 */
	public String getIcqNumber() {
		return icqNumber;
	}

	/**
	 *
	 * @param icqNumber
	 */
	public void setIcqNumber(String icqNumber) {
		this.icqNumber = icqNumber;
	}

	/**
	 *
	 * @return
	 */
	public Date getRegDate() {
		return regDate;
	}

	/**
	 *
	 * @param regDate
	 */
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	/**
	 *
	 * @return
	 */
	public boolean isNewUser() {
		return newUser;
	}

	/**
	 *
	 * @param newUser
	 */
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	/**
	 *
	 * @return
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 *
	 * @param isAdmin
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 *
	 * @return
	 */
	public boolean isContestCreator() {
		return isContestCreator;
	}

	/**
	 *
	 * @param isContestCreator
	 */
	public void setContestCreator(boolean isContestCreator) {
		this.isContestCreator = isContestCreator;
	}

	/**
	 *
	 * @return
	 */
	public String getTotalProblemsSolved() {
		return totalProblemsSolved;
	}

	/**
	 *
	 * @param totalProblemsSolved
	 */
	public void setTotalProblemsSolved(String totalProblemsSolved) {
		this.totalProblemsSolved = totalProblemsSolved;
	}

	/**
	 *
	 * @return
	 */
	public String getTotalContestsMember() {
		return totalContestsMember;
	}

	/**
	 *
	 * @param totalContestsMember
	 */
	public void setTotalContestsMember(String totalContestsMember) {
		this.totalContestsMember = totalContestsMember;
	}

	/**
	 *
	 * @return
	 */
	public boolean isProblemCreator() {
		return isProblemCreator;
	}

	/**
	 *
	 * @param isProblemCreator
	 */
	public void setProblemCreator(boolean isProblemCreator) {
		this.isProblemCreator = isProblemCreator;
	}
}
