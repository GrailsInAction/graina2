<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'requestmap.label', default: 'Requestmap')}"/>
	<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='default.edit.label' titleCodeArgs='[entityName]'>

	<g:form action='update' name='requestmapEditForm'>
		<g:hiddenField name="id" value="${requestmap?.id}"/>
		<g:hiddenField name="version" value="${requestmap?.version}"/>
		<div class="dialog">

			<br/>

			<table>
				<tbody>

					<s2ui:textFieldRow name='url' size='50' labelCode='requestmap.url.label' bean="${requestmap}"
					                   labelCodeDefault='URL' value="${requestmap?.url}"/>

					<s2ui:textFieldRow name='configAttribute' size='50' labelCode='requestmap.configAttribute.label'
					                   bean="${requestmap}" labelCodeDefault='Config Attribute'
					                   value="${requestmap?.configAttribute}"/>

				</tbody>
			</table>
		</div>

		<div style='float:left; margin-top: 10px;'>
		<s2ui:submitButton elementId='update' form='requestmapEditForm' messageCode='default.button.update.label'/>

		<g:if test='${requestmap}'>
		<s2ui:deleteButton />
		</g:if>

		</div>

	</g:form>

	</s2ui:form>

	<g:if test='${requestmap}'>
	<s2ui:deleteButtonForm instanceId='${requestmap.id}'/>
	</g:if>

</div>

</body>
</html>
