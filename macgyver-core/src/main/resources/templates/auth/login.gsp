<html>
	<head>
 	<meta name="layout" content="loginLayout" />
	</head>
	<body>

    <div class="container">

      <form class="form-signin" role="form" action="/login" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="username" class="form-control" placeholder="username" name="username" required autofocus>
        <input type="password" class="form-control" placeholder="password" name="password" required>
     <!--   <label class="checkbox">
          <input type="checkbox" value="remember-me"> Remember me
        </label>
		 -->
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

    </div> <!-- /container -->


  </body>
</html>