<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'registrationCode.label', default: 'RegistrationCode')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='registrationCodeEditForm'>
		<g:hiddenField name="id" value="${registrationCode?.id}"/>
		<g:hiddenField name="version" value="${registrationCode?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='username' labelCode='registrationCode.username.label'
					                   size='50' bean="${registrationCode}"
					                   labelCodeDefault='Username' value="${registrationCode?.username}"/>

					<s2ui:textFieldRow name='token' labelCode='registrationCode.token.label'
					                   size='50' bean="${registrationCode}"
					                   labelCodeDefault='Token' value="${registrationCode?.token}"/>

					<tr class="prop">
						<td valign="top" class="name">
						<label for="dateCreated">${message(code: 'registrationCode.dateCreated.label', default: 'Date Created')}</label>
						</td>
						<td valign="top" class="value">${formatDate(date: registrationCode?.dateCreated)}</td>
					</tr>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='registrationCodeEditForm' messageCode='default.button.update.label'/>

		<g:if test='${registrationCode}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${registrationCode}'>
	<s2ui:deleteButtonForm instanceId='${registrationCode.id}'/>
	</g:if>

</div>

<script>
$(document).ready(function() {
	$("#username").focus().autocomplete({
		minLength: 3,
		cache: false,
		source: "${createLink(action: 'ajaxRegistrationCodeSearch')}"
	});
});
</script>

</body>
</html>
