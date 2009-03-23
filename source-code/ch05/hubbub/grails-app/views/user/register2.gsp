<html>
    <head>
        <title>Register New User (Command Object)</title>
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

        <h1>Register New User (Command Object)</h1>

        <g:hasErrors>
            <div class="errors">
               <g:renderErrors bean="${user}" as="list" />
            </div>
        </g:hasErrors>

        <g:form action="register2">
            <dl>
                <dt>User Id</dt>
                <dd><g:textField name="userId" value="${user?.userId}"/></dd>
                <dt>Password</dt>
                <dd><g:passwordField name="password" value="${user?.password}"/></dd>
                <dt>(repeat)</dt>
                <dd><g:passwordField name="passwordRepeat" value="${user?.passwordRepeat}"/></dd>
                <dt>Full Name</dt>
                <dd><g:textField name="fullName" value="${user?.fullName}"/></dd>
                <dt>Bio</dt>
                <dd><g:textArea name="bio" value="${user?.bio}"/></dd>
                <dt>Email</dt>
                <dd><g:textField name="email" value="${user?.email}"/></dd>
                <dt><g:submitButton name="register" value="Register"/></dt>
            </dl>

        </g:form>

    </body>
</html>
