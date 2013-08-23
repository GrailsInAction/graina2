<div id="sidebar">
   <h3>Login</h3>
   <div id="loginForm">
      <g:form controller="login">
         <ul>
            <li>User Id: <g:textField name="loginId" size="12"/></li>
            <li>Password: 
               <g:passwordField name="password" size="12"/>
            </li>
            <li><g:submitButton name="login" value="Login"/></li>
          </ul>
      </g:form>
   </div>
</div>
