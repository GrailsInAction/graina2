<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Current Auth</title>
</head>

<body>

<table>
	<thead>
	<tr>
		<th>Name</th>
		<th>Value</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>Authorities</td>
		<td>${auth.authorities}</td>
	</tr>
	<tr>
		<td>Details</td>
		<td>${auth.details}</td>
	</tr>
	<tr>
		<td>Principal</td>
		<td>${auth.principal}</td>
	</tr>
	<tr>
		<td>Name</td>
		<td>${auth.name}</td>
	</tr>
	</tbody>
</table>
</body>

</html>
