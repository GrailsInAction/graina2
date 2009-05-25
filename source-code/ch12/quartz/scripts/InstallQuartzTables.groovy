import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"
def SQL_HOME="${quartzPluginDir}/src/templates/sql/tables_"

includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )  

target('default': "Create db tables for quartz") {
    depends( classpath, loadApp, configureApp )

	def c 
	try {

		def dbName = args.split(" ")[0]
		println "Preparing Quartz Db for ${dbName}"
		def st = new File(SQL_HOME + dbName + ".sql").text
		if (st) {
			c = appCtx.getBean('dataSource').getConnection()
			def sql = new groovy.sql.Sql(c)
			def nocom = st.split("\n").collect { line ->  line.startsWith("#") || line.startsWith("--") ? "" : line }
				st = nocom.join(" ").replaceAll("\r", "")
				st.split(";").each { stmt ->
					println "Executing ${stmt}"
					try {
				 		sql.execute(stmt)
				    } catch (Exception e) {
						println "Skipping Statement on error: ${e.message}"
					}	
				}
				if (dbName.startsWith('hsqldb'))
					sql.execute("CHECKPOINT")
				
				println "Execute done"
			
		}
	} catch (Exception e) {
		e.printStackTrace()
	}
	finally {
		c?.close()
	}
}
