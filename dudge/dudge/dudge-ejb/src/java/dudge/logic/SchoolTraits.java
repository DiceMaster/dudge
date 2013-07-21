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

	public static final long serialVersionUID = 1L;
	private Contest contest;

	/**
	 * Creates a new instance of AcmTraits
	 */
	public SchoolTraits(Contest contest) {
		this.contest = contest;
	}

	@Override
	public Contest getContest() {
		return contest;
	}

	@Override
	public boolean isRunAllTests() {
		return false;
	}

	@Override
	public String getMonitorSuffix() {
		return "SCHOOL";
	}
}
