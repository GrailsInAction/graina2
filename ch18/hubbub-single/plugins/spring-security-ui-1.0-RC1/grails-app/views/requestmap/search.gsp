<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<g:set var="entityName" value="${message(code: 'requestmap.label', default: 'Requestmap')}"/>
	<title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<div class="body">

	<s2ui:form width='100%' height='200' elementId='formContainer'
	           titleCode='spring.security.ui.requestmap.search'>

	<g:form action='requestmapSearch' name='requestmapSearchForm'>

		<br/>

		<table>
			<tbody>
			<tr>
				<td><g:message code='requestmap.url.label' default='URL'/>:</td>
				<td><g:textField name='url' size='50' maxlength='255' autocomplete='off' value='${url}'/></td>
			</tr>
			<tr>
				<td><g:message code='requestmap.configAttribute.label' default='Config Attribute'/>:</td>
				<td><g:textField name='configAttribute' size='50' maxlength='255' autocomplete='off' value='${configAttribute}'/></td>
			</tr>
			<tr><td colspan='2'>&nbsp;</td></tr>
			<tr>
				<td colspan='2'><s2ui:submitButton elementId='search' form='requestmapSearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [url: url, configAttribute: configAttribute]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="url" title="${message(code: 'requestmap.url.label', default: 'URL')}" params="${queryParams}"/>
			<g:sortableColumn property="configAttribute" title="${message(code: 'requestmap.configAttribute.label', default: 'Config Attribute')}" params="${queryParams}"/>
		</tr>
		</thead>
		<tbody>
		<g:each in="${results}" status="i" var="requestmap">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${requestmap.id}">${fieldValue(bean: requestmap, field: "url")}</g:link></td>
			<td>${fieldValue(bean: requestmap, field: "configAttribute")}</td>
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
	$("#url").focus();
});
</script>

</body>
</html>
