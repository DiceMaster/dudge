<%@page import="dudge.db.Language" %>

<jsp:useBean id="languagesForm" class="dudge.web.forms.LanguagesForm" scope="session" />

<script>
function validate() {
/*
    $("#languageIdError").addClass("hide");
    $("#languageIdGroup").removeClass("has-error");
    $("#ldescriptionError").addClass("hide");
    $("#ldescriptionGroup").removeClass("has-error");
    $("#fileExtensionError").addClass("hide");
    $("#fileExtensionGroup").removeClass("has-error");
    $("#compilationCommandError").addClass("hide");
    $("#compilationCommandGroup").removeClass("has-error");
    $("#executionCommandError").addClass("hide");
    $("#executionCommandGroup").removeClass("has-error");

    var failed = false;

    // Language ID validation
    if ($("#languageId").val().length < 1)
    {
        $("#languageIdGroup").addClass("has-error");
        $("#languageIdError").removeClass("hide");
        $("#languageIdError").html('<bean:message key="language.languageIdTooShort" />');
        if (!failed) {
            $("#languageIdGroup").scrollintoview();
            failed = true;
        }
    }
    if ($("#login").val().length > 255)
    {
        $("#languageIdGroup").addClass("has-error");
        $("#languageIdError").removeClass("hide");
        $("#languageIdError").html('<bean:message key="language.languageIdTooLong" />');
        if (!failed) {
            $("#languageIdGroup").scrollintoview();
            failed = true;
        }
    }
    
    // Description validation
    if ($("#description").val().length > 500)
    {
        $("#descriptionGroup").addClass("has-error");
        $("#descriptionError").removeClass("hide");
        $("#descriptionError").html('<bean:message key="language.descriptionTooLong" />');
        if (!failed) {
            $("#descriptionGroup").scrollintoview();
            failed = true;
        }
    }
    
    // File extension validation
    if ($("#fileExtension").val().length < 1)
    {
        $("#fileExtensionGroup").addClass("has-error");
        $("#fileExtensionError").removeClass("hide");
        $("#fileExtensionError").html('<bean:message key="language.fileExtensionTooShort" />');
        if (!failed) {
            $("#realNameGroup").scrollintoview();
            failed = true;
        }
    }
    if ($("#fileExtensionName").val().length > 15)
    {
        $("#fileExtensionGroup").addClass("has-error");
        $("#fileExtensionError").removeClass("hide");
        $("#fileExtensionError").html('<bean:message key="language.fileExtensionTooLong" />');
        if (!failed) {
            $("#fileExtensionGroup").scrollintoview();
            failed = true;
        }
    }

    if (failed) {
        return false;
    }
 */
    return true;
}
</script>

<form action="languages.do" class="form-horizontal" method="post" onsubmit="return validate()">
    <c:choose>
        <c:when test="${languagesForm.newLanguage}">
            <input type="hidden" name="reqCode" value="submitCreate">
            <h1><bean:message key="language.create" /></h1>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="reqCode" value="submitEdit" >
            <input type="hidden" name="languageId" value="${languagesForm.languageId}">
            <h1><bean:message key="language.edit" /> ${languagesForm.languageId}</h1>
        </c:otherwise>
    </c:choose>
    <fieldset>
        <legend><bean:message key="language.languageInfo" /></legend>

        <c:if test="${languagesForm.newLanguage}">
            <div class="form-group" id="languageIdGroup">
                <label for="languageId" class="col-lg-3 control-label"><bean:message key="language.id" /></label>
                <div class="col-lg-7">
                    <input type="text" id="languageId" name="languageId" class="form-control">
                </div>
            </div>
            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="languageIdError"></span>
        </c:if>
        <div class="form-group" id="titleGroup">
            <label for="title" class="col-lg-3 control-label"><bean:message key="language.title" /></label>
            <div class="col-lg-7">
                <input type="text" id="title" name="title" class="form-control" value="${languagesForm.title}">
            </div>
        </div>
        <div class="form-group" id="descriptionGroup">
            <label for="description" class="col-lg-3 control-label"><bean:message key="language.description" /></label>
            <div class="col-lg-7">
                <textarea id="description" name="description" class="form-control" rows="3">${languagesForm.description}</textarea>
            </div>
            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="descriptionError"></span>
        </div>
    </fieldset>

    <fieldset>
        <legend><bean:message key="language.technicalParameters" /></legend>
        <div class="form-group" id="fileExtensionGroup">
            <label for="fileExtension" class="col-lg-3 control-label"><bean:message key="language.fileExtension" /></label>
            <div class="col-lg-7">
                <input type="text" id="fileExtension" name="fileExtension" class="form-control" value="${languagesForm.fileExtension}">
            </div>
            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="fileExtensionError"></span>
        </div>
        <div class="form-group" id="compilationCommandGroup">
            <label for="compilationCommand" class="col-lg-3 control-label"><bean:message key="language.compilationCommand" /></label>
            <div class="col-lg-7">
                <input type="text" id="compilationCommand" name="compilationCommand" class="form-control" value="${languagesForm.compilationCommand}">
            </div>
            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="compilationCommandError"></span>
        </div>
        <div class="form-group" id="executionCommandGroup">
            <label for="executionCommand" class="col-lg-3 control-label"><bean:message key="language.executionCommand" /></label>
            <div class="col-lg-7">
                <input type="text" id="executionCommand" name="executionCommand" class="form-control" value="${languagesForm.executionCommand}">
            </div>
            <span class="help-block col-lg-offset-3 col-lg-7 hide" id="executionCommandError"></span>
        </div>
        <div class="form-group">
            <label for="substitutions" class="col-lg-3 control-label"><bean:message key="language.substitutions" /></label>
            <div class="col-lg-7">
                <pre><bean:message key="language.substitution.name" />
<bean:message key="language.substitution.source" />
<bean:message key="language.substitution.executable" />
<bean:message key="language.substitution.testdir" />
<bean:message key="language.substitution.separator" /></pre>
            </div>
        </div>
    </fieldset>

    <div class="form-group">
        <div class="col-lg-offset-3 col-lg-7">
            <button type="submit" class="btn btn-primary">
                <c:choose>
                    <c:when test="${languagesForm.newLanguage}">
                        <bean:message key="language.submitCreate" />
                    </c:when>
                    <c:otherwise>
                        <bean:message key="language.applyChanges" />
                    </c:otherwise>
                </c:choose>
            </button>
        </div>
    </div>
</form>
