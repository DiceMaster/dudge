<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<c:redirect url="solutions.do?reqCode=view">
  <c:param name="contestId" value="${contestId}"/>
  <c:param name="solutionId" value="${solutionsForm.solutionId}"/>
</c:redirect>
