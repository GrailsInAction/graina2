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
    <g:form action="register2">
        <dl>
            <dt>User Id</dt>
            <dd><g:textField name="loginId" value="${user?.loginId}"/></dd>
            <dt>Password</dt>
            <dd><g:passwordField name="password" value="${user?.password}"/></dd>
            <dt>Full Name</dt>
            <dd><g:textField name="fullName" value="${user?.fullName}"/></dd>
            <dt>Bio</dt>
            <dd><g:textArea name="bio" value="${user?.bio}"/></dd>
            <dt>Email</dt>
            <dd>
                <g:textField name="email" value="${user?.email}"/>
                <g:hasErrors bean="${user}" field="email">
                    <g:eachError bean="${user}" field="email">
                        <p style="color: red;"><g:message error="${it}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>
            <dt><g:submitButton name="register" value="Register"/></dt>
        </dl>
    </g:form>
</body>
</html>
