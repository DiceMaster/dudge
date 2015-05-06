/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.web.actions;

import dudge.web.AuthenticationObject;
import dudge.web.forms.FileUploadForm;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Arengor
 */
public class FileUploadAction extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AuthenticationObject ao = AuthenticationObject.extract(request);
		if (ao.getUsername() == null) {
			return mapping.findForward("loginRequired");
		}
		
		if (!ao.getPermissionChecker().canUploadFiles(ao.getUsername())) {
			return mapping.findForward("accessDenied");
		}
 
	    FileUploadForm filesForm = (FileUploadForm)form;
 
	    FormFile file = filesForm.getUpload();
 
	    //Get the servers upload directory real path name
	    String filePath = getServlet().getServletContext().getRealPath("/") +"upload";
 
	    //create the upload folder if not exists
	    File folder = new File(filePath);
	    if(!folder.exists()){
	    	folder.mkdir();
	    }
 
	    String fileName = file.getFileName();
 
	    if(!("").equals(fileName)){  
 
	        System.out.println("Server path:" +filePath);
	        File newFile = new File(filePath, fileName);
 
	        if(!newFile.exists()){
	          FileOutputStream fos = new FileOutputStream(newFile);
	          fos.write(file.getFileData());
	          fos.flush();
	          fos.close();
	        }
	    }
		
		return mapping.findForward("fileUploaded");
	}
	
}
