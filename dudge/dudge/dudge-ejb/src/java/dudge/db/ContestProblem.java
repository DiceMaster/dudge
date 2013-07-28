/*
 * ContestProblem.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 */
package dudge.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity class ContestProblem
 *
 *
 * @author Michael Antonov
 */
@Entity(name = "ContestProblem")
@Table(name = "contest_problems")
@NamedQueries({
	@NamedQuery(name = "ContestProblem.findByContestId", query = "SELECT c FROM ContestProblem c WHERE c.contest.contestId = :contestId"),
	@NamedQuery(name = "ContestProblem.findBy ProblemId", query = "SELECT c FROM ContestProblem c WHERE c.problem.problemId = :problemId"),
	@NamedQuery(name = "ContestProblem.findByProblemOrder", query = "SELECT c FROM ContestProblem c WHERE c.problemOrder = :problemOrder"),
	@NamedQuery(name = "ContestProblem.findByProblemMark", query = "SELECT c FROM ContestProblem c WHERE c.problemMark = :problemMark")
})
@IdClass(dudge.db.ContestProblemPK.class)
public class ContestProblem implements Serializable, Comparable<ContestProblem> {

	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "contest_id", nullable = false, insertable = false, updatable = false)
	private int contestId;
	@JoinColumn(name = "contest_id", referencedColumnName = "contest_id", nullable = false)
	@ManyToOne
	private Contest contest;
	@Id
	@Column(name = "problem_id", nullable = false, insertable = false, updatable = false)
	private int problemId;
	@JoinColumn(name = "problem_id", referencedColumnName = "problem_id", nullable = false)
	@ManyToOne
	private Problem problem;
	@Column(name = "problem_order", nullable = false)
	private int problemOrder;
	@Column(name = "problem_mark", nullable = false)
	private String problemMark;
	@Column(name = "problem_cost", nullable = false)
	private int problemCost;

	/**
	 * Creates a new instance of ContestProblem
	 */
	public ContestProblem() {
	}

	/**
	 * Creates a new instance of this class
	 *
	 * @param ContestProblensPK the ContestProblensPK of the ContestProblems
	 */
	public ContestProblem(Contest contest, Problem problem) {
		this.problem = problem;
		this.contest = contest;
		this.contestId = contest.getContestId();
		this.problemId = problem.getProblemId();
	}

	/**
	 * Creates a new instance of ContestProblem with the specified values.
	 *
	 * @param contest the contest of the ContestProblem
	 * @param problem the problem of the ContestProblem
	 * @param problemOrder the problemOrder of the ContestProblem
	 * @param problemMark the problemMark of the ContestProblem
	 */
	public ContestProblem(Contest contest, Problem problem, int problemOrder, String problemMark, int problemCost) {
		this.contest = contest;
		this.problem = problem;
		this.problemOrder = problemOrder;
		this.problemMark = problemMark;
		this.problemCost = problemCost;
		this.contestId = contest.getContestId();
		this.problemId = problem.getProblemId();
	}

	/**
	 * Gets the problemOrder of this ContestProblem.
	 *
	 * @return the problemOrder
	 */
	public int getProblemOrder() {
		return this.problemOrder;
	}

	/**
	 * Sets the problemOrder of this ContestProblem to the specified value.
	 *
	 * @param problemOrder the new problemOrder
	 */
	public void setProblemOrder(int problemOrder) {
		this.problemOrder = problemOrder;
	}

	/**
	 * Gets the problemMark of this ContestProblem.
	 *
	 * @return the problemMark
	 */
	public String getProblemMark() {
		return this.problemMark;
	}

	/**
	 * Sets the problemMark of this ContestProblem to the specified value.
	 *
	 * @param problemMark the new problemMark
	 */
	public void setProblemMark(String problemMark) {
		this.problemMark = problemMark;
	}

	/**
	 * Gets the problemCost of this ContestProblem.
	 *
	 * @return the problemCost
	 */
	public int getProblemCost() {
		return this.problemCost;
	}

	/**
	 * Sets the problemCost of this ContestProblem to the specified value.
	 *
	 * @param problemCost the new problemCost
	 */
	public void setProblemCost(int problemCost) {
		this.problemCost = problemCost;
	}

	/**
	 * Gets the contest of this ContestProblem.
	 *
	 *
	 * @return the contest
	 */
	public Contest getContest() {
		return this.contest;
	}

	/**
	 * Sets the contest of this ContestProblem to the specified value.
	 *
	 *
	 * @param contest the new contest
	 */
	public void setContest(Contest contest) {
		this.contest = contest;
	}

	/**
	 * Gets the problem of this ContestProblem.
	 *
	 *
	 * @return the problem
	 */
	public Problem getProblem() {
		return this.problem;
	}

	/**
	 * Sets the problem of this ContestProblem to the specified value.
	 *
	 *
	 * @param problem the new problem
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
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
		hash += (this.problem != null ? this.problem.hashCode() : 0);
		return hash;
	}

	/**
	 * Determines whether another object is equal to this ContestProblem. The result is
	 * <code>true</code> if and only if the argument is not null and is a ContestProblem object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ContestProblem)) {
			return false;
		}
		ContestProblem other = (ContestProblem) object;
		if (this.contest == null || !this.getContest().equals(other.getContest())) {
			return false;
		}
		if (this.problem == null || !this.getProblem().equals(other.getProblem())) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ContestProblem cp) {
		if (this.problemOrder < cp.problemOrder) {
			return -1;
		}

		if (this.problemOrder > cp.problemOrder) {
			return 1;
		}

		return 0;
	}

	/**
	 * Returns a string representation of the object. This implementation constructs that representation based on the id fields.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudgedb.ContestProblem[contestId=" + contest.getContestId() + " problemId=" + problem.getProblemId() + "]";
	}
}
