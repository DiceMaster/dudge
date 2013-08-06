package dudge.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author olorin
 */
@Entity(name = "Complaint")
@Table(name = "complaints")
public class Complaint implements Serializable {

	public static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "CompliantIdGen", sequenceName = "complaints_compliant_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ComplaintIdGen")
	@Id
	@Column(name = "complaint_id", nullable = false)
	private int complaintId;
	@JoinColumn(name = "owner", referencedColumnName = "login")
	@ManyToOne
	private User user;
	@JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
	@ManyToOne
	private Problem problem;
	@Column(name = "filing_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date filingTime;
	@Column(name = "message", nullable = false)
	private String message;

	public Complaint() {
	}

	/**
	 *
	 * @return
	 */
	public int getComplaintId() {
		return complaintId;
	}

	/**
	 *
	 * @param complaintId
	 */
	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	/**
	 *
	 * @return
	 */
	public Date getFilingTime() {
		return filingTime;
	}

	/**
	 *
	 * @param filingTime
	 */
	public void setFilingTime(Date filingTime) {
		this.filingTime = filingTime;
	}

	/**
	 *
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 *
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 *
	 * @return
	 */
	public User getOwner() {
		return user;
	}

	/**
	 *
	 * @param owner
	 */
	public void setOwner(User owner) {
		this.user = owner;
	}

	/**
	 *
	 * @return
	 */
	public Problem getProblem() {
		return problem;
	}

	/**
	 *
	 * @param problem
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) complaintId;
		return hash;
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Complaint)) {
			return false;
		}
		Complaint other = (Complaint) object;
		if (this.complaintId != other.complaintId) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "dudge.db.Complaint[id=" + complaintId + "]";
	}
}
