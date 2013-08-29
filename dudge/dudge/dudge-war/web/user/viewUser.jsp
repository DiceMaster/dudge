<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" scope="session" class="dudge.web.actions.UsersAction" />

<script>
    $("#navbarUsers").addClass("active");
</script>

<c:if test="${permissionCheckerRemote.canModifyUser(autentificationObject.username, requestScope.login)}">
<form action="users.do" method="GET">
    <input type="hidden" name="reqCode" value="edit">
    <input type="hidden" name="login" value="${usersForm.login}">
</c:if>
    <h1 class="pull-left"><bean:message key="user.user" /> ${usersForm.login}</h1>
<c:if test="${permissionCheckerRemote.canModifyUser(autentificationObject.username, requestScope.login)}">        
    <div class="pull-right">
        <div class="btn-group dudge-btn-group">
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteUser"><bean:message key="users.delete"/></button>
            <!-- Modal -->
            <div class="modal" id="deleteUser" tabindex="-1" role="dialog" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title"><bean:message key="user.confirmDeleteTitle"/></h4>
                  </div>
                  <div class="modal-body">
                      <bean:message key="user.confirmDeleteMsg"/>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="user.cancel"/></button>
                    <a href="users.do?reqCode=delete&login=${usersForm.login}" class="btn btn-danger"><bean:message key="users.delete"/></a>
                  </div>
                </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            
            <button type="submit" class="btn btn-primary"><bean:message key="user.editProperties"/></button>
        </div>
    </div>
    <div class="clearfix"></div>
</form>
</c:if>

<h3><bean:message key="user.userInfo" /></h3>
<hr>

<dl class="dl-horizontal dl-dudge"> 
<c:if test="${not empty usersForm.realName}">
    <dt><bean:message key="user.realName" /></dt>
    <dd>${usersForm.realName}</dd>
</c:if>
    <dt><bean:message key="user.regDate" /></dt>
    <dd>${usersForm.regDate}</dd>
<c:if test="${not empty usersForm.organization}">
    <dt><bean:message key="user.organization" /></dt>
    <dd>${usersForm.organization}</dd>
</c:if>
<c:if test="${not empty usersForm.age}">
    <dt><bean:message key="user.age" /></dt>
    <dd>${usersForm.age}</dd>
</c:if>
<c:if test="${not empty usersForm.jabberId}">
    <dt><bean:message key="user.jabberId" /></dt>
    <dd>${usersForm.jabberId}</dd>
</c:if>
<c:if test="${not empty usersForm.icqNumber}">
    <dt><bean:message key="user.icqNumber" /></dt>
    <dd>${usersForm.icqNumber}</dd>
</c:if>

    <dt><bean:message key="user.totalContestsMember" /></dt>
    <dd>${usersForm.totalContestsMember}</dd>

    <dt><bean:message key="user.totalProblemsSolved" /></dt>
    <dd>${usersForm.totalProblemsSolved}</dd>
</dl>

<c:if test="${permissionCheckerRemote.canDeepModifyUser(autentificationObject.username, usersForm.login)}">
<h3><bean:message key="user.permissions" /></h3>
<hr>
<dl class="dl-horizontal dl-dudge">
    <dt><bean:message key="user.administrator" /></dt>
    <dd>
        <c:choose>
            <c:when test="${usersForm.admin}"><bean:message key="user.yes" /></c:when>
            <c:otherwise><bean:message key="user.no" /></c:otherwise>
        </c:choose>
    </dd>

    <dt><bean:message key="user.contestCreator" /></dt>
    <dd>
        <c:choose>
            <c:when test="${usersForm.contestCreator}"><bean:message key="user.yes" /></c:when>
            <c:otherwise><bean:message key="user.no" /></c:otherwise>
        </c:choose>
    </dd>

    <dt><bean:message key="user.problemCreator" /></dt>
    <dd>
        <c:choose>
            <c:when test="${usersForm.problemCreator}"><bean:message key="user.yes" /></c:when>
            <c:otherwise><bean:message key="user.no" /></c:otherwise>
        </c:choose>
    </dd>
</dl>        
</c:if>