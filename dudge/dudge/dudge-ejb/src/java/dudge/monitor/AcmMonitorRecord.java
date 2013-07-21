/*
 * AcmMonitorRecord.java
 *
 * Created on October 27, 2007, 4:02 PM
 */
package dudge.monitor;

import dudge.DudgeLocal;
import dudge.db.Contest;
import dudge.db.ContestProblem;
import dudge.db.Solution;
import dudge.db.SolutionStatus;
import dudge.db.User;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AcmMonitorRecord implements Comparable, Serializable {

	public static final long serialVersionUID = 1L;
	// Штрафное время за неудачный сабмит, в миллисекундах.
	final static int submitPenaltyTime = 20 * 60 * 1000;
	private int place = 0;
	private String user;
	private Map<String, Integer> problemsTries = new HashMap<>();
	private Map<String, Boolean> problemsSolved = new HashMap<>();
	private long time;

	public AcmMonitorRecord(DudgeLocal dudgeBean, Contest contest, User user, Date when) {
		this.user = user.getLogin();

		// Штрафное время пользователя в данном соревновании.
		time = 0;

		for (ContestProblem contestProblem : contest.getContestProblems()) {
			String pm = contestProblem.getProblemMark();
			// Решена ли задача.
			problemsSolved.put(pm, false);

			// Количество неуспешных попыток решить задачу.
			problemsTries.put(pm, 0);

			// Полное время в миллисекундах на решение задачи,
			// учитывая 20 минутный штраф за каждое непрошедшее ее отправление.
			long problemTime = 0;

			// Проходим по всем решениям пользователем задачи соревнования.
			for (Solution solution : dudgeBean.getSolutions(user.getLogin(), contest.getContestId(), contestProblem.getProblem().getProblemId())) {
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
					problemsSolved.put(pm, true);

					// Прибавляем ко штрафному времени время от начала
					// соревнования до первой успешной попытки решения.
					problemTime += solution.getSubmitTime().getTime() - contest.getStartTime().getTime();
					break;
				}

				// Прибавляем штраф за неудачную отправку.
				problemTime += submitPenaltyTime;

				// Увеличиваем счетчик неудачных попыток решения.
				problemsTries.put(pm, problemsTries.get(pm) + 1);
			} // for solution

			if (problemsSolved.get(pm)) {
				// Если задача решена, то прибавляем ее штрафное время
				// к общему по пользователю.
				time += problemTime;
			}
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
		if (!(o instanceof AcmMonitorRecord)) {
			throw new RuntimeException("o must be of class AcmMonitorRecord");
		}

		AcmMonitorRecord other = (AcmMonitorRecord) o;
		int thisp = this.getSolvedProblemsCount();
		int otherp = other.getSolvedProblemsCount();
		if (thisp != otherp) {
			return (thisp < otherp) ? -1 : 1;
		}

		long thist = this.getTime();
		long othert = other.getTime();

		if (thist != othert) {
			return (thist > othert) ? -1 : 1;
		}

		return this.getUser().compareTo(other.getUser());
	}

	/**
	 * Позволяет получить количество решенных задач.
	 *
	 * @return количество решенных задач.
	 */
	public int getSolvedProblemsCount() {
		int solvedCount = 0;
		for (Map.Entry<String, Boolean> entry : problemsSolved.entrySet()) {
			if (entry.getValue()) {
				++solvedCount;
			}
		}

		return solvedCount;
	}

	/**
	 * Позволяет узнать, решена ли задача данным пользователем.
	 *
	 * @param problemMark метка задачи в соревновании.
	 * @return true если задача решена, иначе false.
	 */
	public boolean isSolved(String problemMark) {
		return problemsSolved.get(problemMark);
	}

	/**
	 * Возвращает количество неуспешных попыток решить задачу. После решения задачи попытки ее решить не засчитываются.
	 *
	 * @param problemMark отметка задачи в соревновании.
	 * @return количество неуспешных попыток.
	 */
	public int getProblemTriesCount(String problemMark) {
		return problemsTries.get(problemMark);
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
	 * Возвращает штрафное время для данной записи.
	 *
	 * @return штрафное время в миллисекундах.
	 */
	public long getTime() {
		return time;
	}
} // class AcmMonitorRecord
