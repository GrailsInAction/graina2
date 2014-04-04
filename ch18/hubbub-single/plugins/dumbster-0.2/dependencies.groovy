grails.project.work.dir = 'target'
grails.project.source.level = 1.6
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		runtime 'dumbster:dumbster:1.6', {
			excludes 'activation', 'mail'
		}
		runtime 'javax.mail:mail:1.4.5', {
			excludes 'activation'
		}
	}

	plugins {
		build ':release:2.2.0', ':rest-client-builder:1.0.3', {
			export = false
		}
		test ':mail:1.0.1', {
			export = false
		}
	}
}
