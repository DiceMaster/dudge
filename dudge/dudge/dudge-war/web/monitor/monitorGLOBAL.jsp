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
            "sAjaxSource": "monitor.do?reqCode=getGlobalMonitorData&contestId=${monitorForm.contestId}",
            "sDom": 'rt',
            "oLanguage": {
                "sUrl": "l18n/${pageContext.response.locale}.txt"
            },
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(1)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[1] + '">' + aData[1] +'</a>' );
            }
        });
        
        monitorTable.on('xhr', function ( e, o, json ) {
            if (json.frozen) {
                $("#alertFrozen").show();
            } else {
                $("#alertFrozen").hide();
            }
            
            var updatedTime = $("#updatedTime");
            var time = new Date(json.updateTime);
            updatedTime.text('<bean:message key="monitor.updated"/>: ' + time.toLocaleString());
            updatedTime.show();
        } );
 
        setInterval(function(){
            monitorTable.fnReloadAjax();
        }, 60000);
    });
</script>

<h1>${monitorForm.contestCaption}</h1>
<div class="alert alert-warning" id="alertFrozen" hidden="true"><bean:message key="monitor.frozen"/></div>
<div id="updatedTime" hidden="true"></div>
<table class="table" id="monitorGrid">
    <thead>
        <tr>
            <th><bean:message key="monitor.place"/></th>
            <th><bean:message key="monitor.user"/></th>
            <th><bean:message key="monitor.solvedProblems"/></th>
            <th><bean:message key="monitor.rating"/></th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>
