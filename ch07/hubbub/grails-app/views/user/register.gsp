<html>
<head>
    <title>Register New User</title>
    <meta name="layout" content="main"/>
</head>
<body>
    <h1>Register New User</h1>
    <g:if test="${flash.message}">
        <div class="flash">
            ${flash.message}
        </div>
    </g:if>
    <g:hasErrors>
        <div class="errors">
           <g:renderErrors bean="${user}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="register2">
        <dl>
            <dt>User Id</dt>
            <dd>
                <g:textField name="loginId" value="${user?.loginId}"/>
                <g:hasErrors bean="${user}" field="loginId">
                    <g:eachError bean="${user}" field="loginId" var="err">
                        <p style="color: red;"><g:message error="${err}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>

            <dt>Password</dt>
            <dd>
                <g:passwordField name="password" value="${user?.password}"/>
                <g:hasErrors bean="${user}" field="password">
                    <g:eachError bean="${user}" field="password" var="err">
                        <p style="color: red;"><g:message error="${err}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>

            <dt>Full Name</dt>
            <dd>
                <g:textField name="profile.fullName" value="${user?.profile?.fullName}"/>
                <g:hasErrors bean="${user}" field="profile.fullName">
                    <g:eachError bean="${user}" field="profile.fullName" var="err">
                        <p style="color: red;"><g:message error="${err}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>

            <dt>Bio</dt>
            <dd>
                <g:textArea name="profile.bio" value="${user?.profile?.bio}"/>
                <g:hasErrors bean="${user}" field="profile.bio">
                    <g:eachError bean="${user}" field="profile.bio" var="err">
                        <p style="color: red;"><g:message error="${err}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>

            <dt>Email</dt>
            <dd>
                <g:textField name="profile.email" value="${user?.profile?.email}"/>
                <g:hasErrors bean="${user}" field="profile.email">
                    <g:eachError bean="${user}" field="profile.email" var="err">
                        <p style="color: red;"><g:message error="${err}"/></p>
                    </g:eachError>
                </g:hasErrors>
            </dd>
            <dt><g:submitButton name="register" value="Register"/></dt>
        </dl>
    </g:form>
</body>
</html>
