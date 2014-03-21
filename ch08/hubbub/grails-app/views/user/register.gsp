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
            <div class="flash">
                ${flash.message}
               </div>
        </g:if>
        <dl>
            <g:uploadForm>
                <dt>User Id: </dt>
                <dd><g:textField name="loginId" /></dd>
                <dt>Password:</dt>
                <dd><g:passwordField name="password" /></dd>
                <dt>(repeat)</dt>
                <dd><g:passwordField name="passwordRepeat" /></dd>             
                <dt>Country:</dt>
                <dd><g:countrySelect name="country"
                  noSelection="['':'Choose your country...']"/>
                <dt>Photo:</dt>
                <dd><input type="file" name="photo"/></dd>
                <dt>Timezone:</dt>
                <dd><g:timeZoneSelect name="timezone"/></dd>
                <dt>Who introduced you to Hubbub?</dt>
                <dd>
                <g:select name="referrer"
                   from="${com.grailsinaction.Profile.list()}"
                   optionKey="id"
                   optionValue="fullName"
                   noSelection="${['null':'Please Choose...']}" />
                </dd>
                <dt>Spam me forever:</dt>
                <dd>
                    <g:checkBox name="spamMe" checked="true"/>
                </dd>   
                <dt>Email Format:</dt>
                <dd>
                <g:radioGroup name="emailFormat"
                       labels="['Plain','HTML']"
                       values="['P', 'H']"
                       value="H">
                    ${it.label} ${it.radio}
                </g:radioGroup>
                </dd>
                <dt>
                    <g:actionSubmit value="Register" />
                </dt>
                <dd>
                    <g:actionSubmit value="Cancel" action="cancelRegister" />   
                </dd>
            </g:uploadForm>
        </dl>
        <p>
            <g:link controller="post">Back to Hubbub</g:link>
        </p>
        
    </body>
</html>
