/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dudge.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Michael Antonov
 */
@Entity(name = "Application")
@Table(name = "applications")
@NamedQueries({
	@NamedQuery(name = "Application.findByContestId", query = "SELECT ap FROM Application ap WHERE ap.contest.contestId = :contestId")
})
@IdClass(dudge.db.ApplicationPK.class)
public class Application implements Serializable {

	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "contest_id", nullable = false, insertable = false, updatable = false)
	private int contestId;
	@JoinColumn(name = "contest_id", referencedColumnName = "contest_id", nullable = false)
	@ManyToOne
	private Contest contest;
	@Id
	@Column(name = "owner", nullable = false, insertable = false, updatable = false)
	private String ownername;
	@JoinColumn(name = "owner", referencedColumnName = "login", nullable = false)
	@ManyToOne
	private User user;
	@Column(name = "filing_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date filingTime;
	@Column(name = "message", nullable = false)
	private String message;
	@Column(name = "status", nullable = false)
	private String status;

	public Application() {
	}

	/**
	 *
	 * @param contest
	 * @param owner
	 */
	public Application(Contest contest, User owner) {
		this.contest = contest;
		this.user = owner;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}

	public Date getFilingTime() {
		return filingTime;
	}

	public void setFilingTime(Date filingTime) {
		this.filingTime = filingTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getOwner() {
		return user;
	}

	public void setOwner(User owner) {
		this.user = owner;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.contest != null ? this.contest.hashCode() : 0);
		hash += (this.user != null ? this.user.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Application)) {
			return false;
		}
		Application other = (Application) object;

		if (this.contest == null || !this.contest.equals(other.contest)) {
			return false;
		}
		if (this.user == null || !this.user.equals(other.user)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dudge.db.Application[owner=" + user + " contestId=" + contestId + "]";
	}
}
