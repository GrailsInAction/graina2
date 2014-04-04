import grails.plugin.dumbster.Dumbster

import javax.mail.Message.RecipientType

import com.dumbster.smtp.SmtpMessage

class DumbsterGrailsPlugin {

	String version = '0.2'
	String grailsVersion = '2.0 > *'
	String author = 'Burt Beckwith'
	String authorEmail = 'beckwithb@vmware.com'
	String title = 'Dumbster Plugin'
	String description = 'Adds support for the Dumbster SMTP server for testing'
	String documentation = 'http://grails.org/plugin/dumbster'
	List pluginExcludes = [
		'docs/**',
		'src/docs/**'
	]

	String license = 'APACHE'
	def issueManagement = [system: 'GitHub', url: 'https://github.com/burtbeckwith/grails-dumbster/issues']
	def scm = [url: 'https://github.com/burtbeckwith/grails-dumbster']

	def doWithSpring = {
		def conf = application.config.dumbster
		if (!conf.enabled) {
			return
		}

		dumbster(Dumbster) { bean ->
			grailsApplication = application
			bean.initMethod = 'start'
			bean.destroyMethod = 'stop'
		}
	}

	def doWithDynamicMethods = { ctx ->
		def mc = SmtpMessage.metaClass

		def split = { String s -> s.split(',').collect { it.trim() } }

		mc.getSubject = { -> delegate.getHeaderValue('Subject') }
		mc.getDate =    { -> delegate.getHeaderValue('Date') }
		mc.getTo =      { -> delegate.tos[0] }
		mc.getTos =     { -> split(delegate.getHeaderValue('To')) }
		mc.getCc =      { -> delegate.ccs[0] }
		mc.getCcs =     { -> split(delegate.getHeaderValue('Cc')) }
		mc.getBcc =     { -> delegate.bccs[0] }
		mc.getBccs =    { -> split(delegate.getHeaderValue('Bcc')) }
	}
}
