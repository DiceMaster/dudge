<jsp:useBean id="rulesForm" class="dudge.web.forms.RulesForm" scope="session" />
<script src="ckeditor/ckeditor.js"></script>

<h1><bean:message key="rules.editRules"/></h1>
<form class="form" action="rules.do" method="POST">
    <input type="hidden" name="reqCode" value="submitEdit" />
    <textarea class="ckeditor" name="rules">${rulesForm.rules}</textarea>
    <input type="submit" value="<bean:message key="rules.submit" />"/>
</form>