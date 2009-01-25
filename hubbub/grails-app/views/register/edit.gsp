<head>
	<meta name="layout" content="main" />
	<title>Edit Profile</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class='home' href="${createLinkTo(dir:'')}">Home</a></span>
	</div>

	<div class="body">
		<h1>Edit Profile</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${person}">
		<div class="errors">
			<g:renderErrors bean="${person}" as="list" />
		</div>
		</g:hasErrors>

		<g:form>
			<input type="hidden" name="id" value="${person.id}" />
			<input type="hidden" name="version" value="${person.version}" />
			<div class="dialog">
			<table>
				<tbody>
				<tr class='prop'>
					<td valign='top' class='name'><label for='username'>Login Name:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'userId','errors')}'>
						<input type="hidden" name='username' value="${person.userId?.encodeAsHTML()}"/>
						<div style="margin:3px">${person.userId?.encodeAsHTML()}</div>
					</td>
				</tr>

				<tr class='prop'>
					<td valign='top' class='name'><label for='userRealName'>Full Name:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'userRealName','errors')}'>
						<input type="text" name='userRealName' value="${person.userRealName?.encodeAsHTML()}"/>
					</td>
				</tr>

				<tr class='prop'>
					<td valign='top' class='name'><label for='passwd'>Password:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'password','errors')}'>
						<input type="password" name='passwd' value=""/>
					</td>
				</tr>

				<tr class='prop'>
					<td valign='top' class='name'><label for='enabled'>Confirm Password:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'password','errors')}'>
						<input type="password" name='repasswd' />
					</td>
				</tr>

				<tr class='prop'>
					<td valign='top' class='name'><label for='email'>Email:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'email','errors')}'>
						<input type="text" name='email' value="${person.email?.encodeAsHTML()}"/>
					</td>
				</tr>

				<tr class='prop'>
					<td valign='top' class='name'><label for='emailShow'>Show Email:</label></td>
					<td valign='top' class='value ${hasErrors(bean:person,field:'emailShow','errors')}'>
						<g:checkBox name='emailShow' value="${person.emailShow}" ></g:checkBox>
					</td>
				</tr>

				</tbody>
			</table>
			</div>

			<div class="buttons">
				<span class="button"><g:actionSubmit class='save' value="Update" /></span>
			</div>

		</g:form>

	</div>
</body>
