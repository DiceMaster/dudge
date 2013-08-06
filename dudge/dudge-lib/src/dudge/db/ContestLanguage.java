/*
 * ContestLanguage.java
 *
 * Created on 26 Сентябрь 2007 г., 21:28
 *
 */
package dudge.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity class ContestLanguage
 *
 * @author Michael Antonov
 */
@Entity(name = "ContestLanguage")
@Table(name = "contest_languages")
@NamedQueries({
	@NamedQuery(name = "ContestLanguage.findByContestId", query = "SELECT cl FROM ContestLanguage cl WHERE cl.contest.contestId = :contestId")
})
@IdClass(dudge.db.ContestLanguagePK.class)
public class ContestLanguage implements Serializable {

	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "contest_id", nullable = false, insertable = false, updatable = false)
	private int contestId;
	@JoinColumn(name = "contest_id", referencedColumnName = "contest_id", nullable = false)
	@ManyToOne
	private Contest contest;
	@Id
	@Column(name = "language_id", nullable = false, insertable = false, updatable = false)
	private String languageId;
	@JoinColumn(name = "language_id", referencedColumnName = "language_id", nullable = false)
	@ManyToOne
	private Language language;

	/**
	 * Creates a new instance of ContestLanguage
	 */
	public ContestLanguage() {
	}

	public ContestLanguage(Contest contest, Language language) {
		this.contest = contest;
		this.language = language;
		this.contestId = contest.getContestId();
		this.languageId = language.getLanguageId();
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.getContest() != null ? this.getContest().hashCode() : 0);
		hash += (this.getLanguage() != null ? this.getLanguage().hashCode() : 0);
		return hash;
	}

	/**
	 * Determines whether another object is equal to this ContestLanguage. The result is
	 * <code>true</code> if and only if the argument is not null and is a ContestLanguage object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ContestLanguage)) {
			return false;
		}
		ContestLanguage other = (ContestLanguage) object;
		if (this.contest == null || !this.getContest().equals(other.getContest())) {
			return false;
		}
		if (this.language == null || !this.getLanguage().equals(other.getLanguage())) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a string representation of the object. This implementation constructs that representation based on the id fields.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudgedb.ContestLanguage[contestId=" + getContest().getContestId() + " problemId=" + getLanguage().getLanguageId() + "]";
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
