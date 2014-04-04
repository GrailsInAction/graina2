<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Filter Chains</title>
</head>

<body>

<br/>

<table>
	<thead>
	<tr>
		<th>URL Pattern</th>
		<th>Filters</th>
	</tr>
	</thead>
	<tbody>
	<g:each var='entry' in='${filterChainMap}'>
	<tr>
		<td>${entry.key}</td>
		<td>
			<g:each var='filter' in='${entry.value}'>
			${filter.getClass().name}<br/>
			</g:each>
		</td>
	</tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
