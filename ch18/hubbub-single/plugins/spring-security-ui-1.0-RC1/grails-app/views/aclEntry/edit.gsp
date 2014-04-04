<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclEntry.label', default: 'AclEntry')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='310' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='aclEntryEditForm'>
		<g:hiddenField name="id" value="${aclEntry?.id}"/>
		<g:hiddenField name="version" value="${aclEntry?.version}"/>
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

					<s2ui:textFieldRow name='mask' labelCode='aclEntry.mask.label'
					                   size='50' bean="${aclEntry}"
					                   labelCodeDefault='Mask' value="${aclEntry?.mask}"/>

					<s2ui:checkboxRow name='granting' labelCode='aclEntry.granting.label' bean="${aclEntry}"
		                           labelCodeDefault='Granting' value="${aclEntry?.granting}"/>

					<s2ui:checkboxRow name='auditSuccess' labelCode='aclEntry.auditSuccess.label' bean="${aclEntry}"
		                           labelCodeDefault='Audit Success' value="${aclEntry?.auditSuccess}"/>

					<s2ui:checkboxRow name='auditFailure' labelCode='aclEntry.auditFailure.label' bean="${aclEntry}"
		                           labelCodeDefault='Audit Failure' value="${aclEntry?.auditFailure}"/>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='aclEntryEditForm' messageCode='default.button.update.label'/>

		<g:if test='${aclEntry}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${aclEntry}'>
	<s2ui:deleteButtonForm instanceId='${aclEntry.id}'/>
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
