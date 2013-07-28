package dudge.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author olorin
 */
@Entity(name = "New")
@Table(name = "news")
public class News implements Serializable {

	public static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "NewsIdGen", sequenceName = "news_news_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NewsIdGen")
	@Id
	@Column(name = "news_id", nullable = false)
	private int newsId;
	@JoinColumn(name = "author", referencedColumnName = "login")
	@ManyToOne
	private User author;
	@Column(name = "adding_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date addingTime;
	@Column(name = "message", nullable = false)
	private String message;

	/**
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) newsId;
		return hash;
	}

	/**
	 *
	 * @return
	 */
	public Date getAddingTime() {
		return addingTime;
	}

	/**
	 *
	 * @param addingTime
	 */
	public void setAddingTime(Date addingTime) {
		this.addingTime = addingTime;
	}

	/**
	 *
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 *
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 *
	 * @return
	 */
	public int getNewsId() {
		return newsId;
	}

	/**
	 *
	 * @param newsId
	 */
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	/**
	 *
	 * @return
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 *
	 * @param author
	 */
	public void setOwner(User author) {
		this.author = author;
	}

	/**
	 *
	 */
	public News() {
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof News)) {
			return false;
		}
		News other = (News) object;
		if (this.newsId != other.newsId) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "dudge.db.New[id=" + newsId + "]";
	}
}
