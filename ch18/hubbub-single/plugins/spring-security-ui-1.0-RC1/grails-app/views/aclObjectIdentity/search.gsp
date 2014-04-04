<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.aclObjectIdentity.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='275' elementId='formContainer'
	           titleCode='spring.security.ui.aclObjectIdentity.search'>

	<g:form action='aclObjectIdentitySearch' name='aclObjectIdentitySearchForm'>

		<br/>

		<table>
			<tbody>
			<tr>
				<td><g:message code='aclObjectIdentity.aclClass.label' default='AclClass'/>:</td>
				<td colspan='3'>
					<g:select name="aclClass" from="${classes}" optionKey="id" optionValue='className'
					          value="${aclClass}" noSelection="['null': 'All']"/>
				</td>
			</tr>
			<tr>
				<td><g:message code='aclObjectIdentity.objectId.label' default='Object ID'/>:</td>
				<td colspan='3'><g:textField name='objectId' size='50' maxlength='255' value='${objectId}'/></td>
			</tr>
			<tr>
				<td><g:message code='aclObjectIdentity.owner.label' default='Owner'/>:</td>
				<td colspan='3'>
					<g:select name='ownerSid' from="${sids}" optionKey="id" optionValue='sid'
					          value="${ownerSid}" noSelection="['null': 'All']" />
				</td>
			</tr>
			<tr>
				<td><g:message code='aclObjectIdentity.parent.label' default='Parent'/>:</td>
				<td colspan='3'><g:textField name='parent' size='50' maxlength='255' value='${parent}'/></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><g:message code='spring.security.ui.search.true'/></td>
				<td><g:message code='spring.security.ui.search.false'/></td>
				<td><g:message code='spring.security.ui.search.either'/></td>
			</tr>
			<tr>
				<td><g:message code='aclObjectIdentity.entriesInheriting.label' default='Entries Inheriting'/>:</td>
				<g:radioGroup name='entriesInheriting' labels="['','','']" values="[1,-1,0]" value='${entriesInheriting}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td colspan='4'><s2ui:submitButton elementId='search' form='aclObjectIdentitySearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [aclClass: aclClass, objectId: objectId, ownerSid: ownerSid, parent: parent, entriesInheriting: entriesInheriting]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="id" title="${message(code: 'aclObjectIdentity.id.label', default: 'ID')}" />
			<g:sortableColumn property="aclClass.className" title="${message(code: 'aclObjectIdentity.aclClass.label', default: 'AclClass')}" />
			<g:sortableColumn property="objectId" title="${message(code: 'aclObjectIdentity.objectId.label', default: 'Object ID')}" />
			<g:sortableColumn property="entriesInheriting" title="${message(code: 'aclObjectIdentity.entriesInheriting.label', default: 'Entries Inheriting')}" />
			<g:sortableColumn property="owner.sid" title="${message(code: 'aclObjectIdentity.owner.label', default: 'Owner')}" />
			<g:sortableColumn property="parent.id" title="${message(code: 'aclObjectIdentity.parent.label', default: 'Parent')}" />
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="oid">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${oid.id}">${oid.id}</g:link></td>
			<td><g:link action="edit" controller='aclClass' id="${oid.aclClass.id}">${fieldValue(bean: oid.aclClass, field: "className")}</g:link></td>
			<td>${fieldValue(bean: oid, field: "objectId")}</td>
			<td><g:formatBoolean boolean="${oid.entriesInheriting}" /></td>
			<td>
			<g:if test='${oid.owner && oid.owner.principal}'>
				<g:link action="edit" controller='user' params='[username: oid.owner.sid]'>${fieldValue(bean: oid.owner, field: "sid")}</g:link>
			</g:if>
			<g:if test='${oid.owner && !oid.owner.principal}'>
				<g:link action="edit" controller='role' params='[name: oid.owner.sid]'>${fieldValue(bean: oid.owner, field: "sid")}</g:link>
			</g:if>
			&nbsp;
			</td>
			<td>
			<g:if test='${oid.parent}'>
				<g:link action="edit" id='${oid.parent.id}'>${oid.parent.id}</g:link>
			</g:if>
			&nbsp;
			</td>
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
<s2ui:initCheckboxes/>
</script>

</body>
</html>
