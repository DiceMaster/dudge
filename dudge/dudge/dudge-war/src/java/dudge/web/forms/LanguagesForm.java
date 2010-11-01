/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author olorin
 */
public class LanguagesForm extends org.apache.struts.action.ActionForm {
  
	// Действие над текущим языком.
	private boolean newLanguage = false;
	
	// Информация о текущем обрабатываемом языке.
  
	private String languageId;
	private String title;
	private String description;
	private String fileExtension;
	private String compilationCommand;
	private String executionCommand;
   
	public LanguagesForm() {
   }

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		languageId="";
		title="";
		description="";
		fileExtension="";
		compilationCommand="";
		executionCommand="";
	}
	
	

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}


	public String getCompilationCommand() {
		return compilationCommand;
	}

	public void setCompilationCommand(String compilationCommand) {
		this.compilationCommand = compilationCommand;
	}


	public String getExecutionCommand() {
		return executionCommand;
	}

	public void setExecutionCommand(String executionCommand) {
		this.executionCommand = executionCommand;
	}

	public boolean isNewLanguage() {
		return newLanguage;
	}

	public void setNewLanguage(boolean newLanguage) {
		this.newLanguage = newLanguage;
	}
   
   
}
