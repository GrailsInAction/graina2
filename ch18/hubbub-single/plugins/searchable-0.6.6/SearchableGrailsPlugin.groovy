/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import grails.plugin.searchable.internal.compass.CompassGpsUtils
import grails.plugin.searchable.internal.compass.DefaultSearchableMethodFactory
import grails.plugin.searchable.internal.compass.SearchableCompassUtils
import grails.plugin.searchable.internal.compass.config.SessionFactoryLookup
import grails.plugin.searchable.internal.compass.domain.DynamicDomainMethodUtils
import grails.plugin.searchable.internal.compass.mapping.DefaultSearchableCompassClassMappingXmlBuilder
import grails.plugin.searchable.internal.compass.spring.DefaultSearchableCompassFactoryBean
import grails.util.Environment

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.compass.gps.device.hibernate.HibernateGpsDevice
import org.compass.gps.impl.SingleCompassGps
import org.springframework.context.ApplicationContext
import org.springframework.core.JdkVersion

/**
* @author Maurice Nicholson
*/
class SearchableGrailsPlugin {
    static LOG = LogFactory.getLog("grails.plugin.searchable.SearchableGrailsPlugin")

    def version = "0.6.6"
    def title = 'Searchable Plugin'
    def description = '''
Adds rich search functionality to Grails domain models.
Built on Compass (http://www.compass-project.org/) and Lucene (http://lucene.apache.org/)
This version is recommended for JDK 1.5+
'''
    def documentation = "http://grails.org/plugin/searchable"

    def grailsVersion = "2.0 > *"

    def license = "APACHE"
    def developers = [ 
        [ name: "Maurice Nicholson", email: "maurice@freeshell.org" ],
        [ name: "Peter Ledbrook", email: "p.ledbrook@cacoethes.co.uk" ] 
    ]
    def organization = [ name: "Grails Plugin Collective", url: "http://github.com/gpc" ]
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPSEARCHABLE" ]
    def scm = [ url: "https://github.com/gpc/grails-searchable" ]

    def loadAfter = ["autobase", "liquibase", "databaseMigration"]
    def config

    def doWithDynamicMethods = { applicationContext ->
        def compass = applicationContext.getBean("compass")
        def searchableMethodFactory = applicationContext.getBean("searchableMethodFactory")
        DynamicDomainMethodUtils.attachDynamicMethods(searchableMethodFactory, application.domainClasses, compass)
    }

    // Build Compass and Compass::GPS
    def doWithSpring = {
        if (!JdkVersion.isAtLeastJava15()) {
            LOG.error("This version of the Searchable Plugin is only compatible with JDK 1.5+. See the documentation at ${documentation} for the JDK 1.4 alternative")
            System.out.println("ERROR: This version of the Searchable Plugin is only compatible with JDK 1.5+. See the documentation at ${documentation} for the JDK 1.4 alternative")
        }

        // Configuration
        config = getConfiguration(parentCtx, application)

        // Compass
        LOG.debug("Defining Compass and Compass::GPS beans")
        def defaultConnection = getDefaultCompassConnection(application)
        compass(DefaultSearchableCompassFactoryBean) { bean ->
            grailsApplication = application
            compassConnection = config.compassConnection ?: defaultConnection
            compassSettings = config.compassSettings instanceof ConfigObject ? config.compassSettings.toProperties() : config.compassSettings
            defaultExcludedProperties = config.defaultExcludedProperties ?: ["password"]
            defaultFormats = config.defaultFormats instanceof ConfigObject ? config.defaultFormats.toProperties() : config.defaultFormats
            compassClassMappingXmlBuilder = new DefaultSearchableCompassClassMappingXmlBuilder()
        }

        // Compass::GPS
//        lifecycleInjector(org.compass.gps.device.hibernate.lifecycle.DefaultHibernateEntityCollectionLifecycleInjector, true) {}
//        compassGpsDevice(SpringHibernate3GpsDevice) {
        compassGpsDevice(HibernateGpsDevice) { bean ->
            bean.destroyMethod = "stop"
            name = "hibernate"
            sessionFactory = { SessionFactoryLookup sfl ->
                sessionFactory = ref("sessionFactory")
            }
            fetchCount = config.fetchCount ?: 5000
//            lifecycleInjector = lifecycleInjector
        }
        compassGps(SingleCompassGps) {
            compass = compass
            gpsDevices = [compassGpsDevice]
        }

        def defaultMethodOptions = config.defaultMethodOptions
        if (!defaultMethodOptions && config.defaultSearchOptions) {
            LOG.warn(
                "The Searchable Plugin \"defaultSearchOptions\" configuration option is deprecated and " +
                "will be removed in the next version. Upgrade the Searchable plugin configuration to the latest format " +
                "and use \"defaultMethodOptions\" instead"
            )
            System.out.println("WARN: " +
                "The Searchable Plugin \"defaultSearchOptions\" configuration option is deprecated and " +
                "will be removed in the next version. Upgrade the Searchable plugin configuration to the latest format " +
                "and use \"defaultMethodOptions\" instead"
            )
            defaultMethodOptions = [search: config.defaultSearchOptions]
        }
        searchableMethodFactory(DefaultSearchableMethodFactory) {
            compass = compass
            compassGps = compassGps
            defaultMethodOptions = defaultMethodOptions
            grailsApplication = application
        }
        LOG.debug("Done defining Compass and Compass::GPS beans")
    }

    // Post initialization spring config
    def doWithApplicationContext = {
        def compass = applicationContext.getBean("compass")
        if (!SearchableCompassUtils.hasMappings(compass)) {
            return false
        }

        // release locks? Defaults to true.
        if (compass.searchEngineIndexManager.isLocked()) {
            if (config.releaseLocksOnStartup != false) {
                compass.searchEngineIndexManager.releaseLocks()
                LOG.warn("The index was forcefully unlocked. The index is probably out of sync and needs re-building")
            }
        }

        // start the gps, mirroring any changes made through Hibernate API
        // to be mirrored to the search engine - defaults to true.
        def mirrorChanges = config.mirrorChanges != false
        def compassGps = applicationContext.getBean("compassGps")
        if (mirrorChanges) {
            compassGps.start()
            LOG.debug("Started Compass::GPS")
        }

        // index the database on startup?
        def bulkIndex = !(config.bulkIndexOnStartup in [false, "fork"])
        def forkBulkIndex = config.bulkIndexOnStartup in ["fork"]
        if (bulkIndex) {
            CompassGpsUtils.index(compassGps, null)
        } else if (forkBulkIndex) {
            Thread.start {
                CompassGpsUtils.index(compassGps, null)
            }
        } else {
            LOG.debug("Not performing bulk index")
        }
    }

    // Get a configuration instance
    private getConfiguration(ApplicationContext applicationContext, GrailsApplication application) {
        // Try to load Searchable.groovy from the current project and merge into the main config.
        def config = application.config
        try {
            Class configClass = application.getClassLoader().loadClass("Searchable")
            ConfigSlurper configSlurper = new ConfigSlurper(Environment.current.name)
            configSlurper.binding = [
                userHome: System.properties['user.home'],
                grailsEnv: application.metadata["grails.env"],
                appName: application.metadata["app.name"],
                appVersion: application.metadata["app.version"]]
            config.merge(configSlurper.parse(configClass))
        } catch (ClassNotFoundException e) {
            LOG.debug("Not found: ${e.message}")
        }

        // If 'searchable' is in the config, return it.
        if (config.searchable) {
            return config.searchable
        }

        // try to load from Spring context bean
        if (applicationContext.containsBean("searchableConfig")) {
            def searchableConfig = applicationContext.getBean("searchableConfig")
            if (searchableConfig instanceof ConfigObject) {
                config.merge(searchableConfig)
            } else if (searchableConfig instanceof Map) {
                def tmp = new ConfigObject()
                tmp.putAll(searchableConfig)
                config.merge(tmp)
            } else {
                throw new IllegalArgumentException("The 'searchableConfig' Spring bean must be a Map or ConfigObject instance but is: ${searchableConfig?.getClass()}")
            }
            return config.searchable
        }
        // try to load it from the previous config file
        try {
            LOG.debug("Trying to load config from 'SearchableConfiguration.class'")
            def obj = Class.forName('SearchableConfiguration', true, Thread.currentThread().contextClassLoader).newInstance()
            LOG.warn(
                "The Searchable Plugin's 'SearchableConfiguration.groovy' file is deprecated and will be removed in the next version! " +
                "Configuration for the Searchable Plugin should now be defined with the standard Grails config mechanism. " +
                "You can either (1) add the plugin's config properties to \"grails-app/conf/Config.groovy\", or (2) provide a plugin-specific file " +
                "called \"grails-app/conf/Searchable.groovy\". " +
                "Run \"grails install-searchable-config\" to try the second option without affecting your existing configuration, " +
                "but you will need to merge your own settings into the new configuration file."
            )
            System.out.println("WARN: " +
                "The Searchable Plugin's 'SearchableConfiguration.groovy' file is deprecated and will be removed in the next version! " +
                "Configuration for the Searchable Plugin should now be defined with the standard Grails config mechanism. " +
                "You can either (1) add the plugin's config properties to \"grails-app/conf/Config.groovy\", or (2) provide a plugin-specific file " +
                "called \"grails-app/conf/Searchable.groovy\". " +
                "Run \"grails install-searchable-config\" to try the second option without affecting your existing configuration, " +
                "but you will need to merge your own settings into the new configuration file."
            )
            return obj.properties
        } catch (ClassNotFoundException e) {
            LOG.debug("Not found: ${e.message}")
            return [:]
        }
    }

    private getDefaultCompassConnection(application) {
        def appName = application.metadata["app.name"]
        def userHome = System.properties['user.home']
        def grailsEnv = Environment.current.name
        return new File("${userHome}/.grails/projects/${appName}/searchable-index/${grailsEnv}").absolutePath
    }
}
