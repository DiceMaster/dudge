<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="page" />
<jsp:useBean id="problemsAction" scope="page" class="dudge.web.actions.ProblemsAction" />

<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        var table = $('#contestGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "sServerMethod": "POST",
            "bStateSave": true,
            "iDisplayLength": 25,
            "sAjaxSource": "contests.do?reqCode=getContestList",
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 0, 5, 6, 7, 8 ] }
            ],
            "sPaginationType": "bootstrap",
            "sDom": '<"row"<"pull-left"p><"pull-right"f><"clearfix">>rt<"row"<"col-lg-8"p><"col-lg-4"i>>',
            "oLanguage": {
                "sUrl": "l18n/${pageContext.response.locale}.txt"
            },
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="contests.do?reqCode=view&contestId=' + aData[0] + '">' + aData[1] +'</a>' );
            }
        });
        table.fnSort([[4, 'desc']]);

        $("#navbarContests").addClass("active");
    });
</script>

<h1><bean:message key="contests.contests"/></h1>

<table class="table" id="contestGrid">
    <thead>
        <tr>
            <th>ID</th>
            <th><bean:message key="contests.caption"/></th>
            <th><bean:message key="contests.type" /></th>
            <th><bean:message key="contest.isOpen" /></th>
            <th><bean:message key="contests.starts" /></th>
            <th>Duration</th>
            <th>Can join</th>
            <th>Can modify</th>
            <th>Can delete</th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>
