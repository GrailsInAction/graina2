modules = {
	'spring-security-ui' {
		dependsOn 'jquery-ui'

		for (name in ['reset',
		              'smoothness/jquery-ui-1.10.3.custom',
		              'jquery.jgrowl',
		              'jquery.safari-checkbox',
		              'date_input',
		              'jquery.jdMenu',
		              'jquery.jdMenu.slate',
		              'table',
		              'spring-security-ui']) {
			resource url: [dir: 'css', file: name + '.css', plugin: 'spring-security-ui']
		}

		for (name in ['jquery.jgrowl',
		              'jquery.checkbox',
		              'jquery.date_input',
		              'jquery.positionBy',
		              'jquery.bgiframe',
		              'jquery.jdMenu',
		              'jquery.dataTables.min']) {
			resource url: [dir: 'js/jquery', file: name + '.js', plugin: 'spring-security-ui']
		}

		resource url: [dir: 'js', file: 'spring-security-ui.js', plugin: 'spring-security-ui']
	}

	'register' {
		dependsOn 'jquery-ui'

		for (name in ['reset',
		              'spring-security-ui',
		              'smoothness/jquery-ui-1.10.3.custom',
		              'jquery.jgrowl',
		              'jquery.safari-checkbox',
		              'auth']) {
			resource url: [dir: 'css', file: name + '.css', plugin: 'spring-security-ui']
		}

		for (name in ['jquery.jgrowl', 'jquery.checkbox']) {
			resource url: [dir: 'js/jquery', file: name + '.js', plugin: 'spring-security-ui']
		}

		resource url: [dir: 'js', file: 'spring-security-ui.js', plugin: 'spring-security-ui']
	}

	'ajax-login' {
		dependsOn 'jquery'
		resource url: [dir: 'js/jquery', file: 'jquery.form.js', plugin: 'spring-security-ui']
		resource url: [dir: 'js',        file: 'ajaxLogin.js',   plugin: 'spring-security-ui']
	}
}
