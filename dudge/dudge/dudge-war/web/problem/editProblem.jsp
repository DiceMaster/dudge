<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="session" />

<script type="text/javascript">
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

        // If we create new problem, problem with this ID doesn't exists, so we cant't add/edit tests for it.
</script>

    <script type="text/javascript">
  
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
    </script>

<html:form styleId="problemsForm" styleClass="x-form" action="problems.do" method="POST" enctype="multipart/form-data">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">
                <div class="x-form-bd" id="container">
                    <input type="hidden" name="contestId" value="${contestId}"/>
                    <html:hidden property="problemId" />
                    <c:choose>
                        <c:when test="${problemsForm.newProblem}">
                            <html:hidden property="reqCode" value="submitCreate" />
                            <h3 style="margin-bottom:5px;"><bean:message key="problem.newProblem" /></h3>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="reqCode" value="submitEdit" />
                            <h3 style="margin-bottom:5px;"><bean:message key="problem.problem" /> ${problemsForm.problemId}: ${problemsForm.title}</h3>
                        </c:otherwise>
                    </c:choose>

                    <div id="problemTabs">

                        <div id="descriptionTab" class="x-hide-display">
                            <div class="x-form-item">
                                <label><bean:message key="problem.title" /></label>
                                <div class="x-form-element">
                                    <html:text property="title" size="60" styleClass="x-form-text x-form-field"/>
                                </div>
                            </div>
                            <div class="x-form-item">
                                <label><bean:message key="problem.author" /></label>
                                <div class="x-form-element">
                                    <html:text property="author" size="40" styleClass="x-form-text x-form-field"/>
                                </div>
                            </div>
                            <div class="x-form-item">
                                <label><bean:message key="problem.description" /></label>
                                <div class="x-form-element">
                                    <html:textarea property="description" cols="80" rows="25" styleClass="x-form-field"/> 
                                </div>
                            </div>
                        </div>

                        <div id="parametersTab" class="x-hide-display">					
                            <div class="x-form-item">
                                <label><bean:message key="problem.cpuTimeLimit" /></label>
                                <div class="x-form-element">
                                    <html:text property="cpuTimeLimit" styleClass="x-form-text x-form-field"/>  <bean:message key="problem.cpuMetric"/>
                                </div>
                            </div>  

                            <div class="x-form-item">
                                <label><bean:message key="problem.realTimeLimit" /></label>
                                <div class="x-form-element">
                                    <html:text property="realTimeLimit" styleClass="x-form-text x-form-field"/>  <bean:message key="problem.cpuMetric" />
                                </div>
                            </div>   

                            <div class="x-form-item">
                                <label><bean:message key="problem.memoryLimit" /></label>                       
                                <div class="x-form-element">
                                    <html:text property="memoryLimit" styleClass="x-form-text x-form-field"/> <bean:message key="problem.preciseMemoryMetric" />  								
                                </div>                 
                            </div>  

                            <div class="x-form-item">
                                <label><bean:message key="problem.outputLimit" /></label>  
                                <div class="x-form-element">
                                    <html:text property="outputLimit" styleClass="x-form-text x-form-field"/> <bean:message key="problem.preciseMemoryMetric" />  
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="problem.isHidden" /></label>
                                <div class="x-form-element">
                                    <html:checkbox property="hidden">
                                    </html:checkbox>
                                </div>
                            </div>

                        </div>

                        <div id="testsTab" class="x-hide-display">
                            <div id="testsTable"></div>
                        </div>

                        <div id="importTab" class="x-hide-display">
                            <div class="x-form-item">
                                <label><bean:message key="problem.importHint" /></label>
                                <a href="#"><bean:message key="problem.importLink" /></a>
                            </div>
                            <br/>
                            <div class="x-form-item">
                                <label><bean:message key="problem.selectFile" /></label>
                            </div>
                            <div class="x-form-item">
                                <html:file property="file"/>
                            </div>
                        </div>
                    </div>

                    <html:submit>
                        <c:choose>
                            <c:when test="${problemsForm.newProblem}">
                                <bean:message key="problem.addProblem" /> 
                            </c:when>
                            <c:otherwise>
                                <bean:message key="problem.applyChanges" />
                            </c:otherwise>
                        </c:choose>
                    </html:submit>	
                </div>
            </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
