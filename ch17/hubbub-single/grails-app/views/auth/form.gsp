<html>
<head>
    <title>Sign into Hubbub</title>
    <meta name="layout" content="main">
    <g:external dir="css" file="twitter-auth.css"/>
</head>
<body>
    <h1>Sign in</h1>
    <g:form uri="/j_spring_security_check" method="POST">
        <fieldset class="form">
            <div class="fieldcontain required">
                <label for="j_username">Login ID</label>
                <g:textField name="j_username" value="${loginId}"/>
            </div>
            <div class="fieldcontain required">
                <label for="j_password">Password</label>
                <g:passwordField name="j_password"/>
            </div>
            <div class="fieldcontain required">
                <label for="_spring_security_remember_me">Remember me</label>
                <g:checkBox name="_spring_security_remember_me"/>
            </div>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="signIn" value="Sign in"/>
            <twitterAuth:button/>
        </fieldset>
    </g:form>

</body>
</html>
