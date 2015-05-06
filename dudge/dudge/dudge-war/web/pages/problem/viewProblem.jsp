<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<h1 class="pull-left"><bean:message key="problem.problem" /> ${problemsForm.problemId}: ${problemsForm.title}</h1>
<div class="pull-right">
<c:if test="${permissionCheckerRemote.canModifyProblem(autentificationObject.username, problemsForm.problemId)}">    
    <div class="btn-group dudge-btn-group">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
            <bean:message key="problem.editMenu"/>
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li><a href="problems.do?reqCode=edit&problemId=${problemsForm.problemId}"><bean:message key="problem.edit"/></a></li>
            <li><a href="problems.do?reqCode=editTests&problemId=${problemsForm.problemId}"><bean:message key="problem.editTests"/></a></li>
    <c:if test="${permissionCheckerRemote.canDeleteProblem(autentificationObject.username, problemsForm.problemId)}">
            <li class="divider"></li>
            <li><a href="#" data-toggle="modal" data-target="#deleteProblem"><bean:message key="problem.delete"/></a></li>
    </c:if>            
        </ul>
    </div>
    <div class="modal" id="deleteProblem" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
              <h4 class="modal-title"><bean:message key="problem.confirmDeleteTitle"/></h4>
            </div>
            <div class="modal-body">
                <bean:message key="problem.confirmDeleteMsg"/>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="contest.cancel"/></button>
              <a href="problems.do?reqCode=delete&problemId=${problemsForm.problemId}" class="btn btn-danger"><bean:message key="problem.delete"/></a>
            </div>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</c:if>    
</div>
<div class="clearfix"></div>
<c:if test="${permissionCheckerRemote.canAdmin(autentificationObject.username)}">
    <div><p><a href="solutions.do?reqCode=problemSolutions&contestId=${contestId}&problemId=${problemsForm.problemId}"><bean:message key="problem.problemSolutions"/></a></p></div>
</c:if>
<c:if test="${permissionCheckerRemote.canSubmitSolution(autentificationObject.username, contestId, problemsForm.problemId)}">            
<a class="btn btn-default" href="solutions.do?reqCode=submit&contestId=${contestId}&problemId=${problemsForm.problemId}"><bean:message key="problem.submitSolution"/></a>
</c:if>

<dl class="dl-horizontal dl-dudge-wide">
    <dt><bean:message key="problem.owner" /></dt>
    <dd><a href="users.do?reqCode=view&login=${problemsForm.owner}">${problemsForm.owner}</a></dd>
		
    <dt><bean:message key="problem.author" /></dt>
    <dd>${problemsForm.author}</dd>

    <dt><bean:message key="problem.created" /></dt>
    <dd>${problemsForm.createTime}</dd>

    <dt><bean:message key="problem.cpuTimeLimit" /></dt>
    <dd>${problemsForm.cpuTimeLimit} <bean:message key="problem.cpuMetric" /></dd>

    <dt><bean:message key="problem.memoryLimit" /></dt>
    <dd>${problemsForm.memoryLimit / (1024 * 1024)} <bean:message key="problem.memoryMetric" /></dd>

    <dt><bean:message key="problem.outputLimit" /></dt>
    <dd>${problemsForm.outputLimit / (1024 * 1024)} <bean:message key="problem.memoryMetric" /></dd>
</dl>
<h2><bean:message key="problem.description" /></h2>
<p class="problem_info">${problemsForm.description}</p>

<h2><bean:message key="problem.example"/></h2>
<div class="col-lg-6">
    <h3><bean:message key="problem.input" /></h3>
    <pre>${problemsForm.exampleInputTest}</pre>
</div>
<div class="col-lg-6">
    <h3><bean:message key="problem.output" /></h3>
    <pre>${problemsForm.exampleOutputTest}</pre>
</div>