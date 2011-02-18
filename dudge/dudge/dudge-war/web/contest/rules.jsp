<%@page import="java.text.SimpleDateFormat" %>

<%dudge.db.Contest contest = dudgeLocal.getContest(contestId);%>

<h1 align="center" style="font-size:x-large"><%=contest.getCaption()%></h1>
<h2 align="center" style="font-size:large"><bean:message key="contest.rules" /></h2>
<p class="contest_info"><%=contest.getRules()%></p>
