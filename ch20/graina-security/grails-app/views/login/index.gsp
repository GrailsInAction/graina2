<%@page defaultCodec="HTML" %>
<html>
<head>
  <title>Application Login</title>
</head>
<body>
  <h1>Log in to the application</h1>
  <g:form controller="login" action="signIn">
    <div>Username: <input type="text" name="username" value="${params.username}"/></div>
    <div>Password: <input type="password" name="password"/></div>
  </g:form>
</body>
</html>
