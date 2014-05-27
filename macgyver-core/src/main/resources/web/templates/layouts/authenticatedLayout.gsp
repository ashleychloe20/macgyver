
<%@ page import="io.macgyver.core.*" %>
<%@ page import="io.macgyver.core.web.navigation.*" %>
<%
	MenuManager menuManager = Kernel.getInstance().getApplicationContext().getBean(MenuManager.class);
	MenuItem rootMenu = menuManager.getRootMenuForCurrentUser();

%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.ico">

    <title>MacGyver</title>

    <!-- Bootstrap core CSS -->
    <link href="/webjars/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">



    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	    <script src="/webjars/jquery/2.1.1/jquery.min.js"></script>
	    <script src="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	    
	      <g:layoutHead />
  </head>

  <body>

    <div class="container">

      <!-- Static navbar -->
      <div class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/#">MacGyver</a>
          </div>
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
				<!--
              <li class="active"><a href="#">CMDB</a></li>
              <li><a href="#">CMDB</a></li>
              <li><a href="#">Link</a></li>
					-->
					
			<g:each in="${rootMenu.getChildMenuItems()}" var="c">
             <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">${c.getDisplayName()} <b class="caret"></b></a>
				
                <ul class="dropdown-menu">
                	<g:each in="${c.getChildMenuItems()}" var="child">
				
					
                  		  <li><a href="${child.getUriPath()}">${child.getDisplayName()}</a></li>
					
                  	</g:each>
		
                </ul>
              </li>
			  </g:each>
        
             
            </ul>
            <ul class="nav navbar-nav navbar-right">
            
              <li><a href="/logout">logout</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </div>


   <g:layoutBody />

 </div> <!-- /container -->

  </body>
</html>