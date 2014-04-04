<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Voters</title>
</head>

<body>

<br/>

<table>
	<thead>
	<tr><th>Voters</th></tr>
	</thead>
	<tbody>
	<g:each var='voter' in='${voters}'>
	<tr><td>${voter.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
