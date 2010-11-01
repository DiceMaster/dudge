<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dudge</title>     
</head>
<body>

<jsp:useBean id="sessionObject" class="dudge.web.SessionObject" scope="session" />
<% dudge.PermissionCheckerRemote pcb = sessionObject.getPermissionChecker(); %>

<link rel="stylesheet" type="text/css" href="/dudge/ext/resources/css/ext-all.css" />
<script type="text/javascript" src="/dudge/ext/adapter/yui/yui-utilities.js"></script>     
<script type="text/javascript" src="/dudge/ext/adapter/yui/ext-yui-adapter.js"></script>
<script type="text/javascript" src="/dudge/ext/ext-all.js"></script>

<script type="text/javascript" src="scripts/SearchField.js"></script>

<%@ include file="WEB-INF/jspf/openFunctions.jspf" %>

<script type="text/javascript">
document.onkeydown = function() {
	if(window.event && window.event.keyCode == 116)
	{ // Capture and remap F5
		window.event.keyCode = 505;
	}

	if(window.event && window.event.keyCode == 505) 
	{ // New action for F5
		//alert('F5 key was pressed');
		return false; 
		// Must return false or the browser will refresh anyway
	}
}
</script>

<script type="text/javascript">
Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

var viewport;
var tabPanel;
Ext.onReady(function(){

    var searchDS = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'search.do?reqCode=search'
        }),
        reader: new Ext.data.JsonReader({
            root: 'results',
            totalProperty: 'totalCount',
            id: 'obid'
        }, [
			{name: 'obtype', mapping: 'obtype'},
			{name: 'obid', mapping: 'obid'},
			{name: 'caption', mapping: 'caption'},
			{name: 'addInfo', mapping: 'addInfo'}
        ])
	});

	var searchCM = new Ext.grid.ColumnModel([
		{
			header: 'name',
			dataIndex: 'caption',
			width: 200,
			css: 'white-space:normal;',
			renderer: renderResult
        }
		]);

	function renderResult(value, metadata, record, row, col, ds) {
		if(record.get('obtype') == 'user')
			return '<a href="javascript:openUser(\'' +
				record.get('obid') +
				'\')">' +
				record.get('caption') +
				+ '</a>';

		if(record.get('obtype') == 'problem')
			return '<a href="javascript:openProblem(' +
				record.get('obid') +
				')">' +
				record.get('caption') +
				+ '</a>';

		return value;
	}
		
	var tb = new Ext.Toolbar();
	var northTitle = null;
	northTitle = '<b>Dudge : <a href=\"javascript:openContest(<%=sessionObject.getContestId()%>)\"><%=sessionObject.getContest().getCaption()%></a></b>';
	<% if(pcb.canModifyContest(sessionObject.getUsername(), sessionObject.getContestId())) { %>
	northTitle = northTitle + ' (<a href=\"javascript:openContest(<%=sessionObject.getContestId()%>, \'edit\')\"><bean:message key="contest.edit"/></a>)';
	<% } %>

	tabPanel = new Ext.TabPanel({
			region: 'center',
			contentEl: 'tabsRegion',
			autoScroll: false,
			defaults: { layout: 'fit' },
			deferredRender: false
		});
	
	viewport = new Ext.Viewport({
	layout: 'fit',
	items: new Ext.Panel({
		layout: 'border',
		contentEl: 'pan',
		titlebar: true,
		title: northTitle,
		tbar: tb,
		items: [
			tabPanel,
			{
				region: 'west',
				contentEl: 'nav',
				titlebar: true,
				title: '<bean:message key="menu.navigation"/>',
				split: true,
				collapsible: true,
				width: 200,
				//minSize: 150,
				maxSize: 400,
				layoutConfig: { animate: true }
			},
			{
				region: 'east',
				contentEl: 'searchPanel',
				titlebar: true,
				title: '<bean:message key="menu.search"/>',
				split: true,
				collapsible: true,
				collapsed: true,
				width: 200,
				//minSize: 150,
				maxSize: 400,
				layout: 'fit',
				layoutConfig: { animate: true },
				tbar: [
					new Ext.app.SearchField({
						store: searchDS,
						width: '180'
					})
				],
				items: new Ext.grid.GridPanel({
					//renderTo: 'searchGrid',
					width: 'auto',
					height: 'auto',
					//loadMask: true,
					ds: searchDS,
					cm: searchCM,
					sm: new Ext.grid.RowSelectionModel({ singleSelect: true })
					})
			}
		]
		})
	});

	tb.addButton({
		text: '<bean:message key="menu.news"/>',
		handler: function() { openNews(); }
	});
	tb.addButton({
		text: '<bean:message key="menu.contests"/>',
		handler: function() { openContests(); }
	});
	tb.addButton({
		text: '<bean:message key="menu.problems"/>',
		handler: function() { openProblems(); }
	});
	tb.addButton({
		text: '<bean:message key="menu.users"/>',
		handler: function() { openUsers(); }
	});
	 tb.addButton({
		text: '<bean:message key="menu.languages"/>',
		handler: function() { openLanguages(tabPanel); }
	});
	tb.addButton({
		text: '<bean:message key="menu.authors"/>',
		handler: function() { openAuthors(); }
	});
	tb.addButton({
		text: '<bean:message key="menu.registration"/>',
		handler: function() { openRegistration(); }
	});	
	
	tb.addFill();
	
	 // add a combobox for change locales to the toolbar.
    var localesArray = [ ['en', '<bean:message key="locale.english" />'], 
                         ['ru', '<bean:message key="locale.russian" />'] ];
    var store = new Ext.data.SimpleStore({
        fields: ['key', 'displayName'],
        data : localesArray
    });
    
    var comboLocale = new Ext.form.ComboBox({
        store: store,
		editable: false,
        displayField:'displayName',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'<bean:message key="locale.currentLocale" />',
        selectOnFocus:true,
        width:65
    });
    comboLocale.on('select' , function (combo,record,index) 
        {
            var locale_name = record.get('key');
	    var localeConnection = new Ext.data.Connection({method: 'POST' , url: 'locale.do'});
	    localeConnection.request({
	    params: {reqCode: 'setLocale' , locale_name: locale_name},
	    callback: function() { window.location.reload( true );}
	    });	    
	}
    );
    
    tb.addField(comboLocale);
	 
	openNews();
});
</script>

<div id="pan">
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<div id="nav" class="x-layout-inactive-content">		
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
</div>

<div id="tabsRegion" class="x-layout-inactive-content" />

<div id="searchPanel" class="x-layout-inactive-content" />

</div>

</body>
</html>
