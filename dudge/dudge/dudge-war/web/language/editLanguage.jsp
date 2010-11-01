<%@page import="dudge.db.Language" %>

<jsp:useBean id="languagesForm" class="dudge.web.forms.LanguagesForm" scope="session" />
<jsp:useBean id="languagesAction" scope="session" class="dudge.web.actions.LanguagesAction" />

<html:form action="languages" styleClass="x-form">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
		<div class="x-form-bd" id="container">
		    <% if(languagesForm.isNewLanguage()) { %>
		    <html:hidden property="reqCode" value="submitCreate" />
		    <h3 style="margin-bottom:5px;"><bean:message key="language.create" /></h3>
		    <% } else { %>
		    <html:hidden property="reqCode" value="submitEdit" />
		    <html:hidden property="languageId" />
		    <h3 style="margin-bottom:5px;"><bean:message key="language.edit" /> <%=languagesForm.getLanguageId()%></h3>
		    <% } %>
		    <fieldset>
			<legend><bean:message key="language.languageInfo" /></legend>
			
			<%if(languagesForm.isNewLanguage()) {%>
			<div class="x-form-item">
			    <label for="login"><bean:message key="language.id" /></label>
			    <div class="x-form-element">
				<html:text property="languageId" styleId="languageId" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			 <% }; %>
			<div class="x-form-item">
			    <label for="title"><bean:message key="language.title" /></label>
			    <div class="x-form-element">
				<html:text property="title" styleId="title" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="description"><bean:message key="language.description" /></label>
			    <div class="x-form-element">
					 <html:textarea property="description" styleId="description" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
									
			
		    </fieldset>
		    
		    <fieldset>
			<legend><bean:message key="language.technicalParameters" /></legend>
			<div class="x-form-item">
			    <label for="fileExtension"><bean:message key="language.fileExtension" /></label>
			    <div class="x-form-element">
					<html:text property="fileExtension" styleId="fileExtension" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="compilationCommand"><bean:message key="language.compilationCommand" /></label>
			    <div class="x-form-element">
					<html:text property="compilationCommand" styleId="compilationCommand" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="executionCommand"><bean:message key="language.executionCommand" /></label>
			    <div class="x-form-element">
					<html:text property="executionCommand" styleId="executionCommand" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			
		    </fieldset>
		    
		   
		    
		    <html:submit>		
			<% if(languagesForm.isNewLanguage()) { %>
			<bean:message key="language.submitCreate" /> 
			<% } else { %>
			<bean:message key="language.applyChanges" />
			<% } %>
		    </html:submit>        
		</div>
    </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
