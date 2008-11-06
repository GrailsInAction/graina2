dataSource {
	pooled = false
	driverClassName = "org.hsqldb.jdbcDriver"
	username = "sa"
	password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    // cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			//url = "jdbc:hsqldb:file:devDB;shutdown=true"
			//driverClassName = "com.p6spy.engine.spy.P6SpyDriver" // use this driver to enable p6spy logging
			url = "jdbc:hsqldb:mem:devDB"

            //driverClassName = "org.postgresql.Driver"
            //url = "jdbc:postgresql://localhost/hubbub"
            //username = "glen"
	        //password = "password"
            logSql = false
        }
	}
	test {
		dataSource {
			dbCreate = "update"
			//url = "jdbc:hsqldb:mem:testDb"
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost/hubbub"
            username = "glen"
	        password = "password"
            logSql = false
        }
	}
	production {
		dataSource {
			dbCreate = "update"
			// url = "jdbc:hsqldb:file:prodDb;shutdown=true"
			jndiName = "jdbc/hubbub"
        }
	}
}
