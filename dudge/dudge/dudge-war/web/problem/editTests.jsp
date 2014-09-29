<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<script src="scripts/editTests.js"></script>
<script>
    $(document).ready(function() {
        var testList = ${problemsForm.encodedTestList};
        var l10n = {
            loadErrorText: '<bean:message key="problem.tests.cantLoad" />',
            saveErrorText: '<bean:message key="problem.tests.cantSave" />',
            removeErrorText: '<bean:message key="problem.tests.cantRemove" />',
            savedText: '<bean:message key="problem.tests.saved" />',
            saveText: '<bean:message key="problem.tests.save" />',
            savingText: '<bean:message key="problem.tests.saving" />',
            tryAgainText: '<bean:message key="problem.tests.tryAgain" />',
            testText: '<bean:message key="problem.test" />',
            removeText: '<bean:message key="problem.tests.remove" />',
            removingText: '<bean:message key="problem.tests.removing" />'
        };
        initTests(testList, $("#tests .panel"), l10n);
    });
</script>

<div class="pull-left">
    <h1><bean:message key="problem.testsForProblem" /> ${problemsForm.problemId}: ${problemsForm.title}</h1>
</div>
<div class="pull-right">
    <div class="btn-group dudge-btn-group">
        <a role="button" href="problems.do?reqCode=view&problemId=${problemsForm.problemId}" class="btn btn-default dudge-tests-return dudge-top-margin-12">
            <bean:message key="problem.tests.return" />
        </a>
    </div>
</div>
<div class="clearfix"></div>
<div class="modal fade" id="confirmReturn">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><bean:message key="problem.tests.cancel"/></span></button>
                <h4 class="modal-title"><bean:message key="problem.tests.confirmReturnTitle" /></h4>
            </div>
            <div class="modal-body">
                <p><bean:message key="problem.tests.confirmReturnMsg" /></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="problem.tests.cancel"/></button>
                <a role="button" href="problems.do?reqCode=view&problemId=${problemsForm.problemId}" class="btn btn-default"><bean:message key="problem.tests.returnConfirm"/></a>
            </div>
        </div>
    </div>
</div>
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
                <img class="dudge-test-load-throbber hidden" src="img/ajax-loader-small.gif" width="16" height="16"/>
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
                    <textarea class="form-control no-resize dudge-monospace dudge-test-input" rows="10" disabled></textarea>
                </div>
                <div class="col-md-6">
                    <h3 class="dudge-no-top-margin"><bean:message key="problem.output" /></h3>
                    <textarea class="form-control no-resize dudge-monospace dudge-test-output" rows="10" disabled></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="pull-left">
    <button type="button" class="btn btn-default dudge-top-margin-12" id="addTest">
        <img id="throbberAdd" class="hidden" src="img/ajax-loader-small.gif" width="16" height="16"/>
        &nbsp<bean:message key="problem.tests.add"/>
    </button>
    <span id="addError" class="hidden"><bean:message key="problem.tests.cantAdd"/></span>
</div>
<div class="pull-right">
    <a role="button" href="problems.do?reqCode=view&problemId=${problemsForm.problemId}" class="btn btn-default dudge-tests-return dudge-top-margin-12">
        <bean:message key="problem.tests.return" />
    </a>
</div>