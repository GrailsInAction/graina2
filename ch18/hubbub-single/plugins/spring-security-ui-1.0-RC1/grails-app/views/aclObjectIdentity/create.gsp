<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity')}"/>
	<title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='275' elementId='formContainer'
	           titleCode='default.create.label' titleCodeArgs='[entityName]'>

	<g:form action="save" name='aclObjectIdentityCreateForm'>
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
							          value="${aclObjectIdentity?.aclClass?.id}" noSelection="['null': '']"/>
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
							          value="${aclObjectIdentity?.owner?.id}" noSelection="['null': '']"/>
							${fieldError(bean: aclObjectIdentity, field: 'owner')}
						</td>
					</tr>

					<s2ui:textFieldRow name='parent.id' labelCode='aclObjectIdentity.parent.label'
					                   size='50' bean="${aclObjectIdentity}"
					                   labelCodeDefault='Parent' value="${aclObjectIdentity?.parent?.id}"/>

					<s2ui:checkboxRow name='entriesInheriting' labelCode='aclObjectIdentity.entriesInheriting.label' bean="${aclObjectIdentity}"
		                           labelCodeDefault='Entries Inheriting' value="${aclObjectIdentity?.entriesInheriting}"/>

					<tr><td>&nbsp;</td></tr>

					<tr class="prop">
						<td valign="top">
							<s2ui:submitButton elementId='create' form='aclObjectIdentityCreateForm' messageCode='default.button.create.label'/>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

	</g:form>

	</s2ui:form>

</div>

<script>
<s2ui:initCheckboxes/>
</script>

</body>
</html>
