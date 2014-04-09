<%@page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />

<h1>${contestsForm.caption}</h1>
<h2><bean:message key="contest.rules" /></h2>
<div>${contestsForm.rules}</div>
