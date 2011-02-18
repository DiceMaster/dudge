<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" class="dudge.web.actions.UsersAction" scope="session"/>

<html:form styleId="userForm" action="users" styleClass="x-form">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
		<div class="x-form-bd" id="container">
		    <% if(usersForm.isNewUser()) { %>
		    <html:hidden property="reqCode" value="submitRegister" />
		    <h3 style="margin-bottom:5px;"><bean:message key="registration.registration" /></h3>
		    <% } else { %>
		    <html:hidden property="reqCode" value="submitEdit" />
		    <html:hidden property="login" />
		    <h3 style="margin-bottom:5px;"><bean:message key="user.user" /> <%=usersForm.getLogin()%></h3>
		    <% } %>
		    <fieldset>
			<legend><bean:message key="registration.requiredInfo" /></legend>
			
			<%if(usersForm.isNewUser()) {%>
			<div class="x-form-item">
			    <label for="login"><bean:message key="user.login" /></label>
			    <div class="x-form-element">
				<html:text property="login" styleId="login" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="realName"><bean:message key="user.realName" /></label>
			    <div class="x-form-element">
					<html:text property="realName" styleId="realName" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="password"><bean:message key="user.password" /></label>
			    <div class="x-form-element">
				<html:password property="password" styleId="password" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="passwordConfirm"><bean:message key="user.repPassword" /></label>
			    <div class="x-form-element">
				<html:password property="passwordConfirm" styleId="passwordConfirm" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<% }; %>
			
			<div class="x-form-item">
			    <label for="email"><bean:message key="user.email"/></label>
				<div class="x-form-element">
					<html:text property="email" styleId="email" size="20" styleClass="x-form-text x-form-field"/>
				</div>
			</div>
			
		    </fieldset>
		    
		    <fieldset>
			<legend><bean:message key="registration.additionalInfo" /></legend>
			<div class="x-form-item">
			    <label for="organization"><bean:message key="user.organization" /></label>
			    <div class="x-form-element">
					<html:text property="organization" styleId="organization" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="age"><bean:message key="user.age" /></label>
			    <div class="x-form-element">
					<html:text property="age" styleId="age" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="jabberId"><bean:message key="user.jabberId" /></label>
			    <div class="x-form-element">
					<html:text property="jabberId" styleId="jabberId" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="icqNumber"><bean:message key="user.icqNumber" /></label>
			    <div class="x-form-element">
					<html:text property="icqNumber" styleId="icqNumber" size="20" styleClass="x-form-text x-form-field"/>
			    </div>
			</div>
		    </fieldset>
		    
		    <% if (
				pcb.canDeepModifyUser(
					authenticationObject.getUsername(),
					usersForm.getLogin()
					)
				) {%>
		    <fieldset>
			<legend><bean:message key="user.permissions" /></legend>
			
			<div class="x-form-item">
				<label>
				<html:checkbox property="admin" />
				<bean:message key="user.administrator" />
				</label>
			</div>
			
			<div class="x-form-item">	    
				<label>
				<html:checkbox property="contestCreator" />
				<bean:message key="user.contestCreator" />
				</label>
			</div>
			
			<div class="x-form-item">
				<label>
					<html:checkbox property="problemCreator" />
					<bean:message key="user.problemCreator" />
				</label>
			</div>
		    </fieldset>
		    <% }; %>
		    
			<html:submit>
			<% if(usersForm.isNewUser()) { %>
			<bean:message key="registration.register" /> 
			<% } else { %>
			<bean:message key="user.applyChanges" />
			<% } %>
		    </html:submit>        
		</div>
    </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
