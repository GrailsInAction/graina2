<html>
<head>
	<title>Register for Hubbub</title>
	<meta name="layout" content="main">
</head>
<body>
	
	<formset>
		<legend>Signup Now</legend>
		
	<g:form action="save">
		<label for="userId">User Id</label>
		<g:textField name="userId" /> <br/>
		
		<label for="password">Password</label>
		<g:passwordField name="password" /> <br/>
		
		<label for="homepage">Homepage</label>
		<g:textField name="homepage" /> <br/>
		
		<g:submitButton name="register" value="Register"/>
	</g:form>
	
	</formset>
	
</body>
</html>
