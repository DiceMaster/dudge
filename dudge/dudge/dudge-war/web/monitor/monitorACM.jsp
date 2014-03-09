<jsp:useBean id="monitorForm" class="dudge.web.forms.MonitorForm" scope="session" />

<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        var monitorTable = $('#monitorGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "bSort" : false,
            "sServerMethod": "POST",
            "iDisplayLength": 25,
            "sAjaxSource": "monitor.do?reqCode=getAcmMonitorData&contestId=${monitorForm.contestId}",
            "sDom": 'rt',
            "oLanguage": {
                "sUrl": "l18n/${pageContext.response.locale}.txt"
            },
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(1)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[1] + '">' + aData[1] +'</a>' );
            }
        });
 
        setInterval(function(){
            monitorTable.fnReloadAjax();
        }, 60000);
    });
 </script>

<table class="table" id="monitorGrid">
    <thead>
        <tr>
            <th><bean:message key="monitor.place"/></th>
            <th><bean:message key="monitor.user"/></th>
            <th><bean:message key="monitor.solvedProblems"/></th>
<c:forEach items="${monitorForm.problems}" var="contestProblem">
            <th>${contestProblem.problemMark}</th>
</c:forEach>            
            <th><bean:message key="monitor.time" /></th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>
