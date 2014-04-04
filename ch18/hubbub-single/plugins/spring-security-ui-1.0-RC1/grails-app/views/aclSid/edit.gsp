<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclSid.label', default: 'AclSid')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='275' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='aclSidEditForm'>
		<g:hiddenField name="id" value="${aclSid?.id}"/>
		<g:hiddenField name="version" value="${aclSid?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>
					<s2ui:textFieldRow name='sid' labelCode='aclSid.sid.label' bean="${aclSid}"
					                   labelCodeDefault='SID' size='50' value="${aclSid?.sid}"/>

					<s2ui:checkboxRow name='principal' labelCode='aclSid.principal.label' bean="${aclSid}"
		                           labelCodeDefault='Principal' value="${aclSid?.principal}"/>

					<tr>
						<td colspan='2'>
							<g:link action='aclObjectIdentitySearch' controller='aclObjectIdentity' params='[ownerSid: aclSid.id]'>View Associated OIDs</g:link>
						</td>
					</tr>
					<tr>
						<td colspan='2'>
							<g:link action='aclEntrySearch' controller='aclEntry' params='[sid: aclSid.id]'>View Associated ACL Entries</g:link>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='aclSidEditForm' messageCode='default.button.update.label'/>

		<g:if test='${aclSid}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${aclSid}'>
	<s2ui:deleteButtonForm instanceId='${aclSid.id}'/>
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
