/*
 * Runs.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 */
package dudge.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity class Run
 *
 * @author Michael Antonov
 */
@Entity(name = "Run")
@Table(name = "runs")
@NamedQueries({
	@NamedQuery(name = "Run.findBySolutionId", query = "SELECT r FROM Run r WHERE r.solution.solutionId = :solutionId"),
	@NamedQuery(name = "Run.findByTestId", query = "SELECT r FROM Run r WHERE r.test.testId = :testId")
})
@IdClass(dudge.db.RunPK.class)
public class Run implements Serializable, Comparable<Run> {

	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "solution_id", nullable = false, insertable = false, updatable = false)
	private int solutionId;
	@JoinColumn(name = "solution_id", referencedColumnName = "solution_id", nullable = false)
	@ManyToOne
	private Solution solution;
	@Id
	@Column(name = "test_id", nullable = false, insertable = false, updatable = false)
	private int testId;
	@JoinColumn(name = "test_id", referencedColumnName = "test_id", nullable = false)
	@ManyToOne
	private Test test;
	@Column(name = "run_number", nullable = false)
	private int runNumber;
	@Column(name = "result_type", length = 255, nullable = false)
	private String resultType = RunResultType.SUCCESS.toString();
	@Column(name = "memory", nullable = false)
	private int memory;
	@Column(name = "cpu_time", nullable = false)
	private int cpuTime;
	@Column(name = "real_time", nullable = false)
	private int realTime;

	/**
	 * Creates a new instance of Runs
	 */
	public Run() {
	}

	/**
	 * Creates a new instance of Runs with the specified values.
	 */
	public Run(Solution solution, Test test) {
		this.solution = solution;
		this.test = test;
		this.solutionId = solution.getSolutionId();
		this.testId = test.getTestId();
	}

	/**
	 * Creates a new instance of Runs with the specified values.
	 */
	public Run(Solution solution, Test test, RunResultType resultType) {
		this.solution = solution;
		this.test = test;
		this.solutionId = solution.getSolutionId();
		this.testId = test.getTestId();
		this.resultType = resultType.toString();
	}

	@Override
	public int compareTo(Run t) {
		if (this.getRunNumber() == t.getRunNumber()) {
			return 0;
		}

		return (this.getRunNumber() < t.getRunNumber()) ? -1 : 1;
	}

	/**
	 * Gets the solution of this Runs.
	 *
	 * @return the solution
	 */
	public Solution getSolution() {
		return this.solution;
	}

	/**
	 * Sets the solution of this Runs to the specified value.
	 *
	 * @param solution the new solution
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public int getRunNumber() {
		return runNumber;
	}

	public void setRunNumber(int runNumber) {
		this.runNumber = runNumber;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += this.solution.hashCode();
		hash += this.getTest().hashCode();
		return hash;
	}

	/**
	 * Determines whether another object is equal to this Runs. The result is
	 * <code>true</code> if and only if the argument is not null and is a Runs object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Run)) {
			return false;
		}
		Run other = (Run) object;
		if (this.solution == null || !this.solution.equals(other.solution)) {
			return false;
		}
		if (this.test == null || !this.getTest().equals(other.getTest())) {
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
		return "dudge.db.Run[testNumber=" + getTest().getTestId() + ", solutionId=" + solution.getSolutionId() + "]";
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(int cpuTime) {
		this.cpuTime = cpuTime;
	}

	public int getRealTime() {
		return realTime;
	}

	public void setRealTime(int realTime) {
		this.realTime = realTime;
	}

	public RunResultType getResultType() {
		return RunResultType.valueOf(resultType);
	}

	public void setResultType(RunResultType resultType) {
		this.resultType = resultType.toString();
	}
}
