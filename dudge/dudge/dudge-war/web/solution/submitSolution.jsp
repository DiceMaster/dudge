<script type="text/javascript" src="scripts/submit.js"></script>
<jsp:useBean id="solutionsForm" class="dudge.web.forms.SolutionsForm" scope="session" />

<html:form action="solutions.do?reqCode=submitSubmit" styleClass="x-form">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
		<h3 style="margin-bottom:5px;"><bean:message key="submit.submitSolution" /></h3>
		<div class="x-form-bd" id="container">
			<input type="hidden" name="contestId" value="<%=contestId%>"/>
		    <fieldset>
			<legend><bean:message key="submit.sendInformation" /></legend>
			<div class="x-form-item">
			    <label for="language-select"><bean:message key="language.language"/></label>
			    <div class="x-form-element">
				<html:select property="languageId" styleId="language-select">
				    <% java.util.List<dudge.db.ContestLanguage> contestLanguages = solutionsForm.getContestLanguages(); %>
				    <% for (dudge.db.ContestLanguage conLang : contestLanguages){ %>
				    <html:option value='<%=conLang.getLanguage().getLanguageId()%>' >
					<%=conLang.getLanguage().getName()%>
				    </html:option>
				    <% } %>
				</html:select>
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="problem-select"><bean:message key="problem.problem"/></label>
			    <div class="x-form-element">
		   
				<% if(sessionObject.getDudge().getContest(contestId).getType() == dudge.db.ContestType.GLOBAL) { %>
	
				<html:text property="problemId" styleId="problemid" size="5"/>
	
				<% } else { %>
	
				<html:select property="problemId" styleId="problem-select">
				    <% java.util.List<dudge.db.ContestProblem> contestProblems = solutionsForm.getContestProblems(); %>
					<% for (dudge.db.ContestProblem conProb : contestProblems){ %>
				    <html:option value='<%=Integer.toString(conProb.getProblem().getProblemId())%>' >
					<%=conProb.getProblemMark()%>. <%=conProb.getProblem().getTitle()%>
				    </html:option>
				    <% } %>
				</html:select>
	
				<% } %>
				
				<a href="javascript:openContestProblems()"><bean:message key="menu.contestProblems" />...</a>
				
			    </div>
			</div>
			<div class="x-form-item">
			    <label for="sourcecode"><bean:message key="submit.source"/></label>
			    <div class="x-form-element">
				<html:textarea property="sourceCode" styleId="sourcecode" style="width:90%;height:200px" />
			    </div>
			</div>
			<div id="send" style="margin:20px;"></div>  
		    </fieldset>
		</div>
		<html:submit><bean:message key="submit.submitSolution"/></html:submit>
    </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>  
