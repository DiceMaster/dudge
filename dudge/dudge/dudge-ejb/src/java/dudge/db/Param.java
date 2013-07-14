/*
 * Param.java
 *
 * Created on 12 Апрель 2007 г., 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dudge.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity class Param
 *
 * @author Michael Antonov
 */
@Entity(name = "Param")
@Table(name = "params")
@NamedQueries( {
	@NamedQuery(name = "Param.findByName", query = "SELECT p FROM Param p WHERE p.name = :name")
})
public class Param implements Serializable {
    	public static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "pname", nullable = false)
	private String name;
	
	@Lob
	@Column(name = "pvalue", nullable = false)
	private String value;
	
	/** Creates a new instance of Param */
	public Param() {
	}
	
	/**
	 * Creates a new instance of Param with the specified values.
	 * @param name the name of the Param
	 */
	public Param(String name) {
		this.name = name;
	}
	
	/**
	 * Creates a new instance of Param with the specified values.
	 * @param name the name of the Param
	 * @param value the value of the Param
	 */
	public Param(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Gets the name of this Param.
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of this Param to the specified value.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the value of this Param.
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Sets the value of this Param to the specified value.
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Returns a hash code value for the object.  This implementation computes
	 * a hash code value based on the id fields in this object.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}
	
	/**
	 * Determines whether another object is equal to this Param.  The result is
	 * <code>true</code> if and only if the argument is not null and is a Param object that
	 * has the same id field values as this object.
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument;
	 * <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Param)) {
			return false;
		}
		Param other = (Param)object;
		if (this.name == null || !this.name.equals(other.name)) return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object.  This implementation constructs
	 * that representation based on the id fields.
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "dudge.db.Param[name=" + name + "]";
	}
	
}
