<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#userGrid').dataTable( {
            "bProcessing": true,
            "bServerSide": true,
            "bLengthChange": false,
            "iDisplayLength ": 25,
            "sAjaxSource": "users.do?reqCode=getUserList",
            "sPaginationType": "bootstrap",
            "sDom": '<"row"<"pull-left"p><"pull-right"f><"clearfix">>rt<"row"<"col-lg-8"p><"col-lg-4"i>>',
            "oLanguage": {
                "sUrl": "l18n/${pageContext.response.locale}.txt"
            },
            "fnCreatedRow": function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[0] + '">' + aData[0] +'</a>' );
            }
        });

        $("#navbarUsers").addClass("active");
    });
</script>

<h1><bean:message key="users.users"/></h1>

<table class="table" id="userGrid">
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