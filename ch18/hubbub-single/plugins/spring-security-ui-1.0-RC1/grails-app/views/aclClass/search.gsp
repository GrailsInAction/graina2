<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.aclClass.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='175' elementId='formContainer'
	           titleCode='spring.security.ui.aclClass.search'>

	<g:form action='aclClassSearch' name='aclClassSearchForm'>

		<br/>

		<table>
			<tbody>
			<tr>
				<td><g:message code='aclClass.className.label' default='Class Name'/>:</td>
				<td><g:textField name='className' size='60' maxlength='255' autocomplete='off' value='${className}'/></td>
			</tr>
			<tr><td colspan='2'>&nbsp;</td></tr>
			<tr>
				<td colspan='2'><s2ui:submitButton elementId='search' form='aclClassSearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [className: className]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="className" title="${message(code: 'aclClass.className.label', default: 'Class Name')}" params="${queryParams}"/>
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="aclClass">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${aclClass.id}">${fieldValue(bean: aclClass, field: "className")}</g:link></td>
		</tr>
		</g:each>
		</tbody>
	</table>
	</div>

	<div class="paginateButtons">
		<g:paginate total="${totalCount}" params="${queryParams}" />
	</div>

	<div style="text-align:center">
		<s2ui:paginationSummary total="${totalCount}"/>
	</div>

	</g:if>

</div>

<script>
$(document).ready(function() {
	$("#className").focus().autocomplete({
		minLength: 3,
		cache: false,
		source: "${createLink(action: 'ajaxAclClassSearch')}"
	});
});

</script>

</body>
</html>
