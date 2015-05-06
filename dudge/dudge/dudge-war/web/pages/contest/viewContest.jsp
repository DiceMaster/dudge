<%@page import="java.text.SimpleDateFormat" %>
<%@page import="dudge.db.Contest" %>
<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />
<% pageContext.setAttribute("roleTypeUser", dudge.db.RoleType.USER);%>
<c:set var="contestId" value="${contestsForm.contestId}" scope="session" />

<script type="text/javascript">
    $( document ).ready(function() {
        $('#sendApplication').click(function(){
            var message = '<bean:message key="contest.sendApplication" />';
            $.post('contests.do', {reqCode: 'sendApplication' , contestId: ${contestId} , message: message}, function(xml){
                $('.alert').show();
            }, 'xml');
        });
    });
</script>
<form action="contests.do" method="GET">
    <input type="hidden" name="reqCode" value="edit">
    <input type="hidden" name="contestId" value="${contestId}">
    <h1 class="pull-left">${contestsForm.caption}</h1>
    <div class="pull-right">
        <div class="btn-group dudge-btn-group">
<c:if test="${permissionCheckerRemote.canSendApplication(autentificationObject.username, contestId)}">            
            <button type="button" class="btn" id="sendApplication"><bean:message key="contest.sendApplication"/></button>
</c:if>            
<c:if test="${permissionCheckerRemote.canModifyContest(autentificationObject.username, contestId)}">                        
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteContest"><bean:message key="contest.delete"/></button>
            <!-- Modal -->
            <div class="modal" id="deleteContest" tabindex="-1" role="dialog" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title"><bean:message key="contest.confirmDeleteTitle"/></h4>
                  </div>
                  <div class="modal-body">
                      <bean:message key="contest.confirmDeleteMsg"/>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="contest.cancel"/></button>
                    <a href="contests.do?reqCode=delete&contestId=${contestId}" class="btn btn-danger"><bean:message key="contest.delete"/></a>
                  </div>
                </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            
            <button type="submit" class="btn btn-primary"><bean:message key="contest.edit"/></button>
</c:if>            
        </div>
    </div>
    <div class="clearfix"></div>
</form>
<div class="alert alert-success alert-dismissable" hidden="true">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    <bean:message key="contest.applicationSent" />
</div>
<p><a href="contests.do?reqCode=rules&contestId=${contestId}"><bean:message key="contest.rules" /></a></p>
<h2><bean:message key="contest.parameters"/></h2>
<hr>
<dl class="dl-horizontal">
    <dt><bean:message key="contest.type" /></dt>
    <dd>${contestsForm.contestType}</dd>

    <dt><bean:message key="contest.isOpen" /></dt>
    <c:choose>
        <c:when test="${contestsForm.open}">
            <dd><bean:message key="contest.open.yes" /></dd>
        </c:when>
        <c:otherwise>
            <dd><bean:message key="contest.open.no" /></dd>
        </c:otherwise>
    </c:choose>

    <dt><bean:message key="contest.startDate" /></dt>
    <dd>${contestsForm.startDate}</dd>

    <dt><bean:message key="contest.startTime" /></dt>
    <dd>${contestsForm.startHour}:${contestsForm.startMinute}</dd>

    <dt><bean:message key="contest.duration" /></dt>
    <dd>${contestsForm.durationHours}:${contestsForm.durationMinutes}</dd>
</dl>
<h2><bean:message key="contest.description"/></h2>
<hr>
<div>${contestsForm.description} </div>

<p>${contest}</p>
<h2><bean:message key="contest.competitors" /></h2>
<table class="table">
    <thead>
        <tr>
            <th><b><bean:message key="user.login" /></b></th>
            <th><b><bean:message key="user.realName" /></b></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${contestsForm.roles}" var="role">
            <c:if test="${role.roleType == roleTypeUser}">
                <tr>
                    <td><a href="users.do?reqCode=view&login=${role.user}">${role.user}</a></td>
                    <td>${role.realName} </td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>