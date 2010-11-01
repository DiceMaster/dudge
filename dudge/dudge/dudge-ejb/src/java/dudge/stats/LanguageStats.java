/*
 * LanguageStats.java
 *
 * Created on 5 Июнь 2007 г., 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.stats;

/**
 * Inform. class, encapsulated the statistics at any Language.
 *
 * @author Michael Antonov
 */
public class LanguageStats {
	
	private Integer languageId;
	private Integer allAttempts;
	private Integer compabilityErrorAttempts;
	private Integer runtimeErrorAttempts;
	private Integer timeLimitAttempts;
	private Integer memoryLimitAttempts;
	private Integer acceptedAttempts;
	
	/** Creates a new instance of LanguageStats */
	public LanguageStats() {
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getAllAttempts() {
		return allAttempts;
	}

	public void setAllAttempts(Integer allAttempts) {
		this.allAttempts = allAttempts;
	}

	public Integer getCompabilityErrorAttempts() {
		return compabilityErrorAttempts;
	}

	public void setCompabilityErrorAttempts(Integer compabilityErrorAttempts) {
		this.compabilityErrorAttempts = compabilityErrorAttempts;
	}

	public Integer getRuntimeErrorAttempts() {
		return runtimeErrorAttempts;
	}

	public void setRuntimeErrorAttempts(Integer runtimeErrorAttempts) {
		this.runtimeErrorAttempts = runtimeErrorAttempts;
	}

	public Integer getTimeLimitAttempts() {
		return timeLimitAttempts;
	}

	public void setTimeLimitAttempts(Integer timeLimitAttempts) {
		this.timeLimitAttempts = timeLimitAttempts;
	}

	public Integer getMemoryLimitAttempts() {
		return memoryLimitAttempts;
	}

	public void setMemoryLimitAttempts(Integer memoryLimitAttempts) {
		this.memoryLimitAttempts = memoryLimitAttempts;
	}

	public Integer getAcceptedAttempts() {
		return acceptedAttempts;
	}

	public void setAcceptedAttempts(Integer acceptedAttempts) {
		this.acceptedAttempts = acceptedAttempts;
	}
	
}
