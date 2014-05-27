ERROR

<%
request.getAttributeNames().each {  
%>
<p/>
	${it}

<%

}


%>

<p>

${request.getAttribute("status")}: ${request.getAttribute("error")}

${request.getAttribute("javax.servlet.error.exception")}