package dudge.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Aleksandr Tsyganov
 */
public class RulesForm extends ActionForm{
	private String rules;
	private boolean notFilled;

	public RulesForm() {
		
	}
	
	/**
	 *
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		rules = "";
		notFilled = false;
	}
	
	/**
	 * @return the rules
	 */
	public String getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(String rules) {
		this.rules = rules;
	}

	/**
	 * @return are rules empty
	 */
	public boolean isNotFilled() {
		return notFilled;
	}

	/**
	 * @param notFilled set empty flag
	 */
	public void setNotFilled(boolean notFilled) {
		this.notFilled = notFilled;
	}
}
