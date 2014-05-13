<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />

<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 
<link rel="stylesheet" type="text/css" href="select2/select2.css" />
<link rel="stylesheet" type="text/css" href="select2/select2-bootstrap.css" />

<script src="scripts/jquery.scrollintoview.min.js"></script>
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
        var languagesGrid = $('#languagesGrid').dataTable( {
            "aaData": languagesDataSet,
            'sDom': 'rt',
            'bPaginate': false,
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            "aoColumnDefs": [
                { "bVisible": false, "aTargets": [ 1 ] }
            ],
            fnCreatedRow: function( nRow, aData, index ) {
                var checkbox = $('<input type="checkbox" id="language_' + aData[1] + '"' + (aData[0] ? " checked" : "") +'>');
                checkbox.change(function() {
                    languagesDataSet[index][0] = $(this).is(':checked');
                });
                $('td:eq(0)', nRow).empty().append(checkbox);
                $('td:eq(1)', nRow).html( '<a href="languages.do?reqCode=view&languageId=' + aData[1] + '">' + aData[2] +'</a>' );
            }
        } );
        
        var problemsDataSet = ${contestsForm.encodedContestProblems};
        for (var iRow = 0; iRow < problemsDataSet.length; iRow++) {
            problemsDataSet[iRow].push("");
        }
        var problemsGrid = $('#problemsGrid').dataTable( {
            "aaData": problemsDataSet,
            'sDom': 'rt',
            'bPaginate': false,
            'aoColumnDefs': [
                { 'bVisible': false, 'aTargets': [ 3 ] }
            ],
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData, index ) {
                $('td:eq(0)', nRow).html( '<div class="dudge-table-vertical-center">' + aData[0] + '</div>' );
                var buttonDown = $('<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-down text-muted"></span></button>');
                if (index === problemsDataSet.length - 1) {
                    buttonDown.addClass('invisible');
                }
                buttonDown.click(function() {
                    var swap = problemsDataSet[index];
                    problemsDataSet[index] = problemsDataSet[index + 1];
                    problemsDataSet[index + 1] = swap;
                    problemsDataSet[index][0] = String.fromCharCode("A".charCodeAt() + index);
                    problemsDataSet[index + 1][0] = String.fromCharCode("A".charCodeAt() + index + 1);
                    problemsDataSet[index][1] = index;
                    problemsDataSet[index + 1][1] = index + 1;
                    problemsGrid.fnClearTable();
                    problemsGrid.fnAddData(problemsDataSet);
                });
                var buttonUp = $('<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-chevron-up text-muted"></span></button>');
                if (index === 0) {
                    buttonUp.addClass('invisible');
                }
                buttonUp.click(function() {
                    var swap = problemsDataSet[index];
                    problemsDataSet[index] = problemsDataSet[index - 1];
                    problemsDataSet[index - 1] = swap;
                    problemsDataSet[index][0] = String.fromCharCode("A".charCodeAt() + index);
                    problemsDataSet[index - 1][0] = String.fromCharCode("A".charCodeAt() + index - 1);
                    problemsDataSet[index][1] = index;
                    problemsDataSet[index - 1][1] = index + 1;
                    problemsGrid.fnClearTable();
                    problemsGrid.fnAddData(problemsDataSet);
                });
                if (index > 0 && index < problemsDataSet.length - 1) {
                    var buttonGroup = $('<div class="btn-group"></div>');
                    buttonGroup.append(buttonDown).append(buttonUp);
                    $('td:eq(1)', nRow).empty().append(buttonGroup);
                } else {
                    $('td:eq(1)', nRow).empty().append(buttonDown).append(buttonUp);
                }
                var problemDisplayName = isNaN(aData[2]) ? "" : aData[2] + ' - ' + aData[3];
                var select = $('<input type="hidden" class="form-control" id="problem_id_row' + index + '" value="' + problemDisplayName + '">');
                $('td:eq(2)', nRow).empty().append(select);
                select.select2({
                    placeholder: '<bean:message key="contest.problems.selectProblem"/>',
                    minimumInputLength: 1,
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
                        var value = $(element).val();
                        if (value) {
                            callback(value.split(' - '));
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
                select.change(function ( e ) {
                    var iRow = $(this).attr("id").replace("problem_id_row", "");
                    problemsDataSet[iRow][2] = e.added[0];
                    problemsDataSet[iRow][3] = e.added[1];
                });
                var problem_cost = $( '<input class="form-control" type="text" id="problem_cost_row' + index + '" value="' + aData[4] + '">' );
                problem_cost.change(function() {
                    var iRow = $(this).attr("id").replace("problem_cost_row", "");
                    problemsDataSet[iRow][4] = parseInt($(this).val());
                    if (problemsDataSet[iRow][4] === NaN) {
                        problemsDataSet[iRow][4] = 1;
                    }
                });
                $('td:eq(3)', nRow).html( problem_cost );
                var deleteButton = $('<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove text-muted"></span></button>');
                deleteButton.click( function () {
                    problemsDataSet.splice(index, 1);
                    for (var iRow = index; iRow < problemsDataSet.length; iRow++) {
                        problemsDataSet[iRow][0] = String.fromCharCode("A".charCodeAt() + iRow);
                        problemsDataSet[iRow][1] = iRow;
                    }
                    problemsGrid.fnClearTable();
                    problemsGrid.fnAddData(problemsDataSet);
                });
                $('td:eq(4)', nRow).empty().append(deleteButton);
            }
        } );
        $('#addProblem').click(function(){
            problemsDataSet.push([
                String.fromCharCode("A".charCodeAt() + problemsDataSet.length),
                problemsDataSet.length,
                NaN,
                "",
                1,
                ""
            ]);
            problemsGrid.fnClearTable();
            problemsGrid.fnAddData(problemsDataSet);
        });
        var usersDataSet = ${contestsForm.encodedRoles};
        var usersGrid = $('#usersGrid').dataTable( {
            "aaData": usersDataSet,
            'sDom': 'rt',
            'bSort' : false,
            'bPaginate': false,
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
        for (var iApplication = 0; iApplication < applicationsDataSet.length; iApplication++) {
            applicationsDataSet[iApplication].push("");
            applicationsDataSet[iApplication].push("");
        }
        var applicationsGrid = $('#applicationsGrid').dataTable( {
            "aaData": applicationsDataSet,
            'sDom': 'rt',
            'bPaginate': false,
            'bSort' : false,
            'oLanguage': {
                'sUrl': 'l18n/<bean:message key="locale.currentTag"/>.txt'
            },
            fnCreatedRow: function( nRow, aData, index ) {
                $('td:eq(0)', nRow).html( '<a href="users.do?reqCode=view&login=' + aData[0] + '">' + aData[0] +'</a>' );
                $('td:eq(1)', nRow).text( (new Date(aData[1])).toLocaleString() );
                var status = '';
                switch (aData[3]) {
                    case 'NEW': status = '<bean:message key="contest.applications.new"/>'; break;
                    case 'ACCEPTED': status = '<bean:message key="contest.applications.accepted"/>'; break;
                    case 'DECLINED': status = '<bean:message key="contest.applications.declined"/>'; break;
                }
                $('td:eq(3)', nRow).text( status );
                var declineButton = $('<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove text-danger"></span></button>');
                declineButton.click( function () {
                    applicationsDataSet[index][3] = 'DECLINED';
                    applicationsGrid.fnClearTable();
                    applicationsGrid.fnAddData(applicationsDataSet);
                    for (var iUser = 0; iUser < usersDataSet.length; iUser++) {
                        if (usersDataSet[iUser][0] === applicationsDataSet[index][0]) {
                            break;
                        }
                    }
                    if (iUser === usersDataSet.length) {
                        return;
                    }
                    usersDataSet.splice(iUser, 1);    
                    usersGrid.fnClearTable();
                    usersGrid.fnAddData(usersDataSet);
                });
                $('td:eq(4)', nRow).empty().append(declineButton);
                var acceptButton = $('<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-ok text-success"></span></button>');
                acceptButton.click( function () {
                    applicationsDataSet[index][3] = 'ACCEPTED';
                    applicationsGrid.fnClearTable();
                    applicationsGrid.fnAddData(applicationsDataSet);
                    var user = [ applicationsDataSet[index][0], 'USER'];
                    for (var iUser = 0; iUser < usersDataSet.length; iUser++) {
                        if (usersDataSet[iUser][0] === user[0]) {
                            return;
                        }
                    }
                    usersDataSet.push(user);
                    usersGrid.fnClearTable();
                    usersGrid.fnAddData(usersDataSet);
                });
                $('td:eq(5)', nRow).empty().append(acceptButton);
            }
        } );
        
        $('#contestForm').submit(function(event) {
            $('.has-error').removeClass('has-error');
            var hasError = false;
            var firstTabWithError = null;
            var firstElementWithError = null;

            var languages = [];
            for (var iLanguage = 0; iLanguage < languagesDataSet.length; iLanguage++) {
                languages[iLanguage] = {};
                languages[iLanguage].id = languagesDataSet[iLanguage][1];
                languages[iLanguage].enabled = languagesDataSet[iLanguage][0];
            }
            
            var problems = [];
            for (var iProblem = 0; iProblem < problemsDataSet.length; iProblem++) {
                problems[iProblem] = {};
                problems[iProblem].problemId = problemsDataSet[iProblem][2];
                if (isNaN(problems[iProblem].problemId)) {
                    hasError = true;
                    if (firstTabWithError === null) {
                        firstTabWithError = $('#contestTabs a[href="#problemsTab"]');
                        firstElementWithError = $('#problem_id_row' + iProblem);
                    }
                    $('#problem_id_row' + iProblem).addClass('has-error');
                }
                problems[iProblem].order = problemsDataSet[iProblem][1];
                problems[iProblem].mark = problemsDataSet[iProblem][0];
                problems[iProblem].cost = problemsDataSet[iProblem][4];
            }
            
            var roles = [];
            for (var iRole = 0; iRole < usersDataSet.length; iRole++) {
                roles[iRole] = {};
                roles[iRole].login = usersDataSet[iRole][0];
                roles[iRole].role = usersDataSet[iRole][1];
            }

            var applications = [];
            for (var iApplication = 0; iApplication < applicationsDataSet.length; iApplication++) {
                applications[iApplication] = {};
                applications[iApplication].login = applicationsDataSet[iApplication][0];
                applications[iApplication].filing_time = applicationsDataSet[iApplication][1];
                applications[iApplication].message = applicationsDataSet[iApplication][2];
                applications[iApplication].status = applicationsDataSet[iApplication][3];
            }
            
            if (hasError) {
                event.preventDefault();
                firstTabWithError.tab('show');
                firstElementWithError.scrollintoview();
                return;
            }
            $('#encodedContestLanguages').val(JSON.stringify(languages));
            $('#encodedContestProblems').val(JSON.stringify(problems));
            $('#encodedRoles').val(JSON.stringify(roles));
            $('#encodedApplications').val(JSON.stringify(applications));
        });
    } );
</script>

<form class="form" id="contestForm" action="contests.do" method="POST">
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

    <ul class="nav nav-tabs" id="contestTabs">
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
            <button type="button" id="addProblem" class="btn btn-default"><bean:message key="contest.problems.add"/></button>
            <table class="table" id="problemsGrid">
                <thead>
                    <tr>
                        <th class="col-sm-1"><bean:message key="contest.problems.mark"/></th>
                        <th class="col-sm-2"><bean:message key="contest.problems.order"/></th>                        
                        <th class="col-sm-7"><bean:message key="contest.problems.problem"/></th>
                        <th></th>
                        <th class="col-sm-1"><bean:message key="contest.problems.cost"/></th>
                        <th class="col-sm-1"><bean:message key="contest.problems.remove"/></th>
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
                        <th><bean:message key="contest.applications.decline"/></th>
                        <th><bean:message key="contest.applications.accept"/></th>
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

    <input type="hidden" id="encodedRoles" name="encodedRoles" />
    <input type="hidden" id="encodedContestProblems" name="encodedContestProblems" />
    <input type="hidden" id="encodedContestLanguages" name="encodedContestLanguages" />
    <input type="hidden" id="encodedApplications" name="encodedApplications" />

    <c:choose>
        <c:when test="${contestsForm.newContest}">
            <input type="submit" value="<bean:message key="contest.addContest" />">
        </c:when>
        <c:otherwise>
            <input type="submit" value="<bean:message key="contest.applyChanges" />">
        </c:otherwise>
    </c:choose>
</form>
