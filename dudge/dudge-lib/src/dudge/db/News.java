/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author olorin
 */
@Entity(name="New")
@Table(name = "news")
public class News implements Serializable {
	
	@SequenceGenerator(name="NewsIdGen", sequenceName="news_news_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NewsIdGen")
	@Id
	@Column(name = "news_id", nullable = false)
	private int newsId;
	
	@JoinColumn(name = "author", referencedColumnName = "login")
	@ManyToOne
	private User author;
	
	@Column(name = "adding_time", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date addingTime; 
	
	@Column(name="message" , nullable=false)
	private String message;
		
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) newsId;
		return hash;
	}

	public Date getAddingTime() {
		return addingTime;
	}

	public void setAddingTime(Date addingTime) {
		this.addingTime = addingTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public User getAuthor() {
		return author;
	}

	public void setOwner(User author) {
		this.author = author;
	}

	public News() {
	}

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

	@Override
	public String toString() {
		return "dudge.db.New[id=" + newsId + "]";
	}

}
