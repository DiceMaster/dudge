<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<script src="ckeditor/ckeditor.js"></script>
<script type="text/javascript">
/*
    var port = null;
    var testToolbar = new Ext.Toolbar({ height: 'auto'});

    Ext.onReady(function(){
        var problemTabs = new Ext.TabPanel({
            renderTo: 'problemTabs',
            activeTab: 0,
            defaults: {autoHeight: true},
            items: [
                {
                    contentEl: 'descriptionTab',
                    title: '<bean:message key="problem.description" />',
                    layout: 'form'
                },
                {
                    contentEl: 'parametersTab',
                    title: '<bean:message key="problem.parameters" />',
                    layout: 'form'
                },
                {
                    contentEl: 'testsTab',
                    title: '<bean:message key="problem.tests" />',
    <c:if test="${problemsForm.newProblem}">
                    disabled: true,
    </c:if>
                    tbar: testToolbar,
                    layout: 'fit'
                },
                {
                    contentEl: 'importTab',
                    title: '<bean:message key="problem.import" />',
                    layout: 'form'
                }
            ]
        });
    });
*/
</script>

<script type="text/javascript">
/*
    // Global variable for catch the current test ID.
    var testId;
    var testType;

    var testProxy =  new Ext.data.HttpProxy({ url: 'problems.do' });
    var testConnection  = testProxy.getConnection();
    // Create the Data Srore for getting data from request test.
    var testDataStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'problems.do?reqCode=getTest'
        }),

        // create reader that reads the Topic records
        reader: new Ext.data.JsonReader({
            root: 'testData',
            totalProperty: 'totalCount'
        }, [
            {name: 'data', mapping: 'data'}

        ]),

        // turn on remote sorting
        remoteSort: false
    });	

    function testDialogShow(button)
    {
        testId = button.id;
        testType = button.name;

        testDataStore.on('load' , function() { 
            Ext.Msg.show({
                title:'<bean:message key="problem.tests.changeTitle" />',
                msg: '<bean:message key="problem.tests.changeMsg" />',
                buttons: Ext.Msg.OKCANCEL,
                fn: commitTestData,
                multiline: true,
                value: testDataStore.getAt(0).get('data'),
                width: 500,
                closable: false
            });
        });

        testDataStore.load( {params:{testId: button.id, testType: button.name}});
    } // testDialogShow()

    //This function sends the changes in test-data to server througth AJAX-request.
    function commitTestData (btn , text) 
    {   
        if(btn == 'ok')
        {
            testConnection.request(
            {
                method: 'POST',
                url: 'problems.do',
                params: {reqCode: 'commitTest' , testId: testId , testType: testType,  data: text} 
            });
        }
    } // commitTestData

    Ext.onReady(function(){

        Ext.QuickTips.init();

        // Create the Data Store for test table.
        var ds = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: 'problems.do?reqCode=getTestList'
            }),



            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'tests',
                totalProperty: 'totalCount'
            }, [
                {name: 'testId' , mapping: 'testId'},
                {name: 'number', mapping: 'number'},
                {name: 'input', mapping: 'input'},
                {name: 'output', mapping: 'output'}            
            ]),

            // turn on remote sorting
            remoteSort: false
        });

        // Marking for column model of test table.
        var testGridColumns = new Ext.grid.ColumnModel([
            {
                header: '<bean:message key="problem.tests.testId" />',
                dataIndex: 'testId',
                width: 40
            },   
            {
                header: '<bean:message key="problem.tests.number" />',
                dataIndex: 'number',
                width: 40
            },
            {
                header: "<bean:message key="problem.tests.input" />",
                dataIndex: 'input',
                width: 80,
                renderer: render
            },
            {
                header: "<bean:message key="problem.tests.output" />",
                dataIndex: 'output',
                width: 80,
                renderer: render
            }
        ]);

        function render(value, metadata, record, row, col, ds) {
            // Id is a unique test ID.
            var id = parseInt(row) * 2 + parseInt(col);
            return '<input type="button" id="' 
                + record.get('testId')+  '" name="' 
                + parseInt(col-2) +  '" onclick="testDialogShow(this)" value="<bean:message key="problem.tests.change" />" >';
        }

        // Test table.
        testGrid = new Ext.grid.EditorGridPanel({
            applyTo: 'testsTable',
            ds: ds,
            cm: testGridColumns,
            sm: new Ext.grid.RowSelectionModel(),
            autoHeight: true
        });

        ds.load();
        ds.on('load' , function () {
            testGrid.getView().refresh(false);
        });

        testToolbar.addButton({
            text: '<bean:message key="problem.tests.add" />',
            handler : function()
            {
                testConnection.request(
                {
                    method: 'GET',
                    url: 'problems.do',
                    params: {reqCode: 'addTest'},
                    callback: function() {ds.load();}
                });
            }
        });
        testToolbar.addButton({
            text: '<bean:message key="problem.tests.remove" />',
            handler: function()
            {
                testId = testGrid.getSelectionModel().getSelected().get('testId');
                testConnection.request( 
                {
                    method: 'GET',
                    url: 'problems.do',
                    params: {reqCode: 'deleteTest' , testId: testId},
                    callback: function() {ds.load();}    
                });
            }
        });
    });
*/
</script>

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
        <li><a href="#testsTab" data-toggle="tab"><bean:message key="problem.tests" /></a></li>
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

        <div id="testsTab" class="tab-pane">
            <div id="testsTable"></div>
        </div>

        <div id="importTab" class="tab-pane">
            <div class="form-group">
                <p><bean:message key="problem.importHint" /></p>
                <a href="#"><bean:message key="problem.importLink" /></a>
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
