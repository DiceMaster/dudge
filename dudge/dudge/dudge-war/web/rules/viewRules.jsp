<jsp:useBean id="rulesForm" class="dudge.web.forms.RulesForm" scope="session" />
<script>
    $(document).ready(function() {
        $("#navbarRules").addClass("active");
    });
</script>

<h1 class="pull-left"><bean:message key="rules.rules"/></h1>
<c:if test="${permissionCheckerRemote.canEditRules(autentificationObject.username)}">
<div class="pull-right">
    <div class="btn-group dudge-btn-group">
        <a class="btn btn-default" href="rules.do?reqCode=edit"><bean:message key="rules.edit"/></a>
    </div>
</div>
</c:if>
<div class="clearfix"></div>
<c:choose>
    <c:when test="${rulesForm.notFilled}">
<div class="text-center"><bean:message key="rules.empty"/></div>
    </c:when>
    <c:otherwise>
<div>${rulesForm.rules}</div>
    </c:otherwise>
</c:choose>
