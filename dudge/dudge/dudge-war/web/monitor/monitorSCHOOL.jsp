<script type="text/javascript">

var ds = null;
Ext.onReady(function(){
// create the Data Store
ds = new Ext.data.Store({
	// load using script tags for cross domain, if the data in on the same domain as
	// this page, an HttpProxy would be better
	proxy: new Ext.data.HttpProxy({
		url: 'monitor.do?reqCode=getSchoolMonitorData'
	}),

	reader: new Ext.data.JsonReader({
			root: 'rows',
			totalProperty: 'totalCount',
			id: 'user'
		},
		[
			'place',
			'user',
			<% for(dudge.db.ContestProblem contestProblem :
			sessionObject.getDudge()
			.getContest(
					contestId
					).getContestProblems()) { %>
			'problem' + '<%=contestProblem.getProblemMark()%>',
			<% } %>
			'time'
		   ]
		),

	// remote sorting
	remoteSort: false
	});

function renderPlace(value, metadata, record, row, col, ds)
{
	if(value == '0')
		return '-';

	return value;
}

function renderLogin(value, metadata, record, row, col, ds)
{
	return '<a href="javascript:openUser(\''
		+ value + '\')">' + value + '</a>';
}

function renderProblemCell(value, metadata, record, row, col, ds)
{
	return '<center><b>' + value + '</b></center>';
}

// the column model has information about grid columns
// dataIndex maps the column to the specific data field in
// the data store
var cm = new Ext.grid.ColumnModel([
	{
	   header: '<bean:message key="monitor.place"/>',
	   dataIndex: 'place',
	   width: 50
	},
	{
	   header: '<bean:message key="monitor.user"/>',
	   dataIndex: 'user',
	   renderer: renderLogin,
	   width: 80
	},
	<% for(dudge.db.ContestProblem contestProblem :
	sessionObject.getDudge()
	.getContest(
			contestId
			).getContestProblems()) { %>
	{
	   header: '<%=contestProblem.getProblemMark()%>',
	   dataIndex: 'problem' + '<%=contestProblem.getProblemMark()%>',
	   renderer: renderProblemCell,
	   width: 25
	},
	<% } %>
	{
	   header: '<bean:message key="monitor.time" />',
	   dataIndex: 'time',
	   width: 40
	}
	]);

var monitorToolbar = new Ext.Toolbar();

var grid = new Ext.grid.GridPanel({
	applyTo: 'monitorGrid',
	title: '<bean:message key="monitor.monitor"/>',
	ds: ds,
	cm: cm,
	sm: new Ext.grid.RowSelectionModel({ singleSelect:true }),
	autoHeight: true,
	loadMask: true,
	tbar: monitorToolbar
});

monitorToolbar.addButton(
	{
		text: '<bean:message key="monitor.update" />',
		handler: function()
		{
			ds.load({params:{contestId: <%=contestId%>}});
			grid.getView().refresh(false);
		}
	}
	);

monitorToolbar.doLayout();

}); //Ext.onReady()
</script>

<script type="text/javascript">
Ext.onReady(function(){
	ds.load({params:{contestId: <%=contestId%>}});
});
</script>

<div id="monitorGrid"></div>
