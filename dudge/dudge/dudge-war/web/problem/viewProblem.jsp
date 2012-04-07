<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<c:set var="problemId" value="${problemsForm.problemId}" scope="session"/>

<script type="text/javascript" src="scripts/json.js"></script>

<script type="text/javascript">
    Ext.onReady(function(){	
        var buttonsToolbar = Ext.getCmp('content-panel').getTopToolbar();
	
    <c:if test="${permissionCheckerRemote.canSubmitSolution(autentificationObject.username, contestId, problemId)}">
            buttonsToolbar.addButton({
                text: '<bean:message key="problem.submitSolution" />',
                handler: function() {
                    window.location = "solutions.do?reqCode=submit&contestId=${contestId}&problemId=${problemId}";
                }
            });
    </c:if>

    <c:if test="${permissionCheckerRemote.canModifyProblem(autentificationObject.username, problemId)}">
            buttonsToolbar.addButton(
            {
                text: '<bean:message key="problem.edit" />',
                handler: function()
                {
                    location.href = "problems.do?reqCode=edit&problemId=${problemId}";
                }
            }
        );
    </c:if>

    <c:if test="${permissionCheckerRemote.canDeleteProblem(autentificationObject.username, problemId)}">
            buttonsToolbar.addButton(
            {
                text: '<bean:message key="problem.delete" />',
                handler: function()
                {
                    function commitDelete (btn) 
                    {
                        if (btn == 'yes') 
                        {			
                            problemId = ${problemId};
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
    </c:if>

        });
</script>

<div id="centerPanel">
    <html:form action="problems" method="GET">
        <h3 align="center" style="font-size:x-large"><bean:message key="problem.problem" /> ${problemsForm.problemId}: ${problemsForm.title}</h3>
        <p align="center" class="problem_info" >
            <bean:message key="problem.owner" />: <a href="users.do?reqCode=view&login=${problemsForm.owner}">${problemsForm.owner}</a>
            | <bean:message key="problem.author" />: ${problemsForm.author}<br>
            <bean:message key="problem.created" /> ${problemsForm.createTime}<br>
            <bean:message key="problem.cpuTimeLimit" />: ${problemsForm.cpuTimeLimit} <bean:message key="problem.cpuMetric" /><br>
            <bean:message key="problem.memoryLimit" />: ${problemsForm.memoryLimit / (1024 * 1024)} <bean:message key="problem.memoryMetric" /><br>
            <bean:message key="problem.outputLimit" />: ${problemsForm.outputLimit / (1024 * 1024)} <bean:message key="problem.memoryMetric" /><br>
        </p><br>
        <p class="problem_info">${problemsForm.description}</p>

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
                    <td width="50%" valign="top"><pre>${problemsForm.exampleInputTest}</pre></td>
                    <td width="50%" valign="top"><pre>${problemsForm.exampleOutputTest}</pre></td>
                </tr>
            </tbody>
        </table>
    </p>

</html:form>
</div>