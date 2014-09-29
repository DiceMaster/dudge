<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $("#navbarProblems").addClass("active");

        $('#problemsGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "sServerMethod": "POST",
            "bStateSave": true,
            "iDisplayLength": 25,
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 4 ] }
            ],
            "aaSorting": [[0, "desc"]],
            "sAjaxSource": "problems.do?reqCode=getProblemList",
            "sPaginationType": "bootstrap",
            "sDom": '<"row"<"pull-left"p><"pull-right"f><"clearfix">>rt<"row"<"col-lg-8"p><"col-lg-4"i>>',
            "oLanguage": {
                "sUrl": "l18n/<bean:message key="locale.currentTag"/>.txt"
            },
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(1)', nRow).html( '<a href="problems.do?reqCode=view&problemId=' + aData[0] + '">' + aData[1] +'</a>' );
                $('td:eq(3)', nRow).html( (new Date(aData[3])).toLocaleString() );
            }
        });
    });
</script>

<h1><bean:message key="problems.problems"/></h1>
<c:if test="${permissionCheckerRemote.canAddProblem(autentificationObject.username)}">
<div class="btn-group">
    <a class="btn btn-default" href="problems.do?reqCode=create"><bean:message key="problem.newProblem" /></a>
</div>
</c:if>
<table class="table" id="problemsGrid">
    <thead>
        <tr>
            <th><bean:message key="problem.id"/></th>
            <th><bean:message key="problem.title" /></th>
            <th><bean:message key="problem.owner" /></th>
            <th><bean:message key="problem.created" /></th>
            <th><bean:message key="problem.status" /></th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>
