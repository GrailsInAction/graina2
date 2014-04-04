$(document).ready(function() {
	var buttons = {
		Cancel: function() {
			$(this).dialog('close');
		}
	};
	buttons[loginButtonCaption] = function() {
		$('#loginForm').submit();
	};

	$("#loginFormContainer").dialog({
		autoOpen: false,
		height: 270,
		width: 300,
		modal: true,
		buttons: buttons
	});

	$('#loginForm').bind('submit', function() {
		$(this).ajaxSubmit({
			target: '#loginMessage',
			beforeSubmit: function() {
				$('#loginMessage').html(loggingYouIn);
				return true;
			},
			success: function(json) {
				if (json.success) {
					if (json.url) {
						document.location = json.url;
					}
					else {
						$('#loginFormContainer').dialog('close');
						$('#loginLinkContainer').html('Logged in as ' + json.username + ' (' + logoutLink + ')');
					}
				}
				else if (json.error) {
					$('#loginMessage').html("<span class='errorMessage'>" + json.error + '</error>');
				}
				else {
					$('#loginMessage').html(responseText);
				}
			},
			dataType: 'json'
		});
		return false;
	});

	$('#loginLink').click(function() {
		$('#loginFormContainer').show().dialog('open');
		$('#username').focus();
	});
});
