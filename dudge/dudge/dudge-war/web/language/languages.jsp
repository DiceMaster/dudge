<script type="text/javascript">
var ds = null;
Ext.onReady(function(){
	ds = new Ext.data.Store({
		// load using script tags for cross domain, if the data in on the same domain as
		// this page, an HttpProxy would be better
		proxy: new Ext.data.HttpProxy({
			url: 'languages.do?reqCode=getLanguagesList'
		}),

		// create reader that reads the Topic records
		reader: new Ext.data.JsonReader({
			root: 'languages',
			totalProperty: 'totalCount'
		}, [
		{name: 'id', mapping: 'id'},
		{name: 'title', mapping: 'title'},
		{name: 'description', mapping: 'description'},
		{name: 'editable', mapping: 'editable' },
		{name: 'deletable', mapping: 'deletable'}
	])
});	

function renderId(value, metadata, record, row, col, ds)
{
	return '<a href="languages.do?reqCode=view&languageId=' + value + '">' + value + '</a>';
}
	
var cm = new Ext.grid.ColumnModel([{
	header: '<bean:message key="language.id"/>',
	dataIndex: 'id',
	width: 100,
	renderer: renderId,
	css: 'white-space:normal;'
	},{
	header: '<bean:message key="language.title" />',
	dataIndex: 'title',
	width: 150
	},{
	header: '<bean:message key="language.description" />',
	dataIndex: 'description',
	width: 200
	}
	]);

var languagesToolbar = new Ext.Toolbar({  });

var grid = new Ext.grid.GridPanel({
	applyTo: 'languagesGrid',
	title: '<bean:message key="languages.languages"/>',
	ds: ds,
	tbar: languagesToolbar,
	cm: cm,
	autoHeight: true,
	loadMask: true,
	sm: new Ext.grid.RowSelectionModel({ singleSelect:true }),
    viewConfig: {
        forceFit: true
    }
});

<% if (pcb.canAddLanguage(sessionObject.getUsername())) {%>		
languagesToolbar.addButton({
	text: '<bean:message key="languages.create" />',
	handler: function() { window.location = "languages.do?reqCode=create"; }
	});
<%}%>
			 
var btnEdit = new Ext.Toolbar.Button({
	text: '<bean:message key="languages.edit" />',
	disabled: true,
	handler:function()
	{
		var languageId = grid.getSelectionModel().getSelected().get('id');
		window.location = "languages.do?reqCode=edit&languageId=" + languageId;
	}
	});

languagesToolbar.addButton(btnEdit);

var btnDelete = new Ext.Toolbar.Button({
   text: '<bean:message key="languages.delete" />',
   disabled: true,
   handler: function()
   {
	   function commitDelete (btn) {
		   if (btn == 'yes') {
			   var id = grid.getSelectionModel().getSelected().get('id');
			   var proxy =  new Ext.data.HttpProxy({
				   url: 'languages.do?reqCode=getLanguagesList'
			   });

			   proxy.getConnection().request({
				   method: 'GET' ,
				   url: 'languages.do' ,
				   params: {reqCode: 'delete' , id: id} ,
				   callback: function() { ds.load({});}
			   });
			   grid.getView().refresh(false);
		   }
	   }

		Ext.MessageBox.confirm('<bean:message key="language.confirmDeleteTitle" />',
			 '<bean:message key="language.confirmDeleteMsg" />',
			 commitDelete);
	}
 });

languagesToolbar.addButton(btnDelete);

// This is callback for enabling/disabling button "Edit", for selected language in list.
grid.on('rowclick' , viewOrNotViewEdit);
function viewOrNotViewEdit (grid , rowIndex , e) {
	if ( grid.getStore().getAt(rowIndex).get('editable') ) {
		btnEdit.enable();
	} else {
	btnEdit.disable();
	}
}

// This is callback for enabling/disabling button "Delete", for selected language in list.
grid.on('rowclick' , viewOrNotViewDelete);
function viewOrNotViewDelete (grid , rowIndex , e) {
	if ( grid.getStore().getAt(rowIndex).get('deletable') ) {
		btnDelete.enable();
	} else {
		btnDelete.disable();
	}
}

languagesToolbar.doLayout();

}); //Ext.onReady()
 </script>

<script type="text/javascript">
	Ext.onReady(function(){
		ds.load({});
	});
</script>

<div id="languagesGrid"></div>
