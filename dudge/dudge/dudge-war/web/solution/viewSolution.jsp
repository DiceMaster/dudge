<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<script type="text/javascript">
Ext.onReady(function() {
	var solutionToolbar = Ext.getCmp('content-panel').getTopToolbar();

	solutionToolbar.addButton({
		text: '<bean:message key="solution.update" />',
		handler: function(){
			window.location.reload(true);
			}
		});
});
</script>

<div id="solutionContent">

<h3><bean:message key="solution.solution" /> <%=solutionsForm.getSolutionId()%></h3>
<bean:message key="solution.status" />: 

<% String status = solutionsForm.getStatus(); %>

<% if(status == "NEW") { %>
<b><bean:message key="solution.status.NEW" /></b>
<% } %>

<% if(status == "INTERNAL_ERROR") { %>
<b><bean:message key="solution.status.INTERNAL_ERROR" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<p><bean:message key="solution.stackTrace" /><br>
<textarea class="x-form-text" readonly="true" style="width:100%;height:250px"><%=solutionsForm.getStatusMessage()%></textarea>
<% } %>

<% if(status == "DISQUALIFIED") { %>
<b><bean:message key="solution.status.DISQUALIFIED" /></b>
<% } %>

<% if(status == "COMPILING") { %>
<b><bean:message key="solution.status.COMPILING" /></b>
<% } %>

<% if(status == "COMPILATION_ERROR") {%>
<b><bean:message key="solution.status.COMPILATION_ERROR" /></b>
<p><bean:message key="solution.compilerOutput" /><br>
<textarea class="x-form-text" readonly="true" style="width:100%;height:250px"><%=solutionsForm.getStatusMessage()%></textarea>
<% } %>

<% if(status == "RUNNING") { %>
<b><bean:message key="solution.status.RUNNING" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "RUNTIME_ERROR") { %>
<b><bean:message key="solution.status.RUNTIME_ERROR" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "MEMORY_LIMIT") { %>
<b><bean:message key="solution.status.MEMORY_LIMIT" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "TIME_LIMIT") { %>
<b><bean:message key="solution.status.TIME_LIMIT" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "OUTPUT_LIMIT") { %>
<b><bean:message key="solution.status.OUTPUT_LIMIT" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "PRESENTATION_ERROR") { %>
<b><bean:message key="solution.status.PRESENTATION_ERROR" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "WRONG_ANSWER") { %>
<b><bean:message key="solution.status.WRONG_ANSWER" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "SECURITY_VIOLATION") { %>
<b><bean:message key="solution.status.SECURITY_VIOLATION" /> 
 <bean:message key="solution.onTest"/> <%=solutionsForm.getCurrentTestNumber()%></b>
<% } %>

<% if(status == "SUCCESS") { %>
<b><bean:message key="solution.status.SUCCESS" /></b>
<% } %>

<p><br><br>
<pre><%=solutionsForm.getSourceCode()%></pre>
</div>
