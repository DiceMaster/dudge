/*
 * ProblemStats.java
 *
 * Created on 5 Июнь 2007 г., 14:28
 *
 */
package dudge.stats;

/**
 * Inform. class, encapsulated the statistics at any Problem.
 *
 * @author Michael Antonov
 */
public class ProblemStats {

	private Integer problemId;
	private Integer allAttempts;
	private Integer allSuccessfullyAttempts;
	private Integer averageAttempts; // В день
	private Integer averageSuccessfullyAttempts; // В день

	/**
	 * Creates a new instance of ProblemStats
	 */
	public ProblemStats() {
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public Integer getAllAttempts() {
		return allAttempts;
	}

	public void setAllAttempts(Integer allAttempts) {
		this.allAttempts = allAttempts;
	}

	public Integer getAllSuccessfullyAttempts() {
		return allSuccessfullyAttempts;
	}

	public void setAllSuccessfullyAttempts(Integer allSuccessfullyAttempts) {
		this.allSuccessfullyAttempts = allSuccessfullyAttempts;
	}

	public Integer getAverageAttempts() {
		return averageAttempts;
	}

	public void setAverageAttempts(Integer averageAttempts) {
		this.averageAttempts = averageAttempts;
	}

	public Integer getAverageSuccessfullyAttempts() {
		return averageSuccessfullyAttempts;
	}

	public void setAverageSuccessfullyAttempts(Integer averageSuccessfullyAttempts) {
		this.averageSuccessfullyAttempts = averageSuccessfullyAttempts;
	}
}
