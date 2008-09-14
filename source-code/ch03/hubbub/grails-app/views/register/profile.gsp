<html>
<head>
	<title>Profile for ${user.userId}</title>
	<meta name="layout" content="main"/>
	<style>dt { float:left; margin-left: 3em; margin-right: 1em;}</style>
</head>
<body>
	
	<h1>Profile for ${user.userId}</h1>
	<dl>
		<dt>User Id:</dt>
		<dd>${user.userId}</dd>

		<dt>Homepage:</dt>
		<dd><a href="${user.homepage}">${user.homepage}</a></dd>
				
	</dl>
	<g:link action='changePassword' id="${user.id}">Change Password</g:link>

</body>
</html>
