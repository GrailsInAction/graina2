<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>User Cache</title>
</head>

<body>

<br/>

<g:if test='${cache}'>
<h4>UserCache: ${cache.getClass().name}</h4>

<br/>

<table>
	<tr>
		<td>Size</td>
		<td>${cache.size}</td>
	</tr>
	<tr>
		<td>Status</td>
		<td>${cache.status}</td>
	</tr>
	<tr>
		<td>Name</td>
		<td>${cache.name}</td>
	</tr>
	<tr>
		<td>GUID</td>
		<td>${cache.guid}</td>
	</tr>
	<tr>
		<td>Statistics</td>
		<td>
			<table>
				<tr>
					<td>Cache Hits</td>
					<td>${cache.statistics.cacheHits}</td>
				</tr>
				<tr>
					<td>In-memory Hits</td>
					<td>${cache.statistics.inMemoryHits}</td>
				</tr>
				<tr>
					<td>On-disk Hits</td>
					<td>${cache.statistics.onDiskHits}</td>
				</tr>
				<tr>
					<td>Cache Misses</td>
					<td>${cache.statistics.cacheMisses}</td>
				</tr>
				<tr>
					<td>Object Count</td>
					<td>${cache.statistics.objectCount}</td>
				</tr>
				<tr>
					<td>Memory Store Object Count</td>
					<td>${cache.statistics.memoryStoreObjectCount}</td>
				</tr>
				<tr>
					<td>Disk Store Object Count</td>
					<td>${cache.statistics.diskStoreObjectCount}</td>
				</tr>
				<tr>
					<td>Statistics Accuracy Description</td>
					<td>${cache.statistics.statisticsAccuracyDescription}</td>
				</tr>
				<tr>
					<td>Average Get Time</td>
					<td>${cache.statistics.averageGetTime}</td>
				</tr>
				<tr>
					<td>Eviction Count</td>
					<td>${cache.statistics.evictionCount}</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr><th colspan='2'>${cache.size} user${cache.size == 1 ? '' : 's'}</th></tr>
	<tr>
		<th>Username</th>
		<th>User</th>
	</tr>
	<g:each var='k' in='${cache.keys}'>
	<tr>
		<td>${k}</td>
		<td>${cache.get(k)?.value}</td>
	</tr>
	</g:each>
</table>
</g:if>
<g:else>
<h4>Not Caching Users</h4>
</g:else>

</body>

</html>
