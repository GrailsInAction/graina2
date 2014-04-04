<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.aclSid.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='225' elementId='formContainer'
	           titleCode='spring.security.ui.aclSid.search'>

	<g:form action='aclSidSearch' name='aclSidSearchForm'>

		<br/>

		<table>
			<tbody>

			<tr>
				<td><g:message code='aclSid.sid.label' default='SID'/>:</td>
				<td colspan='3'><g:textField name='sid' size='50' maxlength='255' autocomplete='off' value='${sid}'/></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><g:message code='spring.security.ui.search.true'/></td>
				<td><g:message code='spring.security.ui.search.false'/></td>
				<td><g:message code='spring.security.ui.search.either'/></td>
			</tr>
			<tr>
				<td><g:message code='aclSid.principal.label' default='Principal'/>:</td>
				<g:radioGroup name='principal' labels="['','','']" values="[1,-1,0]" value='${principal}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr><td colspan='4'>&nbsp;</td></tr>
			<tr>
				<td colspan='4'><s2ui:submitButton elementId='search' form='aclSidSearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [sid: sid, principal: principal]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="sid" title="${message(code: 'aclSid.sid.label', default: 'SID')}" params="${queryParams}"/>
			<g:sortableColumn property="principal" title="${message(code: 'aclSid.principal.label', default: 'Principal')}" params="${queryParams}"/>
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="aclSid">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${aclSid.id}">${fieldValue(bean: aclSid, field: "sid")}</g:link></td>
			<td><g:formatBoolean boolean="${aclSid.principal}"/></td>
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
	$("#sid").focus().autocomplete({
		minLength: 3,
		cache: false,
		source: "${createLink(action: 'ajaxAclSidSearch')}"
	});
});

<s2ui:initCheckboxes/>

</script>

</body>
</html>
