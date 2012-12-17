<%@page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />

<script type="text/javascript" src="/dudge/ext/examples/ux/CheckColumn.js"></script>
<script type="text/javascript">
    // Defining data stores for using in callback-function, set hidden-fields in contestsForm.
    var userDs;
    var problemDs;
    var languageDs;
    var applicationDs;

    Ext.onReady(function(){
        var userToolbar = new Ext.Toolbar({ height: 'auto' });
        var applicationToolbar = new Ext.Toolbar({ height: 'auto' });
        var problemToolbar = new Ext.Toolbar({ height: 'auto' });

        var contestTabs = new Ext.TabPanel({
            renderTo: 'contestTabs',
            activeTab: 0,
            defaults: {autoHeight: true},
            items: [
                {
                    contentEl: 'descriptionTab',
                    title: '<bean:message key="contest.description" />',
                    layout: 'form'
                },
                {
                    contentEl: 'parametersTab',
                    title: '<bean:message key="contest.parameters" />',
                    layout: 'form'
                },
                {
                    contentEl: 'problemsTab',
                    title: '<bean:message key="contest.problems" />',
                    tbar: problemToolbar,
                    layout: 'fit'
                },
                {
                    contentEl: 'rulesTab',
                    title: '<bean:message key="contest.rules" />',
                    layout: 'fit'
                },
                {
                    contentEl: 'usersTab',
                    title: '<bean:message key="contest.users" />',
                    tbar: userToolbar,
                    layout: 'fit'
                },
                {
                    contentEl: 'applicationsTab',
                    title: '<bean:message key="contest.applications" />',
                    tbar: applicationToolbar,
                    layout: 'fit'
                },
                {
                    contentEl: 'languagesTab',
                    title: '<bean:message key="contest.languages"/>',
                    layout: 'fit'
                }
            ]
        });

        Ext.QuickTips.init();

        /*	var descriptionEditor = new Ext.form.HtmlEditor({applyTo: 'description'});

        var rulesEditor = new Ext.form.HtmlEditor({applyTo: 'rules'});*/

        //////////////////////////////
        // CONTEST ROLES
        /////////////////////////////


        var userGridColumns = new Ext.grid.ColumnModel([{
                header: '<bean:message key="contest.users.login" />',
                dataIndex: 'login',
                width: 70,
                editor: new Ext.form.TextField({
                    allowBlank: false,
                    vtype:'alphanum'
                })
            },{
                header: "<bean:message key="contest.users.role" />",
                dataIndex: 'role',
                width: 120,
                renderer: renderRole,
                editor: new Ext.form.ComboBox({
                    typeAhead: true,
                    triggerAction : 'all',
                    transform: 'role',
                    lazyRender: true
                })
            }
        ]);
  
        function renderRole(value, metadata, record, row, col, ds) {
            if(record.get('role') == 'ADMINISTRATOR') return '<bean:message key="contest.users.admin"/>';
            if(record.get('role') == 'OBSERVER') return '<bean:message key="contest.users.observer"/>';
            if(record.get('role') == 'USER') return '<bean:message key="contest.users.user"/>';
        }
	
        userGridColumns.defaultSortable = true;
        var user = Ext.data.Record.create([
            {name: 'login', type: 'string'},
            {name: 'role'}
        ]);
	
        userDs = new Ext.data.Store({
            // The data obtained from property encodedRoles of Struts form - contestsForm.  
            proxy: new Ext.data.MemoryProxy(${contestsForm.encodedRoles}),
			 
            // the return will be JSON, so lets set up a reader
            reader: new Ext.data.JsonReader({
                root: 'roles',
                totalProperty: 'rolesTotalCount'
            }, [
                {name: 'login' , mapping: 'login'},
                {name: 'role', mapping: 'role'}          
            ]),
		 
            // turn on remote sorting
            remoteSort: false
        });	
        userDs.load();
	 
        var userGrid = new Ext.grid.EditorGridPanel({
            applyTo: 'usersTable',
            width: 'auto',
            autoHeight: true,
            ds: userDs,
            cm: userGridColumns,
            sm: new Ext.grid.RowSelectionModel()
        });
	 
        var userCounter = 0;
        userToolbar.addButton({
            text: '<bean:message key="contest.users.add" />',
            handler : function(){
                userCounter++;
                var u = new user({
                    login: '',
                    role: 'USER',
                    id : userCounter
                });
                userGrid.stopEditing();
                userDs.insert(0, u);
                userGrid.startEditing(0, 0);
            }
        });
        userToolbar.addButton({
            text: '<bean:message key="contest.users.remove" />',
            handler: function()
            {
                var roles = userGrid.getSelectionModel().getSelections();
                for(var i = 0; i < roles.length; i++) {
                    userDs.remove( roles[i] );
                }
                userGrid.getView().refresh(false);
            }
        });
        userToolbar.addButton({
            text: '<bean:message key="contest.users.clear" />',
            handler : function(){
                userDs.removeAll();     
            }
        });
		
        //////////////////////////////
        // CONTEST APPLICATIONS
        /////////////////////////////
		
		
        var applicationGridColumns = new Ext.grid.ColumnModel([{
                header: '<bean:message key="contest.applications.login" />',
                dataIndex: 'login',
                renderer: renderLogin,
                width: 70
            },{
                header: "<bean:message key="contest.applications.filingTime" />",
                dataIndex: 'filing_time',
                width: 150
            },{
                header: "<bean:message key="contest.applications.message" />",
                dataIndex: 'message',
                width: 500
            },{
                header: "<bean:message key="contest.applications.status" />",
                dataIndex: 'status',
                width: 70,
                renderer: renderApplicationStatus
            } 
        ]);
  
        function renderLogin(value, metadata, record, row, col, ds)
        {
            return '<a href="javascript:openUser(\''
                + value + '\')\">' + value + '</a>';
        }
	 
        function renderApplicationStatus(value, metadata, record, row, col, ds) {
            if(record.get('status') == 'NEW') return '<bean:message key="contest.applications.new"/>';
            if(record.get('status') == 'ACCEPTED') return '<bean:message key="contest.applications.accepted"/>';
            if(record.get('status') == 'DECLINED') return '<bean:message key="contest.applications.declined"/>';
        }
	
        applicationGridColumns.defaultSortable = true;
		
        applicationDs = new Ext.data.Store({
		
            // The data obtained from property encodedRoles of Struts form - contestsForm.  
            proxy: new Ext.data.MemoryProxy(${contestsForm.encodedApplications}),
			 
            // the return will be JSON, so lets set up a reader
            reader: new Ext.data.JsonReader({
                root: 'applications',
                totalProperty: 'applicationsTotalCount'
            }, [
                {name: 'login' , mapping: 'login'},
                {name: 'filing_time', mapping: 'filing_time'},
                {name: 'message', mapping: 'message'},
                {name: 'status', mapping: 'status'}
            ]),
		 
            // turn on remote sorting
            remoteSort: false
        });	
        applicationDs.load();
	 
        var applicationGrid = new Ext.grid.GridPanel({
            applyTo: 'applicationsTable',
            width: 'auto',
            height: 'auto',
            ds: applicationDs,
            cm: applicationGridColumns,
            sm: new Ext.grid.RowSelectionModel()
        });
	 
        // Endorse applications -- a user to add a selected list of participants
        // and set the status of the application as ACCEPTED.
        applicationToolbar.addButton({
            text: '<bean:message key="contest.applications.accept" />',
            handler : function(){
			  
                var applications = applicationGrid.getSelectionModel().getSelections();
                for(var i = 0; i < applications.length; i++) {
				    
                    applications[i].set('status' , 'ACCEPTED'); 
					 
                    // Check if this this already include in this contest --- don't include in this case.
                    var isExist = false;
                    for(var j = 0; j < userDs.getCount(); j++) {
                        if(userDs.getAt(j).get('login') == applications[i].get('login')) isExist = true;
                    }
                    if(isExist) continue;
					 
                    userCounter++;
                    var u = new user({
                        login: applications[i].get('login'),
                        role: 'USER',
                        id : userCounter
                    });
                    userGrid.stopEditing();
                    userDs.insert(0, u);
                    userGrid.startEditing(0, 0);
					
                }		  		   
            }
        });
	   
		
        applicationToolbar.addButton({
            text: '<bean:message key="contest.applications.decline" />',
            handler: function()
            {
                var applications = applicationGrid.getSelectionModel().getSelections();
                for(var i = 0; i < applications.length; i++) {
                    applications[i].set('status' , 'DECLINED'); 
                }		  		   
            }
        });
	   
        //////////////////////////////
        // CONTEST PROBLEMS
        /////////////////////////////
		
        var problemGridColumns = new Ext.grid.ColumnModel([{
                header: '<bean:message key="contest.problems.problemId" />',
                dataIndex: 'problemId',
                width: 70,
                editor: new Ext.form.TextField({
                    allowBlank: false,
                    vtype:'alphanum'
                })
            },{
                header: "<bean:message key="contest.problems.order" />",
                dataIndex: 'order',
                width: 70,
                editor: new Ext.form.TextField({
                    allowBlank: false,
                    vtype:'alphanum'
                })
            },{
                header: "<bean:message key="contest.problems.mark" />",
                dataIndex: 'mark',
                width: 70,
                editor: new Ext.form.TextField({
                    allowBlank: false,
                    vtype:'alphanum'
                })
            },{
                header: "<bean:message key="contest.problems.cost" />",
                dataIndex: 'cost',
                width: 70,
                editor: new Ext.form.TextField({
                    allowBlank: false,
                    vtype:'alphanum'
                })
            }
        ]);

        problemGridColumns.defaultSortable = true;
        var problem = Ext.data.Record.create([
            {name: 'problemId', type: 'string'},
            {name: 'order', type: 'string'},
            {name: 'mark', type: 'string'},
            {name: 'cost', type: 'string'}
        ]);

        problemDs = new Ext.data.Store({
	
            // The data obtained from property encodedContestProblems of Struts form - contestsForm.  
            proxy: new Ext.data.MemoryProxy(${contestsForm.encodedContestProblems}),
		 
            // the return will be JSON, so lets set up a reader
            reader: new Ext.data.JsonReader({
                root: 'problems',
                totalProperty: 'problemsTotalCount'
            }, [
                {name: 'problemId' , mapping: 'problemId'},
                {name: 'order', mapping: 'order'},
                {name: 'mark', mapping: 'mark'},
                {name: 'cost', mapping: 'cost'}
            ]),
	 
            // turn on remote sorting
            remoteSort: false
        });	
 
        problemDs.load();
 
        var problemGrid = new Ext.grid.EditorGridPanel({
            applyTo: 'problemsTable',
            width: 'auto',
            autoHeight: true,
            ds: problemDs,
            cm: problemGridColumns,
            sm: new Ext.grid.RowSelectionModel()
        });
 
        var problemCounter = 0;
 
        problemToolbar.addButton({
            text: '<bean:message key="contest.problems.add" />',
            handler : function(){
                problemCounter++;
                var p = new problem({
                    problemId: '0',
                    order: '1',
                    mark: 'A',
                    cost: '1',
                    id : problemCounter
                });
                problemGrid.stopEditing();
                problemDs.insert(0, p);
                problemGrid.startEditing(0, 0);
            }
        });
        problemToolbar.addButton({
            text: '<bean:message key="contest.problems.remove" />',
            handler: function()
            {
                var problems = problemGrid.getSelectionModel().getSelections();
                for(var i = 0; i < problems.length; i++) {
                    problemDs.remove( problems[i] );
                }
                problemGrid.getView().refresh(false);
            }
        });
        problemToolbar.addButton({
            text: '<bean:message key="contest.problems.clear" />',
            handler : function(){
                problemDs.removeAll();     
            }
        });
        //problemGrid.show();
 
        //////////////////////////////
        // CONTEST LANGUAGES
        /////////////////////////////	
 
        function formatBoolean(value){
            return value ? 'Yes' : 'No'; 
        };
 
        var languageGridColumns = new Ext.grid.ColumnModel([{
                xtype: 'checkcolumn',
                header: '<bean:message key="contest.languages.enabled" />',
                dataIndex: 'enabled',
                width: 70
            },{
                header: "<bean:message key="contest.languages.id" />",
                dataIndex: 'id',
                width: 70
            },{
                header: "<bean:message key="contest.languages.title" />",
                dataIndex: 'title',
                width: 120
            },{
                header: "<bean:message key="contest.languages.description" />",
                dataIndex: 'description',
                width: 250
            }
        ]);

        languageDs = new Ext.data.Store({
	
            // The data obtained from property encodedContestLanguages of Struts form - contestsForm.  
            proxy: new Ext.data.MemoryProxy(${contestsForm.encodedContestLanguages}),
		 
            // the return will be JSON, so lets set up a reader
            reader: new Ext.data.JsonReader({
                root: 'languages',
                totalProperty: 'languagesTotalCount'
            }, [
                {name: 'enabled' , mapping: 'enabled', type: 'bool'},
                {name: 'id', mapping: 'id'},
                {name: 'title', mapping: 'title'},
                {name: 'description', mapping: 'description'},
            ]),
	 
            // turn on remote sorting
            remoteSort: false
        });	
 
        languageDs.load();
 
        var languageGrid = new Ext.grid.EditorGridPanel({
            applyTo: 'languagesTable',
            width: 'auto',
            height: 'auto',
            ds: languageDs,
            cm: languageGridColumns,
            sm: new Ext.grid.RowSelectionModel()
        });
        //languageGrid.show();

        languageGrid.doLayout();
    }); //Ext.onReady()

    // Function, handles the form-submit event. 
    function saveCollectionsValues() {
	
        // commit languages
	
        languageDs.commitChanges(); 
        var languages = new Array();
        for (var i = 0; i < languageDs.getCount(); i++) {
            var x = languageDs.getAt(i);
            languages[i] = new Object();
            languages[i].enabled = x.get('enabled');
            languages[i].id = x.get('id');		    		    
        }
        var jsonEncodedLanguages = Ext.util.JSON.encode(languages);
        document.getElementById('encodedContestLanguages').value = jsonEncodedLanguages;
	
        // commit problems
        problemDs.commitChanges(); 
        var problems = new Array();
        for (var i = 0; i < problemDs.getCount(); i++) {
            var x = problemDs.getAt(i);
            problems[i] = new Object();
            problems[i].problemId = x.get('problemId');
            problems[i].order = x.get('order');
            problems[i].mark = x.get('mark');
            problems[i].cost = x.get('cost');
        }
        var jsonEncodedProblems = Ext.util.JSON.encode(problems);
        document.getElementById('encodedContestProblems').value = jsonEncodedProblems;
	
	
        // commit roles
        userDs.commitChanges(); 
        var roles = new Array();
        for (var i = 0; i < userDs.getCount(); i++) {
            var a = userDs.getAt(i);
            roles[i] = new Object();
            roles[i].login = a.get('login');
            roles[i].role = a.get('role');                 
        }
        var jsonEncodedRoles = Ext.util.JSON.encode(roles);
        document.getElementById('encodedRoles').value = jsonEncodedRoles;
	 
        // commit applications
        applicationDs.commitChanges(); 
        var applications = new Array();
        for (var i = 0; i < applicationDs.getCount(); i++) {
            var a = applicationDs.getAt(i);
            applications[i] = new Object();
            applications[i].login = a.get('login');
            applications[i].filing_time = a.get('filing_time');
            applications[i].message = a.get('message');
            applications[i].status = a.get('status');
		 
        }
        var jsonEncodedApplications = Ext.util.JSON.encode(applications);
        document.getElementById('encodedApplications').value = jsonEncodedApplications;
	  
    }
</script>

<html:form styleId="contestsForm" styleClass="x-form" action="contests.do"
           onsubmit="saveCollectionsValues()">
    <div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>
    <div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc">

                <div class="x-form-bd" id="container">
                    <input type="hidden" name="contestId" value="${sessionScope.contestId}"/>

                    <c:choose>
                        <c:when test="${contestsForm.newContest}">
                            <html:hidden property="reqCode" value="submitCreate" />
                            <h3 style="margin-bottom:5px;"><bean:message key="contest.newContest" /></h3>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="reqCode" value="submitEdit" />
                            <h3 style="margin-bottom:5px;">${contestsForm.caption}</h3>
                        </c:otherwise>    
                    </c:choose>

                    <div id="contestTabs">
                        <div id="descriptionTab" class="x-hide-display">
                            <div class="x-form-item">
                                <label><bean:message key="contest.caption" /></label>
                                <div class="x-form-element">
                                    <html:text property="caption" />
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.description" /></label>
                                <div class="x-form-element">
                                    <html:textarea property="description" cols="80" rows="25"/>
                                </div>
                            </div>
                        </div>

                        <div id="parametersTab" class="x-hide-display">
                            <div class="x-form-item">
                                <label><bean:message key="contest.type" /></label>                       
                                <div class="x-form-element">
                                    <html:select  property="contestType" styleId="contestType">
                                        <html:options property="contestTypes"/>
                                    </html:select>							
                                </div>                 
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.isOpen" /></label>
                                <div class="x-form-element">
                                    <html:checkbox property="open">
                                    </html:checkbox>
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.startDate" /></label>  
                                <div class="x-form-element">
                                    <% SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");%>
                                    <html:text property="startDate" styleId="startDate" size="8" />
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.startTime" /></label>
                                <div class="x-form-element">
                                    <html:text property="startHour" styleId="startHour" size="2"  />
                                    <b>:</b>
                                    <html:text property="startMinute" styleId="startMinute" size="2" />
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.duration" /></label>
                                <div class="x-form-element">
                                    <html:text property="durationHours" styleId="durationHours" size="2"  />
                                    <b>:</b>
                                    <html:text property="durationMinutes" styleId="durationMinutes" size="2" />
                                </div>
                            </div>

                            <div class="x-form-item">
                                <label><bean:message key="contest.freezeTime" /></label>
                                <div class="x-form-element">
                                    <html:text property="freezeTime" size="20" />
                                </div>
                            </div>
                        </div>

                        <div id="problemsTab" class="x-hide-display">
                            <div id="problemsTable"></div>
                        </div>

                        <div id="rulesTab" class="x-hide-display">
                            <div class="x-form-item">
                                <label><bean:message key="contest.rules" /></label>
                                <div class="x-form-element">
                                    <html:textarea property="rules" cols="80" rows="25"/>
                                </div>
                            </div>
                        </div>

                        <div class="x-hide-display">
                            <!-- you must define the select box here, as the custom editor for the 'role' column will require it -->
                            <select name="role" id="role" style="display: none;">
                                <option value="ADMINISTRATOR"><bean:message key="contest.users.admin" /></option>
                                <option value="USER"><bean:message key="contest.users.user" /></option>
                                <option value="OBSERVER"><bean:message key="contest.users.observer" /></option>
                            </select>
                        </div>

                        <div id="usersTab" class="x-hide-display">
                            <div id="usersTable"></div>							
                        </div>

                        <div id="applicationsTab" class="x-hide-display">
                            <div id="applicationsTable"></div>
                        </div>

                        <div id="languagesTab" class="x-hide-display">
                            <div id="languagesTable"></div>
                        </div>

                    </div>

                    <html:hidden property="encodedRoles" styleId="encodedRoles" />
                    <html:hidden property="encodedContestProblems" styleId="encodedContestProblems" />
                    <html:hidden property="encodedContestLanguages" styleId="encodedContestLanguages" />
                    <html:hidden property="encodedApplications" styleId="encodedApplications" />

                    <html:submit>
                        <c:choose>
                            <c:when test="${contestsForm.newContest}">
                                <bean:message key="contest.addContest" /> 
                            </c:when>
                            <c:otherwise>
                                <bean:message key="contest.applyChanges" />
                            </c:otherwise>
                        </c:choose>
                    </html:submit>

                </div>
            </div></div></div>
    <div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>
</html:form>
