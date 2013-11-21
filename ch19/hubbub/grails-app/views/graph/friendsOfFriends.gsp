<html>
<head>
    <title>Friends Of Friends</title>
    <meta name="layout" content="main">
</head>
<body>
    <h1>Friends Of Friends</h1>
    <table>
        <tr>
            <th>User</th><th>Is A Friend Of</th><th>Who Is A Friend Of</th>
        </tr>
        <g:each in="${resultsTable}" var="row">
             <tr>
                 <td><g:link action="friendsOfFriends" id="${row.myself.loginId}">${row.myself.fullName}</g:link></td>
                 <td><g:link action="friendsOfFriends" id="${row.friend.loginId}">${row.friend.fullName}</g:link></td>
                 <td><g:link action="friendsOfFriends" id="${row.fof.loginId}">${row.fof.fullName}</g:link></td>
             </tr>
        </g:each>
    </table>


</body>
</html>
