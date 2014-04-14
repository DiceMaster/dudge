<%@page import="dudge.db.Contest" pageEncoding="UTF-8"%>
<jsp:useBean id="announceForm" class="dudge.web.forms.AnnounceForm" scope="session" />
<jsp:useBean id="announceAction" class="dudge.web.actions.AnnounceAction" scope="session"/>

<script>
    $(document).ready(function() {
        $("#navbarAnounce").addClass("active");
    });
</script>

<c:if test="${announceForm.noContests}">
<h3 class="text-center"><bean:message key="contests.noContests"/></h3>
<p class="text-center"><a class="btn btn-primary" href="contests.do?reqCode=list"><bean:message key="contests.list" /></a></p>
</c:if>

<c:if test="${not empty announceForm.highlightedContest}">
<div class="jumbotron">
    <h1>${announceForm.highlightedContest.caption}</h1>
    <div>${announceForm.highlightedContest.description}</div>
    <p><a class="btn btn-primary btn-lg" href="contests.do?reqCode=view&contestId=${announceForm.highlightedContest.contestId}"><bean:message key="announce.details"/></a></p>
</div>
</c:if>

<c:forEach items="${announceForm.activeContests}" var="contest">
    <c:if test="${contest != announceForm.highlightedContest}">
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2>${contest.caption}</h2>
            </div>
            <div class="panel-body">
                <div>${contest.description}</div>
                <p><a class="btn btn-primary btn-lg" href="contests.do?reqCode=view&contestId=${contest.contestId}"><bean:message key="announce.details"/></a></p>
            </div>
        </div>        
    </div>
</div>
    </c:if>
</c:forEach>

<c:forEach items="${announceForm.pendingContests}" var="contest">
    <c:if test="${contest != announceForm.highlightedContest}">
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2>${contest.caption}</h2>
            </div>
            <div class="panel-body">
                <div>${contest.description}</div>
                <p><a class="btn btn-primary btn-lg" href="contests.do?reqCode=view&contestId=${contest.contestId}"><bean:message key="announce.details"/></a></p>
            </div>
        </div>        
    </div>
</div>
    </c:if>
</c:forEach>

<c:forEach items="${announceForm.recentlyFinishedContests}" var="contest">
    <c:if test="${contest != announceForm.highlightedContest}">
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2>${contest.caption}</h2>
            </div>
            <div class="panel-body">
                <div>${contest.description}</div>
                <p><a class="btn btn-primary btn-lg" href="contests.do?reqCode=view&contestId=${contest.contestId}"><bean:message key="announce.details"/></a></p>
            </div>
        </div>        
    </div>
</div>
    </c:if>
</c:forEach>
