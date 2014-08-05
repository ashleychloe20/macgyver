
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>
<%@page import="org.ocpsoft.prettytime.PrettyTime"%>

<html>
<head>
	<meta name="layout" content="authenticatedLayout" />
</head>
<body>

  <script type="text/javascript" type="text/javascript">

  $(document).ready(function () {

      (function ($) {

          $('#filter').keyup(function () {

              var rex = new RegExp($(this).val(), 'i');
              $('.searchable tr').hide();
              $('.searchable tr').filter(function () {
                  return rex.test($(this).text());
              }).show();

          })

      }(jQuery));

  });
  
  </script>
  

  
  <div class="panel panel-primary">
    <div class="panel-heading">
      <h3 class="panel-title">App Instances</h3>
    </div>
	<input id="filter" type="text" class="form-control" placeholder="type here to filter...">
    <div class="computeInstances">

  
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			
	<tr>
	<th>Environment</th>
	<th class="sort" data-sort="internalHostname" >Host</th>
	<th class="sort" data-sort="appId">App Name</th>
	<th class="sort" data-sort="appId">Classifier</th>
	<th>Version</th>
	<th class="sort" data-sort="appId">Revision</th>
	<th class="sort" data-sort="appId">Branch</th>
	<th class="sort" data-sort="appId">Last Contact</th>
		
		

	</tr>
</thead>
<tbody class="searchable">
	<g:each in="${results}" var="result">
	<%
	
	def props = result.path("data")
	def ts = props.path("lastContactTs").asLong(0)
	def lastContact  = (ts==0) ? "" : new PrettyTime().format(new Date(ts))
	def scmViewRevisionUrl = props.path("scmViewRevisionUrl").asText()
	
	%>
	  <tr>
		  <td>${props.path("environment").asText()}</td>
		  <td>${props.path("host").asText()}</td>
		  <td>${props.path("appId").asText()}</td>
		  <td>${props.path("classifier").asText()}</td>
		  <td>${props.path("version").asText()}</td>
	
	  <td>
  		<g:if test="${scmViewRevisionUrl.length()>0}">
  			<a href="${scmViewRevisionUrl}">
  		</g:if>
		  
		  ${props.path("scmRevision").asText()}
	<g:if test="${scmViewRevisionUrl.length()>0}">
		<a href="${scmViewRevisionUrl}">
	</g:if>
	  
	  </td>
	  
	   <td>${props.path("scmBranch").asText()}</td>
	    <td>${lastContact}</td>
	  </tr>
	</g:each>
	
</tbody>
	</table>

    </div>
  </div>
</body>
</html>

  
  
  
