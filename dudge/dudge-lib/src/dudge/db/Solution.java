/*
 * Solution.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.db;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/**
 * Entity class Solution
 *
 * @author Michael Antonov
 */
@Entity(name="Solution")
@Table(name = "solutions")
@NamedQueries( {
	@NamedQuery(name = "Solution.findBySolutionId", query = "SELECT s FROM Solution s WHERE s.solutionId = :solutionId"),
	@NamedQuery(name = "Solution.getPendingSolutions", query = "SELECT s FROM Solution s WHERE s.status = 'NEW'"),
	@NamedQuery(name = "Solution.findByContestId" , query= "SELECT s FROM Solution s WHERE s.contest.contestId = :contestId"),
	@NamedQuery(name = "Solution.getLastSolutions", query = "SELECT s FROM Solution s" +
	" ORDER BY s.submitTime DESC"),
	@NamedQuery(name = "Solution.findByUserContestProblem", query = "SELECT s FROM Solution s" +
	" WHERE s.user.login = :login" +
			" AND s.contest.contestId = :contestId" +
			" AND s.problem.problemId = :problemId" +
			" ORDER BY s.submitTime")
})
public class Solution implements Serializable, Cloneable {
	
	@SequenceGenerator(name="SolutionIdGen", sequenceName="solutions_solution_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SolutionIdGen")
	@Id
	@Column(name = "solution_id", nullable = false)
	private int solutionId;
	
	@Column(name = "submit_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitTime = new Date();
	
	@JoinColumn(name = "username", referencedColumnName = "login")
	@ManyToOne
	private User user;
	
	@JoinColumn(name = "contest_id", referencedColumnName = "contest_id")
	@ManyToOne
	private Contest contest;
	
	@JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
	@ManyToOne
	private Problem problem;
	
	@JoinColumn(name = "language_id", referencedColumnName = "language_id")
	@ManyToOne
	private Language language;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "solution")
	private Collection<Run> runs = new ArrayList<Run>();
	
	@Column(name="status", length=255, nullable=false)
	private String status = SolutionStatus.NEW.toString();
	
	@Lob
	@Column(name="source_code", nullable = false)
	private String sourceCode = "";
	
	@Lob
	@Column(name="status_message", nullable = false)
	private String statusMessage = "";
	
	@Column(name = "compilation_time", nullable = false)
	private Integer compilationTime = 0;
		
	/** Creates a new instance of Solution */
	public Solution() {
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public RunResultType getLastRunResult() {
		if(runs.size() == 0)
			return null;
		
		List<Run> sortedRuns = new LinkedList<Run>(runs);
		Collections.sort(sortedRuns);
		return sortedRuns.get(sortedRuns.size() - 1).getResultType();
	}
	
	/**
	 * Позволяет узнать, прошло ли решение все тесты.
	 * @return true если решение прошло все тесты,
	 * иначе false.
	 */
	public boolean isAllTestsPassed() {
            if (this.runs != null) {
		for(Run run : this.runs) {
			if(run.getResultType() != RunResultType.SUCCESS)
				return false;
		}
            }
		
		return true;
	}	
	
	/**
	 * Gets the solutionId of this Solution.
	 * @return the solutionId
	 */
	public int getSolutionId() {
		return this.solutionId;
	}
	
	/**
	 * Sets the solutionId of this Solution to the specified value.
	 * @param solutionId the new solutionId
	 */
	public void setSolutionId(int solutionId) {
		this.solutionId = solutionId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	
	/**
	 * Gets the user of this Solution.
	 *
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * Sets the user of this Solution to the specified value.
	 *
	 * @param new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the contest of this Solution.
	 *
	 * @return the contest
	 */
	public Contest getContest() {
		return this.contest;
	}
	
	/**
	 * Sets the contest of this Solution to the specified value.
	 *
	 * @param contest the new contest
	 */
	public void setContest(Contest contest) {
		this.contest = contest;
	}
	
	/**
	 * Gets the problem of this Solution.
	 *
	 * @return the problem
	 */
	public Problem getProblem() {
		return this.problem;
	}
	
	/**
	 * Sets the problem of this Solution to the specified value.
	 *
	 * @param problem the new problem
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	/**
	 * Gets the language of this Solution.
	 * @return the language
	 */
	public Language getLanguage() {
		return this.language;
	}
	
	/**
	 * Sets the language of this Solution to the specified value.
	 * @param language the new language
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	/**
	 * Gets the runs of this Solution.
	 * 
	 * @return the runs
	 */
	public Collection<Run> getRuns() {
		return this.runs;
	}
	
	/**
	 * Sets the runs of this Solution to the specified value.
	 * 
	 * @param runs the new runs
	 */
	public void setRuns(Collection<Run> runsCollection) {
		this.runs = runsCollection;
	}
	
	public SolutionStatus getStatus() {
		return SolutionStatus.valueOf(status);
	}
	
	public void setStatus(SolutionStatus status) {
		this.status = status.toString();
	}
	
	/**
	 * Returns a hash code value for the object.  This implementation computes
	 * a hash code value based on the id fields in this object.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += this.solutionId;
		return hash;
	}
	
	/**
	 * Determines whether another object is equal to this Solution.  The result is
	 * <code>true</code> if and only if the argument is not null and is a Solution object that
	 * has the same id field values as this object.
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument;
	 * <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Solution)) {
			return false;
		}
		Solution other = (Solution)object;
		if (this.solutionId != other.solutionId) return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object.  This implementation constructs
	 * that representation based on the id fields.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudge.db.Solution[solutionId=" + solutionId + "]";
	}
	
	public String getSourceCode() {
		return sourceCode;
	}
	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	/*private void writeObject(java.io.ObjectOutputStream out)
	throws java.io.IOException {
		// Для устранения LAZY relationship при сериализации.
		//runs.size();
		out.defaultWriteObject();
	}*/

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Integer getCompilationTime() {
		return compilationTime;
	}

	public void setCompilationTime(Integer compilationTime) {
		this.compilationTime = compilationTime;
	}
}
