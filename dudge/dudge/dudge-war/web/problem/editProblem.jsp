<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<script src="ckeditor/ckeditor.js"></script>

<form class="form" action="problems.do" method="POST" enctype="multipart/form-data">
    <c:choose>
        <c:when test="${problemsForm.newProblem}">
    <input type="hidden" name="reqCode" value="submitCreate">
    <h1><bean:message key="problem.newProblem" /></h1>
        </c:when>
        <c:otherwise>
    <input type="hidden" name="reqCode" value="submitEdit">
    <input type="hidden" name="problemId" value="${problemsForm.problemId}">
    <h1><bean:message key="problem.problem" /> ${problemsForm.problemId}: ${problemsForm.title}</h1>
        </c:otherwise>
    </c:choose>

    <ul class="nav nav-tabs">
        <li class="active"><a href="#descriptionTab" data-toggle="tab"><bean:message key="problem.description" /></a></li>
        <li><a href="#parametersTab" data-toggle="tab"><bean:message key="problem.parameters" /></a></li>
        <li><a href="#importTab" data-toggle="tab"><bean:message key="problem.import" /></a></li>
    </ul>
    <div class="tab-content">
        <div id="descriptionTab" class="tab-pane active">
            <div class="form-group">
                <label><bean:message key="problem.title" /></label>
                <input type="text" name="title" class="form-control" value="${problemsForm.title}">
            </div>
            <div class="form-group">
                <label><bean:message key="problem.author" /></label>
                <input type="text" name="author" class="form-control" value="${problemsForm.author}">
            </div>
            <div class="form-group">
                <label><bean:message key="problem.description" /></label>
                <textarea class="ckeditor" name="description">${problemsForm.description}</textarea> 
            </div>
        </div>

        <div id="parametersTab" class="tab-pane">
            <div class="form-group">
                <label><bean:message key="problem.cpuTimeLimit" /></label>
                <input type="text" name="cpuTimeLimit" class="form-control" value="${problemsForm.cpuTimeLimit}">  <bean:message key="problem.cpuMetric"/>
            </div>  

            <div class="form-group">
                <label><bean:message key="problem.realTimeLimit" /></label>
                <input type="text" name="realTimeLimit" class="form-control" value="${problemsForm.realTimeLimit}">  <bean:message key="problem.cpuMetric" />
            </div>   

            <div class="form-group">
                <label><bean:message key="problem.memoryLimit" /></label>                       
                <input type="text" name="memoryLimit" class="form-control" value="${problemsForm.memoryLimit}"> <bean:message key="problem.preciseMemoryMetric" />  								
            </div>  

            <div class="form-group">
                <label><bean:message key="problem.outputLimit" /></label>  
                <input type="text" name="outputLimit" class="form-control" value="${problemsForm.outputLimit}"> <bean:message key="problem.preciseMemoryMetric" />  
            </div>

            <div class="checkbox">
                <label>
                    <input type="checkbox" name="hidden" <c:if test="${problemsForm.hidden}">checked</c:if> >
                    <bean:message key="problem.isHidden" />
                </label>
            </div>
        </div>

        <div id="importTab" class="tab-pane">
            <div class="form-group">
                <p><bean:message key="problem.importHint" /></p>
                <a href="dist/problemc.zip"><bean:message key="problem.importLink" /></a>
            </div>
            <div class="form-group">
                <label><bean:message key="problem.selectFile" /></label>
            </div>
            <div class="form-group">
                <input type="file" name="file" class="form-control">
            </div>
        </div>
    </div>

    <button type="submit" class="btn btn-primary">
        <c:choose>
            <c:when test="${problemsForm.newProblem}">
                <bean:message key="problem.addProblem" /> 
            </c:when>
            <c:otherwise>
                <bean:message key="problem.applyChanges" />
            </c:otherwise>
        </c:choose>
    </button>
</form>
