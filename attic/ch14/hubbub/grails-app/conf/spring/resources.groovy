import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter
import org.hibernate.jmx.StatisticsService

// Place your Spring DSL code here
beans = {
    // This is required for the HTTP Basic authentication to work properly.
    // It fixes the plugin so that a 401 status is sent back to the client
    // for an unauthenticated user, rather than the default 302.
    authenticationEntryPoint(org.springframework.security.ui.basicauth.BasicProcessingFilterEntryPoint) {
        realmName = 'Hubbub'
    }

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
