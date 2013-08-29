<script>
    $(document).ready(function() {
        $("#navbarProblems").addClass("active");
    });
</script>

<script type="text/javascript">
    var pageSize = 20;
    var ds = null;
    Ext.onReady(function(){
        // create the Data Store
        ds = new Ext.data.Store({
            // load using script tags for cross domain, if the data in on the same domain as
            // this page, an HttpProxy would be better
            proxy: new Ext.data.HttpProxy({
                url: 'problems.do?reqCode=getProblemList'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root: 'problems',
                totalProperty: 'totalCount'
            }, [
                {name: 'deletable' , mapping: 'deletable'},
                'id',
                'title',
                'owner',
                'create_time',
                'is_healthy'
            ]
        )
        });	

        // the column model has information about grid columns
        // dataIndex maps the column to the specific data field in
        // the data store
        var cm = new Ext.grid.ColumnModel([{
                header: '<bean:message key="problem.id" />',
                dataIndex: 'id',
                width: 25,
                css: 'white-space:normal;'
            },{
                header: '<bean:message key="problem.title" />',
                dataIndex: 'title',
                width: 200,
                renderer: renderTitle
            },{
                header: '<bean:message key="problem.owner" />',
                dataIndex: 'owner',
                width: 150,
                renderer: renderOwner
            },{
                header: '<bean:message key="problem.created" />',
                dataIndex: 'create_time',
                width: 75
            },{
                header: '<bean:message key="problem.status" />',
                dataIndex: 'is_healthy',
                width: 150,
                renderer: renderStatus
            }]
    );

        var problemsToolbar = new Ext.Toolbar({ });

        // add a paging toolbar to the grid's footer
        var paging = new Ext.PagingToolbar({
            store: ds,
            pageSize: pageSize,
            displayInfo: true,
            displayMsg: 'Displaying problems {0} - {1} of {2}',
            emptyMsg: "No problems to display"
        });

        // create the  grid
        var grid = new Ext.grid.GridPanel({
            applyTo: 'problemsGrid',
            title: '<bean:message key="problems.problems"/>',
            autoHeight: true,
            loadMask: true,
            ds: ds,
            cm: cm,
            sm: new Ext.grid.RowSelectionModel({ singleSelect:true }),
            tbar: problemsToolbar,
            bbar: paging,
            viewConfig: {
                forceFit: true
            }
        });    	

        function renderOwner(value, metadata, record, row, col, ds)
        {
            return '<a href="users.do?reqCode=view&login='
                + record.get('owner') + '">' + record.get('owner') + '</a>';
        }

        function renderTitle(value, metadata, record, row, col, ds)
        {
            return '<a href="problems.do?reqCode=view&problemId='
                + record.get('id') + '">' + record.get('title') + '</a>';
        }

        function renderStatus(value, metadata, record, row, col, ds)
        {
            if(value == true)
                return '<font color="green">OK</font>';

            return '<font color="red">UNCHECKED TESTS</font>';
        }

    <c:if test="${permissionCheckerRemote.canAddProblem(autentificationObject.username)}">
            problemsToolbar.addButton({
                text: '<bean:message key="problems.createProblem" />',
                handler: function() { window.location = 'problems.do?reqCode=create'; }
            });  
    </c:if>

            var btnDelete = new Ext.Toolbar.Button({
                text: '<bean:message key="problems.deleteProblem" />',
                cls: 'btnDelete',
                disabled: true,
                handler:function()
                {
                    function commitDelete (btn)
                    {
                        if (btn == 'yes')
                        {
                            problemId = grid.getSelectionModel().getSelected().get('id');
                            var problemConnection =  (new Ext.data.HttpProxy({})).getConnection();
                            problemConnection.request({
                                method: 'POST' ,
                                url: 'problems.do',
                                params: {reqCode: 'delete' , problemId: problemId},
                                callback: function() { ds.load({params:{start:0, limit:pageSize}});}
                            });
                        }
                    }

                    Ext.MessageBox.confirm('<bean:message key="problem.confirmDeleteTitle" />',
                    '<bean:message key="problem.confirmDeleteMsg" />',
                    commitDelete);
                }
            })
            problemsToolbar.addButton(btnDelete);

            // This is callback for enable/disable button "Delete", for selected problem in list.
            grid.on('rowclick', viewOrNotViewDelete);

            function viewOrNotViewDelete (grid , rowIndex , e) {
                if ( grid.getStore().getAt(rowIndex).get('deletable') ) {
                    btnDelete.enable();
                } else {
                    btnDelete.disable();
                }
            }

            problemsToolbar.doLayout();

        }); //Ext.onReady()
</script>

<script type="text/javascript">
    Ext.onReady(function(){
        ds.load({params:{start:0, limit:pageSize}});
    });
</script>

<div id="problemsGrid"></div>
