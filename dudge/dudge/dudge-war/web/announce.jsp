<%@page import="dudge.db.Contest"%>
<jsp:useBean id="announceForm" class="dudge.web.forms.AnnounceForm" scope="session" />
<jsp:useBean id="announceAction" class="dudge.web.actions.AnnounceAction" scope="session"/>

<h1 align="center"><bean:message key="contests.contests"/></h1>
<p><p>

    <c:set var="activeContests" value="${announceForm.activeContests}"/>
    <c:set var="pendingContests" value="${announceForm.pendingContests}"/>
    <c:set var="recentlyFinishedContests" value="${announceForm.recentlyFinishedContests}"/>

    <c:if test="${fn:length(activeContests) gt 0}">
    <h2 align="center"><bean:message key="contests.active"/></h2>
    <c:forEach items="${activeContests}" var="contest">
        <p align="center"><a onclick="setContestId(id)" id="${contest.contestId}" href="contests.do?reqCode=view&contestId=${contest.contestId}">${contest.caption}</a></p>
    </c:forEach>
</c:if>

<c:if test="${fn:length(pendingContests) gt 0}">
    <h2 align="center"><bean:message key="contests.pending"/></h2>
    <c:forEach items="${pendingContests}" var="contest">
        <p align="center"><a onclick="setContestId(id)" id="${contest.contestId}" href="contests.do?reqCode=view&contestId=${contest.contestId}">${contest.caption}</a></p>
    </c:forEach>
</c:if>

<c:if test="${fn:length(recentlyFinishedContests) gt 0}">
    <h2 align="center"><bean:message key="contests.recentlyFinished"/></h2>
    <c:forEach items="${recentlyFinishedContests}" var="contest">
        <p align="center"><a onclick="setContestId(id)" id="${contest.contestId}" href="contests.do?reqCode=view&contestId=${contest.contestId}">${contest.caption}</a></p>
    </c:forEach>
</c:if>

<script type="text/javascript">
    function setContestId(contestId) {
        request.setAttribute("contestId", contestId);
        session.setAttribute("contestId", contestId);
    }
</script>

