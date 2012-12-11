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

<a href="solutions.do?reqCode=submit&contestId=${contestId}"><bean:message key="solution.submitAnother"/></a>
<p><br>

<div id="solutionContent">

    <h3><bean:message key="solution.solution" /> ${solutionsForm.solutionId}</h3>
    <bean:message key="solution.status" />: 

    <c:if test="${solutionsForm.status eq 'NEW'}">
        <b><bean:message key="solution.status.NEW" /></b>
    </c:if>

    <c:if test="${solutionsForm.status eq 'INTERNAL_ERROR'}">
        <b><bean:message key="solution.status.INTERNAL_ERROR" /> 
            <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
        <p><bean:message key="solution.stackTrace" /><br>
            <textarea class="x-form-text" readonly="true" style="width:100%;height:250px">${solutionsForm.statusMessage}</textarea>
        </c:if>

        <c:if test="${solutionsForm.status eq 'DISQUALIFIED'}">
            <b><bean:message key="solution.status.DISQUALIFIED" /></b>
        </c:if>

        <c:if test="${solutionsForm.status eq 'COMPILING'}">
            <b><bean:message key="solution.status.COMPILING" /></b>
        </c:if>

        <c:if test="${solutionsForm.status eq 'COMPILATION_ERROR'}">
            <b><bean:message key="solution.status.COMPILATION_ERROR" /></b>
        <p><bean:message key="solution.compilerOutput" /><br>
            <textarea class="x-form-text" readonly="true" style="width:100%;height:250px">${solutionsForm.statusMessage}</textarea>
        </c:if>

        <c:if test="${solutionsForm.status eq 'RUNNING'}">
            <b><bean:message key="solution.status.RUNNING" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'RUNTIME_ERROR'}">
            <b><bean:message key="solution.status.RUNTIME_ERROR" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'MEMORY_LIMIT'}">
            <b><bean:message key="solution.status.MEMORY_LIMIT" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'TIME_LIMIT'}">
            <b><bean:message key="solution.status.TIME_LIMIT" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'OUTPUT_LIMIT'}">
            <b><bean:message key="solution.status.OUTPUT_LIMIT" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'PRESENTATION_ERROR'}">
            <b><bean:message key="solution.status.PRESENTATION_ERROR" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'WRONG_ANSWER'}">
            <b><bean:message key="solution.status.WRONG_ANSWER" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'SECURITY_VIOLATION'}">
            <b><bean:message key="solution.status.SECURITY_VIOLATION" /> 
                <bean:message key="solution.onTest"/> ${solutionsForm.currentTestNumber}</b>
            </c:if>

        <c:if test="${solutionsForm.status eq 'SUCCESS'}">
            <b><bean:message key="solution.status.SUCCESS" /></b>
        </c:if>

    <p><br><br>
    <textarea readonly wrap="off" style="border: 1; width: 50%; height: 80%; position: absolute;">${solutionsForm.sourceCode}</textarea>
</div>
