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
    	public static final long serialVersionUID = 1L;
	
	private Contest contest;
	
	/**
	 * Creates a new instance of AcmTraits
	 */
	public AcmTraits(Contest contest) {
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
		return "ACM";
	}	
}
