<html>
<head><title>HTTP Status ${request.getAttribute("status")} ${request.getAttribute("error")}</title>
</head>
<body>
<h1>HTTP Status ${request.getAttribute("status")} ${request.getAttribute("error")}</h1>
<p/>

<%
def e = request.getAttribute("javax.servlet.error.exception")
if (e) {
	def sw = new StringWriter()
	def pw = new PrintWriter(sw)
	e.printStackTrace(pw)
	pw.close()
%>
<pre>${sw.toString()}</pre>
<%
}
%>


%{--
<g:each in="${request.getAttributeNames()}" var="n">
<p>${n} : ${request.getAttribute(n)}
</g:each>
--}%
</body>
</html>