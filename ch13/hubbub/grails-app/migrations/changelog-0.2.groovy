databaseChangeLog = {

	changeSet(author: "Glen (generated)", id: "1383972018405-1") {
		addColumn(tableName: "profile") {
			column(name: "twitter_id", type: "varchar(255)") {
				constraints(nullable: "true")
			}
		}
	}

    changeSet(author: "Glen (by hand)", id: "1383972018405-2") {
        renameColumn(tableName: "profile",
                oldColumnName: 'twitter_id', newColumnName: 'twitter_name')
    }
}
