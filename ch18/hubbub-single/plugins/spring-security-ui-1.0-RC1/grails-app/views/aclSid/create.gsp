<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'aclSid.label', default: 'AclSid')}"/>
	<title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='default.create.label' titleCodeArgs='[entityName]'>

	<g:form action="save" name='aclSidCreateForm'>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='sid' labelCode='aclSid.sid.label' bean="${aclSid}"
					                   size='50' labelCodeDefault='SID' value="${aclSid?.sid}"/>

					<s2ui:checkboxRow name='principal' labelCode='aclSid.principal.label' bean="${aclSid}"
		                           labelCodeDefault='Principal' value="${aclSid?.principal}"/>

					<tr><td>&nbsp;</td></tr>

					<tr class="prop">
						<td valign="top">
							<s2ui:submitButton elementId='create' form='aclSidCreateForm' messageCode='default.button.create.label'/>
						</td>
					</tr>

				</tbody>
			</table>
		</div>

	</g:form>

	</s2ui:form>

</div>

<script>
$(document).ready(function() {
	$('#sid').focus();
});

<s2ui:initCheckboxes/>
</script>

</body>
</html>
