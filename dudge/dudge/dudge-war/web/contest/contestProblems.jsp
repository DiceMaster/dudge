<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />
<jsp:useBean id="contestsAction" class="dudge.web.actions.ContestsAction" scope="session" />

<script type="text/javascript">
 var problemsToolbar = new Ext.Toolbar({});
 
Ext.onReady(function() {
	
	var grid = new Ext.grid.TableGrid("problemsTable");
	grid.render();

    problemsToolbar.addButton(
		{
			text: '<bean:message key="problem.submitSolution" />',
			handler: function()
			{
			    problemId = grid.getSelectionModel().getSelected().get('tcol-1');
			    openSubmit(problemId, <%=sessionObject.getContestId()%>);
			},
			// Check, can user submit problem in current contest, or not.
				<% if( pcb.canSubmitSolution(sessionObject.getUsername() , sessionObject.getContest().getContestId()) ) { %> 
			disabled: false
				<% } else {%>
			disabled: true
			    <% }; %>
	 }
		);
		 
	<%if(pcb.canModifyContest(sessionObject.getUsername(), sessionObject.getContest().getContestId())) {%>	  
	 problemsToolbar.addButton(
		{
			text: '<bean:message key="solution.resubmitAll" />',
			handler: function()
			{
			    problemId = grid.getSelectionModel().getSelected().get('tcol-1');
			    var contestConnection =  (new Ext.data.HttpProxy({})).getConnection();
			    contestConnection.request({
					method: 'POST' ,
					url: 'contests.do',
					params: {reqCode: 'resubmitAll' , problemId: problemId},
					callback: function() { openStatus();}    
			    });
			}			
	   });
	<%} ;%>
});

/** 
 * @class Ext.grid.TableGrid 
 * @extends Ext.grid.Grid 
 * A Grid which creates itself from an existing HTML table element. 
 * @constructor 
 * @param {String/HTMLElement/Ext.Element} table The table element from which this grid will be created -  
 * The table MUST have some type of size defined for the grid to fill. The container will be  
 * automatically set to position relative if it isn't already. 
 * @param {Object} config A config object that sets properties on this grid and has two additional (optional) 
 * properties: fields and columns which allow for customizing data fields and columns for this grid. 
 * @history 
 * 2007-03-01 Original version by Nige "Animal" White 
 * 2007-03-10 jvs Slightly refactored to reuse existing classes 
 */ 
Ext.grid.TableGrid = function(table, config) {
    config = config || {};
    var cf = config.fields || [], ch = config.columns || [];
    table = Ext.get(table);

    var ct = table.insertSibling();

    var fields = [], cols = [];
    var headers = table.query("thead th");
	for (var i = 0, h; h = headers[i]; i++) {
		var text = h.innerHTML;
		var name = 'tcol-'+i;

        fields.push(Ext.applyIf(cf[i] || {}, {
            name: name,
            mapping: 'td:nth('+(i+1)+')/@innerHTML'
        }));

		cols.push(Ext.applyIf(ch[i] || {}, {
			'header': text,
			'dataIndex': name,
			'width': h.offsetWidth,
			'tooltip': h.title,
            'sortable': true
        }));
	}

    var ds  = new Ext.data.Store({
        reader: new Ext.data.XmlReader({
            record:'tbody tr'
        }, fields)
    });

	ds.loadData(table.dom);

    var cm = new Ext.grid.ColumnModel(cols);

    if(config.width || config.height){
        ct.setSize(config.width || 'auto', config.height || 'auto');
    } else {
        ct.setWidth(table.getWidth());
    }

    if(config.remove !== false){
        table.remove();
    }

    Ext.applyIf(this, {
        'ds': ds,
        'cm': cm,
        'sm': new Ext.grid.RowSelectionModel(),
        autoHeight:true,
        autoWidth:false,
		tbar: problemsToolbar
    });
    Ext.grid.TableGrid.superclass.constructor.call(this, ct, {});
};

Ext.extend(Ext.grid.TableGrid, Ext.grid.GridPanel);
</script>

<html:form action="contests.do?reqCode=listOfProblems">
<table cellspacing="0" id="problemsTable">
<thead>
	<tr style="background:#eeeeee;">
	<th><bean:message key="problem.mark" /></th>

	<th><bean:message key="problem.id" /></th>
	<th><bean:message key="problem.title" /></th>
	</tr>
</thead>
<tbody>
	<% 
	java.util.List<dudge.db.ContestProblem> contestProblems =
		(java.util.List<dudge.db.ContestProblem>) sessionObject.getContest().getContestProblems(); %>

	<% for (dudge.db.ContestProblem conProb : contestProblems) {%>

	<%if(pcb.canViewProblem(sessionObject.getUsername(),
		conProb.getProblem().getProblemId())) { %>
	<tr>
	<td><%=conProb.getProblemMark() %></td>
	<td><%=conProb.getProblem().getProblemId()%></td>
	<td>
		<a href="javascript:openProblem(<%=Integer.toString( conProb.getProblem().getProblemId() )%>)"><%=conProb.getProblem().getTitle()%></a>
	</td>
	</tr>
	<%} else {%>
	<tr>
	<td><%=conProb.getProblemMark() %></td>
	<td><%=conProb.getProblem().getProblemId()%></td>
	<td> <b> --- </b></td>
	</tr>
	<%}%>

	<%}%>
</tbody>
</table>
</html:form>
