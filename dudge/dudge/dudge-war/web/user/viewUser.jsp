<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" scope="session" class="dudge.web.actions.UsersAction" />

<html:form action="users" method="GET" styleClass="x-form">
    <html:hidden property="reqCode" value="edit" />
    <html:hidden property="login" />
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
                <div class="x-form-bd" id="container"> 
                    <h3 style="margin-bottom:5px;"><bean:message key="user.user" /> ${usersForm.login}</h3>
                    <fieldset>
                        <legend><bean:message key="user.userInfo" /></legend>

                        <c:if test="${not empty usersForm.realName}">
                            <div class="x-form-item">
                                <label><bean:message key="user.realName" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.realName}</b>
                                </div>
                            </div>
                        </c:if>
                        <div class="x-form-item">
                            <label><bean:message key="user.regDate" /></label>
                            <div class="x-form-element">
                                <b> ${usersForm.regDate}</b>
                            </div>
                        </div>
                        <c:if test="${not empty usersForm.organization}">
                            <div class="x-form-item">
                                <label><bean:message key="user.organization" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.organization}</b>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty usersForm.age}">
                            <div class="x-form-item">
                                <label><bean:message key="user.age" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.age}</b>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty usersForm.jabberId}">
                            <div class="x-form-item">
                                <label><bean:message key="user.jabberId" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.jabberId}</b>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty usersForm.icqNumber}">
                            <div class="x-form-item">
                                <label><bean:message key="user.icqNumber" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.icqNumber}</b>
                                </div>
                            </div>
                        </c:if>

                        <div class="x-form-item">
                            <label><bean:message key="user.totalContestsMember" /></label>
                            <div class="x-form-element">
                                <b> ${usersForm.totalContestsMember}</b>
                            </div>
                        </div>
                        <div class="x-form-item">
                            <label><bean:message key="user.totalProblemsSolved" /></label>
                            <div class="x-form-element">
                                <b> ${usersForm.totalProblemsSolved}</b>
                            </div>
                        </div>
                    </fieldset>

                    <c:if test="${permissionCheckerRemote.canDeepModifyUser(autentificationObject.username, usersForm.login)}">
                        <fieldset>
                            <legend><bean:message key="user.permissions" /></legend>

                            <div class="x-form-item">
                                <label><bean:message key="user.administrator" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.admin}</b>
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="user.contestCreator" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.contestCreator}</b>
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="user.problemCreator" /></label>
                                <div class="x-form-element">
                                    <b> ${usersForm.problemCreator}</b>
                                </div>
                            </div>
                        </fieldset>
                    </c:if>

                    <c:if test="${permissionCheckerRemote.canModifyUser(autentificationObject.username, requestScope.login)}">
                        <div id="buttons" style="margin:5px;">
                            <html:submit> <bean:message key="contest.editProperties"/>
                            </html:submit>
                        </div>  
                    </c:if>

                </div> 
            </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
