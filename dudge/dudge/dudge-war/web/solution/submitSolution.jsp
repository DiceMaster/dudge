<script type="text/javascript" src="scripts/submit.js"></script>
<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />
<% pageContext.setAttribute("contestTypeGlobal", dudge.db.ContestType.GLOBAL); %>

<html:form action="solutions.do?reqCode=submitSubmit" styleClass="x-form">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
                <h3 style="margin-bottom:5px;"><bean:message key="submit.submitSolution" /></h3>
                <div class="x-form-bd" id="container">
                    <input type="hidden" name="contestId" value="${contestId}"/>
                    <fieldset>
                        <legend><bean:message key="submit.sendInformation" /></legend>
                        <div class="x-form-item">
                            <label for="language-select"><bean:message key="language.language"/></label>
                            <div class="x-form-element">
                                <html:select property="languageId" styleId="language-select">
                                    <c:forEach items="${solutionsForm.contestLanguages}" var="contestLanguage">
                                        <html:option value='${contestLanguage.language.languageId}' >
                                            ${contestLanguage.language.name}
                                        </html:option>
                                    </c:forEach> 
                                </html:select>
                            </div>
                        </div>
                        <div class="x-form-item">
                            <label for="problem-select"><bean:message key="problem.problem"/></label>
                            <div class="x-form-element">

                                <c:forEach items="${contestLocal.contests}" var="contest">
                                    <c:if test="${contest.contestId == contestId}">
                                        <c:choose>
                                            <c:when test="${contest.type == contestTypeGlobal}">
                                                <html:text property="problemId" styleId="problemid" size="5"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:select property="problemId" styleId="problem-select">
                                                    <c:forEach items="${solutionsForm.contestProblems}" var="contestProblem">
                                                        <html:option value='${contestProblem.problem.problemId}' >
                                                            ${contestProblem.problemMark}. ${contestProblem.problem.title}
                                                        </html:option>
                                                    </c:forEach> 
                                                </html:select>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>  
                                </c:forEach>

                                <a href="javascript:openContestProblems()"><bean:message key="menu.contestProblems" />...</a>

                            </div>
                        </div>
                        <div class="x-form-item">
                            <label for="sourcecode"><bean:message key="submit.source"/></label>
                            <div class="x-form-element">
                                <html:textarea property="sourceCode" styleId="sourcecode" style="width:96%;height:400pt;margin:1%;" />
                            </div>
                        </div>
                        <div id="send" style="margin:20px;"></div>  
                    </fieldset>
                </div>
                <html:submit><bean:message key="submit.submitSolution"/></html:submit>
            </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>  
