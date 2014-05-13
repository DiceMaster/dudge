<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 
<link rel="stylesheet" type="text/css" href="select2/select2.css" />
<link rel="stylesheet" type="text/css" href="select2/select2-bootstrap.css" />

<script src="scripts/jquery.dataTables.min.js"></script>
<script src="scripts/dudge-tables.js"></script>
<script src="ckeditor/ckeditor.js"></script>

<script src="select2/select2.min.js"></script>
<c:if test="${sessionScope['org.apache.struts.action.LOCALE'].language != 'en'}">
<script src="select2/select2_locale_${sessionScope['org.apache.struts.action.LOCALE'].language}.js"></script>
</c:if>

<script type="text/javascript">
    $(document).ready(function() {
        var languagesDataSet = ${contestsForm.encodedContestLanguages};
        $('#languagesGrid').dataTable( {
            "aaData": languagesDataSet,
            'sDom': 'rt',
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 1 ] }
            ],
            fnCreatedRow: function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<input type="checkbox" id="language_' + aData[1] + '"' + (aData[0] ? " checked" : "") +'>' );
                $('td:eq(1)', nRow).html( '<a href="languages.do?reqCode=view&languageId=' + aData[1] + '">' + aData[2] +'</a>' );
            }
        } );
        
        var problemsDataSet = ${contestsForm.encodedContestProblems};
        $('#problemsGrid').dataTable( {
            "aaData": problemsDataSet,
            'sDom': 'rt',
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData, index ) {
                $('td:eq(0)', nRow).html( '<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-down text-muted"></span></button><button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-up text-muted"></span></button>' );
                $('td:eq(1)', nRow).html( '<div class="dudge-table-vertical-center">' + aData[1] + '</div>' );
                var select = $('<input type="hidden" class="form-control id="problem_id_row' + index + '" value="' + aData[2] + '">');
                $('td:eq(2)', nRow).empty().append(select);
                select.select2({
                    placeholder: '<bean:message key="contest.problems.selectProblem"/>',
                    minimumInputLength: 2,
                    ajax: {
                        url: 'problems.do',
                        data: function(term, page) {
                            return {
                                reqCode: 'getProblemList',
                                sSearch: term,
                                iSortCol_0: 0,
                                bSortable_0: true,
                                sSortDir_0: 'desc'
                            };
                        },
                        results: function(data, page) {
                             return {results: data.aaData};
                        }
                    },
                    initSelection: function(element, callback) {
                        var id = $(element).val();
                        if (id !== "") {
                            $.ajax("problems.do", {
                                data: {
                                    reqCode: 'getProblem',
                                    problemId: id
                                }
                            }).done(function(data) { callback(data); });
                        }
                    },
                    formatResult: function ( problem ) {
                        return problem[0] + ' - ' + problem[1];
                    },
                    formatSelection: function ( problem ) {
                        return problem[0] + ' - ' + problem[1];
                    },
                    id: function( problem ) {
                        return problem[0];
                    }
                });
                $('td:eq(3)', nRow).html( '<input class="form-control" type="text" id="problem_cost_row' + index + '" value="' + aData[3] + '">' );
            }
        } );
        var usersDataSet = ${contestsForm.encodedRoles};
        $('#usersGrid').dataTable( {
            "aaData": usersDataSet,
            'sDom': 'rt',
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[0] + '">' + aData[0] +'</a>' );
                var role = '';
                switch (aData[1]) {
                    case 'ADMINISTRATOR': role = '<bean:message key="contest.users.admin"/>'; break;
                    case 'OBSERVER': role = '<bean:message key="contest.users.observer"/>'; break;
                    case 'USER': role = '<bean:message key="contest.users.user"/>'; break;
                }
                $('td:eq(1)', nRow).text( role );
            }
        } );
        var applicationsDataSet = ${contestsForm.encodedApplications};
        $('#applicationsGrid').dataTable( {
            "aaData": applicationsDataSet,
            'sDom': 'rt',
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData ) {
                $('td:eq(0)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[0] + '">' + aData[0] +'</a>' );
                $('td:eq(1)', nRow).text( (new Date(aData[1])).toLocaleString() );
                var status = '';
                switch (aData[3]) {
                    case 'NEW': status = '<bean:message key="contest.applications.new"/>'; break;
                    case 'ACCEPTED': status = '<bean:message key="contest.applications.accepted"/>'; break;
                    case 'DECLINED': status = '<bean:message key="contest.applications.declined"/>'; break;
                }
                $('td:eq(3)', nRow).text( status );
            }
        } );
    } );
    
    function saveCollectionsValues() {
        
    }
</script>

<form class="form" action="contests.do" method="POST" onsubmit="saveCollectionsValues()">
    <c:choose>
        <c:when test="${contestsForm.newContest}">
            <input type="hidden" name="reqCode" value="submitCreate">
            <h3><bean:message key="contest.newContest" /></h3>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="reqCode" value="submitEdit">
            <input type="hidden" name="contestId" value="${sessionScope.contestId}">
            <h3>${contestsForm.caption}</h3>
        </c:otherwise>    
    </c:choose>

    <ul class="nav nav-tabs">
        <li class="active"><a href="#descriptionTab" data-toggle="tab"><bean:message key="contest.description" /></a></li>
        <li><a href="#parametersTab" data-toggle="tab"><bean:message key="contest.parameters" /></a></li>
        <li><a href="#problemsTab" data-toggle="tab"><bean:message key="contest.problems" /></a></li>
        <li><a href="#rulesTab" data-toggle="tab"><bean:message key="contest.rules" /></a></li>
        <li><a href="#usersTab" data-toggle="tab"><bean:message key="contest.users" /></a></li>
        <li><a href="#applicationsTab" data-toggle="tab"><bean:message key="contest.applications" /></a></li>
        <li><a href="#languagesTab" data-toggle="tab"><bean:message key="contest.languages"/></a></li>
    </ul>        
    <div class="tab-content">
        <fieldset id="descriptionTab" class="tab-pane active">
            <div class="form-group">
                <label><bean:message key="contest.caption" /></label>
                <input type="text" class="form-control" name="caption" value="${contestsForm.caption}">
            </div>

            <div class="form-group">
                <label><bean:message key="contest.description" /></label>
                <textarea class="ckeditor" name="description">${contestsForm.description}</textarea>
            </div>
        </fieldset>

        <div id="parametersTab" class="tab-pane">
            <div class="form-group">
                <label><bean:message key="contest.type" /></label>                       
                <select class="form-control" name="contestType">
                    <c:forEach items="${contestsForm.contestTypes}" var="type">
                    <option value="${type}" <c:if test="${contestsForm.contestType eq type}">selected</c:if> >${type}</option>
                    </c:forEach>
                </select>							
            </div>

            <div class="checkbox">
                <label>
                    <input type="checkbox" name="open" <c:if test="${contestsForm.open}">checked</c:if> >
                    <bean:message key="contest.isOpen" />
                </label>
            </div>

            <div class="form-group">
                <label><bean:message key="contest.startDate" /></label>  
                <input type="text" class="form-control" name="startDate" size="8" value="${contestsForm.startDate}">
            </div>

            <div class="form-group">
                <label><bean:message key="contest.startTime" /></label>
                <div class="x-form-element">
                    <input type="text" name="startHour" size="2" value="${contestsForm.startHour}">
                    :
                    <input type="text" name="startMinute" size="2" value="${contestsForm.startMinute}">
                </div>
            </div>

            <div class="form-group">
                <label><bean:message key="contest.duration" /></label>
                <div class="x-form-element">
                    <input type="text" name="durationHours" styleId="durationHours" size="2" value="${contestsForm.durationHours}">
                    :
                    <input type="text" name="durationMinutes" styleId="durationMinutes" size="2" value="${contestsForm.durationMinutes}">
                </div>
            </div>

            <div class="form-group">
                <label><bean:message key="contest.freezeTime" /></label>
                <input type="text" class="form-control" name="freezeTime" size="20" value="${contestsForm.freezeTime}">
            </div>
        </div>

        <div id="problemsTab" class="tab-pane">
            <table class="table" id="problemsGrid">
                <thead>
                    <tr>
                        <th class="col-sm-1"></th>
                        <th class="col-sm-1"><bean:message key="contest.problems.mark"/></th>
                        <th class="col-sm-8"><bean:message key="contest.problems.problem"/></th>
                        <th class="col-sm-2"><bean:message key="contest.problems.cost"/></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div id="rulesTab" class="tab-pane">
            <div class="form-group">
                <label><bean:message key="contest.rules" /></label>
                <textarea class="ckeditor" name="rules">${contestsForm.rules}</textarea>
            </div>
        </div>

        <div id="usersTab" class="tab-pane">
            <table class="table" id="usersGrid">
                <thead>
                    <tr>
                        <th><bean:message key="contest.users.login"/></th>
                        <th><bean:message key="contest.users.role"/></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div id="applicationsTab" class="tab-pane">
            <table class="table" id="applicationsGrid">
                <thead>
                    <tr>
                        <th><bean:message key="contest.applications.login"/></th>
                        <th><bean:message key="contest.applications.filingTime"/></th>
                        <th><bean:message key="contest.applications.message"/></th>
                        <th><bean:message key="contest.applications.status"/></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div id="languagesTab" class="tab-pane">
            <table class="table" id="languagesGrid">
                <thead>
                    <tr>
                        <th><bean:message key="contest.languages.enabled"/></th>
                        <th><bean:message key="contest.languages.id"/></th>
                        <th><bean:message key="contest.languages.title"/></th>
                        <th><bean:message key="contest.languages.description"/></th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>

    <input type="hidden" name="encodedRoles" />
    <input type="hidden" name="encodedContestProblems" />
    <input type="hidden" name="encodedContestLanguages" />
    <input type="hidden" name="encodedApplications" />

    <c:choose>
        <c:when test="${contestsForm.newContest}">
            <input type="submit" value="<bean:message key="contest.addContest" />">
        </c:when>
        <c:otherwise>
            <input type="submit" value="<bean:message key="contest.applyChanges" />">
        </c:otherwise>
    </c:choose>
</form>
