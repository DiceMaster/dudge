/*
 * LabTraits.java
 *
 * Created on September 26, 2007, 6:34 PM
 */

package dudge.logic;

import dudge.db.Contest;

/**
 *
 * @author Vladimir Shabanov
 */
public class LabTraits implements ContestTraits{
	
	private Contest contest;
	
	/**
	 * Creates a new instance of LabTraits
	 */
	public LabTraits(Contest contest) {
		this.contest = contest;
	}

	public Contest getContest() {
		return contest;
	}

	public boolean isRunAllTests() {
		return true;
	}

	public String getMonitorSuffix() {
		return "LAB";
	}	
}
