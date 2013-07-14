/*
 * RolePK.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.db;

import java.io.Serializable;
import javax.persistence.Column;

/**
 * Primary Key class RolePK for entity class Role
 *
 * @author Michael Antonov
 */
public class RolePK implements Serializable {
    	public static final long serialVersionUID = 1L;
	
	@Column(name = "contest_id", nullable = false)
	private int contestId;
	
	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "role_type", nullable = false)
	private String roleType;
	
	/** Creates a new instance of RolePK */
	public RolePK() {
	}
	
	/**
	 * Creates a new instance of RolePK with the specified values.
	 * @param roleType the role type of the RolePK
	 * @param username the username of the RolePK
	 * @param contestId the contestId of the RolePK
	 */
	public RolePK(RoleType roleType, String username, int contestId) {
		this.roleType = roleType.toString();
		this.username = username;
		this.contestId = contestId;
	}
	
	/**
	 * Gets the contestId of this RolePK.
	 * @return the contestId
	 */
	public int getContestId() {
		return this.contestId;
	}
	
	/**
	 * Sets the contestId of this RolePK to the specified value.
	 * @param contestId the new contestId
	 */
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	
	/**
	 * Gets the username of this RolePK.
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Sets the username of this RolePK to the specified value.
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the role of this RolePK.
	 * @return the role
	 */
	public String getRoleType() {
		return this.roleType;
	}
	
	/**
	 * Sets the role of this RolePK to the specified value.
	 * @param role the new role
	 */
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	/**
	 * Returns a hash code value for the object.  This implementation computes
	 * a hash code value based on the id fields in this object.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.roleType != null ? this.roleType.hashCode() : 0);
		hash += (this.username != null ? this.username.hashCode() : 0);
		hash += (int)contestId;
		return hash;
	}
	
	/**
	 * Determines whether another object is equal to this RolePK.  The result is
	 * <code>true</code> if and only if the argument is not null and is a RolePK object that
	 * has the same id field values as this object.
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument;
	 * <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof RolePK)) {
			return false;
		}
		RolePK other = (RolePK)object;
		if (this.roleType == null || !this.roleType.equals(other.roleType)) return false;
		if (this.username == null || !this.username.equals(other.username)) return false;
		if (this.contestId != other.contestId) return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object.  This implementation constructs
	 * that representation based on the id fields.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudgedb.RolePK[roleType=" + roleType + ", username=" + username + ", contestId=" + contestId + "]";
	}
	
}
