
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
      <h3 class="panel-title">Scripts</h3>
    </div>
	<input id="filter" type="text" class="form-control" placeholder="type here to filter...">
    <div class="resources">

  
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			
	<tr>
	<th>Path</th>
	<th>Provider</th>
	<th>Actions</th>
		

	</tr>
</thead>
<tbody class="searchable">
	<g:each in="${scripts}" var="script">
	<%
	

	
	%>
	<form action="/admin/scripts/exec" method="POST">
	<input type="hidden" name="scriptHash" value="${script.path("hash").asText()}" />
	  <tr>
		  <td class="vert-align" >${script.path("path").asText()}</td>
		   <td class="vert-align">${script.path("providerType").asText()}</td>
		   <td> <button type="submit" class="btn btn-default">Submit</button></td>

	  </tr>
	  </form>
	</g:each>
	
</tbody>
	</table>

    </div>
  </div>
</body>
</html>

  
  
  
