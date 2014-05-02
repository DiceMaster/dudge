/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.solution;

import dudge.db.ContestProblem;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 *
 * @author Arengor
 */
public class SolutionDescription {
	private int solutionId;
	private long submitTime;
	private String userName;
	private int contestId;
	private String contestName;
	private int problemId;
	private String problemName;
	private String languageName;
	private String status;
	private String statusMessage;
	private int runs;

	public SolutionDescription(Solution solution, SolutionMessageSource messageSource) {
		solutionId = solution.getSolutionId();
		submitTime = solution.getSubmitTime().getTime();
		userName = solution.getUser().getLogin();
		contestId = solution.getContest().getContestId();
		contestName = solution.getContest().getCaption();
		problemId = solution.getProblem().getProblemId();
		
		String mark = null;
		for (ContestProblem cp : solution.getContest().getContestProblems()) {
			if (cp.getProblem().getProblemId() == solution.getProblem().getProblemId()) {
				mark = cp.getProblemMark();
				break;
			}
		}
		if (mark != null) {
			problemName = mark + " - " + solution.getProblem().getTitle();
		} else {
			problemName = solution.getProblem().getTitle();
		}
		
		languageName = solution.getLanguage().getName();
		
		int testNumber;
		try {
			testNumber = Collections.max(solution.getRuns()).getRunNumber();
		} catch (NoSuchElementException e) {
			testNumber = 0;
		}
		
		if (solution.getStatus() != SolutionStatus.PROCESSED || solution.getLastRunResult() == null) {
			status = messageSource.getSolutionStatusMessage(solution.getStatus());
			switch (solution.getStatus()) {
				case RUNNING:
				case INTERNAL_ERROR:
					status += " " + messageSource.getOnTestMessage() + " " + testNumber;
					break;
			}
		} else {
			status = messageSource.getRunResultMessage(solution.getLastRunResult());
			switch (solution.getLastRunResult()) {
				case MEMORY_LIMIT:
				case OUTPUT_LIMIT:
				case PRESENTATION_ERROR:
				case RUNTIME_ERROR:
				case TIME_LIMIT:
				case WRONG_ANSWER:
					status += " " + messageSource.getOnTestMessage() + " " + testNumber;
					break;
			}			
		}
		statusMessage = solution.getStatusMessage();
	}
	
	/**
	 * @return the solutionId
	 */
	public int getSolutionId() {
		return solutionId;
	}

	/**
	 * @return the submitTime
	 */
	public long getSubmitTime() {
		return submitTime;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the contestId
	 */
	public int getContestId() {
		return contestId;
	}

	/**
	 * @return the contestName
	 */
	public String getContestName() {
		return contestName;
	}

	/**
	 * @return the problemId
	 */
	public int getProblemId() {
		return problemId;
	}

	/**
	 * @return the problemName
	 */
	public String getProblemName() {
		return problemName;
	}

	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the runs
	 */
	public int getRuns() {
		return runs;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	
	
}
