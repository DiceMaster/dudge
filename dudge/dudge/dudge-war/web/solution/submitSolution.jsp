<script type="text/javascript" src="scripts/submit.js"></script>
<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />
<% pageContext.setAttribute("contestTypeGlobal", dudge.db.ContestType.GLOBAL); %>

<form class="form-horizontal" action="solutions.do" method="post">
    <h1><bean:message key="submit.submitSolution" /></h1>
    <input type="hidden" name="reqCode" value="submitSubmit">
    <input type="hidden" name="contestId" value="${solutionsForm.contestId}">
    
    <div class="form-group">
        <label for="languageId" class="col-lg-2 control-label"><bean:message key="language.language"/></label>
        <div class="col-lg-9">
            <select name="languageId" class="form-control">
                                    <c:forEach items="${solutionsForm.contestLanguages}" var="contestLanguage">
                    <option value='${contestLanguage.language.languageId}' >
                                            ${contestLanguage.language.name}
                    </option>
                                    </c:forEach> 
            </select>
                            </div>
                        </div>
    <div class="form-group">
        <label for="problemId" class="col-lg-2 control-label"><bean:message key="problem.problem"/></label>
        <div class="col-lg-9">
<c:choose>
    <c:when test="${contest.type == contestTypeGlobal}">
            <input type="text" name="problemId" class="form-control">
    </c:when>
    <c:otherwise>
            <select name="problemId" class="form-control">
        <c:forEach items="${solutionsForm.contestProblems}" var="contestProblem">
            <c:choose>
                <c:when test="${contestProblem.problem.problemId == solutionsForm.problemId}">
                <option value='${contestProblem.problem.problemId}' selected>${contestProblem.problemMark}. ${contestProblem.problem.title}</option>
                </c:when>
                <c:otherwise>
                <option value='${contestProblem.problem.problemId}' >${contestProblem.problemMark}. ${contestProblem.problem.title}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach> 
            </select>
    </c:otherwise>
</c:choose>
                            </div>
                        </div>
    <div class="form-group">
        <label for="sourceCode" class="col-lg-2 control-label"><bean:message key="submit.source"/></label>
        <div class="col-lg-9">
            <textarea name="sourceCode" class="form-control" rows="20"></textarea>
        </div>
                            </div>
    <div class="form-group">
        <div class="col-lg-offset-2 col-lg-9">
            <button type="submit" class="btn btn-primary"><bean:message key="submit.submitSolution" /></button>
                        </div>
                </div>
</form>
