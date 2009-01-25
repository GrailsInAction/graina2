

<div id="sidebar">

       <h3>Login</h3>
        <div id="loginForm">
            <form action="${resource(file: 'j_spring_security_check')}">
                <table>
                  <tr>
                    <td>User Id:</td><td><g:textField name="j_username"/></td>
                  </tr>
                  <tr>
                    <td>Password:</td><td><input name="j_password" type="password"/></td>
                  </tr>
                  <tr>
				    <td>Remember:</td><td><input type='checkbox' name='_spring_security_remember_me'<g:if test='${hasCookie}'> checked='checked'</g:if>/></td>
                  </tr>
                  <tr>
                    <td colspan="2"><g:submitButton name="login" value="Login"/></td>
                  </tr>
                  <tr>
				    <td colspan="2">try "glen" or "peter" with "password"</td>
                  </tr>
                </table>
				
            </form>
        </div>

</div>
