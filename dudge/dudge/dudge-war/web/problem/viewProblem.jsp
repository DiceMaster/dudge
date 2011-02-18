<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<script type="text/javascript" src="scripts/json.js"></script>

<script type="text/javascript">
Ext.onReady(function(){	
	var buttonsToolbar = Ext.getCmp('content-panel').getTopToolbar();
	  
	<%
	if (
		pcb.canSubmitSolution(
		authenticationObject.getUsername(),
		contestId,
		problemsForm.getProblemId())
		) {
	%>
	buttonsToolbar.addButton({
		text: '<bean:message key="problem.submitSolution" />',
		handler: function() {
			window.location = 'solutions.do?reqCode=submit'
			+ '&contestId=<%=contestId%>'
			+ '&problemId=<%=request.getParameter("problemId")%>';
		}
	});
	<%}%>
	<%
	if (
		pcb.canModifyProblem(
		authenticationObject.getUsername(),
		Integer.parseInt(request.getParameter("problemId"))
		)
		) {
	%>
	buttonsToolbar.addButton(
		{
			text: '<bean:message key="problem.edit" />',
			handler: function()
			{
				location.href = 'problems.do?reqCode=edit&problemId='
					+ '<%=request.getParameter("problemId")%>';
			}
		}
		);
	<%}%>
	<%
	if (
		pcb.canDeleteProblem(
		authenticationObject.getUsername(),
		Integer.parseInt(request.getParameter("problemId"))
		)
		) {
	%>
	buttonsToolbar.addButton(
		{
			text: '<bean:message key="problem.delete" />',
			handler: function()
			{
			     function commitDelete (btn) 
			     {
				if (btn == 'yes') 
				{			
				    problemId = '<%=request.getParameter("problemId")%>';
				    var problemConnection =  (new Ext.data.HttpProxy({})).getConnection();
				    problemConnection.request({
					method: 'POST' ,
					url: 'problems.do',
					params: {reqCode: 'delete' , problemId: problemId},
					callback: function() 
						{ 
						    location.href = 'problems.do?reqCode=list';
						}    
				    });
				}		        
			    }
		
			    Ext.MessageBox.confirm('<bean:message key="problem.confirmDeleteTitle" />',
			    '<bean:message key="problem.confirmDeleteMsg" />',
			    commitDelete);
			}
		}
		);
	<%}%>
});
</script>

<div id="centerPanel">
<html:form action="problems" method="GET">
    <h3 align="center" style="font-size:x-large"><bean:message key="problem.problem" /> <%=problemsForm.getProblemId()%>: <%=problemsForm.getTitle()%></h3>
    <p align="center" class="problem_info" >
	<bean:message key="problem.owner" />: <a href="users.do?reqCode=view&login=<%=problemsForm.getOwner()%>"><%=problemsForm.getOwner()%></a>
	| <bean:message key="problem.author" />: <%=problemsForm.getAuthor()%><br>
	<bean:message key="problem.created" /> <%=problemsForm.getCreateTime()%><br>
	<bean:message key="problem.cpuTimeLimit" />: <%=problemsForm.getCpuTimeLimit()%> <bean:message key="problem.cpuMetric" /><br>
	<bean:message key="problem.memoryLimit" />: <%=problemsForm.getMemoryLimit()/(1024*1024)%> <bean:message key="problem.memoryMetric" /><br>
	<bean:message key="problem.outputLimit" />: <%=problemsForm.getOutputLimit()/(1024*1024)%> <bean:message key="problem.memoryMetric" /><br>
    </p><br>
    <p class="problem_info"><%=problemsForm.getDescription()%></p>
		
	<p class="problem_info">
	<h2 align="center" style="font-size:large"><bean:message key="problem.example" /></h2>
	<table align="center" border="1" cellspacing="0" width="90%">
	    <thead>
		<tr>
		    <th><bean:message key="problem.input" /></th>
		    <th><bean:message key="problem.output" /></th>
		</tr>
	    </thead>
	    <tbody>
		<tr>
		    <td width="50%" valign="top"><pre><%=problemsForm.getExampleInputTest() %></pre></td>
		    <td width="50%" valign="top"><pre><%=problemsForm.getExampleOutputTest() %></pre></td>
		</tr>
	    </tbody>
	</table>
    </p>
    
</html:form>
</div>