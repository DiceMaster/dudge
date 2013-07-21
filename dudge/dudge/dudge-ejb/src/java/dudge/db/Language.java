/*
 * Language.java
 *
 * Created on 12 Апрель 2007 г., 19:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dudge.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

/**
 * Entity class Language
 *
 * @author Michael Antonov
 */
@Entity(name = "Language")
@Table(name = "languages")
@NamedQueries({
	@NamedQuery(name = "Language.getLanguages", query = "SELECT l FROM Language l"),
	@NamedQuery(name = "Language.findByLanguageId", query = "SELECT l FROM Language l WHERE l.languageId = :languageId"),
	@NamedQuery(name = "Language.findByName", query = "SELECT l FROM Language l WHERE l.languageId = :languageId"),
	@NamedQuery(name = "Language.findByDescription", query = "SELECT l FROM Language l WHERE l.description = :description"),
	@NamedQuery(name = "Language.findByFileExtension", query = "SELECT l FROM Language l WHERE l.fileExtension = :fileExtension")
})
public class Language implements Serializable {

	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "language_id", nullable = false, updatable = false)
	private String languageId;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "file_extension", nullable = false)
	private String fileExtension;
	@Column(name = "compilation_cmd", nullable = false)
	private String compilationCommand;
	@Column(name = "execution_cmd", nullable = false)
	private String executionCommand;

	/**
	 * Creates a new instance of Language
	 */
	public Language() {
	}

	/**
	 * Creates a new instance of Language with the specified values.
	 *
	 * @param languageId the languageId of the Language
	 */
	public Language(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * Creates a new instance of Language with the specified values.
	 *
	 * @param languageId the languageId of the Language
	 * @param title the title of the Language
	 * @param description the description of the Language
	 */
	public Language(String languageId, String name, String description, String fileExtension, String compilationCommand, String executionCommand) {
		this.languageId = languageId;
		this.title = name;
		this.description = description;
		this.fileExtension = fileExtension;
		this.compilationCommand = compilationCommand;
		this.executionCommand = executionCommand;
	}

	/**
	 * Gets the languageId of this Language.
	 *
	 * @return the languageId
	 */
	public String getLanguageId() {
		return this.languageId;
	}

	/**
	 * Sets the languageId of this Language to the specified value.
	 *
	 * @param languageId the new languageId
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * Gets the title of this Language.
	 *
	 * @return the title
	 */
	public String getName() {
		return this.title;
	}

	/**
	 * Sets the title of this Language to the specified value.
	 *
	 * @param title the new title
	 */
	public void setName(String name) {
		this.title = name;
	}

	/**
	 * Gets the description of this Language.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of this Language to the specified value.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the fileExtension of this Language.
	 *
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return this.fileExtension;
	}

	/**
	 * Sets the fileExtension of this Language to the specified value.
	 *
	 * @param fileExtension the new fileExtension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 *
	 * @return
	 */
	public String getCompilationCommand() {
		return compilationCommand;
	}

	/**
	 *
	 * @param compilationCommand
	 */
	public void setCompilationCommand(String compilationCommand) {
		this.compilationCommand = compilationCommand;
	}

	/**
	 *
	 * @return
	 */
	public String getExecutionCommand() {
		return executionCommand;
	}

	/**
	 *
	 * @param executionCommand
	 */
	public void setExecutionCommand(String executionCommand) {
		this.executionCommand = executionCommand;
	}

	/**
	 * Returns a hash code value for the object. This implementation computes a hash code value based on the id fields in this object.
	 *
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.languageId != null ? this.languageId.hashCode() : 0);
		return hash;
	}

	/**
	 * Determines whether another object is equal to this Language. The result is
	 * <code>true</code> if and only if the argument is not null and is a Language object that has the same id field values as this object.
	 *
	 * @param object the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the argument; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Language)) {
			return false;
		}
		Language other = (Language) object;
		if (this.languageId == null || !this.languageId.equals(other.languageId)) {
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
		return "dudge.db.Language[languageId=" + languageId + "]";
	}

	/**
	 * Converts a collection of dudge.db.Language objects to a collection of String objects.
	 *
	 * @return converted collection of String objects.
	 */
	static public Collection<String> convertCollection(Collection<Language> langs) {
		Collection<String> stringCollection = new ArrayList<>();

		for (Language l : langs) {
			stringCollection.add(l.getName());
		}

		return stringCollection;
	}
}
