<!DOCTYPE html>
<html lang="en">
<head>
<title>ACE in Action</title>
<style type="text/css" media="screen">
    #editor { 
    height: 275px;
    border: 1px solid #DDD;
    border-radius: 4px;
    border-bottom-right-radius: 0px;
    margin-top: 5px;
    }

     
</style>
</head>
<body>


<form method="post" action="/admin/console">
<textarea id="scriptTextArea" placeholder="[Enter text]" class="description" name="scriptText" required="required">${scriptText!""}</textarea>
<div id="editor">
</div>
<input type="submit" name="execute" />
</form>

<script src="http://cdn.jsdelivr.net/ace/1.1.01/min/ace.js" type="text/javascript" charset="utf-8"></script>
<script>
    var editor = ace.edit("editor");
    //var textarea = document.getElementsByTagName("textarea")[0];
    var textarea = document.getElementById("scriptTextArea");
    textarea.style.display="none";
    editor.getSession().setValue(textarea.value);
	editor.getSession().on('change', function(){
  textarea.value=editor.getSession().getValue();
});
    
    
    editor.setTheme("ace/theme/xcode");
    editor.getSession().setMode("ace/mode/groovy");
    editor.renderer.setPadding(100);
</script>
<p>RVAL: ${scriptVal!''}
<p>Out: ${scriptOut!""}
<p>Exception ${scriptException!""}
</body>
</html>