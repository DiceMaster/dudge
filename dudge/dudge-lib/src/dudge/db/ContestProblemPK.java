/*
 * ContestProblemPK.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 */
package dudge.db;

import java.io.Serializable;
import javax.persistence.Column;

/**
 * Primary Key class ContestProblemPK for entity class ContestProblems
 *
 *
 * @author Michael Antonov
 */
public class ContestProblemPK implements Serializable {

	public static final long serialVersionUID = 1L;
	@Column(name = "contest_id", nullable = false)
	private int contestId;
	@Column(name = "problem_id", nullable = false)
	private int problemId;

	/**
	 * Creates a new instance of ContestProblemPK
	 */
	public ContestProblemPK() {
	}

	/**
	 * Creates a new instance of ContestProblemPK with the specified values.
	 *
	 * @param problemId the problem ID of the ContestProblemPK
	 * @param contestId the contest ID of the ContestProblemPK
	 */
	public ContestProblemPK(int contestId, int problemId) {
		this.contestId = contestId;
		this.problemId = problemId;
	}

	/**
	 * Gets the contestId of this ContestProblemPK.
	 *
	 * @return the contest
	 */
	public int getContestId() {
		return this.contestId;
	}

	/**
	 * Sets the contest of this ContestProblemPK to the specified value.
	 *
	 * @param contest the new contest
	 */
	public void setContest(int contestId) {
		this.contestId = contestId;
	}

	/**
	 * Gets the problemId of this ContestProblemPK.
	 *
	 * @return the problem
	 */
	public int getProblemId() {
		return this.problemId;
	}

	/**
	 * Sets the problem of this ContestProblemPK to the specified value.
	 *
	 * @param problemId the new problemId
	 */
	public void setProblem(int problemId) {
		this.problemId = problemId;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += problemId;
		hash += contestId;
		return hash;
	}

	/**
	 * Determines whether another object is equal to this ContestProblemPK. The result is
	 * <code>true</code> if and only if the argument is not null and is a ContestProblemPK object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ContestProblemPK)) {
			return false;
		}
		ContestProblemPK other = (ContestProblemPK) object;
		if (this.problemId != other.problemId) {
			return false;
		}
		if (this.contestId != other.contestId) {
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
		return "dudgedb.ContestProblemPK[contestId=" + contestId + " problemId=" + problemId + "]";
	}
}
