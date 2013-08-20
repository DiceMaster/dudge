<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
     $(document).ready(function() {
        $('#userGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "bFilter": false,
            "bLengthChange": false,
            "bInfo": false,
            "sAjaxSource": "users.do?reqCode=getUserList",
            "sPaginationType": "bootstrap",
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[0] + '">' + aData[0] +'</a>' );
            }
        });
    });
</script>

<h1><bean:message key="users.users"/></h1>

<table class="table table-hover" id="userGrid">
    <thead>
        <tr>
            <th><bean:message key="user.login"/></th>
            <th><bean:message key="user.regDate" /></th>
            <th><bean:message key="user.realName" /></th>
            <th><bean:message key="user.organization" /></th>
        </tr>
    </thead>
    <tbody>       
    </tbody>
</table>