/*
 * RunsPK.java
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
 * Primary Key class RunsPK for entity class Runs
 *
 * @author Michael Antonov
 */
public class RunPK implements Serializable {
    	public static final long serialVersionUID = 1L;

	@Column(name = "solution_id", nullable=false)
	private int solutionId;

	@Column(name = "test_id", nullable=false)
	private int testId;

	/** Creates a new instance of RunsPK */
	public RunPK() {
	}
	
	/**
	 * Creates a new instance of RunsPK with the specified values.
	 */
	public RunPK(int solutionId, int testId) {
		this.solutionId = solutionId;
		this.testId = testId;
	}
	
	/**
	 * Gets the solutionId of this RunPK.
	 * @return the solutionId
	 */
	public int getSolutionId() {
		return this.solutionId;
	}
	
	/**
	 * Sets the solutionId of this RunPK to the specified value.
	 * @param solutionId the new solutionId
	 */
	public void setSolution(int solutionId) {
		this.solutionId = solutionId;
	}
	
	/**
	 * Gets the testId of this RunPK.
	 *
	 * @return the testId
	 */
	public int getTestId() {
		return this.testId;
	}
	
	/**
	 * Sets the test of this RunPK to the specified value.
	 *
	 * @param test the new test
	 */
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	/**
	 * Returns a hash code value for the object.  This implementation computes
	 * a hash code value based on the id fields in this object.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += testId;
		hash += solutionId;
		return hash;
	}
	
	/**
	 * Determines whether another object is equal to this RunsPK.  The result is
	 * <code>true</code> if and only if the argument is not null and is a RunsPK object that
	 * has the same id field values as this object.
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument;
	 * <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof RunPK)) {
			return false;
		}
		RunPK other = (RunPK)object;
		if (this.solutionId != other.solutionId) return false;
		if (this.testId != other.testId) return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object.  This implementation constructs
	 * that representation based on the id fields.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudge.db.RunPK[testNumber=" + testId + ", solutionId=" + solutionId + "]";
	}
}
