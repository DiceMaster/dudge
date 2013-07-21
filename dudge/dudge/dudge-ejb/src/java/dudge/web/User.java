package dudge.web;

import java.util.Date;
import java.io.Serializable;

/**
 * Class User
 *
 * @author Vladimir Shabanov
 */
public class User implements Serializable {

	public static final long serialVersionUID = 1L;
	private String login;
	private String email;
	private String realName = "";
	private String organization = "";
	private Date regDate;
	private String jabberId = "";
	private Integer icqNumber = null;
	private boolean isAdmin;
	private boolean canCreateContest;
	private boolean canCreateProblem;

	/**
	 * Creates a new instance of User
	 */
	public User() {
	}

	public User(dudge.db.User user) {
		this.login = user.getLogin();
		this.email = user.getEmail();
		this.realName = user.getRealName();
		this.organization = user.getOrganization();
		this.regDate = user.getRegDate();
		this.jabberId = user.getJabberId();
		this.icqNumber = user.getIcqNumber();
		this.isAdmin = user.isAdmin();
		this.canCreateContest = user.canCreateContest();
		this.canCreateProblem = user.canCreateProblem();
	}

	/**
	 * Creates a new instance of User with the specified values.
	 *
	 * @param login the login of the User
	 */
	public User(String login) {
		this.login = login;
	}

	/**
	 * Creates a new instance of User with the specified values.
	 *
	 * @param login the login of the User
	 * @param email the email of the User
	 */
	public User(String login, String email) {
		this.login = login;
		this.email = email;
		this.regDate = new Date();
	}

	/**
	 * Gets the login of this User.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Sets the login of this User to the specified value.
	 *
	 * @param login the new login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Gets the email of this User.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email of this User to the specified value.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.login != null ? this.login.hashCode() : 0);
		return hash;
	}

	/**
	 * Determines whether another object is equal to this User. The result is
	 * <code>true</code> if and only if the argument is not null and is a User object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof User)) {
			return false;
		}
		User other = (User) object;
		if (this.login == null || !this.login.equals(other.login)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a string representation of the object. This implementation constructs that representation based on the id fields.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudgedb.User[login=" + login + "]";
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean canCreateContest() {
		return canCreateContest;
	}

	public void setCreateContest(boolean canCreateContest) {
		this.canCreateContest = canCreateContest;
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

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getJabberId() {
		return jabberId;
	}

	public void setJabberId(String jabberId) {
		this.jabberId = jabberId;
	}

	public Integer getIcqNumber() {
		return icqNumber;
	}

	public void setIcqNumber(Integer icqNumber) {
		this.icqNumber = icqNumber;
	}

	public boolean canCreateProblem() {
		return canCreateProblem;
	}

	public void setCreateProblem(boolean canCreateProblem) {
		this.canCreateProblem = canCreateProblem;
	}
}
