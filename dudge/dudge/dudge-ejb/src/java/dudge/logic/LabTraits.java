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
public class LabTraits implements ContestTraits {

	public static final long serialVersionUID = 1L;
	private Contest contest;

	/**
	 * Creates a new instance of LabTraits
	 */
	public LabTraits(Contest contest) {
		this.contest = contest;
	}

	@Override
	public Contest getContest() {
		return contest;
	}

	@Override
	public boolean isRunAllTests() {
		return true;
	}

	@Override
	public String getMonitorSuffix() {
		return "LAB";
	}
}
