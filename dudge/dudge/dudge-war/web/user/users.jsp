<script type="text/javascript">
var pageSize = 20;
var ds = null;
Ext.onReady(function(){
// create the Data Store
ds = new Ext.data.Store({
	// load using script tags for cross domain, if the data in on the same domain as
	// this page, an HttpProxy would be better
	proxy: new Ext.data.HttpProxy({
		url: 'users.do?reqCode=getUserList'
	}),

	// create reader that reads the Topic records
	reader: new Ext.data.JsonReader({
		root: 'users',
		totalProperty: 'totalCount'
	}, [
		{name: 'login', mapping: 'login'},
		{name: 'realname', mapping: 'realname'},
		{name: 'organization', mapping: 'organization'},
		{name: 'regdate', mapping: 'regdate'}
	   ])
});	

function renderLogin(value, metadata, record, row, col, ds)
{
	return '<a href="users.do?reqCode=view&login='
		+ value + '">' + value + '</a>';
}

// the column model has information about grid columns
// dataIndex maps the column to the specific data field in
// the data store
var cm = new Ext.grid.ColumnModel([{
	   header: '<bean:message key="user.login"/>',
	   dataIndex: 'login',
	   width: 100,
	   renderer: renderLogin,
	   css: 'white-space:normal;'
	},{
	   header: '<bean:message key="user.regDate" />',
	   dataIndex: 'regdate',
	   width: 150
	},{
	   header: '<bean:message key="user.realName" />',
	   dataIndex: 'realname',
	   width: 200
	   //renderer: renderLast
	},{
		header: '<bean:message key="user.organization" />',
		dataIndex: 'organization',
		width: 150
	}]);

var usersToolbar = new Ext.Toolbar({  });

var paging = new Ext.PagingToolbar({
	store: ds,
	pageSize: pageSize,
	displayInfo: true,
	displayMsg: 'Displaying users {0} - {1} of {2}',
	emptyMsg: "No users to display"
});    

var grid = new Ext.grid.GridPanel({
	applyTo: 'usersGrid',
	title: '<bean:message key="users.users"/>',
	ds: ds,
	tbar: usersToolbar,
	bbar: paging,
	autoHeight: true,
	loadMask: true,
	cm: cm,
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	loadMask: true,
	viewConfig: {
		forceFit: true
	}

});
	
<%
if ( pcb.canDeleteUser(authenticationObject.getUsername()) ) {
%>
usersToolbar.addButton(
	{
		text: '<bean:message key="users.delete" />',
		handler: function()
		{
			function commitDelete (btn)
			{
				if (btn == 'yes')
				{
					var login = grid.getSelectionModel().getSelected().get('login');
					var proxy =  new Ext.data.HttpProxy({
						url: 'users.do?reqCode=getUserList'
					});

					proxy.getConnection().request({
					method: 'GET' ,
					url: 'users.do' ,
					params: {reqCode: 'delete' , login: login} ,
					callback: function() { ds.load({params:{start:0, limit:pageSize}});}
					});
					ds.load({params:{start:0, limit:pageSize}});
					grid.getView().refresh(false);
				}
			}

			Ext.MessageBox.confirm('<bean:message key="user.confirmDeleteTitle" />',
			'<bean:message key="user.confirmDeleteMsg" />',
			commitDelete);
		}
	}
	);
<%}%>

usersToolbar.doLayout();

}); //Ext.onReady()
</script>

<script type="text/javascript">
Ext.onReady(function(){
	ds.load({params:{start:0, limit:pageSize}});
});
</script>

<div id="usersGrid"></div>
