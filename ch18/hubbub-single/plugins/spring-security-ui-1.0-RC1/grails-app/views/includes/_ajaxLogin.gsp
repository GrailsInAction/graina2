<style>
input.login {
	display: block;
}
</style>

<div class='s2ui_center'>

<div id="loginFormContainer" style='display:none' title="${message(code:'spring.security.ui.login.signin')}">
	<g:form controller='j_spring_security_check' name="loginForm" autocomplete='off'>

	<label for="username"><g:message code='spring.security.ui.login.username'/></label>
	<input class='login' name="j_username" id="username" size="20" />

	<label for="password"><g:message code='spring.security.ui.login.password'/></label>
	<input class='login'type="password" name="j_password" id="password" size="20" />

	<input type="checkbox" class="checkbox" name="_spring_security_remember_me" id="remember_me" checked="checked" />
	<label for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label> |

	<g:link controller='register' action='forgotPassword'><g:message code='spring.security.ui.login.forgotPassword'/></g:link>
	<g:link controller='register'><g:message code='spring.security.ui.login.register'/></g:link>

	<input type='submit' class='s2ui_hidden_button' />

	</g:form>
	<div id='loginMessage' style='color: red; margin-top: 10px;'></div>
</div>
</div>

<script>
var loginButtonCaption = "<g:message code='spring.security.ui.login.login'/>";
var logoutLink = '<%=link(controller: 'logout') { 'Logout' }%>';
var loggingYouIn = "<g:message code='spring.security.ui.login.loggingYouIn'/>";
</script>
<g:javascript src='jquery/jquery.form.js' plugin='spring-security-ui'/>
<g:javascript src='ajaxLogin.js' plugin='spring-security-ui'/>
