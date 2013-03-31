/*
 * AcmTraits.java
 *
 * Created on September 26, 2007, 6:33 PM
 */

package dudge.logic;

import dudge.db.Contest;

/**
 *
 * @author Vladimir Shabanov
 */
public class AcmTraits implements ContestTraits {
	
	private Contest contest;
	
	/**
	 * Creates a new instance of AcmTraits
	 */
	public AcmTraits(Contest contest) {
		this.contest = contest;
	}

	public Contest getContest() {
		return contest;
	}

	public boolean isRunAllTests() {
		return false;
	}

	public String getMonitorSuffix() {
		return "ACM";
	}	
}
