<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<bean:message key="solution.sumbitSuccessful"/>
<p><a href="solutions.do?reqCode=view&contestId=${contestId}&solutionId=${solutionsForm.solutionId}"
><bean:message key="solution.viewSolution"/></a>
| 
<a href="solutions.do?reqCode=submit&contestId=${contestId}"><bean:message key="solution.submitAnother"/></a>
