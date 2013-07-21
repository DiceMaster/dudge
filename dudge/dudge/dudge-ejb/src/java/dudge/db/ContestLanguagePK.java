/*
 * ContestProblemPK.java
 *
 * Created on 26 Сентябрь 2007 г., 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
public class ContestLanguagePK implements Serializable {

	public static final long serialVersionUID = 1L;
	@Column(name = "contest_id", nullable = false)
	private int contestId;
	@Column(name = "language_id", nullable = false)
	private String languageId;

	/**
	 * Creates a new instance of ContestProblemPK
	 */
	public ContestLanguagePK() {
	}

	/**
	 * Creates a new instance of ContestProblemPK with the specified values.
	 *
	 * @param problemId the problem ID of the ContestProblemPK
	 * @param contestId the contest ID of the ContestProblemPK
	 */
	public ContestLanguagePK(int contestId, String languageId) {
		this.contestId = contestId;
		this.languageId = languageId;
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
		this.setContestId(contestId);
	}

	/**
	 * Gets the problemId of this ContestProblemPK.
	 *
	 * @return the problem
	 */
	public String getLanguageId() {
		return this.languageId;
	}

	/**
	 * Sets the problem of this ContestProblemPK to the specified value.
	 *
	 * @param problemId the new problemId
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += getLanguageId().hashCode();
		hash += getContestId();
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
		if (!(object instanceof ContestLanguagePK)) {
			return false;
		}
		ContestLanguagePK other = (ContestLanguagePK) object;
		if (!this.getLanguageId().equals(other.getLanguageId())) {
			return false;
		}
		if (this.getContestId() != other.getContestId()) {
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
		return "dudgedb.ContestLanguagePK[contestId=" + getContestId() + " languageId=" + getLanguageId() + "]";
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
}
