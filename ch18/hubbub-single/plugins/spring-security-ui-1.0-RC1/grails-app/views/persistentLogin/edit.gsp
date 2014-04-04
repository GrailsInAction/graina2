<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'persistentLogin.label', default: 'PersistentLogin')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='275' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='persistentLoginEditForm'>
		<g:hiddenField name="id" value="${persistentLogin?.id}"/>
		<g:hiddenField name="version" value="${persistentLogin?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<tr class="prop">
						<td valign="top" class="name">
						<label for="series">${message(code: 'persistentLogin.series.label', default: 'Series')}</label>
						</td>
						<td valign="top" class="value">${persistentLogin.id}</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name">
						<label for="username">${message(code: 'persistentLogin.username.label', default: 'Username')}</label>
						</td>
						<td valign="top" class="value">${persistentLogin.username}</td>
					</tr>

					<s2ui:textFieldRow name='token' labelCode='persistentLogin.token.label'
					                   size='50' bean="${persistentLogin}"
					                   labelCodeDefault='Token' value="${persistentLogin?.token}"/>

					<s2ui:dateFieldRow name='lastUsed' labelCode='persistentLogin.lastUsed.label'
					                   size='50' bean="${persistentLogin}"
					                   labelCodeDefault='Last Used' value="${persistentLogin?.lastUsed}"/>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='persistentLoginEditForm' messageCode='default.button.update.label'/>

		<g:if test='${persistentLogin}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${persistentLogin}'>
	<s2ui:deleteButtonForm instanceId='${persistentLogin.id}'/>
	</g:if>

</div>

</body>
</html>
