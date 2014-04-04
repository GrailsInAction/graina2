<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclClass.label', default: 'AclClass')}" />
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='aclClassEditForm'>
		<g:hiddenField name="id" value="${aclClass?.id}"/>
		<g:hiddenField name="version" value="${aclClass?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>
					<s2ui:textFieldRow name='className' labelCode='aclClass.className.label' bean="${aclClass}"
					                   labelCodeDefault='Class Name' size='60' value="${aclClass?.className}"/>

					<tr>
						<td colspan='2'>
							<g:link action='aclObjectIdentitySearch' controller='aclObjectIdentity' params='[aclClass: aclClass.id]'>View Associated OIDs</g:link>
						</td>
					</tr>
					<tr>
						<td colspan='2'>
							<g:link action='aclEntrySearch' controller='aclEntry' params='[aclClass: aclClass.id]'>View Associated ACL Entries</g:link>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='aclClassEditForm' messageCode='default.button.update.label'/>

		<g:if test='${aclClass}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${aclClass}'>
	<s2ui:deleteButtonForm instanceId='${aclClass.id}'/>
	</g:if>

</div>

</body>
</html>
