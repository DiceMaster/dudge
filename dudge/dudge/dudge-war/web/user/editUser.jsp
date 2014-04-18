<%@page import="dudge.db.User" %>

<jsp:useBean id="usersForm" class="dudge.web.forms.UsersForm" scope="session" />
<jsp:useBean id="usersAction" class="dudge.web.actions.UsersAction" scope="session"/>

<script type="text/javascript" src="scripts/jquery.scrollintoview.min.js"></script>
<script type="text/javascript">
    function validate() {
    
        $("#loginFieldError").addClass("hide");
        $("#loginGroup").removeClass("has-error");
        $("#realNameFieldError").addClass("hide");
        $("#realNameGroup").removeClass("has-error");
        $("#passwordFieldError").addClass("hide");
        $("#passwordGroup").removeClass("has-error");
        $("#emailFieldError").addClass("hide");
        $("#emailGroup").removeClass("has-error");
    
        var failed = false;
        
        // Login validation
        var loginExp = /^[a-zA-Z0-9-_]+$/;
        if (!loginExp.test($("#login").val()))
        {
            $("#loginGroup").addClass("has-error");
            $("#loginFieldError").removeClass("hide");
            $("#loginFieldError").html('<bean:message key="register.loginWrongSymbols" />');
            if (!failed) {
                $("#loginGroup").scrollintoview();
                failed = true;
            }
        }
        if ($("#login").val().length < 3)
        {
            $("#loginGroup").addClass("has-error");
            $("#loginFieldError").removeClass("hide");
            $("#loginFieldError").html('<bean:message key="register.loginTooShort" />');
            if (!failed) {
                $("#loginGroup").scrollintoview();
                failed = true;
            }
        }
        if ($("#login").val().length > 20)
        {
            $("#loginGroup").addClass("has-error");
            $("#loginFieldError").removeClass("hide");
            $("#loginFieldError").html('<bean:message key="register.loginTooLong" />');
            if (!failed) {
                $("#loginGroup").scrollintoview();
                failed = true;
            }
        }
    
        // Realname validation
        if ($("#realName").val().length < 3)
        {
            $("#realNameGroup").addClass("has-error");
            $("#realNameFieldError").removeClass("hide");
            $("#realNameFieldError").html('<bean:message key="register.realNameTooShort" />');
            if (!failed) {
                $("#realNameGroup").scrollintoview();
                failed = true;
            }
        }
        if ($("#realName").val().length > 100)
        {
            $("#realNameGroup").addClass("has-error");
            $("#realNameFieldError").removeClass("hide");
            $("#realNameFieldError").html('<bean:message key="register.realNameTooLong" />');
            if (!failed) {
                $("#realNameGroup").scrollintoview();
                failed = true;
            }
        }
        
        // Password validation
        var password = $("#password").val();
        var confirmPassword = $("#passwordConfirm").val();
        if (password != confirmPassword)
        {
            $("#passwordGroup").addClass("has-error");
            $("#passwordFieldError").removeClass("hide");
            $("#passwordFieldError").html('<bean:message key="register.passwordWrongConfirm" />');
            if (!failed) {
                $("#passwordGroup").scrollintoview();
                failed = true;
            }
        }
        if (password.length < 3)
        {
            $("#passwordGroup").addClass("has-error");
            $("#passwordFieldError").removeClass("hide");
            $("#passwordFieldError").html('<bean:message key="register.passwordTooShort" />');
            if (!failed) {
                $("#passwordGroup").scrollintoview();
                failed = true;
            }
        }
        if (password.length > 20)
        {
            $("#passwordGroup").addClass("has-error");
            $("#passwordFieldError").removeClass("hide");
            $("#passwordFieldError").html('<bean:message key="register.passwordTooLong" />');
            if (!failed) {
                $("#passwordGroup").scrollintoview();
                failed = true;
            }
        }
        
        // Email validation
        var emailExp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!emailExp.test($("#email").val()))
        {
            $("#emailGroup").addClass("has-error");
            $("#emailFieldError").removeClass("hide");
            $("#emailFieldError").html('<bean:message key="register.emailMalformed" />');
            if (!failed) {
                $("#emailGroup").scrollintoview();
                failed = true;
            }
        }
    
        if (failed) {
            return false;
        }
        
        return true;
    }
</script>

<form class="form-horizontal" action="users.do" method="post" onsubmit="return validate()">
    <fieldset>
        <c:choose>
            <c:when test="${usersForm.newUser}">
                <input type="hidden" name="reqCode" value="submitRegister">
                <h1><bean:message key="registration.registration" /></h3>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="reqCode" value="submitEdit">
                <input type="hidden" name="login" value="${usersForm.login}">
                <h1><bean:message key="user.user" /> ${usersForm.login}</h3>
            </c:otherwise>
        </c:choose>

        <fieldset>
            <legend><bean:message key="registration.requiredInfo" /></legend>

            <c:if test="${usersForm.newUser}">
                <div class="form-group" id="loginGroup">
                    <label for="login" class="col-lg-3 control-label"><bean:message key="user.login" /></label>
                    <div class="col-lg-7">
                        <input type="text" id="login" name="login" class="form-control">
                    </div>
                    
                    <c:choose>
                        <c:when test="${usersForm.hasLoginError}">
                            <span class="help-block col-lg-offset-3 col-lg-7" id="loginFieldError"><bean:message key="${usersForm.errorMessageKey}" /></span>
                        </c:when>
                        <c:otherwise>
                            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="loginFieldError"></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-group" id="realNameGroup">
                    <label for="realName" class="col-lg-3 control-label"><bean:message key="user.realName" /></label>
                    <div class="col-lg-7">
                        <input type="text" id="realName" name="realName" class="form-control">
                    </div>
                    <span class="help-block col-lg-offset-3 col-lg-7 hide" id="realNameFieldError"></span>
                </div>
                <div class="form-group" id="passwordGroup">
                    <label for="password" class="col-lg-3 control-label"><bean:message key="user.password" /></label>
                    <div class="col-lg-7">
                        <input type="password" id="password" name="password" class="form-control">
                    </div>
                    <c:choose>
                        <c:when test="${usersForm.hasPasswordError}">
                            <span class="help-block col-lg-offset-3 col-lg-7" id="passwordFieldError"><bean:message key="${usersForm.errorMessageKey}" /></span>
                        </c:when>
                        <c:otherwise>
                            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="passwordFieldError"></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-group">
                    <label for="passwordConfirm" class="col-lg-3 control-label"><bean:message key="user.repPassword" /></label>
                    <div class="col-lg-7">
                        <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-control">
                    </div>
                </div>    
            </c:if>

            <div class="form-group" id="emailGroup">
                <label for="email" class="col-lg-3 control-label"><bean:message key="user.email"/></label>
                <div class="col-lg-7">
                    <input type="text" id="email" name="email" class="form-control" value="${usersForm.email}">
                </div>
                <span class="help-block col-lg-offset-3 col-lg-7 hide" id="emailFieldError"></span>
            </div>
        </fieldset>

        <fieldset>
            <legend><bean:message key="registration.additionalInfo" /></legend>
            <div class="form-group">
                <label for="organization" class="col-lg-3 control-label"><bean:message key="user.organization" /></label>
                <div class="col-lg-7">
                    <input type="text" id="organization" name="organization" class="form-control" value="${usersForm.organization}">
                </div>
            </div>
            <div class="form-group">
                <label for="faculty" class="col-lg-3 control-label"><bean:message key="user.faculty" /></label>
                <div class="col-lg-7">
                    <input type="text" id="faculty" name="faculty" class="form-control" value="${usersForm.faculty}">
                </div>
            </div>    
            <div class="form-group">
                <label for="course" class="col-lg-3 control-label"><bean:message key="user.course" /></label>
                <div class="col-lg-7">
                    <input type="text" id="course" name="course" class="form-control" value="${usersForm.course}">
                </div>
            </div>
            <div class="form-group">
                <label for="group" class="col-lg-3 control-label"><bean:message key="user.group" /></label>
                <div class="col-lg-7">
                    <input type="text" id="group" name="group" class="form-control" value="${usersForm.group}">
                </div>
            </div>
            <div class="form-group">
                <label for="age" class="col-lg-3 control-label"><bean:message key="user.age" /></label>
                <div class="col-lg-7">
                    <input type="text" id="age" name="age" class="form-control" value="${usersForm.age}">
                </div>
            </div>
            <div class="form-group">
                <label for="jabberId" class="col-lg-3 control-label"><bean:message key="user.jabberId" /></label>
                <div class="col-lg-7">
                    <input type="text" id="jabberId" name="jabberId" class="form-control" value="${usersForm.jabberId}">
                </div>
            </div>
            <div class="form-group" class="col-lg-3 control-label">
                <label for="icqNumber" class="col-lg-3 control-label"><bean:message key="user.icqNumber" /></label>
                <div class="col-lg-7">
                    <input type="text" id="icqNumber" name="icqNumber" class="form-control" value="${usersForm.icqNumber}">
                </div>
            </div>
        </fieldset>

        <c:if test="${permissionCheckerRemote.canDeepModifyUser(autentificationObject.username, usersForm.login)}">
            <fieldset>
                <legend><bean:message key="user.permissions" /></legend>

                <div class="form-group">
                    <div class="col-lg-offset-3 col-lg-7">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="admin" <c:if test="${usersForm.admin}">checked</c:if> >
                                <bean:message key="user.administrator" />
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-offset-3 col-lg-7">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="contestCreator" <c:if test="${usersForm.contestCreator}">checked</c:if> >
                                <bean:message key="user.contestCreator" />
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-offset-3 col-lg-7">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="problemCreator" <c:if test="${usersForm.problemCreator}">checked</c:if> >
                                <bean:message key="user.problemCreator" />
                            </label>
                        </div>
                    </div>
                </div>
            </fieldset>
        </c:if>

        <div class="form-group">
            <div class="col-lg-offset-3 col-lg-7">
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${usersForm.newUser}">
                            <bean:message key="registration.register" />
                        </c:when>
                        <c:otherwise>
                            <bean:message key="user.applyChanges" />
                        </c:otherwise>
                    </c:choose>
                </button>
            </div>
        </div>
    </fieldset>
</form>
