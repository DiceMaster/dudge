/*
 * LoginForm.java
 *
 * Created on June 10, 2007, 10:32 PM
 */
package dudge.web.forms;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Vladimir Shabanov
 */
public class LoginForm extends ActionForm {

	public static final long serialVersionUID = 1L;
	private String username = "";
	private String password = "";

	/**
	 * Creates a new instance of LoginForm
	 */
	public LoginForm() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
