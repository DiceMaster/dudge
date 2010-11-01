<h1 align="center"><bean:message key="contests.contests"/></h1>
<p><p>
<%
java.util.List<dudge.db.Contest> activeContests = sessionObject.getDudge().getActiveContests();
java.util.List activeContestsList = activeContests;
java.util.Collections.sort(activeContestsList);

java.util.List<dudge.db.Contest> pendingContests = sessionObject.getDudge().getPendingContests();
java.util.List pendingContestsList = pendingContests;
java.util.Collections.sort(pendingContestsList);

java.util.List<dudge.db.Contest> recentlyFinishedContests = sessionObject.getDudge().getRecentlyFinishedContests();
java.util.List recentlyFinishedContestsList = recentlyFinishedContests;
java.util.Collections.sort(recentlyFinishedContestsList);
%>

<%
if (activeContests.size() > 0)
{
%>
<h2 align="center"><bean:message key="contests.active"/></h2>
<%
for(dudge.db.Contest contest : activeContests){
%>
<p align="center"><a href="contests.do?reqCode=view&contestId=<%=contest.getContestId()%>"><%=contest.getCaption()%></a></p>
<%
} // for
} // if
%>

<%
if (pendingContests.size() > 0)
{
%>
<h2 align="center"><bean:message key="contests.pending"/></h2>
<%
for(dudge.db.Contest contest : pendingContests){
%>
<p align="center"><a href="contests.do?reqCode=view&contestId=<%=contest.getContestId()%>"><%=contest.getCaption()%></a></p>
<%
} // for
} // if
%>

<%
if (recentlyFinishedContests.size() > 0)
{
%>
<h2 align="center"><bean:message key="contests.recentlyFinished"/></h2>
<%
for(dudge.db.Contest contest : recentlyFinishedContests){
%>
<p align="center"><a href="contests.do?reqCode=view&contestId=<%=contest.getContestId()%>"><%=contest.getCaption()%></a></p>
<%
} // for
} // if
%>
