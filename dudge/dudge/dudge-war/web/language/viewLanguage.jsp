<%@page import="dudge.db.Language"%>

<jsp:useBean id="languagesForm" class="dudge.web.forms.LanguagesForm" scope="session" />
<jsp:useBean id="languagesAction" scope="session" class="dudge.web.actions.LanguagesAction" />

<html:form action="languages" method="GET" styleClass="x-form">
    <html:hidden property="reqCode" value="edit" />
    <html:hidden property="languageId" />
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
		<div class="x-form-bd" id="container"> 
		    <h3 style="margin-bottom:5px;"><bean:message key="language.language" /> <%=languagesForm.getLanguageId()%></h3>
		    <fieldset>
			<legend><bean:message key="language.languageInfo" /></legend>
			
			<% if(!languagesForm.getTitle().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="language.title" /></label>
			    <div class="x-form-element">
				<b> <%=languagesForm.getTitle()%></b>
			    </div>
			</div>
			<%};%>
			<div class="x-form-item">
			    <label><bean:message key="language.description" /></label>
			    <div class="x-form-element">
				<b> <%=languagesForm.getDescription()%></b>
			    </div>
			</div>
			</fieldset>
			<fieldset>
			    <legend><bean:message key="language.technicalParameters" /></legend>
			    
			    <div class="x-form-item">
			    <label><bean:message key="language.fileExtension" /></label>
			    <div class="x-form-element">
				<b> <%=languagesForm.getFileExtension()%></b>
			    </div>
			</div>
			
			<div class="x-form-item">
			    <label><bean:message key="language.compilationCommand" /></label>
			    <div class="x-form-element">
				<b> <%=languagesForm.getCompilationCommand()%></b>
			    </div>
			</div>
			
			<div class="x-form-item">
			    <label><bean:message key="language.executionCommand" /></label>
			    <div class="x-form-element">
				<b> <%=languagesForm.getExecutionCommand()%></b>
			    </div>
			</div>
			</fieldset>
					   
		    <% if(pcb.canModifyLanguage(authenticationObject.getUsername())) {	%>
		    <div id="buttons" style="margin:5px;">
			<html:submit> <bean:message key="language.editProperties"/>
			</html:submit>
		    </div>
		    <% };%>
		</div> 
    </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
