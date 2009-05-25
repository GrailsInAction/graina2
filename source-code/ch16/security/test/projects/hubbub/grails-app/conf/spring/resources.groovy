import com.grailsinaction.security.HashPasswordHandler
import com.grailsinaction.security.Sha1Hasher

import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.hibernate.jmx.StatisticsService

// Place your Spring DSL code here
beans = {
    // Hibernate statistics collection.
    hibernateStats(StatisticsService) {
        statisticsEnabled = true
        sessionFactory = ref("sessionFactory")
    }

    mbeanServer(MBeanServerFactoryBean) {
        locateExistingServerIfPossible = true
    }

    exporter(MBeanExporter) {
        server = mbeanServer
        beans = ["org.hibernate:name=statistics": hibernateStats]
    }
}
