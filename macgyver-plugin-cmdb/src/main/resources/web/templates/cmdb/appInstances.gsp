
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>
<%@page import="org.ocpsoft.prettytime.PrettyTime"%>

<html>
<head>
	<meta name="layout" content="authenticatedLayout" />
</head>
<body>


  

  
  <div class="panel panel-primary">
    <div class="panel-heading">
      <h3 class="panel-title">App Instances</h3>
    </div>

    <div class="computeInstances">

    <div id="xxgrid" style="width: 100%; height: 600px;"></div>


    <script>
    $(function () {
 

 
        $('#xxgrid').w2grid({
            name: 'grid',
            header: 'App Instances',
			url : '/cmdb/appInstances/data.json',
	
	        show: {
	            toolbar: true,
				footer: true
	        },
            columns: [
                { field: 'environment', caption: 'Environment', size: '30%' , sortable:true},
                { field: 'host', caption: 'Host', size: '30%' },
                { field: 'appId', caption: 'App', size: '40%' , sortable:true},
				{ field: 'classifier', caption: 'Classifier', size: '40%' , sortable:true},
				{ field: 'version', caption: 'Version', size: '40%' , sortable:true},
				{ field: 'scmRevision', caption: 'Revision', size: '40%' , sortable:true},
				{ field: 'scmBranch', caption: 'Branch', size: '40%' , sortable:true},
				{ field: 'lastContactTsPretty', caption: 'Last Contact', size: '40%' , sortable:true}
            ]
        });
    });
    </script>

    </div>
  </div>
</body>
</html>

  
  
  
