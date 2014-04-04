<%@ page import="org.codehaus.groovy.grails.plugins.PluginManagerHolder" %>
<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title><g:layoutTitle default='Security Management Console'/></title>

<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon"/>

<s2ui:resources module='spring-security-ui' />
<%--

The 'resources' tag in SecurityUiTagLib renders these tags if you're not using the resources plugin:

   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'reset.css',plugin:'spring-security-ui')}"/>
   <g:javascript library='jquery' plugin='jquery' />
   <jqui:resources />
   <link rel="stylesheet" media="screen" href="${resource(dir:'css/smoothness',file:'jquery-ui-1.10.3.custom.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.jgrowl.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.safari-checkbox.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'date_input.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.jdMenu.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.jdMenu.slate.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'table.css',plugin:'spring-security-ui')}"/>
   <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'spring-security-ui.css',plugin:'spring-security-ui')}"/>

or these if you are:

   <r:require module="spring-security-ui"/>
   <r:layoutResources/>

If you need to customize the resources, replace the <s2ui:resources> tag with
the explicit tags above and edit those, not the taglib code.
--%>

<%-- tab icons --%>
<style>
.icon_role {
	background-image: url('${fam.icon(name: 'lock')}');
}
.icon_users {
	background-image: url('${fam.icon(name: 'group')}');
}
.icon_user {
	background-image: url('${fam.icon(name: 'user')}');
}
.icon_error {
	background-image: url('${fam.icon(name: 'exclamation')}');
}
.icon_info {
	background-image: url('${fam.icon(name: 'information')}');
}
.icon, .ui-tabs .ui-tabs-nav li a.icon {
	background-repeat: no-repeat;
	padding-left: 24px;
	background-position: 4px center;
}
</style>

<g:layoutHead/>

</head>

<body>

	<div>

		<div>

			<ul class="jd_menu jd_menu_slate">
				<li><a class="accessible"><g:message code="spring.security.ui.menu.users"/></a>
					<ul>
						<li><g:link controller="user" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
						<li><g:link controller="user" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
					</ul>
				</li>
				<li><a class="accessible"><g:message code="spring.security.ui.menu.roles"/></a>
					<ul>
						<li><g:link controller="role" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
						<li><g:link controller="role" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
					</ul>
				</li>
				<g:if test='${SpringSecurityUtils.securityConfig.securityConfigType?.toString() == 'Requestmap'}'>
				<li><a class="accessible"><g:message code="spring.security.ui.menu.requestmaps"/></a>
					<ul>
						<li><g:link controller="requestmap" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
						<li><g:link controller="requestmap" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
					</ul>
				</li>
				</g:if>
				<g:if test='${SpringSecurityUtils.securityConfig.rememberMe.persistent}'>
				<li><a class="accessible"><g:message code="spring.security.ui.menu.persistentLogins"/></a>
					<ul>
						<li><g:link controller="persistentLogin" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
					</ul>
				</li>
				</g:if>
				<li><a class="accessible"><g:message code="spring.security.ui.menu.registrationCode"/></a>
					<ul>
						<li><g:link controller="registrationCode" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
					</ul>
				</li>
				<g:if test="${PluginManagerHolder.pluginManager.hasGrailsPlugin('springSecurityAcl')}">
				<li><a class="accessible"><g:message code="spring.security.ui.menu.acl"/></a>
					<ul>
						<li><g:message code="spring.security.ui.menu.aclClass"/> &raquo;
							<ul>
								<li><g:link controller="aclClass" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
								<li><g:link controller="aclClass" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
							</ul>
						</li>
						<li><g:message code="spring.security.ui.menu.aclSid"/> &raquo;
							<ul>
								<li><g:link controller="aclSid" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
								<li><g:link controller="aclSid" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
							</ul>
						</li>
						<li><g:message code="spring.security.ui.menu.aclObjectIdentity" /> &raquo;
							<ul>
								<li><g:link controller="aclObjectIdentity" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
								<li><g:link controller="aclObjectIdentity" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
							</ul>
						</li>
						<li><g:message code="spring.security.ui.menu.aclEntry" /> &raquo;
							<ul>
								<li><g:link controller="aclEntry" action='search'><g:message code="spring.security.ui.search"/></g:link></li>
								<li><g:link controller="aclEntry" action='create'><g:message code="spring.security.ui.create"/></g:link></li>
							</ul>
						</li>
					</ul>
				</li>
				</g:if>
				<li><a class="accessible"><g:message code="spring.security.ui.menu.appinfo"/></a>
					<ul>
					<li><g:link action='config' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.config'/></g:link></li>
					<li><g:link action='mappings' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.mappings'/></g:link></li>
					<li><g:link action='currentAuth' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.auth'/></g:link></li>
					<li><g:link action='usercache' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.usercache'/></g:link></li>
					<li><g:link action='filterChain' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.filters'/></g:link></li>
					<li><g:link action='logoutHandler' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.logout'/></g:link></li>
					<li><g:link action='voters' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.voters'/></g:link></li>
					<li><g:link action='providers' controller='securityInfo'><g:message code='spring.security.ui.menu.appinfo.providers'/></g:link></li>
					</ul>
				</li>
			</ul>

			<div id='s2ui_header_body'>

				<div id='s2ui_header_title'>
					Spring Security Management Console
				</div>

				<span id='s2ui_login_link_container'>

				<nobr>
				<div id='loginLinkContainer'>
				<sec:ifLoggedIn>
				Logged in as <sec:username/> (<g:link controller='logout'>Logout</g:link>)
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<a href='#' id='loginLink'>Login</a>
				</sec:ifNotLoggedIn>

				<sec:ifSwitched>
				<a href='${request.contextPath}/j_spring_security_exit_user'>
					Resume as <sec:switchedUserOriginalUsername/>
				</a>
				</sec:ifSwitched>
				</div>
				</nobr>

				</span>
			</div>

		</div>

		<div id="s2ui_main">
			<div id="s2ui_content">
				<s2ui:layoutResources module='spring-security-ui' />
				<g:layoutBody/>
<%--
<g:javascript src='jquery/jquery.jgrowl.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.checkbox.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.date_input.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.positionBy.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.bgiframe.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.jdMenu.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.dataTables.min.js' plugin='spring-security-ui'/>
<g:javascript src='spring-security-ui.js' plugin='spring-security-ui'/>
--%>
			</div>
		</div>

	</div>

<g:render template='/includes/ajaxLogin' plugin='spring-security-ui'/>

<s2ui:showFlash/>

</body>
</html>
