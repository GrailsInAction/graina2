<html>
<head>
	
	<title>Search</title>
	<style>
		body {
			font-family: Arial;
		}
		label {
			margin-right: 1em;
			float: left;
			text-align: right;
			width: 10em;
		}
	</style>
</head>
<body>
	<g:if test="${profiles}">
	Result count: ${profiles.size()}
	<p/>
	<ul>
	<g:each var="profile" in="${profiles}">
		<li>${profile.fullName} ${profile.homepage} ${profile.email}
	</g:each>
	</ul>
	</g:if>
	<g:else>
	<fieldset>
	<legend>Search Me</legend>
	<g:form action="search">	
		<label for="fullName">Name: </label><g:textField name="fullName" /><p/>
		<label for="homepage">Homepage: </label><g:textField name="homepage"/><p/>
		<label for="email">Email: </label><g:textField name="email"/><p/>
		<label for="queryType">Query</label>
		<g:radioGroup name="queryType" labels="['And', 'Or', 'Not']" values="['and', 'or', 'not']" value="and" >
		<g:message code="${it.label}" /> ${it.radio}
		</g:radioGroup>	
		<p/>
		<label for="q">then...</label><g:submitButton name="q" value="Search"/><p/>
	</g:form>
	</fieldset>
	</g:else>
</body>
</html>