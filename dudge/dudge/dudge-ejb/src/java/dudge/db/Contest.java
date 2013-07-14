/*
 * Contest.java
 *
 * Created on 12 апреля 2007 г., 19:36
 */

package dudge.db;

import dudge.logic.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Entity class Contest
 *
 * @author Michael Antonov
 */
@Entity(name="Contest")
@Table(name = "contests")
@NamedQueries( {
	@NamedQuery(name = "Contest.getContests", query = "SELECT c FROM Contest c"),
        @NamedQuery(name = "Contest.getActiveContests", query = "SELECT c FROM Contest c WHERE c.duration = 0 OR c.startTime < current_timestamp"),
        @NamedQuery(name = "Contest.getPendingContests", query = "SELECT c FROM Contest c WHERE c.type = 'GLOBAL' OR c.startTime > current_timestamp"),
        @NamedQuery(name = "Contest.getRecentlyFinishedContests", query = "SELECT c FROM Contest c WHERE c.duration <> 0 AND c.startTime < current_timestamp"),
	@NamedQuery(name = "Contest.findByContestId", query = "SELECT c FROM Contest c WHERE c.contestId = :contestId"),
	@NamedQuery(name = "Contest.findByType", query = "SELECT c FROM Contest c WHERE c.type = :type"),
	@NamedQuery(name = "Contest.findByStartTime", query = "SELECT c FROM Contest c WHERE c.startTime = :startTime"),
	@NamedQuery(name = "Contest.findByDuration", query = "SELECT c FROM Contest c WHERE c.duration = :duration")
})
public class Contest implements Serializable, Comparable<Contest> {
    	public static final long serialVersionUID = 1L;
	
	@SequenceGenerator(name="ContestIdGen", sequenceName="contests_contest_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ContestIdGen")
	@Id
	@Column(name = "contest_id", nullable = false)
	private int contestId;
	
	@Column(name="caption" , nullable = false)
	private String caption = "";
	
	@Column(name="description")
	private String description = "";
	
	@Column(name = "con_type", length=255, nullable = false)
	private String type = "GLOBAL";
	
	@Column(name = "start_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime = new Date();
	
	@Column(name = "duration", nullable = false)
	private int duration = 0;
	
	@Column(name="freeze_time" , nullable=false)
	private int freezeTime = 0;

	@Column(name="is_open" , nullable=false)
	private boolean open = false;

	@Column(name="rules", nullable=false)
	private String rules = "";
	
	/*@JoinTable(name = "contest_languages", joinColumns =  {
		@JoinColumn(name = "contest_id", referencedColumnName = "contest_id")
	}, inverseJoinColumns =  {
		@JoinColumn(name = "language_id", referencedColumnName = "language_id")
	})
	@ManyToMany
	private Collection<Language> languages;*/
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private Collection<Solution> solutions;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private Collection<Role> roles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
        @OrderBy("problemOrder ASC")
	private List<ContestProblem> contestProblems;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private Collection<ContestLanguage> contestLanguages;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contest")
	private Collection<Application> applications;

	/** Creates a new instance of Contest */
	public Contest() {
		this.contestProblems = new ArrayList<>();
		this.contestLanguages = new ArrayList<>();
		this.roles = new ArrayList<>();
		this.solutions = new ArrayList<>();
		this.applications = new ArrayList<>();
	}
	
	/**
	 * Creates a new instance of Contest with the specified values.
	 * @param type the type of the Contest
	 * @param startTime the startTime of the Contest
	 */
	public Contest(String caption , String description , String rules ,
                        ContestType type, Date startTime, int duration) {
		this();
		this.caption = caption;
		this.description = description;
                this.rules = rules;
		this.type = type.name();
		this.startTime = startTime;
		this.duration = duration;
		
	}
	
	/*private void writeObject(java.io.ObjectOutputStream out)
	throws java.io.IOException {
		// Для устранения LAZY relationship при сериализации.
		contestLanguages.size();
		contestProblems.size();
		roles.size();
		solutions.size();
		applications.size();
		
		out.defaultWriteObject();
	}*/
	
	/**
	 * Gets the contestId of this Contest.
	 * @return the contestId
	 */
	public int getContestId() {
		return contestId;
	}
	
	/**
	 * Sets the contestId of this Contest to the specified value.
	 * @param contestId the new contestId
	 */
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
	
	/**
	 * Gets the type of this Contest.
	 * @return the type
	 */
	public ContestType getType() {
		return ContestType.valueOf(type);
	}
	
	/**
	 * Sets the type of this Contest to the specified value.
	 * @param type the new type
	 */
	public void setType(ContestType type) {
		this.type = type.name();
	}
	
	/**
	 * Gets the startTime of this Contest.
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the startTime of this Contest to the specified value.
	 * @param startTime the new startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Возвращает продолжительность соревнования.
	 * @return продолжительность соревнования в секундах или 0,
	 * если соревнование длится вечно.
	 */
	public Integer getDuration() {
		return duration;
	}
	
	/**
	 * Устанавливает продолжительность соревнования.
	 * @param duration новая продолжительность соревнования.
	 * 0 означает бесконечную длительность.
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	/**
	 * Позволяет узнать, длится ли соревнование вечно.
	 * @return true если соревнование длится вечно,
	 * false - если нет.
	 */
	public boolean isInfinite() {
		return duration == 0;
	}
			
	public Date getEndTime() {
		return new Date(this.getStartTime().getTime() + this.getDuration() * 1000);
	}
	
	/**
	 * Gets the solutions of this Contest.
	 *
	 * @return the solutions
	 */
	public Collection<Solution> getSolutions() {
		return solutions;
	}
	
	/**
	 * Sets the solutions of this Contest to the specified value.
	 *
	 * @param solutions the new solutions
	 */
	public void setSolutions(Collection<Solution> solutions) {
		this.solutions = solutions;
	}
	
	/**
	 * Gets the roles of this Contest.
	 *
	 * @return the roles
	 */
	public Collection<Role> getRoles() {
		return roles;
	}
	
	/**
	 * Sets the roles of this Contest to the specified value.
	 *
	 * @param roles the new roles
	 */
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	/**
	 * Gets the contestProblems of this Contest.
	 *
	 * @return the contestProblems
	 */
	public List<ContestProblem> getContestProblems() {
		return contestProblems;
	}
	
	/**
	 * Sets the contestProblems of this Contest to the specified value.
	 *
	 * @param contestProblems the new contestProblems
	 */
	public void setContestProblems(List<ContestProblem> contestProblemsCollection) {
		this.contestProblems = contestProblemsCollection;
	}
	
	/**
	 * Returns a hash code value for the object.  This implementation computes
	 * a hash code value based on the id fields in this object.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += contestId;
		return hash;
	}
	
	/**
	 * Determines whether another object is equal to this Contest.  The result is
	 * <code>true</code> if and only if the argument is not null and is a Contest object that
	 * has the same id field values as this object.
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument;
	 * <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Contest)) {
			return false;
		}
		Contest other = (Contest)object;
		if (contestId != other.contestId) return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object.  This implementation constructs
	 * that representation based on the id fields.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Integer.toString(contestId);
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

        public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}
	
	public Collection<ContestLanguage> getContestLanguages() {
		return contestLanguages;
	}
	
	public void setContestLanguages(Collection<ContestLanguage> contestLanguages) {
		this.contestLanguages = contestLanguages;
	}

	/**
	 * Возвращает время, за которое до конца соревнования
	 * замораживается монитор. Например 3600 означает, что монитор
	 * будет заморожен за час до конца соревнования.
	 * @return время заморозки до конца соревнования в секундах
	 * (0 означает, что монитор не замораживается).
	 */
	public Integer getFreezeTime() {
		return freezeTime;
	}

	/**
	 * Устанавливает время заморозки монитора до конца соревнования.
	 * Например 3600 означает, что монитор будет заморожен за час до конца соревнования.
	 * @param freezeTime время до конца соревнования в секундах, за которое
	 * монитор будет заморожен.
	 */
	public void setFreezeTime(Integer freezeTime) {
		this.freezeTime = freezeTime;
	}

	public ContestTraits getTraits() {
		switch(this.getType()) {
			case GLOBAL:
				return new GlobalTraits(this);
			case ACM:
				return new AcmTraits(this);
			case LAB:
				return new LabTraits(this);
			case SCHOOL:
				return new SchoolTraits(this);
		}
		
		return null;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public Collection<Application> getApplications() {
		return applications;
	}

	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}

	/**
	 * Позволяет определить, идет ли соревнование.
	 * @return true если этап регистрации завершен
	 * и сорвенование идет (и не завершилось).
	 * Иначе false.
	 */
	public boolean isInProgress()
	{
		if(new Date().compareTo(this.getStartTime()) >= 0
				&& (
					new Date().compareTo(this.getEndTime()) <= 0
					||
					this.isInfinite()
					)
				)
			return true;

		return false;
	}

	/**
	 * Позволяет определить, идет ли регистрация в соревнование
	 * или оно ожидает начало
	 * @return true если в соревнование все еще идет регистрация
	 * или оно ожидает своего начала. Иначе false.
	 */
	public boolean isPending()
	{
		if(new Date().compareTo(this.getStartTime()) < 0)
			return true;

		if(new Date().compareTo(this.getStartTime()) >= 0
				&& this.getType() == ContestType.GLOBAL)
			return true;

		return false;
	}

	/**
	 * Позволяет определить, завершилось ли соревнование.
	 * @return true если соревнование завершилось,
	 * иначе false.
	 */
	public boolean isFinished()
	{
		if(this.isInfinite())
			return false;
		
		if(new Date().compareTo(this.getEndTime()) > 0)
			return true;

		return false;
	}

        @Override
        public int compareTo(Contest o) {
            
            if (o.getStartTime().before(startTime))
                return -1;
            else
                if (startTime.before(o.getStartTime()))
                    return 1;
                else
                    return 0;
        }
}
