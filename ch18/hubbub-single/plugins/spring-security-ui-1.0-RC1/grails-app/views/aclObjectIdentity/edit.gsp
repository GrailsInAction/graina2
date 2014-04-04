<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='310' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='aclObjectIdentityEditForm'>
		<g:hiddenField name="id" value="${aclObjectIdentity?.id}"/>
		<g:hiddenField name="version" value="${aclObjectIdentity?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<tr class="prop">
						<td valign="top" class="name">
							<label for="aclClass"><g:message code="aclObjectIdentity.aclClass.label" default="AclClass" /></label>
						</td>
						<td valign="top" class="value ${hasErrors(bean: aclObjectIdentity, field: 'aclClass', 'errors')}">
							<g:select name="aclClass.id" from="${classes}" optionKey="id" optionValue='className'
							          value="${aclObjectIdentity?.aclClass?.id}"/>
							${fieldError(bean: aclObjectIdentity, field: 'aclClass')}
						</td>
					</tr>

					<s2ui:textFieldRow name='objectId' labelCode='aclObjectIdentity.objectId.label'
					                   size='50' bean="${aclObjectIdentity}"
					                   labelCodeDefault='Object ID' value="${aclObjectIdentity?.objectId}"/>

					<tr class="prop">
						<td valign="top" class="name">
							<label for="owner.id"><g:message code="aclObjectIdentity.owner.label" default="Owner" /></label>
						</td>
						<td valign="top" class="value ${hasErrors(bean: aclObjectIdentity, field: 'owner', 'errors')}">
							<g:select name="owner.id" from="${sids}" optionKey="id" optionValue='sid'
							          value="${aclObjectIdentity?.owner?.id}" />
						</td>
					</tr>

					<s2ui:textFieldRow name='parent.id' labelCode='aclObjectIdentity.parent.label'
					                   size='50' bean="${aclObjectIdentity}"
					                   labelCodeDefault='Parent' value="${aclObjectIdentity?.parent?.id}"/>

					<s2ui:checkboxRow name='entriesInheriting' labelCode='aclObjectIdentity.entriesInheriting.label' bean="${aclObjectIdentity}"
		                           labelCodeDefault='Entries Inheriting' value="${aclObjectIdentity?.entriesInheriting}"/>

					<tr>
						<td colspan='2'>
							<g:link action='aclEntrySearch' controller='aclEntry' params='[aclObjectIdentity: aclObjectIdentity.id]'>View Associated ACL Entries</g:link>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='aclObjectIdentityEditForm' messageCode='default.button.update.label'/>

		<g:if test='${aclObjectIdentity}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${aclObjectIdentity}'>
	<s2ui:deleteButtonForm instanceId='${aclObjectIdentity.id}'/>
	</g:if>

</div>

<script>
$(document).ready(function() {
	$('#sid').focus();
	<s2ui:initCheckboxes/>
});
</script>

</body>
</html>
