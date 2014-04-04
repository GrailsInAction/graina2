grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
		mavenRepo 'http://download.java.net/maven/2/'
	}

	plugins {
		compile ':spring-security-core:2.0-RC2'
		compile ':mail:1.0.1'
		compile ':jquery:1.10.2'
		compile ':jquery-ui:1.8.24'
		compile ':famfamfam:1.0.1'

		compile ':spring-security-acl:2.0-RC1', {
			export = false
		}

		compile ":hibernate:$grailsVersion", {
			export = false
		}

		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
