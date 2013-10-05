<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<script type="text/javascript">
    Ext.onReady(function() {

        var responseFunction = function(response) {
            var status = Ext.util.JSON.decode(response.responseText);
            var statusMessageBlock = Ext.get('statusMessageBlock');
            var throbber = Ext.get('throbber');
            statusMessageBlock.setVisibilityMode(Ext.Element.DISPLAY);
            statusMessageBlock.hide();
            throbber.setVisibilityMode(Ext.Element.DISPLAY);
            throbber.hide();
 
            switch (status.status) {
                case 'NEW':
                    throbber.show();
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.NEW" /></b>');
                    break;
                case 'INTERNAL_ERROR':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.INTERNAL_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    statusMessageBlock.show();
                    Ext.get('statusMessageTitle').update('<bean:message key="solution.stackTrace" /><br>');
                    Ext.get('statusMessageText').update(status.statusMessage);
                    break;
                case 'DISQUALIFIED':
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.DISQUALIFIED" /></b>');
                    break;
                case 'COMPILING':
                    throbber.show();
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.COMPILING" /></b>');
                    break;
                case 'COMPILATION_ERROR':
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.COMPILATION_ERROR" /></b>');
                    statusMessageBlock.show();
                    Ext.get('statusMessageTitle').update('<bean:message key="solution.compilerOutput" /><br>');
                    Ext.get('statusMessageText').update(status.statusMessage);
                    break;
                case 'RUNNING':
                    throbber.show();
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.RUNNING" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;
                case 'RUNTIME_ERROR':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.RUNTIME_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;  
                case 'MEMORY_LIMIT':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.MEMORY_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;
                case 'TIME_LIMIT':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.TIME_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;    
                case 'OUTPUT_LIMIT':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.OUTPUT_LIMIT" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;
                case 'PRESENTATION_ERROR':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.PRESENTATION_ERROR" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;
                case 'WRONG_ANSWER':
                    Ext.get('statusElement').update(
                        '<b><bean:message key="solution.status.WRONG_ANSWER" /> ' +
                        '<bean:message key="solution.onTest"/> ' + status.currentTestNumber + '</b>'
                    );
                    break;
                case 'SECURITY_VIOLATION':
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.SECURITY_VIOLATION" /> </b>');
                    break;
                case 'SUCCESS':
                    Ext.get('statusElement').update('<b><bean:message key="solution.status.SUCCESS" /></b>');
                    break;
            }

            if (status.status === 'NEW' ||
                status.status === 'COMPILING' ||
                status.status === 'RUNNING') {
                setTimeout(requestFunction, 2000);
            }
        }
        
        var request = {
            url: 'solutions.do',
            params: {
                reqCode: 'getSolutionStatus',
                solutionId: ${solutionsForm.solutionId}
            },
            success: responseFunction
        }
        
        var requestFunction = function() {
            Ext.Ajax.request(request);
        }
        
        responseFunction({responseText: '${solutionsForm.StatusToJSONText()}'});
    });

</script>

<a href="solutions.do?reqCode=submit&contestId=${contestId}"><bean:message key="solution.submitAnother"/></a>
<p><br>

<div id="solutionContent">

    <h3><bean:message key="solution.solution" /> ${solutionsForm.solutionId}</h3>
    <p>
        <bean:message key="solution.status" />:
        <img id="throbber" src="img/ajax-loader.gif" />
        <span id="statusElement"></span>
    </p>
    <div id="statusMessageBlock" style="display:none;">
        <p id="statusMessageTitle"></p>
        <textarea id="statusMessageText" class="x-form-text" readonly="true" style="width:90%;height:250px"></textarea>
    </div>

    <p><br><br></p>
    <textarea readonly wrap="off" style="border: 1; width: 50%; height: 80%; position: absolute;">${solutionsForm.sourceCode}</textarea>
</div>
