<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<jsp:useBean id="reportingBean" class="dudge.ReportingBean" />
<c:set var="reportingBean" value="${autentificationObject.reporting}" />

<div id="reportsGrid">
    <p><a href="reports.do?contestId=${contestId}&reqCode=print">ClickMe</a>
</div>    
