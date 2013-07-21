/*
 * Roles.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dudge.db;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 * Entity class Roles
 *
 * @author Michael Antonov
 */
@Entity(name = "Role")
@Table(name = "roles")
@NamedQueries({
	@NamedQuery(name = "Role.findByContestId", query = "SELECT r FROM Role r WHERE r.contest.contestId = :contestId"),
	@NamedQuery(name = "Role.findByUsername", query = "SELECT r FROM Role r WHERE r.user.login = :username"),
	@NamedQuery(name = "Role.findByRole", query = "SELECT r FROM Role r WHERE r.roleType = :roleType"),
	@NamedQuery(name = "Role.isInRole",
			query = "SELECT r FROM Role r WHERE r.contest.contestId = :contestId AND r.user.login = :username AND r.roleType = :roleType")
})
@IdClass(dudge.db.RolePK.class)
public class Role implements Serializable {

	public static final long serialVersionUID = 1L;
	protected static final Logger logger = Logger.getLogger(User.class.toString());
	@Id
	@Column(name = "contest_id", nullable = false, insertable = false, updatable = false)
	private int contestId;
	@JoinColumn(name = "contest_id", referencedColumnName = "contest_id", nullable = false)
	@ManyToOne
	private Contest contest;
	@Id
	@Column(name = "username", nullable = false, insertable = false, updatable = false)
	private String username;
	@JoinColumn(name = "username", referencedColumnName = "login", nullable = false)
	@ManyToOne
	private User user;
	@Id
	@Column(name = "role_type", length = 255, nullable = false)
	private String roleType;

	/**
	 * Creates a new instance of Roles
	 */
	public Role() {
	}

	/**
	 * Creates a new instance of role with the specified values.
	 *
	 * @param contest the contest of the role
	 * @param user the user of the role
	 * @param roleType the role type of the role
	 */
	public Role(Contest contest, User user, RoleType roleType) {
		this.contest = contest;
		this.contestId = contest.getContestId();
		this.user = user;
		this.username = user.getLogin();
		this.roleType = roleType.toString();
	}

	/**
	 * Gets the contest of this Roles.
	 *
	 * @return the contest
	 */
	public Contest getContest() {
		return this.contest;
	}

	/**
	 * Sets the contest of this Roles to the specified value.
	 *
	 * @param contest the new contest
	 */
	public void setContest(Contest contest) {
		this.contest = contest;
	}

	/**
	 * Gets the user of this Roles.
	 *
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the user of this Roles to the specified value.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.contest != null ? this.contest.hashCode() : 0);
		hash += (this.user != null ? this.user.hashCode() : 0);
		hash += (this.roleType != null ? this.roleType.hashCode() : 0);
		return hash;
	}

	/**
	 * Determines whether another object is equal to this Roles. The result is
	 * <code>true</code> if and only if the argument is not null and is a Roles object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
		if (this.contest == null || !this.contest.equals(other.contest)) {
			return false;
		}
		if (this.user == null || !this.user.equals(other.user)) {
			return false;
		}
		if (this.roleType == null || !this.roleType.equals(other.roleType)) {
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
		return "dudge.db.Role[rolePK=" + roleType + "]";
	}

	public RoleType getRoleType() {
		return RoleType.valueOf(roleType);
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType.toString();
	}
}
