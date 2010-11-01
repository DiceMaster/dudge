<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" scope="session" class="dudge.web.actions.UsersAction" />

<html:form action="users" method="GET" styleClass="x-form">
    <html:hidden property="reqCode" value="edit" />
    <html:hidden property="login" />
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
		<div class="x-form-bd" id="container"> 
		    <h3 style="margin-bottom:5px;"><bean:message key="user.user" /> <%=usersForm.getLogin()%></h3>
		    <fieldset>
			<legend><bean:message key="user.userInfo" /></legend>
			
			<% if(!usersForm.getRealName().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="user.realName" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getRealName()%></b>
			    </div>
			</div>
			<%};%>
			<div class="x-form-item">
			    <label><bean:message key="user.regDate" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getRegDate()%></b>
			    </div>
			</div>
			<% if(!usersForm.getOrganization().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="user.organization" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getOrganization()%></b>
			    </div>
			</div>
			<%};%>
			<% if(!usersForm.getAge().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="user.age" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getAge()%></b>
			    </div>
			</div>
			<%};%>
			<% if(!usersForm.getJabberId().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="user.jabberId" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getJabberId()%></b>
			    </div>
			</div>
			<%};%>
			<% if(!usersForm.getIcqNumber().equals("")) {%>
			<div class="x-form-item">
			    <label><bean:message key="user.icqNumber" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getIcqNumber()%></b>
			    </div>
			</div>
			<%};%>
			
			<div class="x-form-item">
			    <label><bean:message key="user.totalContestsMember" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getTotalContestsMember()%></b>
			    </div>
			</div>
			<div class="x-form-item">
			    <label><bean:message key="user.totalProblemsSolved" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.getTotalProblemsSolved()%></b>
			    </div>
			</div>
		    </fieldset>
		    
			<% if (
				pcb.canDeepModifyUser(
					sessionObject.extract(request.getSession()).getUsername(),
					usersForm.getLogin()
					)
				) {%>
			<fieldset>
			    <legend><bean:message key="user.permissions" /></legend>
			    
			    <div class="x-form-item">
			    <label><bean:message key="user.administrator" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.isAdmin()%></b>
			    </div>
			</div>
			
			<div class="x-form-item">
			    <label><bean:message key="user.contestCreator" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.isContestCreator()%></b>
			    </div>
			</div>
			
			<div class="x-form-item">
			    <label><bean:message key="user.problemCreator" /></label>
			    <div class="x-form-element">
				<b> <%=usersForm.isProblemCreator()%></b>
			    </div>
			</div>
			</fieldset>
			<% }; %>
		   
		    <% if(pcb.canModifyUser(sessionObject.getUsername(), request.getParameter("login"))) {	%>	
		    <div id="buttons" style="margin:5px;">
			<html:submit> <bean:message key="contest.editProperties"/>
			</html:submit>
		    </div>
		    <% };%>
		</div> 
    </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
