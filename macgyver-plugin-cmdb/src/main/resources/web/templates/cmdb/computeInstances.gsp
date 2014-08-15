
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
      <h3 class="panel-title">Compute Instances</h3>
    </div>
	<input id="filter" type="text" class="form-control" placeholder="type here to filter...">
    <div class="computeInstances">

  
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			
	<tr>
	<th>name</th>

		
		

	</tr>
</thead>
<tbody class="searchable">
	<g:each in="${results}" var="result">
	<%
	

	
	%>
	  <tr>
		  <td>${result.path("name").asText()}</td>
		  
	

	  </tr>
	</g:each>
	
</tbody>
	</table>

    </div>
  </div>
</body>
</html>

  
  
  
