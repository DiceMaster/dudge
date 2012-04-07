<%@page import="java.text.SimpleDateFormat" %>
<%@page import="dudge.db.Contest" %>
<jsp:useBean id="contestsForm" class="dudge.web.forms.ContestsForm" scope="session" />
<% pageContext.setAttribute("roleTypeUser", dudge.db.RoleType.USER);%>
<c:set var="contestId" value="${contestsForm.contestId}" scope="session" />

<script type="text/javascript">
    Ext.onReady(function(){
        var buttonsToolbar = Ext.getCmp('content-panel').getTopToolbar();
    <c:if test="${permissionCheckerRemote.canSendApplication(autentificationObject.username, contestId)}">
            var btnApplication = new Ext.Toolbar.Button({
                text: '<bean:message key="contest.sendApplication" />',
                handler:function() {
                    var contestId = ${contestId};
                    var contestConnection =  (new Ext.data.HttpProxy({})).getConnection();
                    function sendApplication (btn , text) {
                        if(btn == 'ok')	{
                            contestConnection.request({
                                method: 'POST',
                                url: 'contests.do',
                                params: {reqCode: 'sendApplication' , contestId: contestId , message: text},
                                callback: function() {
                                    Ext.MessageBox.alert('<bean:message key="contest.applicationSentTitle" />',
                                    '<bean:message key="contest.applicationSent" />');
                                }
                            });
                        }
                    } // function sendApplication()
                    Ext.Msg.show({
                        title:'<bean:message key="application.filingTitle" />',
                        msg: '<bean:message key="application.filingMessage" />',
                        buttons: Ext.Msg.OKCANCEL,
                        fn: sendApplication,
                        animEl: btnApplication,
                        multiline: true,
                        value: '<bean:message key="application.reasonSample" />',
                        defaultTextHeight: 250,
                        width: 500,
                        closable: false
                    });
                } // handler function
            }); // button
            buttonsToolbar.addButton(btnApplication);   
    </c:if>
            
    <c:if test="${permissionCheckerRemote.canModifyContest(autentificationObject.username, contestId)}">
            buttonsToolbar.addButton({
                text: '<bean:message key="contest.edit" />',
                handler: function()
                {
                    location.href = 'contests.do?reqCode=edit&contestId=${contestId}';
                }
            });
    </c:if>
        
    <c:if test="${permissionCheckerRemote.canDeleteContest(autentificationObject.username, contestId)}">
            buttonsToolbar.addButton(
            {
                text: '<bean:message key="contest.delete" />',
                handler: function()
                {
                    location.href = 'contests.do?reqCode=delete&contestId=${contestId}';
                }
            }
        );
    </c:if>

            buttonsToolbar.doLayout();
            buttonsToolbar.render();
        }); //Ext.onReady()
</script>

<html:form action="contests" method="GET">
    <h3 align="center" style="font-size:x-large">${contestsForm.caption}</h3>
    <p align="center" class="contest_info" >
        <a href="contests.do?reqCode=rules&contestId=${contestId}"><bean:message key="contest.rules" /></a><br>
        <bean:message key="contest.type" />: ${contestsForm.contestType}<br>

        <c:choose>
            <c:when test="${contestsForm.open}">
                <bean:message key="contest.isOpen" />: <bean:message key="contest.open.yes" /><br>    
            </c:when>
            <c:otherwise>
                <bean:message key="contest.isOpen" />: <bean:message key="contest.open.no" /><br>  
            </c:otherwise>
        </c:choose>
        <bean:message key="contest.startDate" />: ${contestsForm.startDate}<br>
        <bean:message key="contest.startTime" />: ${contestsForm.startHour}:${contestsForm.startMinute}<br>
        <bean:message key="contest.duration" />: ${contestsForm.durationHours}:${contestsForm.durationMinutes}<br>

    </p><br>
    <p class="contest_info">${contestsForm.description} </p>
    <p class="contest_info">
    <h2 align="center" style="font-size:large"><bean:message key="contest.competitors" /></h2>
    <table align="center" border="1" cellspacing="0" width="50%">
        <thead>
            <tr>
                <th><b><bean:message key="user.login" /></b></th>
                <th><b><bean:message key="user.realName" /></b></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${contest.roles}" var="role">
                <c:if test="${role.roleType == roleTypeUser}">
                    <tr>
                        <td width="50%"><a href="users.do?reqCode=view&login=${role.user.login}">${role.user.login}</a></td>
                        <td width="50%">${role.user.realName} </td>
                    </tr>
                </c:if>
            </c:forEach>
        </tbody>
    </table>
</p>
</html:form>