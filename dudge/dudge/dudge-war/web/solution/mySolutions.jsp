<%  dudge.SolutionLocal solutionLocal = dudge.web.ServiceLocator.getInstance().lookupSolutionBean();
    pageContext.setAttribute("solutionLocal", solutionLocal);
    dudge.web.SolutionMessageProvider solutionMessageProvider = dudge.web.SolutionMessageProvider.getInstance(request);
    pageContext.setAttribute("solutionMessageProvider", solutionMessageProvider);
%>
    
    
<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 
<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>

<script type="text/javascript">

    $(document).ready(function() {
        $("#navbarStatus").addClass("active");
        
        $('tr>td:nth-child(2)').each( function() {
            var submitTime = new Date(parseInt($(this).text()));
            $(this).text(submitTime.toLocaleString());
        } );
    });
</script>

<h1><bean:message key="status.status" /></h1>

<td><a href="contests.do?reqCode=view&contestId=${solutionDescription.contestId}">${solutionDescription.contestName}</a></td>
<p><strong><bean:message key="contest.contest" /></strong>: <a href="contests.do?reqCode=view&contestId=${solutionsForm.contestId}">${solutionsForm.contestName}</a></p>
<table class="table" id="statusGrid">
    <thead>
        <tr>
            <th><bean:message key="solution.id"/></th>
            <th><bean:message key="solution.time"/></th>
            <th><bean:message key="problem.problem" /></th>
            <th><bean:message key="language.language" /></th>
            <th><bean:message key="solution.status" /></th>
        </tr>
    </thead>
    <tbody>
        
<c:forEach items="${solutionLocal.getSolutionDescriptions(
                        autentificationObject.username,
                        solutionsForm.contestId,
                        solutionMessageProvider
                   )}" var="solutionDescription">
        <tr>
            <td><a href="solutions.do?reqCode=view&solutionId=${solutionDescription.solutionId}">${solutionDescription.solutionId}</a></td>
            <td>${solutionDescription.submitTime}</td>
            <td><a href="problems.do?reqCode=view&contestId=${solutionDescription.contestId}&problemId=${solutionDescription.problemId}">${solutionDescription.problemName}</a></td>
            <td>${solutionDescription.languageName}</td>
            <td>${solutionDescription.status}</td>
        </tr>
</c:forEach>
    </tbody>
</table>

