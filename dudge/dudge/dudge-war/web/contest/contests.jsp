<link rel="stylesheet" type="text/css" href="css/dudge-styles.css" /> 

<jsp:useBean id="problemsForm" class="dudge.web.forms.ProblemsForm" scope="page" />
<jsp:useBean id="problemsAction" scope="page" class="dudge.web.actions.ProblemsAction" />
<script type="text/javascript" src="scripts/json.js"></script>

<script type="text/javascript">
var pageSize = 20;
var ds = null;
Ext.onReady(function(){
// create the Data Store
ds = new Ext.data.Store({
	// load using script tags for cross domain, if the data in on the same domain as
	// this page, an HttpProxy would be better
	proxy: new Ext.data.HttpProxy({
		url: 'contests.do?reqCode=getContestList'
	}),

	// create reader that reads the Topic records
	reader: new Ext.data.JsonReader({
		root: 'contests',
		totalProperty: 'totalCount'
	}, [
	'joinable',
	'editable',
	'deletable',
	{name: 'id' , mapping: 'id'},
	{name: 'caption', mapping: 'caption'},
	{name: 'type', mapping: 'type'},
	{name: 'starts', mapping: 'starts'},
	{name: 'duration', mapping: 'duration'},
	{name: 'is_open', mapping: 'is_open'}
	   ]),

	// turn on remote sorting
	remoteSort: false
});	

// the column model has information about grid columns
// dataIndex maps the column to the specific data field in
// the data store
var cm = new Ext.grid.ColumnModel([{
	   header: '<bean:message key="contests.caption" />',
	   dataIndex: 'caption',
	   width: 300,
   sortable: true,
   renderer: renderCaption,
	   css: 'white-space:normal;'
	},{
   header: '<bean:message key="contests.type" />',
	   dataIndex: 'type',
	   width: 80,
   sortable: true
	},{
   header: '<bean:message key="contest.isOpen" />',
	   dataIndex: 'is_open',
	   width: 80,
	sortable: true,
	renderer: renderOpen
	},{
	   header: '<bean:message key="contests.starts" />',
	   dataIndex: 'starts',
	   width: 300,
   sortable: true
},{
	   header: '<bean:message key="contests.duration" />',
	   dataIndex: 'duration',
	   width: 150,
   sortable: true,
	   renderer: renderDuration
	}
]);

  function renderOpen(value, metadata, record, row, col, ds) {
	if(record.get('is_open')) return '<bean:message key="contest.open.yes" />';
	return '<bean:message key="contest.open.no" />';
	};

var contestsToolbar = new Ext.Toolbar({});

// create the  grid
var grid = new Ext.grid.GridPanel({
	applyTo: 'contestsGrid',
	title: '<bean:message key="contests.contests"/>',
	autoHeight: true,
	loadMask: true,
	ds: ds,
	cm: cm,
	sm: new Ext.grid.RowSelectionModel({ singleSelect: true }),
	tbar: contestsToolbar,
	viewConfig: {
		forceFit: true
	}
});

function renderCaption(value, metadata, record, row, col, ds)
{
	return '<a href="contests.do?reqCode=view&contestId='
	 + record.get('id')
	 + '">' + record.get('caption') + '</a>';
};

function renderDuration(value, metadata, record, row, col, ds)
{
	return value/3600 + ':' + (value % 3600) / 60;
};

<% if (pcb.canAddContest(sessionObject.getUsername())) { %>		
contestsToolbar.addButton({
	text: '<bean:message key="contest.create" />',
	handler: function() {
		window.location = 'contests.do?reqCode=create';
	}
});
<%};%>

var btnEdit = new Ext.Toolbar.Button({
	text: '<bean:message key="contest.edit" />',
	disabled: true,
	handler:function()
	{
		var contestId = grid.getSelectionModel().getSelected().get('id');
		window.location = 'contests.do?reqCode=edit&contestId=' + contestId;
	}
});

contestsToolbar.addButton(btnEdit);

var btnDelete = new Ext.Toolbar.Button({
	text: '<bean:message key="contest.delete" />',
	disabled: true,
	handler:function()
	{
		function commitDelete (btn)
		{
		if (btn == 'yes')
		{
			var contestId = grid.getSelectionModel().getSelected().get('id');
			var contestConnection =  (new Ext.data.HttpProxy({})).getConnection();
			contestConnection.request({
				method: 'POST' ,
				url: 'contests.do',
				params: {reqCode: 'delete' , contestId: contestId},
				callback: function() { ds.load();}
			});
		}
		}

		Ext.MessageBox.confirm('<bean:message key="contest.confirmDeleteTitle" />',
		'<bean:message key="contest.confirmDeleteMsg" />',
		commitDelete);
	}
});

contestsToolbar.addButton(btnDelete);

// This is callback for enabling/disabling button "Join", for selected contest in list.
grid.on('rowclick' , viewOrNotViewJoin);
function viewOrNotViewJoin (grid , rowIndex , e) {
	if ( grid.getStore().getAt(rowIndex).get('joinable') ) {
		btnJoin.enable();
	} else {
		btnJoin.disable();
	}
}

// This is callback for enabling/disabling button "Edit", for selected contest in list.
grid.on('rowclick' , viewOrNotViewEdit);
function viewOrNotViewEdit (grid , rowIndex , e) {
	if ( grid.getStore().getAt(rowIndex).get('editable') ) {
		btnEdit.enable();
	} else {
		btnEdit.disable();
	}
}

// This is callback for enabling/disabling button "Delete", for selected contest in list.
grid.on('rowclick' , viewOrNotViewDelete);
function viewOrNotViewDelete (grid , rowIndex , e) {
	if ( grid.getStore().getAt(rowIndex).get('deletable') ) {
		btnDelete.enable();
	} else {
		btnDelete.disable();
	}
	}

contestsToolbar.doLayout();

});	//Ext.onReady()
</script>

<script type="text/javascript">
Ext.onReady(function(){
	ds.load({params:{start:0, limit:pageSize}});
});
</script>

<div id="contestsGrid"></div>    
