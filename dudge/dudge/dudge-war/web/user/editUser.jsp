<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" class="dudge.web.actions.UsersAction" scope="session"/>

<link rel="stylesheet" type="text/css" href="/dudge/css/editUser.css" />
<script type="text/javascript">
    function checkform() {
    
        document.getElementById("loginFieldError").style.visibility = "hidden";
        document.getElementById("realNameFieldError").style.visibility = "hidden";
        document.getElementById("passwordFieldError").style.visibility = "hidden";
        document.getElementById("emailFieldError").style.visibility = "hidden";
    
        // Login validation
        var loginExp = /^[a-zA-Z0-9-_]+$/;
        if (!loginExp.test(document.getElementById("login").value))
        {
            document.getElementById("loginFieldError").style.visibility = "visible";
            document.getElementById("loginFieldError").innerHTML = '<bean:message key="register.loginWrongSymbols" />';
            return false;
        }
        if (document.getElementById("login").value.length < 3)
        {
            document.getElementById("loginFieldError").style.visibility = "visible";
            document.getElementById("loginFieldError").innerHTML = '<bean:message key="register.loginTooShort" />';
            return false;
        }
        if (document.getElementById("login").value.length > 20)
        {
            document.getElementById("loginFieldError").style.visibility = "visible";
            document.getElementById("loginFieldError").innerHTML = '<bean:message key="register.loginTooLong" />';
            return false;
        }
    
        // Realname validation
        if (document.getElementById("realName").value.length < 3)
        {
            document.getElementById("realNameFieldError").style.visibility = "visible";
            document.getElementById("realNameFieldError").innerHTML = '<bean:message key="register.realNameTooShort" />';
            return false;
        }
        if (document.getElementById("realName").value.length > 20)
        {
            document.getElementById("realNameFieldError").style.visibility = "visible";
            document.getElementById("realNameFieldError").innerHTML = '<bean:message key="register.realNameTooLong" />';
            return false;
        }
        
        /*
        // Password validation
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("passwordConfirm").value;
        var passwordFieldError = document.getElementById("passwordFieldError");
        if (password.length < 3)
        {
            passwordFieldError.style.visibility = "visible";
            passwordFieldError.innerHTML = '<bean:message key="register.passwordTooShort" />';
            return false;
        }
        if (password.length > 20)
        {
            passwordFieldError.style.visibility = "visible";
            passwordFieldError.innerHTML = '<bean:message key="register.passwordTooLong" />';
            return false;
        }
        if (password != confirmPassword)
        {
            passwordFieldError.style.visibility = "visible";
            passwordFieldError.innerHTML = '<bean:message key="register.passwordWrongConfirm" />';
            return false;
        }
         */
        
        // Email validation
        var emailExp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!emailExp.test(document.getElementById("email").value))
        {
            document.getElementById("emailFieldError").style.visibility = "visible";
            document.getElementById("emailFieldError").innerHTML = '<bean:message key="register.emailMalformed" />';
            return false;
        }
    
        return true;
    }
</script>

<html:form styleId="userForm" action="users" styleClass="x-form" onsubmit="return checkform()">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
                <div class="x-form-bd" id="container">

                    <c:choose>
                        <c:when test="${usersForm.newUser}">
                            <html:hidden property="reqCode" value="submitRegister" />
                            <h3 style="margin-bottom:5px;"><bean:message key="registration.registration" /></h3>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="reqCode" value="submitEdit" />
                            <html:hidden property="login" />
                            <h3 style="margin-bottom:5px;"><bean:message key="user.user" /> ${usersForm.login}</h3>
                        </c:otherwise>
                    </c:choose>

                    <fieldset>
                        <legend><bean:message key="registration.requiredInfo" /></legend>

                        <c:if test="${usersForm.newUser}">
                            <div class="tableRow">
                                <div class="tableCell">
                                    <label for="login"><bean:message key="user.login" /></label>
                                </div>
                                <div class="tableCell">
                                    <html:text  property="login" styleId="login" size="20" styleClass="x-form-text x-form-field"/>
                                </div>
                                <c:choose>
                                    <c:when test="${usersForm.hasLoginError}">
                                        <div class="validationError" id="loginFieldError" style="visibility: visible">
                                            <bean:message key="${usersForm.errorMessageKey}" />
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="validationError" id="loginFieldError"></div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="tableRow">
                                <div class="tableCell">
                                    <label for="realName"><bean:message key="user.realName" /></label>
                                </div>
                                <div class="tableCell">
                                    <html:text property="realName" styleId="realName" size="20" styleClass="x-form-text x-form-field"/>
                                </div>
                                <div class="validationError" id="realNameFieldError"></div>
                            </div>
                            <div class="tableRow">
                                <div class="tableCell">
                                    <label for="password"><bean:message key="user.password" /></label>
                                </div>
                                <div class="tableCell">
                                    <html:password property="password" styleId="password" size="20" styleClass="x-form-text x-form-field"/>
                                </div>
                                <c:choose>
                                    <c:when test="${usersForm.hasPasswordError}">
                                        <div class="validationError" id="passwordFieldError" style="visibility: visible">
                                            <bean:message key="${usersForm.errorMessageKey}" />
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="validationError" id="passwordFieldError"></div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="tableRow">
                                <div class="tableCell">
                                    <label for="passwordConfirm"><bean:message key="user.repPassword" /></label>
                                </div>
                                <div class="tableCell">
                                    <html:password property="passwordConfirm" styleId="passwordConfirm" size="20" styleClass="x-form-text x-form-field"/>
                                </div>
                            </div>    
                        </c:if>

                        <div class="tableRow">
                            <div class="tableCell">
                                <label for="email"><bean:message key="user.email"/></label>
                            </div>
                            <div class="tableCell">
                                <html:text property="email" styleId="email" size="20" styleClass="x-form-text x-form-field"/>
                            </div>
                            <div class="validationError" id="emailFieldError"></div>
                        </div>

                    </fieldset>

                    <fieldset>
                        <legend><bean:message key="registration.additionalInfo" /></legend>
                        <div class="tableRow">
                            <div class="tableCell">
                                <label for="organization"><bean:message key="user.organization" /></label>
                            </div>
                            <div class="tableCell">
                                <html:text property="organization" styleId="organization" size="20" styleClass="x-form-text x-form-field"/>
                            </div>
                        </div>
                        <div class="tableRow">
                            <div class="tableCell">
                                <label for="age"><bean:message key="user.age" /></label>
                            </div>
                            <div class="tableCell">
                                <html:text property="age" styleId="age" size="20" styleClass="x-form-text x-form-field"/>
                            </div>
                        </div>
                        <div class="tableRow">
                            <div class="tableCell">
                                <label for="jabberId"><bean:message key="user.jabberId" /></label>
                            </div>
                            <div class="tableCell">
                                <html:text property="jabberId" styleId="jabberId" size="20" styleClass="x-form-text x-form-field"/>
                            </div>
                        </div>
                        <div class="tableRow">
                            <div class="tableCell">
                                <label for="icqNumber"><bean:message key="user.icqNumber" /></label>
                            </div>
                            <div class="tableCell">
                                <html:text property="icqNumber" styleId="icqNumber" size="20" styleClass="x-form-text x-form-field"/>
                            </div>
                        </div>
                    </fieldset>

                    <c:if test="${permissionCheckerRemote.canDeepModifyUser(autentificationObject.username, usersForm.login)}">
                        <fieldset>
                            <legend><bean:message key="user.permissions" /></legend>

                            <div class="tableRow">
                                <label>
                                    <html:checkbox property="admin" />
                                    <bean:message key="user.administrator" />
                                </label>
                            </div>

                            <div class="tableRow">	    
                                <label>
                                    <html:checkbox property="contestCreator" />
                                    <bean:message key="user.contestCreator" />
                                </label>
                            </div>

                            <div class="tableRow">
                                <label>
                                    <html:checkbox property="problemCreator" />
                                    <bean:message key="user.problemCreator" />
                                </label>
                            </div>
                        </fieldset>
                    </c:if>

                    <html:submit>
                        <c:choose>
                            <c:when test="${usersForm.newUser}">
                                <bean:message key="registration.register" />     
                            </c:when>
                            <c:otherwise>
                                <bean:message key="user.applyChanges" />
                            </c:otherwise>
                        </c:choose>
                    </html:submit>        
                </div>
            </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
