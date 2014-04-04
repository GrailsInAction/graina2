<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.aclEntry.search'/></title>
</head>

<body>

<div>

	<s2ui:form width='100%' height='310' elementId='formContainer'
	           titleCode='spring.security.ui.aclEntry.search'>

	<g:form action='aclEntrySearch' name='aclEntrySearchForm'>
		<table>
			<tbody>
			<tr>
				<td><g:message code='aclEntry.aclObjectIdentity.label' default='AclObjectIdentity'/>:</td>
				<td colspan='3'><g:textField name='aclObjectIdentity' size='50' maxlength='255' value='${aclObjectIdentity}'/></td>
			</tr>
			<tr>
				<td><g:message code='aclEntry.aceOrder.label' default='Ace Order'/>:</td>
				<td colspan='3'><g:textField name='aceOrder' size='50' maxlength='255' value='${aceOrder}'/></td>
			</tr>
			<tr>
				<td><g:message code='aclEntry.sid.label' default='SID'/>:</td>
				<td colspan='3'>
					<g:select name='sid' from="${sids}" optionKey="id" optionValue='sid'
					          value="${sid}" noSelection="['null': 'All']" />
				</td>
			</tr>
			<tr>
				<td><g:message code='aclEntry.mask.label' default='Mask'/>:</td>
				<td colspan='3'><g:textField name='mask' size='50' maxlength='255' value='${mask}'/></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><g:message code='spring.security.ui.search.true'/></td>
				<td><g:message code='spring.security.ui.search.false'/></td>
				<td><g:message code='spring.security.ui.search.either'/></td>
			</tr>
			<tr>
				<td><g:message code='aclEntry.granting.label' default='Granting'/>:</td>
				<g:radioGroup name='granting' labels="['','','']" values="[1,-1,0]" value='${granting}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td><g:message code='aclEntry.auditSuccess.label' default='Audit Success'/>:</td>
				<g:radioGroup name='auditSuccess' labels="['','','']" values="[1,-1,0]" value='${auditSuccess}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td><g:message code='aclEntry.auditFailure.label' default='Audit Failure'/>:</td>
				<g:radioGroup name='auditFailure' labels="['','','']" values="[1,-1,0]" value='${auditFailure}'>
				<td><%=it.radio%></td>
				</g:radioGroup>
			</tr>
			<tr>
				<td colspan='4'><s2ui:submitButton elementId='search' form='aclEntrySearchForm' messageCode='spring.security.ui.search'/></td>
			</tr>
			</tbody>
		</table>
	</g:form>

	</s2ui:form>

	<g:if test='${searched}'>

<%
def queryParams = [aclObjectIdentity: aclObjectIdentity, aceOrder: aceOrder, sid: sid, mask: mask, granting: granting, auditSuccess: auditSuccess, auditFailure: auditFailure]
%>

	<div class="list">
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="id" title="${message(code: 'aclEntry.id.label', default: 'ID')}" />
			<g:sortableColumn property="aclObjectIdentity.id" title="${message(code: 'aclEntry.aclObjectIdentity.label', default: 'AclObjectIdentity')}" />
			<g:sortableColumn property="aceOrder" title="${message(code: 'aclEntry.aceOrder.label', default: 'Ace Order')}" />
			<g:sortableColumn property="sid.id" title="${message(code: 'aclEntry.sid.label', default: 'SID')}" />
			<g:sortableColumn property="mask" title="${message(code: 'aclEntry.mask.label', default: 'Mask')}" />
			<g:sortableColumn property="granting" title="${message(code: 'aclEntry.granting.label', default: 'Granting')}" />
			<g:sortableColumn property="auditSuccess" title="${message(code: 'aclEntry.auditSuccess.label', default: 'Audit Success')}" />
			<g:sortableColumn property="auditFailure" title="${message(code: 'aclEntry.auditFailure.label', default: 'Audit Failure')}" />
		</tr>
		</thead>

		<tbody>
		<g:each in="${results}" status="i" var="entry">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td><g:link action="edit" id="${entry.id}">${entry.id}</g:link></td>
			<td><g:link action="edit" controller='aclObjectIdentity' id="${entry.aclObjectIdentity.id}">${entry.aclObjectIdentity.id}</g:link></td>
			<td>${entry.aceOrder}</td>
			<td><g:link action="edit" controller='aclSid' id="${entry.sid.id}">${fieldValue(bean: entry.sid, field: "sid")}</g:link></td>
			<td>${permissionFactory.buildFromMask(entry.mask)}</td>
			<td><g:formatBoolean boolean="${entry.granting}" /></td>
			<td><g:formatBoolean boolean="${entry.auditSuccess}" /></td>
			<td><g:formatBoolean boolean="${entry.auditFailure}" /></td>
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
