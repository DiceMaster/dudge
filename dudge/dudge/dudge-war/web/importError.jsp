<p><bean:message key="problem.importError"/><p>

<pre>
<%
	java.lang.Exception ex = (java.lang.Exception)request.getAttribute("exception");
	ex.printStackTrace(new java.io.PrintWriter(out));
%>
</pre>