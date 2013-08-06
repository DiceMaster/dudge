/*
 * User.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 */
package dudge.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 * Entity class User
 *
 * @author Michael Antonov
 */
@Entity(name = "User")
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = "User.getUsers", query = "SELECT u FROM User u ORDER BY u.login"),
	@NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login"),
	@NamedQuery(name = "User.findByPwdHash", query = "SELECT u FROM User u WHERE u.pwdHash = :pwdHash"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {

	public static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(User.class.toString());
	@Id
	@Column(name = "login", nullable = false, updatable = false)
	private String login;
	@Column(name = "pwd_hash", nullable = false)
	private String pwdHash;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "real_name", nullable = false)
	private String realName = "";
	@Column(name = "organization", nullable = false)
	private String organization = "";
	@Column(name = "stud_faculty", nullable = true)
	private String faculty = "";
	@Column(name = "stud_course", nullable = true)
	private Integer course = null;
	@Column(name = "stud_group", nullable = true)
	private String group = "";
	@Column(name = "register_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date regDate;
	@Column(name = "age", nullable = true)
	private Integer age = null;
	@Column(name = "jabber_id", length = 255, nullable = false)
	private String jabberId = "";
	@Column(name = "icq_number", nullable = true)
	private Integer icqNumber = null;
	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin;
	@Column(name = "can_create_contest", nullable = false)
	private boolean canCreateContest;
	@Column(name = "can_create_problem", nullable = false)
	private boolean canCreateProblem;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Collection<Role> rolesCollection;

	/**
	 * Creates a new instance of User
	 */
	public User() {
		rolesCollection = new ArrayList<>();
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
	 * @param pwdHash the pwdHash of the User
	 * @param email the email of the User
	 */
	public User(String login, String pwdHash, String email) {
		this.login = login;
		this.pwdHash = pwdHash;
		this.email = email;
		this.regDate = new Date();
	}

	/**
	 * Gets the login of this User.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
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
	 * Gets the pwdHash of this User.
	 *
	 * @return the pwdHash
	 */
	public String getPwdHash() {
		return pwdHash;
	}

	/**
	 * Sets the pwdHash of this User to the specified value.
	 *
	 * @param pwdHash the new pwdHash
	 */
	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}

	/**
	 * Gets the email of this User.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
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
	 * Gets the rolesCollection of this User.
	 *
	 * @return the rolesCollection
	 */
	public Collection<Role> getRolesCollection() {
		return rolesCollection;
	}

	/**
	 * Sets the rolesCollection of this User to the specified value.
	 *
	 * @param rolesCollection the new rolesCollection
	 */
	public void setRolesCollection(Collection<Role> rolesCollection) {
		this.rolesCollection = rolesCollection;
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
	public boolean canCreateContest() {
		return canCreateContest;
	}

	/**
	 *
	 * @param canCreateContest
	 */
	public void setCreateContest(boolean canCreateContest) {
		this.canCreateContest = canCreateContest;
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
	public Integer getCourse() {
		return course;
	}

	/**
	 *
	 * @param course
	 */
	public void setCourse(Integer course) {
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
	public Integer getAge() {
		return age;
	}

	/**
	 *
	 * @param age
	 */
	public void setAge(Integer age) {
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
	public Integer getIcqNumber() {
		return icqNumber;
	}

	/**
	 *
	 * @param icqNumber
	 */
	public void setIcqNumber(Integer icqNumber) {
		this.icqNumber = icqNumber;
	}

	/**
	 *
	 * @return
	 */
	public boolean canCreateProblem() {
		return canCreateProblem;
	}

	/**
	 *
	 * @param canCreateProblem
	 */
	public void setCreateProblem(boolean canCreateProblem) {
		this.canCreateProblem = canCreateProblem;
	}
}
