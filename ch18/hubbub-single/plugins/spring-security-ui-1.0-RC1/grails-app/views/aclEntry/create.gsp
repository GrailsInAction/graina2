<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclEntry.label', default: 'AclEntry')}"/>
	<title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='310' elementId='formContainer'
	           titleCode='default.create.label' titleCodeArgs='[entityName]'>

	<g:form action="save" name='aclEntryCreateForm'>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='aclObjectIdentity.id' labelCode='aclEntry.aclObjectIdentity.label'
					                   size='50' bean="${aclEntry}"
					                   labelCodeDefault='AclObjectIdentity' value="${aclEntry?.aclObjectIdentity?.id}"/>

					<s2ui:textFieldRow name='aceOrder' labelCode='aclEntry.aceOrder.label'
					                   size='50' bean="${aclEntry}"
					                   labelCodeDefault='Ace Order' value="${aclEntry?.aceOrder}"/>

					<tr class="prop">
						<td valign="top" class="name">
							<label for="sid.id"><g:message code="aclEntry.sid.label" default="SID" /></label>
						</td>
						<td valign="top" class="value ${hasErrors(bean: aclEntry, field: 'sid', 'errors')}">
							<g:select name="sid.id" from="${sids}" optionKey="id" optionValue='sid'
							          value="${aclEntry?.sid?.id}" noSelection="['null': '']"/>
						</td>
					</tr>

					<s2ui:textFieldRow name='mask' labelCode='aclEntry.mask.label' bean="${aclEntry}"
					                   size='50' labelCodeDefault='Mask' value="${aclEntry?.mask}"/>

					<s2ui:checkboxRow name='granting' labelCode='aclEntry.granting.label' bean="${aclEntry}"
		                           labelCodeDefault='Granting' value="${aclEntry?.granting}"/>

					<s2ui:checkboxRow name='auditSuccess' labelCode='aclEntry.auditSuccess.label' bean="${aclEntry}"
		                           labelCodeDefault='Audit Success' value="${aclEntry?.auditSuccess}"/>

					<s2ui:checkboxRow name='auditFailure' labelCode='aclEntry.auditFailure.label' bean="${aclEntry}"
		                           labelCodeDefault='Audit Failure' value="${aclEntry?.auditFailure}"/>

					<tr class="prop">
						<td valign="top">
							<s2ui:submitButton elementId='create' form='aclEntryCreateForm' messageCode='default.button.create.label'/>
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
