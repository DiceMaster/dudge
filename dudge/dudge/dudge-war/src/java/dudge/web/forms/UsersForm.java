/*
 * UsersForm.java
 *
 * Created on 15 Август 2007 г., 23:36
 */
package dudge.web.forms;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author olorin
 * @version
 */
public class UsersForm extends org.apache.struts.action.ActionForm {
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

    public boolean isHasEmailError() {
        return hasEmailError;
    }

    public void setHasEmailError(boolean hasEmailError) {
        this.hasEmailError = hasEmailError;
    }

    public boolean isHasLoginError() {
        return hasLoginError;
    }

    public void setHasLoginError(boolean hasLoginError) {
        this.hasLoginError = hasLoginError;
    }

    public boolean isHasPasswordError() {
        return hasPasswordError;
    }

    public void setHasPasswordError(boolean hasPasswordError) {
        this.hasPasswordError = hasPasswordError;
    }

    public boolean isHasRealNameError() {
        return hasRealNameError;
    }

    public void setHasRealNameError(boolean hasRealNameError) {
        this.hasRealNameError = hasRealNameError;
    }

    public String getErrorMessageKey() {
        return errorMessageKey;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getJabberId() {
        return jabberId;
    }

    public void setJabberId(String jabberId) {
        this.jabberId = jabberId;
    }

    public String getIcqNumber() {
        return icqNumber;
    }

    public void setIcqNumber(String icqNumber) {
        this.icqNumber = icqNumber;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isContestCreator() {
        return isContestCreator;
    }

    public void setContestCreator(boolean isContestCreator) {
        this.isContestCreator = isContestCreator;
    }

    public String getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(String totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public String getTotalContestsMember() {
        return totalContestsMember;
    }

    public void setTotalContestsMember(String totalContestsMember) {
        this.totalContestsMember = totalContestsMember;
    }

    public boolean isProblemCreator() {
        return isProblemCreator;
    }

    public void setProblemCreator(boolean isProblemCreator) {
        this.isProblemCreator = isProblemCreator;
    }
}
