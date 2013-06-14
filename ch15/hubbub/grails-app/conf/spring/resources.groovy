import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.jms.connection.SingleConnectionFactory

beans = {
    jmsConnectionFactory(SingleConnectionFactory) {
        targetConnectionFactory = { ActiveMQConnectionFactory cf ->
            brokerURL = "tcp://localhost:61616"
        }
    }
}
