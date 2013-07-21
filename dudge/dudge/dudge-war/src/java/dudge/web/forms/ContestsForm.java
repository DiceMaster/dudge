/*
 * ContestsForm.java
 * Created on June 10, 2007, 11:46 PM
 */
package dudge.web.forms;

import dudge.db.ContestType;
import dudge.db.RoleType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vladimir Shabanov
 */
public class ContestsForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	private List<ContestType> contestTypes = Collections.synchronizedList(new ArrayList<ContestType>());
	private List<RoleType> roleTypes = Collections.synchronizedList(new ArrayList<RoleType>());
	//  Действие над текущим соревнованием.
	private boolean newContest = false;
	// Текущее редактируемое / создаваемое соревнование.
	private int contestId;
	// Технические параметры соревнования
	private String caption;
	private String description;
	private String rules;
	private String contestType;
	private String startDate;
	private String startHour;
	private String startMinute;
	private String durationHours;
	private String durationMinutes;
	private String freezeTime;
	private boolean open;
	// Пользователи и задачи, участвующие в контесте, 
	// а так же разрешенные языки и поданные заявки.
	private String encodedRoles;
	private String encodedContestProblems;
	private String encodedContestLanguages;
	private String encodedApplications;
	// Роли пользователей.
	private String userRoles;

	/**
	 * Creates a new instance of ContestsForm
	 */
	public ContestsForm() {
	}

	/**
	 *
	 * @param mapping
	 * @param request
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		open = false;
		caption = "";
		description = "";
		rules = "";
		contestType = "";
		startDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		startHour = "12";
		startMinute = "00";
		durationHours = "05";
		durationMinutes = "00";
		freezeTime = "0";
		contestTypes.clear();
		roleTypes.clear();
		encodedContestLanguages = "";
		encodedContestProblems = "";
		encodedRoles = "";
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = Integer.parseInt(contestId);
	}

	public String getContestType() {
		return contestType;
	}

	public void setContestType(String contestType) {
		this.contestType = contestType;
	}

	public String getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(String durationHours) {
		this.durationHours = durationHours;
	}

	public List<ContestType> getContestTypes() {
		return contestTypes;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}

	public String getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(String durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public List<RoleType> getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(List<RoleType> roleTypes) {
		this.roleTypes = roleTypes;
	}

	public boolean isNewContest() {
		return newContest;
	}

	public void setNewContest(boolean newContest) {
		this.newContest = newContest;
	}

	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}

	public String getEncodedRoles() {
		return encodedRoles;
	}

	public void setEncodedRoles(String encodedRoles) {
		this.encodedRoles = encodedRoles;
	}

	public String getEncodedContestProblems() {
		return encodedContestProblems;
	}

	public void setEncodedContestProblems(String encodedContestProblems) {
		this.encodedContestProblems = encodedContestProblems;
	}

	public String getEncodedContestLanguages() {
		return encodedContestLanguages;
	}

	public void setEncodedContestLanguages(String encodedContestLanguages) {
		this.encodedContestLanguages = encodedContestLanguages;
	}

	/**
	 * @return время заморозки монитора, в минутах.
	 */
	public String getFreezeTime() {
		return freezeTime;
	}

	/**
	 * @param freezeTime время заморозки монитора, в минутах.
	 */
	public void setFreezeTime(String freezeTime) {
		this.freezeTime = freezeTime;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getEncodedApplications() {
		return encodedApplications;
	}

	public void setEncodedApplications(String encodedApplications) {
		this.encodedApplications = encodedApplications;
	}
}
