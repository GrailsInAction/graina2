<html>

<head>
<title>Login</title>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'reset.css',plugin:'spring-security-ui')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'spring-security-ui.css',plugin:'spring-security-ui')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'auth.css',plugin:'spring-security-ui')}"/>
</head>

<body>
	<div id='login'>
		<div class='inner'>
			<div class='fheader'>Please Login..</div>
			<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
				<p>
					<label for='openid_identifier'>OpenID Identity</label>
					<g:textField name='openid_identifier' class='text_'/>
				</p>
				<p>
					<input type='submit' value='Login'/>
				</p>
			</form>
		</div>
	</div>

<script type='text/javascript'>
(function() { document.forms['loginForm'].elements['openid_identifier'].focus(); })();
</script>

</body>
</html>
