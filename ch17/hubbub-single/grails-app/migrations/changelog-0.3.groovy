databaseChangeLog = {

	changeSet(author: "Glen (generated)", id: "1383973002263-1") {
		dropColumn(columnName: "TWITTER_NAME", tableName: "PROFILE")
	}

    changeSet(author: "Glen (hand-coded)", id: "1383973002263-2") {
        grailsChange {

            change {

                println "Resetting all passwords..."

                def allUsers = sql.rows("select * from user")
                println "Resetting passwords for ${allUsers.size} users"

                Random random = new Random(System.currentTimeMillis())
                def passwordChars = [ 'A'..'Z', 'a'..'z', '0'..'9' ].flatten()

                allUsers.each { user ->
                    StringBuilder randomPassword = new StringBuilder()
                    1.upto(8) { randomPassword.append(
                        passwordChars.get(random.nextInt(passwordChars.size())))
                    }
                    println "Random password is ${randomPassword} for user ${user.login_id}"
                    sql.execute "update user set password = ? where id = ?",
                            [ randomPassword.toString(), user.id]

                }
                println "Done resetting passwords..."

            }
        }
    }
}