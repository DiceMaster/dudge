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
<a href="problems.do?reqCode=view&contestId=${solutionForm.contestId}&problemId=${solutionsForm.problemId}">${solutionsForm.problemName}</a>

<table class="table" id="statusGrid">
    <thead>
        <tr>
            <th><bean:message key="solution.id"/></th>
            <th><bean:message key="solution.time"/></th>
            <th><bean:message key="user.user" /></th>
            <th><bean:message key="contest.contest" /></th>
            <th><bean:message key="language.language" /></th>
            <th><bean:message key="solution.status" /></th>
        </tr>
    </thead>
    <tbody>
        
    <c:forEach items="${solutionLocal.getProblemSolutions(
                        solutionsForm.problemId,
                        solutionMessageProvider
                   )}" var="solution">
        <tr>
            <td><a href="solutions.do?reqCode=view&solutionId=${solution.solutionId}">${solution.solutionId}</a></td>
            <td>${solution.submitTime}</td>
            <td><a href="users.do?reqCode=view&login=${solution.userName}">${solution.userName}</a></td>
            <td><a href="contests.do?reqCode=view&contestId=${solution.contestId}">${solution.contestName}</a></td>            
            <td>${solution.languageName}</td>
            <td>${solution.status}</td>
        </tr>
</c:forEach>
    </tbody>
</table>

