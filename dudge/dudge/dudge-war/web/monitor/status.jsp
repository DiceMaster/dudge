<script>
    $(document).ready(function() {
        $("#navbarStatus").addClass("active");
    });
</script>

<script type="text/javascript">
var ds = null;
var solutionCount = 20;
Ext.onReady(function(){
// create the Data Store
ds = new Ext.data.Store({
	// load using script tags for cross domain, if the data in on the same domain as
	// this page, an HttpProxy would be better
	proxy: new Ext.data.HttpProxy({
		url: 'solutions.do?reqCode=getLastSolutions'
	}),

	reader: new Ext.data.JsonReader({
		root: 'solutions',
		totalProperty: 'totalCount',
		id: 'solutionId'
		}, [
			{name: 'solutionId', mapping: 'solutionId'},
			{name: 'submitTime', mapping: 'submitTime'},
			{name: 'user', mapping: 'user'},
			{name: 'contestId', mapping: 'contestId'},
			{name: 'problemId', mapping: 'problemId'},
			{name: 'languageId', mapping: 'languageId'},
			{name: 'status', mapping: 'status'}
		   ]
		),

	// turn on remote sorting
	remoteSort: false
	});

function renderLogin(value, metadata, record, row, col, ds)
{
	return '<a href="users.do?reqCode=view&login='
		+ record.get('user') + '">' + record.get('user') + '</a>';
}

function renderSolution(value, metadata, record, row, col, ds)
{
	return '<a href="solutions.do?reqCode=view&solutionId='
		+ record.get('solutionId') + '">' + record.get('solutionId') + '</a>';
}

function renderContest(value, metadata, record, row, col, ds)
{
	return '<a href="contests.do?reqCode=view&contestId='
		+ record.get('contestId') + '">' + record.get('contestId') + '</a>';
}

function renderProblem(value, metadata, record, row, col, ds)
{
	return '<a href="problems.do?reqCode=view&problemId='
		+ record.get('problemId') + '">' + record.get('problemId') + '</a>';
}

function renderLanguage(value, metadata, record, row, col, ds)
{
	return '<a href="languages.do?reqCode=view&languageId='
		+ record.get('languageId') + '">' + record.get('languageId') + '</a>';
}

// the column model has information about grid columns
// dataIndex maps the column to the specific data field in
// the data store
var cm = new Ext.grid.ColumnModel([
	{
	   header: '<bean:message key="solution.id"/>',
	   dataIndex: 'solutionId',
	   renderer: renderSolution,
	   width: 25
	},
	{
	   header: '<bean:message key="solution.time" />',
	   dataIndex: 'submitTime',
	   width: 110
	},
	{
	   header: '<bean:message key="user.user" />',
	   dataIndex: 'user',
	   renderer: renderLogin,
	   width: 80
	},
	{
		header: '<bean:message key="contest.contest" />',
		dataIndex: 'contestId',
		renderer: renderContest,
		width: 60
	},
	{
		header: '<bean:message key="problem.problem" />',
		dataIndex: 'problemId',
		renderer: renderProblem,
		width: 60
	},
	{
		header: '<bean:message key="language.language" />',
		dataIndex: 'languageId',
		renderer: renderLanguage,
		width: 60
	},
	{
		header: '<bean:message key="solution.status" />',
		dataIndex: 'status',
		width: 150
	}
	]);

var statusToolbar = new Ext.Toolbar();

var grid = new Ext.grid.GridPanel({
	applyTo: 'statusGrid',
	title: '<bean:message key="status.status" />',
	ds: ds,
	cm: cm,
	autoHeight: true,
	selModel: new Ext.grid.RowSelectionModel({ singleSelect:true }),
	tbar: statusToolbar,
	viewConfig: {
		forceFit: true
	}

});

statusToolbar.addButton({
	text: '<bean:message key="status.update" />',
	handler: function(){
		ds.load({params:{limit: solutionCount}});
		grid.getView().refresh(false);
		}
	});

statusToolbar.doLayout();

}); //Ext.onReady()
</script>

<script type="text/javascript">
Ext.onReady(function(){
	ds.load({params:{limit: solutionCount}});
});
</script>

<div id="statusGrid"></div>
