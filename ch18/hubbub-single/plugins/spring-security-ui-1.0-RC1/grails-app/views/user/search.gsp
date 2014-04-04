<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.user.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='375' elementId='formContainer'
	           titleCode='spring.security.ui.user.search'>

	<g:form action='userSearch' name='userSearchForm'>

		<br/>

		<table>
			<tbody>

			<tr>
				<td><g:message code='user.username.label' default='Username'/>:</td>
				<td colspan='3'><g:textField name='username' size='50' maxlength='255' autocomplete='off' value='${username}'/></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><g:message code='spring.security.ui.search.true'/></td>
				<td><g:message code='spring.security.ui.search.false'/></td>
				<td><g:message code='spring.security.ui.search.either'/></td>
			</tr>
			<tr>
				<td><g:message code='user.enabled.label' default='Enabled'/>:</td>
				<g:radioGroup name='enabled' labels="['','','']" values="[1,-1,0]" value='${enabled}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td><g:message code='user.accountExpired.label' default='Account Expired'/>:</td>
				<g:radioGroup name='accountExpired' labels="['','','']" values="[1,-1,0]" value='${accountExpired}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td><g:message code='user.accountLocked.label' default='Account Locked'/>:</td>
				<g:radioGroup name='accountLocked' labels="['','','']" values="[1,-1,0]" value='${accountLocked}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td><g:message code='user.passwordExpired.label' default='Password Expired'/>:</td>
				<g:radioGroup name='passwordExpired' labels="['','','']" values="[1,-1,0]" value='${passwordExpired}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr><td colspan='4'>&nbsp;</td></tr>
			<tr>
				<td colspan='4'><s2ui:submitButton elementId='search' form='userSearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [username: username, enabled: enabled, accountExpired: accountExpired, accountLocked: accountLocked, passwordExpired: passwordExpired]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="username" title="${message(code: 'user.username.label', default: 'Username')}" params="${queryParams}"/>
			<g:sortableColumn property="enabled" title="${message(code: 'user.enabled.label', default: 'Enabled')}" params="${queryParams}"/>
			<g:sortableColumn property="accountExpired" title="${message(code: 'user.accountExpired.label', default: 'Account Expired')}" params="${queryParams}"/>
			<g:sortableColumn property="accountLocked" title="${message(code: 'user.accountLocked.label', default: 'Account Locked')}" params="${queryParams}"/>
			<g:sortableColumn property="passwordExpired" title="${message(code: 'user.passwordExpired.label', default: 'Password Expired')}" params="${queryParams}"/>
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="user">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${user.id}">${fieldValue(bean: user, field: "username")}</g:link></td>
			<td><g:formatBoolean boolean="${user.enabled}"/></td>
			<td><g:formatBoolean boolean="${user.accountExpired}"/></td>
			<td><g:formatBoolean boolean="${user.accountLocked}"/></td>
			<td><g:formatBoolean boolean="${user.passwordExpired}"/></td>
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
		source: "${createLink(action: 'ajaxUserSearch')}"
	});
});

<s2ui:initCheckboxes/>

</script>

</body>
</html>
