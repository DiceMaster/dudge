<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 
<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $("#navbarStatus").addClass("active");
        
        var table = $('#statusGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "sServerMethod": "POST",
            "bSort" : false,
            "bStateSave": true,
            "iDisplayLength": 25,
            "sAjaxSource": "solutions.do?reqCode=getLastSolutions",
            "sPaginationType": "bootstrap",
            "sDom": '<"row"p>rt<"row"<"col-lg-8"p><"col-lg-4"i>>',
            "oLanguage": {
                "sUrl": "l18n/${pageContext.response.locale}.txt"
            },
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 3, 5, 9 ] }
            ],
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="solutions.do?reqCode=view&solutionId=' + aData[0] + '">' + aData[0] +'</a>' );
                $('td:eq(1)', nRow).html( new Date(aData[1]).toLocaleString() );
                $('td:eq(2)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[2] + '">' + aData[2] +'</a>' );
                $('td:eq(3)', nRow).html( '<a href="contests.do?reqCode=view&contestId=' + aData[3] + '">' + aData[4] +'</a>' );
                $('td:eq(4)', nRow).html( '<a href="problems.do?reqCode=view&contestId=' + aData[3] + '&problemId=' + aData[5] + '">' + aData[6] +'</a>' );
                
                var status;
                switch (aData[8]) {
                    case "NEW":
                        status = '<bean:message key="solution.status.NEW"/>';
                        break;
                    case "INTERNAL_ERROR":
                        status = '<bean:message key="solution.status.INTERNAL_ERROR"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "DISQUALIFIED":
                        status = '<bean:message key="solution.status.DISQUALIFIED"/>';
                        break;
                    case "COMPILING":
                        status = '<bean:message key="solution.status.COMPILING"/>';
                        break;
                    case "COMPILATION_ERROR":
                        status = '<bean:message key="solution.status.COMPILATION_ERROR"/>';
                        break;
                    case "RUNNING":
                        status = '<bean:message key="solution.status.RUNNING"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "RUNTIME_ERROR":
                        status = '<bean:message key="solution.status.RUNTIME_ERROR"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "MEMORY_LIMIT":
                        status = '<bean:message key="solution.status.MEMORY_LIMIT"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "TIME_LIMIT":
                        status = '<bean:message key="solution.status.TIME_LIMIT"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "OUTPUT_LIMIT":
                        status = '<bean:message key="solution.status.OUTPUT_LIMIT"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "PRESENTATION_ERROR":
                        status = '<bean:message key="solution.status.PRESENTATION_ERROR"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "WRONG_ANSWER":
                        status = '<bean:message key="solution.status.WRONG_ANSWER"/> ' +
                                 '<bean:message key="solution.onTest"/> ' + aData[9];
                        break;
                    case "SECURITY_VIOLATION":
                        status = '<bean:message key="solution.status.SECURITY_VIOLATION"/>';
                        break;
                    case "SUCCESS":
                        status = '<bean:message key="solution.status.SUCCESS"/>';
                        break;
                    default:
                        status = "";
                }
                $('td:eq(6)', nRow).html( status );
            }
        });
    });
</script>

<h1><bean:message key="status.status" /></h1>

<table class="table" id="statusGrid">
    <thead>
        <tr>
            <th><bean:message key="solution.id"/></th>
            <th><bean:message key="solution.time"/></th>
            <th><bean:message key="user.user" /></th>
            <th>Contest ID</th>
            <th><bean:message key="contest.contest" /></th>
            <th>Problem ID</th>
            <th><bean:message key="problem.problem" /></th>
            <th><bean:message key="language.language" /></th>
            <th><bean:message key="solution.status" /></th>
            <th>On test</th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>

