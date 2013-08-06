/*
 * AcmMonitorRecord.java
 *
 * Created on October 27, 2007, 4:02 PM
 */
package dudge.monitor;

import dudge.SolutionLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import dudge.db.User;
import java.io.Serializable;
import java.util.Date;

public class GlobalMonitorRecord implements Comparable, Serializable {

	public static final long serialVersionUID = 1L;
	// Штраф за неудачный сабмит, в очках рейтинга.
	final static int submitPenaltyTime = 20 * 60 * 1000;
	private int place = 0;
	private String user;
	private int problemsSolved = 0;
	private int rating = 0;

	/**
	 *
	 * @param solutionBean
	 * @param contest
	 * @param user
	 * @param when
	 */
	public GlobalMonitorRecord(SolutionLocal solutionBean, Contest contest, User user, Date when) {
		this.user = user.getLogin();

		rating = 0;

		for (ContestProblem contestProblem : contest.getContestProblems()) {

			// Проходим по всем решениям пользователем задачи соревнования.
			for (Solution solution : solutionBean.getSolutions(user.getLogin(), contest.getContestId(), contestProblem.getProblem().getProblemId())) {
				// Не учитываем решения, которые не были обработаны
				// (в том числе не учитываем с ошибками компиляции).
				if (solution.getStatus() != SolutionStatus.PROCESSED) {
					continue;
				}

				// Не учитываем решения, отправленные перед началом соревнования,
				// после его конца и после определенного времени, кои таковое задано.
				if (solution.getSubmitTime().before(contest.getStartTime())
						|| (!contest.isInfinite() && solution.getSubmitTime().after(contest.getEndTime()))
						|| (when != null && solution.getSubmitTime().after(when))) {
					continue;
				}

				if (solution.isAllTestsPassed()) {
					// Задача решена.
					++problemsSolved;
					rating += 1;

					break;
				}
			} // for solution

		} // for contestProblem

	} // AcmMonitorRecord() constructor

	/**
	 * Сравнивает эту запись с другой в том же соревновании. Сравнение происходит по количеству решенных задач, потом, в случае совпадения количеств, по
	 * штрафному времени.
	 *
	 * @return -1 если эта запись стоит на месте, более низком, чем посланная, 1 - если на более высоком, 0 если записи равны.
	 */
	@Override
	public int compareTo(Object o) {
		if (!(o instanceof GlobalMonitorRecord)) {
			throw new RuntimeException("o must be of class AcmMonitorRecord");
		}

		GlobalMonitorRecord other = (GlobalMonitorRecord) o;
		int thisr = this.getRating();
		int otherr = other.getRating();
		if (thisr != otherr) {
			return (thisr < otherr) ? -1 : 1;
		}

		return this.getUser().compareTo(other.getUser());
	}

	/**
	 * Позволяет получить количество решенных задач.
	 *
	 * @return количество решенных задач.
	 */
	public int getSolvedProblemsCount() {
		return problemsSolved;
	}

	/**
	 * Возвращает место этой записи в общей таблице соревнования.
	 *
	 * @return место.
	 */
	public int getPlace() {
		return place;
	}

	/**
	 * Устанавливает место этой записи в общей таблице соревнования. Места могут совпадать для нескольких записей.
	 *
	 * @param place новое место.
	 */
	public void setPlace(int place) {
		this.place = place;
	}

	/**
	 * Возвращает имя пользователя для данной записи.
	 *
	 * @return имя пользователя.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Возвращает рейтинг для данной записи.
	 *
	 * @return рейтинг.
	 */
	public int getRating() {
		return rating;
	}
} // class AcmMonitorRecord
