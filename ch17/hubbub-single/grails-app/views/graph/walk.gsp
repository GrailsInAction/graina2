<html>
<head>
    <title>Walking The Graph</title>
    <meta name="layout" content="main">
</head>
<body>
    <h1>Walking The Graph</h1>
    <table>
        <tr>
            <th>User</th><th>Following</th>
        </tr>
        <g:each in="${nodeList}" var="node">
             <tr>
                 <td><g:link action="walk" id="${node.loginId}">${node.fullName}</g:link></td>
                 <td>
                     <ul>
                     <g:each in="${node.following}" var="following">
                         <li><g:link action="walk" id="${following.loginId}">${following.fullName}</g:link></li>
                     </g:each>
                     </ul>
             </tr>
        </g:each>
    </table>


</body>
</html>
