<jsp:useBean id="filesForm" class="dudge.web.forms.FilesForm" scope="session" />

<script type='text/javascript'>
    window.parent.CKEDITOR.tools.callFunction(${filesForm.CKEditorFuncNum}, '/upload/${filesForm.upload.fileName}', '');
</script>
