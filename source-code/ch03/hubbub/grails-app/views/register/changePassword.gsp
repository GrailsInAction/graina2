<html>
<head>
	<title>Change Password for ${user.userId}</title>
	<meta name="layout" content="main">
</head>
<body>
	
	<g:if test="${flash.message}">
    	<div style="background-color: pink;">${flash.message}</div>
    </g:if>
	
	<formset>
		<legend>Change Password for ${user.userId}</legend>
		
	<g:form action="updatePassword" id="${user.id}">
		
		<label for="password">Current</label>
		<g:passwordField name="password"/> <br/>
		
		<label for="newpassword">New Password</label>
		<g:passwordField name="newpassword"/> <br/>
		
		<label for="confirm">And Again</label>
		<g:passwordField name="confirm"/> <br/>
		
		<g:submitButton name="changePassword" value="Change It"/>
	</g:form>
	
	</formset>
	
</body>
</html>
