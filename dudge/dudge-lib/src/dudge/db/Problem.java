/*
 * Problem.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 */
package dudge.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 * Entity class Problem
 *
 * @author Michael Antonov
 */
@Entity(name = "Problem")
@Table(name = "problems")
@NamedQueries({
	@NamedQuery(name = "Problem.getProblems", query = "SELECT p FROM Problem p ORDER BY p.problemId"),
	@NamedQuery(name = "Problem.findByProblemId", query = "SELECT p FROM Problem p WHERE p.problemId = :problemId"),
	@NamedQuery(name = "Problem.findByTitle", query = "SELECT p FROM Problem p WHERE p.title = :title"),
	@NamedQuery(name = "Problem.findByMemoryLimit", query = "SELECT p FROM Problem p WHERE p.memoryLimit = :memoryLimit"),
	@NamedQuery(name = "Problem.findByCpuTimeLimit", query = "SELECT p FROM Problem p WHERE p.cpuTimeLimit = :cpuTimeLimit"),
	@NamedQuery(name = "Problem.findByRealTimeLimit", query = "SELECT p FROM Problem p WHERE p.realTimeLimit = :realTimeLimit"),
	@NamedQuery(name = "Problem.findByOutputLimit", query = "SELECT p FROM Problem p WHERE p.outputLimit = :outputLimit"),
	@NamedQuery(name = "Problem.getAllAttempts", query = "SELECT COUNT(s) FROM Solution s WHERE s.problem.problemId = :problemId")
})
public class Problem implements Serializable {

	public static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Problem.class.toString());
	@SequenceGenerator(name = "ProblemIdGen", sequenceName = "problems_problem_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProblemIdGen")
	@Id
	@Column(name = "problem_id", nullable = false)
	private int problemId;
	@Column(name = "title", nullable = false)
	private String title = "";
	@Column(name = "is_hidden", nullable = false)
	private boolean hidden = true;
	@Column(name = "is_healthy", nullable = false)
	private boolean healthy = true;
	@Lob
	@Column(name = "description", nullable = false)
	private String description = "";
	@Column(name = "create_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = new Date();
	@Column(name = "memory_limit", nullable = false)
	private int memoryLimit = 64 * 1024 * 1024;
	@Column(name = "cpu_time_limit", nullable = false)
	private int cpuTimeLimit = 1000;
	@Column(name = "real_time_limit", nullable = false)
	private int realTimeLimit = 1 * 60 * 1000;
	@Column(name = "output_limit", nullable = false)
	private int outputLimit = 1024 * 1024;
	@JoinColumn(name = "owner", referencedColumnName = "login")
	@ManyToOne
	private User owner;
	@Column(name = "author", nullable = false)
	private String author = "";
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "problem")
	private Collection<Test> tests;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "problem")
	private Collection<Complaint> complaints;

	/**
	 * Creates a new instance of Problem
	 */
	public Problem() {
	}

	/**
	 * Creates a new instance of Problem with the specified values.
	 *
	 * @param title the title of the Problem
	 * @param description the description of the Problem
	 * @param memoryLimit the memoryLimit of the Problem
	 * @param cpuTimeLimit the cpuTimeLimit of the Problem
	 * @param realTimeLimit the realTimeLimit of the Problem
	 * @param outputLimit the outputLimit of the Problem
	 */
	public Problem(String title, String description, int memoryLimit, int cpuTimeLimit, int realTimeLimit, int outputLimit) {
		this.title = title;
		this.description = description;
		this.memoryLimit = memoryLimit;
		this.cpuTimeLimit = cpuTimeLimit;
		this.realTimeLimit = realTimeLimit;
		this.outputLimit = outputLimit;
		this.tests = new ArrayList<>();
	}

	/**
	 * Gets the problemId of this Problem.
	 *
	 * @return the problemId
	 */
	public int getProblemId() {
		return this.problemId;
	}

	/**
	 * Sets the problemId of this Problem to the specified value.
	 *
	 * @param problemId the new problemId
	 */
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}

	/**
	 * Gets the title of this Problem.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title of this Problem to the specified value.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the description of this Problem.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of this Problem to the specified value.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the memoryLimit of this Problem.
	 *
	 * @return the memoryLimit
	 */
	public int getMemoryLimit() {
		return this.memoryLimit;
	}

	/**
	 * Sets the memoryLimit of this Problem to the specified value.
	 *
	 * @param memoryLimit the new memoryLimit
	 */
	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	/**
	 * Gets the cpuTimeLimit of this Problem.
	 *
	 * @return the cpuTimeLimit
	 */
	public int getCpuTimeLimit() {
		return this.cpuTimeLimit;
	}

	/**
	 * Sets the cpuTimeLimit of this Problem to the specified value.
	 *
	 * @param cpuTimeLimit the new cpuTimeLimit
	 */
	public void setCpuTimeLimit(int cpuTimeLimit) {
		this.cpuTimeLimit = cpuTimeLimit;
	}

	/**
	 * Gets the realTimeLimit of this Problem.
	 *
	 * @return the realTimeLimit
	 */
	public int getRealTimeLimit() {
		return this.realTimeLimit;
	}

	/**
	 * Sets the realTimeLimit of this Problem to the specified value.
	 *
	 * @param realTimeLimit the new realTimeLimit
	 */
	public void setRealTimeLimit(int realTimeLimit) {
		this.realTimeLimit = realTimeLimit;
	}

	/**
	 * Gets the outputLimit of this Problem.
	 *
	 * @return the outputLimit
	 */
	public int getOutputLimit() {
		return this.outputLimit;
	}

	/**
	 * Sets the outputLimit of this Problem to the specified value.
	 *
	 * @param outputLimit the new outputLimit
	 */
	public void setOutputLimit(int outputLimit) {
		this.outputLimit = outputLimit;
	}

	/**
	 * Gets the owner of this Problem.
	 *
	 * @return the owner
	 */
	public User getOwner() {
		return this.owner;
	}

	/**
	 * Sets the owner of this Problem to the specified value.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Gets the tests of this Problem.
	 *
	 * @return the tests
	 */
	public Collection<Test> getTests() {
		return this.tests;
	}

	/**
	 * Sets the tests of this Problem to the specified value.
	 *
	 * @param tests the new tests
	 */
	public void setTests(Collection<Test> tests) {
		this.tests = tests;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += this.problemId;
		return hash;
	}

	/**
	 * Determines whether another object is equal to this Problem. The result is
	 * <code>true</code> if and only if the argument is not null and is a Problem object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Problem)) {
			return false;
		}
		Problem other = (Problem) object;
		if (this.problemId != other.problemId) {
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
		return "dudge.db.Problem[problemId=" + problemId + "]";
	}

	/**
	 * 
	 * @return 
	 */
	public Date getCreateTime() {
		return createTime;
	}
/**
 * 
 * @param createTime 
 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
/**
 * 
 * @return 
 */
	public boolean isHidden() {
		return hidden;
	}
/**
 * 
 * @param isHidden 
 */
	public void setHidden(boolean isHidden) {
		this.hidden = isHidden;
	}
/**
 * 
 * @return 
 */
	public boolean isHealthy() {
		return healthy;
	}
/**
 * 
 * @param healthy 
 */
	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
	}
/**
 * 
 * @return 
 */
	public Collection<Complaint> getComplaints() {
		return complaints;
	}
/**
 * 
 * @param complaints 
 */
	public void setComplaints(Collection<Complaint> complaints) {
		this.complaints = complaints;
	}
/**
 * 
 * @return 
 */
	public String getAuthor() {
		return author;
	}
/**
 * 
 * @param author 
 */
	public void setAuthor(String author) {
		this.author = author;
	}
}
