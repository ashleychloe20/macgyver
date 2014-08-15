
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

    <div class="resources">

  	Result

    </div>
  </div>
</body>
</html>

  
  
  
