<%@page import="java.util.List" %>


<html>
	<head>
 	<meta name="layout" content="authenticatedLayout" />

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
  
	</head>
	<body>
  
  <div class="panel panel-primary">
    <div class="panel-heading">
      <h3 class="panel-title">app instances</h3>
    </div>
	<input id="filter" type="text" class="form-control" placeholder="type here to filter...">
    <div class="computeInstances">

  
	<table  class="table table-striped table-bordered table-condensed" style="   table-layout: fixed;  word-wrap: break-word;">
		<thead>
			
	<tr><th>name</th><th >class name</th>
	
		

	</tr>
</thead>
<tbody class="searchable">
	<g:each in="${beans}" var="result" >
	  <tr>
		  <td><small>${result.get("name")}</small></td>
		  	  <td ><small>${result.get("className")}</small></td>
	
	  </tr>
	</g:each>
	
</tbody>
	</table>

    </div>
  </div>
  </body>
  </html>
