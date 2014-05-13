<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {        
        var languagesTable = $('#languagesGrid').dataTable( {
            'bProcessing': true,
            'bServerSide': true,
            'bSort' : false,
            'sServerMethod': 'POST',
            'sAjaxSource': 'languages.do?reqCode=getLanguagesList',
            'sDom': 'rt',
            'bPaginate': false,
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 3, 4 ] }
            ],
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="languages.do?reqCode=view&languageId=' + aData[0] + '">' + aData[0] +'</a>' );
            }
        });
    });
</script>

<h1><bean:message key="languages.languages"/></h1>
<c:if test="${permissionCheckerRemote.canAddLanguage(autentificationObject.username)}">
<div class="btn-group">
    <a class="btn btn-default" href="languages.do?reqCode=create"><bean:message key="languages.create" /></a>
</div>
</c:if>
<table class="table" id="languagesGrid">
    <thead>
        <tr>
            <th><bean:message key="language.id"/></th>
            <th><bean:message key="language.title"/></th>
            <th><bean:message key="language.description"/></th>
            <th>Editable</th>
            <th>Deletable</th>
        </tr>
    </thead>
    <tbody>
    </tbody>
</table>
