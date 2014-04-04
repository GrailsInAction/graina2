<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}"/>
	<title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='200' elementId='formContainer'
	           titleCode='default.create.label' titleCodeArgs='[entityName]'>

	<g:form action="save" name='roleCreateForm'>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='authority' labelCode='role.authority.label' bean="${role}"
					                   size='50' labelCodeDefault='Authority' value="${role?.authority}"/>

					<tr><td>&nbsp;</td></tr>

					<tr class="prop">
						<td valign="top">
							<s2ui:submitButton elementId='create' form='roleCreateForm' messageCode='default.button.create.label'/>
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
	$('#authority').focus();
});
</script>

</body>
</html>
