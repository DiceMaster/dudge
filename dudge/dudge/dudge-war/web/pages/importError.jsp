<div class="alert alert-danger">
    <bean:message key="problem.importError"/>
</div>

<pre>
<%
	java.lang.Exception ex = (java.lang.Exception)request.getAttribute("exception");
	ex.printStackTrace(new java.io.PrintWriter(out));
%>
</pre>