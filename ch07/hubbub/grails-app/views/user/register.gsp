<html>
<head>
    <title>Register New User</title>
    <meta name="layout" content="main"/>
</head>
<body>
    <h1>Register New User</h1>
    <g:hasErrors>
        <div class="errors">
            <g:renderErrors bean="${user}" as="list" />
        </div>
    </g:hasErrors>
    <g:if test="${flash.message}">
        <div class="flash">${flash.message}</div>
    </g:if>
    <g:form action="register">
        <fieldset class="form">
            <div class="fieldcontain required">
                <label for="loginId">Login ID</label>
                <g:textField name="loginId" value="${user?.loginId}"/>
            </div>
            <div class="fieldcontain required">
                <label for="password">Password</label>
                <g:passwordField name="password"/>
            </div>
            <div class="fieldcontain required">
                <label for="profile.fullName">Full Name</label>
                <g:textField name="profile.fullName" value="${user?.profile?.fullName}"/>
            </div>
            <div class="fieldcontain required">
                <label for="profile.bio">Bio</label>
                <g:textArea name="profile.bio" value="${user?.profile?.bio}"/>
            </div>
            <div class="fieldcontain required">
                <label for="profile.email">Email</label>
                <g:textField name="profile.email" value="${user?.profile?.email}"/>
                <g:hasErrors bean="${user}" field="profile.email">
                    <g:eachError bean="${user}" field="profile.email">
                        <p style="color: red;"><g:message error="${it}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </div>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="register" value="Register"/>
        </fieldset>
    </g:form>
</body>
</html>
