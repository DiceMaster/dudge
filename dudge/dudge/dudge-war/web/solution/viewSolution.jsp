<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<style type="text/css">
pre.prettyprint {
    border: 1px solid #ccc;
    margin-bottom: 0;
    padding: 9.5px;
}
li.L0, li.L1, li.L2, li.L3,
li.L5, li.L6, li.L7, li.L8 {
    list-style-type: decimal !important
}
</style>

<script type="text/javascript" src="prettify/run_prettify.js"></script>
<script type="text/javascript">
    $( document ).ready(function() {
        var submitTime = new Date(${solutionsForm.submitTime});
        $('#submitTime').text(submitTime.toLocaleString());

        var responseFunction = function(status) {
            var statusBlock = $("#statusBlock");
            var statusMessageBlock = $('#statusMessageBlock');
            var throbber = $('#throbber');
            statusMessageBlock.hide();
            throbber.hide();
            statusBlock.removeClass();
 
            switch (status.status) {
                case 'NEW':
                    throbber.show();
                    statusBlock.addClass('alert alert-info');
                    $('#statusElement').text('<bean:message key="solution.status.NEW" />');
                    break;
                case 'INTERNAL_ERROR':
                    statusBlock.addClass('alert alert-danger');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.INTERNAL_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    statusMessageBlock.show();
                    $('#statusMessageTitle').text('<bean:message key="solution.stackTrace" />');
                    $('#statusMessageText').text(status.statusMessage);
                    break;
                case 'DISQUALIFIED':
                    statusBlock.addClass('alert alert-danger');
                    $('#statusElement').text('<bean:message key="solution.status.DISQUALIFIED" />');
                    break;
                case 'COMPILING':
                    throbber.show();
                    statusBlock.addClass('alert alert-info');
                    $('#statusElement').text('<bean:message key="solution.status.COMPILING" />');
                    break;
                case 'COMPILATION_ERROR':
                    statusBlock.addClass('alert alert-danger');
                    $('#statusElement').text('<bean:message key="solution.status.COMPILATION_ERROR" />');
                    statusMessageBlock.show();
                    $('#statusMessageTitle').text('<bean:message key="solution.compilerOutput" />');
                    $('#statusMessageText').text(status.statusMessage);
                    break;
                case 'RUNNING':
                    throbber.show();
                    statusBlock.addClass('alert alert-info');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.RUNNING" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;
                case 'RUNTIME_ERROR':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.RUNTIME_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;  
                case 'MEMORY_LIMIT':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.MEMORY_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;
                case 'TIME_LIMIT':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.TIME_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;    
                case 'OUTPUT_LIMIT':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.OUTPUT_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;
                case 'PRESENTATION_ERROR':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.PRESENTATION_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;
                case 'WRONG_ANSWER':
                    statusBlock.addClass('alert alert-warning');
                    $('#statusElement').text(
                        '<bean:message key="solution.status.WRONG_ANSWER" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber
                    );
                    break;
                case 'SECURITY_VIOLATION':
                    statusBlock.addClass('alert alert-danger');
                    $('#statusElement').text('<bean:message key="solution.status.SECURITY_VIOLATION" />');
                    break;
                case 'SUCCESS':
                    statusBlock.addClass('alert alert-success');
                    $('#statusElement').text('<bean:message key="solution.status.SUCCESS" />');
                    break;
            }

            if (status.status === 'NEW' ||
                status.status === 'COMPILING' ||
                status.status === 'RUNNING') {
                setTimeout(requestFunction, 2000);
            }
        };      
        
        var requestFunction = function() {
            $.getJSON(
                "solutions.do",
                {
                    reqCode: 'getSolutionStatus',
                    solutionId: ${solutionsForm.solutionId}
                },
                responseFunction
            );
        };
        
        var initialStatusMessage = $('#initialStatusMessageText').text();
        responseFunction({ status: '${solutionsForm.status}', currentTestNumber: '${solutionsForm.currentTestNumber}', statusMessage: initialStatusMessage });
    });

</script>

<a href="solutions.do?reqCode=submit&contestId=${contestId}"><bean:message key="solution.submitAnother"/></a>
<h1><bean:message key="solution.solution" /> ${solutionsForm.solutionId}</h1>
<p><strong><bean:message key="problem.problem" /></strong>: ${solutionsForm.problemName}</p>
<p><strong><bean:message key="solution.author" /></strong>: <a href="users.do?reqCode=view&login=${solutionsForm.userId}">${solutionsForm.userId}</a></p>
<p><strong><bean:message key="solution.time" /></strong>: <span id="submitTime"></span></p>
<div id="statusBlock" class="alert alert-info">
    <strong><bean:message key="solution.status" />:</strong>
    <img id="throbber" src="img/ajax-loader.gif" />
    <span id="statusElement"></span>
</div>
<div id="statusMessageBlock">    
    <h2 id="statusMessageTitle"></h2>
    <pre id="statusMessageText"></pre>
    <div hidden="true" id="initialStatusMessageText"><c:out value="${solutionsForm.statusMessage}" escapeXml="true"/></div>
</div>
<h2><bean:message key="solution.source" /></h2>
<pre class="prettyprint linenums"><c:out value="${solutionsForm.sourceCode}" escapeXml="true"/></pre>
