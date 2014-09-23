<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<script src="scripts/editTests.js"></script>
<script>
    $(document).ready(function() {
        var testList = ${problemsForm.encodedTestList};
        initTests(testList, $("#tests .panel"), {
            loadingErrorText: '<bean:message key="problem.tests.cantLoad" />',
            savingErrorText: '<bean:message key="problem.tests.cantSave" />',
            savedText: '<bean:message key="problem.tests.saved" />',
            saveText: '<bean:message key="problem.tests.save" />',
            savingText: '<bean:message key="problem.tests.saving" />',
            tryAgainText: '<bean:message key="problem.tests.tryAgain" />',
            testText: '<bean:message key="problem.test" />'
        });
    });
</script>


<h1><bean:message key="problem.testsForProblem" /> ${problemsForm.problemId}: ${problemsForm.title}</h1>
<div class="modal fade" id="removeDialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><bean:message key="problem.tests.cancel"/></span></button>
                <h4 class="modal-title"><bean:message key="problem.tests.confirmDeleteTitle" /></h4>
            </div>
            <div class="modal-body">
                <p>
                    <bean:message key="problem.tests.confirmDeleteMsg" />
                    &nbsp<span id="testIndex">1</span>?
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="problem.tests.cancel"/></button>
                <button type="button" id="removeTest" class="btn btn-danger" data-dismiss="modal"><bean:message key="problem.tests.remove"/></button>
            </div>
        </div>
    </div>
</div>
<div class="panel-group" id="tests">
    <div class="panel panel-default hidden" id="testTemplate">
        <div class="panel-heading clearfix">
            <h4 class="panel-title dudge-panel-vertical-center pull-left">
                <a data-toggle="collapse" class="dudge-test-header" href="#">
                    <span class="dudge-test-name"><bean:message key="problem.test"/> 1</span>
                    <span class="caret"></span>
                </a>
            </h4>
            <div class="pull-right">
                <span class="dudge-test-error hidden"><bean:message key="problem.tests.cantSave"/></span>
                <button type="button" class="btn btn-default btn-sm dudge-test-save">
                    <img class="dudge-test-throbber hidden" src="img/ajax-loader-small.gif" width="16" height="16"/>
                    &nbsp;<span class="dudge-test-save-text"><bean:message key="problem.tests.saved"/></span>
                </button>
                <button type="button" class="btn btn-default btn-sm dudge-test-remove">
                    <img class="dudge-test-remove-throbber hidden" src="img/ajax-loader-small.gif" width="16" height="16"/>
                    &nbsp<bean:message key="problem.tests.remove"/>
                </button>
            </div>
        </div>
        <div class="panel-collapse collapse">
            <div class="panel-body">
                <div class="col-md-6">
                    <h3 class="dudge-no-top-margin"><bean:message key="problem.input" /></h3>
                    <textarea class="form-control no-resize dudge-monospace dudge-test-input" rows="15" disabled></textarea>
                </div>
                <div class="col-md-6">
                    <h3 class="dudge-no-top-margin"><bean:message key="problem.output" /></h3>
                    <textarea class="form-control no-resize dudge-monospace dudge-test-output" rows="15" disabled></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<button type="button" class="btn btn-default" id="addTest">
    <img id="throbberAdd" class="hidden" src="img/ajax-loader-small.gif" width="16" height="16"/>
    &nbsp<bean:message key="problem.tests.add"/>
</button>
<span id="addError" class="hidden"><bean:message key="problem.tests.cantAdd"/></span>
