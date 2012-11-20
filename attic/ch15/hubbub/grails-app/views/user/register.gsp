<html>
    <head>
        <title>Register New User</title>
        <meta name="layout" content="main"/>
        <style>
            dd {
                text-align: left;
                margin-left: 80px;
                margin-top: 5px;
            }
        </style>
    </head>
    <body>

        <h1>Register New User</h1>

        <g:hasErrors>
            <div class="errors">
               <g:renderErrors bean="${user}" as="list" />
            </div>
        </g:hasErrors>

        <g:form action="register">
            <dl>
                <dt>User Id</dt>
                <dd><g:textField name="userId" value="${user?.userId}"/></dd>
                <dt>Password</dt>
                <dd><g:passwordField name="password" value="${user?.password}"/></dd>
                <dt>Full Name</dt>
                <dd><g:textField name="profile.fullName" value="${user?.profile?.fullName}"/></dd>
                <dt>Bio</dt>
                <dd><g:textArea name="profile.bio" value="${user?.profile?.bio}"/></dd>
                <dt>Email</dt>
                <dd>
                    <g:textField name="profile.email" value="${user?.profile?.email}"/>
                    <g:hasErrors bean="${user}" field="profile.email">
                        <g:eachError bean="${user}" field="profile.email">
                            <p style="color: red;"><g:message error="${it}"/></p>
                        </g:eachError>
                    </g:hasErrors>
                </dd>
                <dt><g:submitButton name="register" value="Register"/></dt>
            </dl>

        </g:form>

    </body>
</html>
