<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.persistentLogin.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='spring.security.ui.persistentLogin.search'>

	<g:form action='persistentLoginSearch' name='persistentLoginSearchForm'>

		<br/>

		<table>
			<tbody>
			<tr>
				<td><g:message code='persistentLogin.username.label' default='Username'/>:</td>
				<td><g:textField name='username' size='50' maxlength='255' autocomplete='off' value='${username}'/></td>
			</tr>
			<tr>
				<td><g:message code='persistentLogin.token.label' default='Token'/>:</td>
				<td><g:textField name='token' size='50' maxlength='255' autocomplete='off' value='${token}'/></td>
			</tr>
			<tr>
				<td><g:message code='persistentLogin.series.label' default='Series'/>:</td>
				<td><g:textField name='series' size='50' maxlength='255' autocomplete='off' value='${series}'/></td>
			</tr>
			<tr><td colspan='2'>&nbsp;</td></tr>
			<tr>
				<td colspan='2'><s2ui:submitButton elementId='search' form='persistentLoginSearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [username: username, token: token, series: series]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="id" title="${message(code: 'persistentLogin.series.label', default: 'Series')}" params="${queryParams}"/>
			<g:sortableColumn property="username" title="${message(code: 'persistentLogin.username.label', default: 'Username')}" params="${queryParams}"/>
			<g:sortableColumn property="token" title="${message(code: 'persistentLogin.token.label', default: 'Token')}" params="${queryParams}"/>
			<g:sortableColumn property="lastUsed" title="${message(code: 'persistentLogin.lastUsed.label', default: 'Last Used')}" params="${queryParams}"/>
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="persistentLogin">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${persistentLogin.id}">${fieldValue(bean: persistentLogin, field: "id")}</g:link></td>
			<td><g:link controller='user' action='edit' params='[username: persistentLogin.username]'>${fieldValue(bean: persistentLogin, field: "username")}</g:link></td>
			<td>${fieldValue(bean: persistentLogin, field: "token")}</td>
			<td><g:formatDate format='MM/dd/yyyy' date="${persistentLogin.lastUsed}" /></td>
		</tr>
		</g:each>
		</tbody>
	</table>
	</div>

	<div class="paginateButtons">
		<g:paginate total="${totalCount}" params="${queryParams}" />
	</div>

	<div style="text-align:center">
		<s2ui:paginationSummary total="${totalCount}"/>
	</div>

	</g:if>

</div>

<script>
$(document).ready(function() {
	$("#username").focus().autocomplete({
		minLength: 3,
		cache: false,
		source: "${createLink(action: 'ajaxPersistentLoginSearch')}"
	});
});

</script>

</body>
</html>
