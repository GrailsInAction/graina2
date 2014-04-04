<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Authentication Providers</title>
</head>

<body>

<br/>

<table>
	<thead>
	<tr><th>Authentication Providers</th></tr>
	</thead>
	<tbody>
	<g:each var='provider' in='${providers}'>
	<tr><td>${provider.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
