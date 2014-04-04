<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'requestmap.label', default: 'Requestmap')}"/>
	<title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='default.create.label' titleCodeArgs='[entityName]'>

	<g:form action="save" name='requestmapCreateForm'>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='url' labelCode='requestmap.url.label' bean="${requestmap}"
					                   size='50' labelCodeDefault='URL' value="${requestmap?.url}"/>

					<s2ui:textFieldRow name='configAttribute' labelCode='requestmap.configAttribute.label'
					                   size='50' bean="${requestmap}" labelCodeDefault='Config Attribute'
					                   value="${requestmap?.configAttribute}"/>

					<tr><td>&nbsp;</td></tr>

					<tr class="prop">
						<td valign="top">
							<s2ui:submitButton elementId='create' form='requestmapCreateForm' messageCode='default.button.create.label'/>
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
	$('#url').focus();
});
</script>

</body>
</html>
