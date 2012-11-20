<html>
    <head>
        <title>Register New User</title>
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

        <g:hasErrors bean="${userDetails}">
            <div class="errors">
               <g:renderErrors bean="${userDetails}" as="list" />
            </div>
        </g:hasErrors>

        <g:form action="register2">
            <dl>
                <dt>User Id</dt>
                <dd><g:textField name="userId" value="${userDetails?.userId}"/></dd>
                <dt>Password</dt>
                <dd><g:passwordField name="password" value="${userDetails?.password}"/></dd>
                <dt>(repeat)</dt>
                <dd><g:passwordField name="passwordRepeat" value="${userDetails?.passwordRepeat}"/></dd>
                <dt>Full Name</dt>
                <dd><g:textField name="fullName" value="${userDetails?.fullName}"/></dd>
                <dt>Bio</dt>
                <dd><g:textArea name="bio" value="${userDetails?.bio}"/></dd>
                <dt>Email</dt>
                <dd><g:textField name="email" value="${userDetails?.email}"/></dd>
                <dt><g:submitButton name="register" value="Register"/></dt>
            </dl>

        </g:form>

    </body>
</html>
