<%@page import="java.text.SimpleDateFormat" %>
<%@page import="dudge.db.Contest" %>
<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />

<script type="text/javascript">
Ext.onReady(function(){

var buttonsToolbar = Ext.getCmp('content-panel').getTopToolbar();

 <% if (pcb.canSendApplication(sessionObject.getUsername(), contestId)) {%>
 var btnApplication = new Ext.Toolbar.Button({
	text: '<bean:message key="contest.sendApplication" />',
	handler:function() {
		var contestId = <%=contestId%>;
		var contestConnection =  (new Ext.data.HttpProxy({})).getConnection();
		function sendApplication (btn , text) {
			if(btn == 'ok')	{
				contestConnection.request({
					method: 'POST',
					url: 'contests.do',
					params: {reqCode: 'sendApplication' , contestId: contestId , message: text},
					callback: function() {
						Ext.MessageBox.alert('<bean:message key="contest.applicationSentTitle" />',
							 '<bean:message key="contest.applicationSent" />');
						 }
					 });
				 }
		 } // function sendApplication()

		 Ext.Msg.show({
			  title:'<bean:message key="application.filingTitle" />',
			   msg: '<bean:message key="application.filingMessage" />',
			   buttons: Ext.Msg.OKCANCEL,
			   fn: sendApplication,
			   animEl: btnApplication,
			   multiline: true,
				value: '<bean:message key="application.reasonSample" />',
				defaultTextHeight: 250,
				width: 500,
				closable: false
			});
		} // handler function
}); // button

buttonsToolbar.addButton(btnApplication);
<% } %>
<%
if (pcb.canModifyContest(
		sessionObject.getUsername(),
		contestId
		)) {
%>
	buttonsToolbar.addButton({
		text: '<bean:message key="contest.edit" />',
		handler: function()
		{
		  location.href = 'contests.do?reqCode=edit&contestId=<%=contestId%>';
		}
	});
<%}%>
<%
if (pcb.canDeleteContest(
		sessionObject.getUsername(),
		contestId
		)) {
%>
	 buttonsToolbar.addButton(
		 {
			  text: '<bean:message key="contest.delete" />',
			  handler: function()
			  {
				  location.href = 'contests.do?reqCode=delete&contestId=<%=contestId%>';
			   }
		   }
	   );
<%}%>

buttonsToolbar.doLayout();
buttonsToolbar.render();
}); //Ext.onReady()
</script>

<html:form action="contests" method="GET">
	<h3 align="center" style="font-size:x-large"><%=contestsForm.getCaption()%></h3>
	<p align="center" class="contest_info" >
				<a href="contests.do?reqCode=rules&contestId=<%=request.getParameter("contestId")%>"><bean:message key="contest.rules" /></a><br>
		<bean:message key="contest.type" />: <%=contestsForm.getContestType()%><br>
		<%if(contestsForm.isOpen()) {%>
		 <bean:message key="contest.isOpen" />: <bean:message key="contest.open.yes" /><br>
		<% } else {%>
		 <bean:message key="contest.isOpen" />: <bean:message key="contest.open.no" /><br>
		 <% };%>
		<bean:message key="contest.startDate" />: <%=contestsForm.getStartDate()%><br>
		<bean:message key="contest.startTime" />: <%=contestsForm.getStartHour()%>:<%=contestsForm.getStartMinute()%><br>
		<bean:message key="contest.duration" />: <%=contestsForm.getDurationHours()%>:<%=contestsForm.getDurationMinutes()%><br>
	</p><br>
	<p class="contest_info"><%=contestsForm.getDescription()%> </p>
	<p class="contest_info">
		<h2 align="center" style="font-size:large"><bean:message key="contest.competitors" /></h2>
		<table align="center" border="1" cellspacing="0" width="50%">
			<thead>
				<tr>
					<th><b><bean:message key="user.login" /></b></th>
					<th><b><bean:message key="user.realName" /></b></th>
				</tr>
			</thead>
			<tbody>
				<% java.util.List<dudge.db.Role> roles = (java.util.List<dudge.db.Role>) sessionObject.getDudge().getContest( contestId ).getRoles();%>
				<% for (dudge.db.Role role : roles) {
		if (role.getRoleType() != dudge.db.RoleType.USER) {
			continue;
		}
				%>
				<tr>
					<td width="50%"><a href="users.do?reqCode=view&login=<%=role.getUser().getLogin()%>"><%=role.getUser().getLogin() %></a></td>
					<td width="50%"><%=role.getUser().getRealName() %> </td>
				</tr>
				<%}
	;%>
			</tbody>
		</table>
	</p>
</html:form>