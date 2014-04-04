<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#languagesGrid tbody').on('click', 'tr', function( e ) {
            languagesTable.$('tr.selectedRow').removeClass('selectedRow');
            $(this).addClass('selectedRow');
            var anSelected = languagesTable.$('tr.selectedRow');
            if ( anSelected.length !== 0 ) {
                var rowData = languagesTable.fnGetData(anSelected[0]);
                if (rowData[4]) {
                    $('#deleteBtn').removeClass('disabled');
                    $('#deleteDlgBtn').attr('href', 'languages.do?reqCode=delete&id=' + rowData[0]);
                } else {
                    $('#deleteBtn').addClass('disabled');
                    $('#deleteDlgBtn').attr('href', '#');
                }
                if (rowData[3]) {
                    $('#editBtn').removeClass('disabled');
                    $('#editBtn').attr('href', 'languages.do?reqCode=edit&languageId=' + rowData[0]);
                } else {
                    $('#editBtn').addClass('disabled');
                    $('#editBtn').attr('href', '#');
                }    
            }
        });
        
        var languagesTable = $('#languagesGrid').dataTable( {
            'bProcessing': true,
            'bServerSide': true,
            'bSort' : false,
            'sServerMethod': 'POST',
            'sAjaxSource': 'languages.do?reqCode=getLanguagesList',
            'sDom': 'rt',
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 3, 4 ] }
            ],
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            }
        });
    });
</script>

<h1><bean:message key="languages.languages"/></h1>
<div class="btn-group">
<c:if test="${permissionCheckerRemote.canAddLanguage(autentificationObject.username)}">
    <a class="btn btn-default" href="languages.do?reqCode=create"><bean:message key="languages.create" /></button>
</c:if>
    <a id="editBtn" class="btn btn-default disabled" role="button" href="#"><bean:message key="languages.edit"/></a>
    <div class="modal" id="deleteLanguage" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
              <h4 class="modal-title"><bean:message key="language.confirmDeleteTitle"/></h4>
            </div>
            <div class="modal-body">
                <bean:message key="language.confirmDeleteMsg"/>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="contest.cancel"/></button>
              <a id="deleteDlgBtn" href="#" class="btn btn-danger"><bean:message key="problem.delete"/></a>
            </div>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <a class="btn btn-default disabled" id="deleteBtn" role="button" href="#" data-toggle="modal" data-target="#deleteLanguage"><bean:message key="languages.delete"/></a>        
</div>
<div class="clearfix"></div>
<table class="table table-hover " id="languagesGrid">
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
