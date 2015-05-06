<jsp:useBean id="fileUploadForm" class="dudge.web.forms.FileUploadForm" scope="session" />

<script type='text/javascript'>
    window.parent.CKEDITOR.tools.callFunction(${fileUploadForm.CKEditorFuncNum}, 'upload/${fileUploadForm.upload.fileName}', '');
</script>
