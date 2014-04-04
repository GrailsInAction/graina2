package grails.plugin.dumbster

import org.springframework.mail.javamail.JavaMailSenderImpl

import com.dumbster.smtp.SimpleSmtpServer
import com.dumbster.smtp.SmtpMessage

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class Dumbster {

	def grailsApplication

	Integer port

	protected SimpleSmtpServer server

	/**
	 * Starts the server; called by Spring, so shouldn't be called directly.
	 */
	void start() {

		def conf = grailsApplication.config.dumbster

		if (conf.port instanceof Number) {
			port = conf.port
		}
		else {
			port = 1025
			while (true) {
				try {
					new ServerSocket(port).close()

					// update the mail plugin's JavaMailSender if available
					if (grailsApplication.mainContext.containsBean('mailSender')) {
						def mailSender = grailsApplication.mainContext.mailSender
						if (mailSender instanceof JavaMailSenderImpl) {
							mailSender.port = port
						}
					}

					break
				}
				catch (IOException e) {
					port++
					if (port > 10000) {
						throw new RuntimeException('Cannot find open port for Dumbster SMTP server')
					}
				}
			}
		}

		server = SimpleSmtpServer.start(port)
	}

	/**
	 * Stops the server; called by Spring, so shouldn't be called directly.
	 */
	void stop() {
		server?.stop()
	}

	/**
	 * Remove all sent emails. Call this in the setUp() method in your integration tests.
	 */
	void reset() {
		if (!server) return

		for (Iterator iter = server.receivedEmail; iter.hasNext(); ) {
			iter.next()
			iter.remove()
		}
	}

	/**
	 * Check if stopped.
	 * @return <code>true</code> if the server was stopped (or never started)
	 */
	boolean isStopped() { server ? server.stopped : true }

	/**
	 * Get all current messages.
	 * @return the messages
	 */
	List<SmtpMessage> getMessages() { server ? server.receivedEmail.collect { it } : [] }

	/**
	 * Get the number of sent messages.
	 * @return the number
	 */
	int getMessageCount() { server ? server.receivedEmailSize : 0 }
}
