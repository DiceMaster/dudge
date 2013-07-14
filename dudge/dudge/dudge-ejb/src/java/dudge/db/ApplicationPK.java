/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.db;

import java.io.Serializable;
import javax.persistence.Column;

/**
 *
 * @author Michael Antonov
 */
public class ApplicationPK implements Serializable{
    	public static final long serialVersionUID = 1L;

	@Column(name = "contest_id", nullable = false)
	private int contestId;
	
	@Column(name = "owner", nullable = false)
	private String ownername;

	public ApplicationPK() {
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ApplicationPK other = (ApplicationPK) obj;
		if (this.contestId != other.contestId) {
			return false;
		}
		if (this.ownername == null || !this.ownername.equals(other.ownername)){
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + this.contestId;
		hash = 47 * hash + (this.ownername != null ? this.ownername.hashCode() : 0);
		return hash;
	}

	public ApplicationPK(int contestId, String ownername) {
		this.contestId = contestId;
		this.ownername = ownername;
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

}
