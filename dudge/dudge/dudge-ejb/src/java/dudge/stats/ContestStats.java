/*
 * ContestStats.java
 *
 * Created on 5 Июнь 2007 г., 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.stats;

/**
 * Inform. class, encapsulated the statistics at any Contest.
 * @author Michael Antonov
 */
public class ContestStats {
	
	private Integer contestId;
	private Integer allAttempts;
	private Integer successfullyAttempts;
			
	/** Creates a new instance of ContestStats */
	public ContestStats() {
	}

	public Integer getAllAttempts() {
		return allAttempts;
	}

	public void setAllAttempts(Integer allAttempts) {
		this.allAttempts = allAttempts;
	}

	public Integer getSuccessfullyAttempts() {
		return successfullyAttempts;
	}

	public void setSuccessfullyAttempts(Integer successfullyAttempts) {
		this.successfullyAttempts = successfullyAttempts;
	}

	public Integer getContestId() {
		return contestId;
	}

	public void setContestId(Integer contestId) {
		this.contestId = contestId;
	}
	
}
