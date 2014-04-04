// SpringSecurityUI namespace
if (typeof SpringSecurityUI == 'undefined') {
	SpringSecurityUI = new Object();

	/**
	 * Displays a message.
	 * @param  type  the type of message
	 * @param  text  the message text
	 * @param  duration  how long to display the message
	 */
	/*public*/ SpringSecurityUI.message = function(type, text, duration) {
		var clazz = 'icon '
		if (type == 'error') {
			clazz += 'icon_error';
		}
		else if (type == 'info') {
			clazz += 'icon_info';
		}
		$.jGrowl('<span class="' + clazz + '">' + text + '</span>', {
			life: duration
		});
	};
}
