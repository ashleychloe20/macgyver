<%@page import="java.util.List" %>


<html>
	<head>
 	<meta name="layout" content="authenticatedLayout" />
	</head>
	<body>

<%
if (cipherText) {
%>
<p class="bg-danger">Encrpyted Cipher Text: ${cipherText}</p>
<%}%>

<form class="form-horizontal" role="form" action="/admin/encryptString" method="post">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-2 control-label">String To Encrypt</label>
    <div class="col-sm-10">
      <input type="password" class="form-control" id="inputEmail3" placeholder="" name="input">
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword3" class="col-sm-2 control-label">Key Alias</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputPassword3" placeholder="" name="key" value="mac0">
    </div>
  </div>

  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Encrypt</button>
    </div>
  </div>
</form>
</body>
</html>
