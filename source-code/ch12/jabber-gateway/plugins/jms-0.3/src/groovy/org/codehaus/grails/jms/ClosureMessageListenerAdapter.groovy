package org.codehaus.grails.jms;

import org.springframework.jms.listener.adapter.MessageListenerAdapter
import javax.jms.JMSException

class ClosureMessageListenerAdapter extends MessageListenerAdapter {

	ClosureMessageListenerAdapter(delegate) {
		super(delegate)
	}
	
	protected Object invokeListenerMethod(String methodName, Object[] arguments) throws JMSException {
		def closure = null
		try {
			closure = getDelegate()[methodName]
		} catch (Exception e) {}
		
		if (closure) {
			closure.call(arguments)
		} else {
			super.invokeListenerMethod(methodName, arguments)
		}
	}

}