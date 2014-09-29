<%@page import="dudge.db.Language"%>

<jsp:useBean id="languagesForm" class="dudge.web.forms.LanguagesForm" scope="session" />

<c:choose>
    <c:when test="${permissionCheckerRemote.canModifyLanguage(autentificationObject.username)}">
<form action="languages.do" method="GET">
    <input type="hidden" name="reqCode" value="edit">
    <input type="hidden" name="languageId" value="${languagesForm.languageId}">
    <h1 class="pull-left"><bean:message key="language.language" /> ${languagesForm.languageId}</h1>
    <div class="pull-right">
        <div class="btn-group dudge-btn-group">
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteLanguage"><bean:message key="languages.delete"/></button>
            <!-- Modal -->
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
                    <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="user.cancel"/></button>
                    <a href="languages.do?reqCode=delete&languageId=${languagesForm.languageId}" class="btn btn-danger"><bean:message key="languages.delete"/></a>
                  </div>
                </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->            
            <button type="submit" class="btn btn-primary"><bean:message key="language.editProperties"/></button>
        </div>
    </div>
    <div class="clearfix"></div>
</form>
    </c:when>
    <c:otherwise>
<h1><bean:message key="language.language" /> ${languagesForm.languageId}</h1>
    </c:otherwise>
</c:choose>

<h3><bean:message key="language.languageInfo" /></h3>
<hr>
<dl class="dl-horizontal dl-dudge-small">
<c:if test="${not empty languagesForm.title}">
    <dt><bean:message key="language.title" /></dt>
    <dd>${languagesForm.title}</dd>
</c:if>
    <dt><bean:message key="language.description" /></dt>
    <dd>${languagesForm.description}</dd>
</dl>
<h3><bean:message key="language.technicalParameters" /></h3>
<hr>
<dl class="dl-horizontal dl-dudge-small">
    <dt><bean:message key="language.fileExtension" /></dt>
    <dd>${languagesForm.fileExtension}</dd>
    <dt><bean:message key="language.compilationCommand" /></dt>
    <dd>${languagesForm.compilationCommand}</dd>
    <dt><bean:message key="language.executionCommand" /></dt>
    <dd>${languagesForm.executionCommand}</dd>
</dl>
