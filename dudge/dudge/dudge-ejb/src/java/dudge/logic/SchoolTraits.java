/*
 * AcmTraits.java
 *
 * Created on September 12, 2009, 3:15 PM
 */

package dudge.logic;

import dudge.db.Contest;

/**
 *
 * @author Arengor
 */
public class SchoolTraits implements ContestTraits {

	private Contest contest;

	/**
	 * Creates a new instance of AcmTraits
	 */
	public SchoolTraits(Contest contest) {
		this.contest = contest;
	}

	public Contest getContest() {
		return contest;
	}

	public boolean isRunAllTests() {
		return false;
	}

	public String getMonitorSuffix() {
		return "SCHOOL";
	}
}
