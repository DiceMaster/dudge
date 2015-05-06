package dudge.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Aleksandr Tsyganov
 */
public class FileUploadForm extends ActionForm {
		
	private FormFile upload;
	private String CKEditorFuncNum;
	private String url;
 
	public FormFile getUpload() {
		return upload;
	}
 
	public void setUpload(FormFile file) {
		this.upload = file;
	}
	
	public String getCKEditorFuncNum() {
		return CKEditorFuncNum;
	}

	public void setCKEditorFuncNum(String CKEditorFuncNum) {
		this.CKEditorFuncNum = CKEditorFuncNum;
	}

		public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
 
	@Override
	public ActionErrors validate(ActionMapping mapping,
	   HttpServletRequest request) {
 
	    ActionErrors errors = new ActionErrors();
 
	    if( getUpload().getFileSize()== 0){
	       errors.add("common.file.err", new ActionMessage("error.common.file.required"));
	       return errors;
	    }
 
	    return errors;
	}
}
