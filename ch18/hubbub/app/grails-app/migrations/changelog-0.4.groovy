databaseChangeLog = {

    changeSet(author: "Peter", id: "User changes") {
        addColumn(tableName: "USER") {
            column(name: "ENABLED", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "ACCOUNT_EXPIRED", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "ACCOUNT_LOCKED", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "PASSWORD_EXPIRED", type: "BIT") {
                constraints(nullable: "false")
            }
        }

        renameColumn(tableName: "USER",
                     oldColumnName: "PASSWORD",
                     newColumnName: "PASSWORD_HASH",
                     columnDataType: "VARCHAR(255)")
        modifyDataType(tableName: "USER",
                       columnName: "PASSWORD_HASH",
                       newDataType: "VARCHAR(255)")

        createTable(tableName: "USER_ROLE") {
            column(name: "USER_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "ROLE_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "ROLE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_27")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "AUTHORITY", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "TWITTER_USER") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_27")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "TWITTER_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "USER_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "USERNAME", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "TOKEN_SECRET", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "TOKEN", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "REGISTRATION_CODE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_27")
            }

            column(name: "DATE_CREATED", type: "TIMESTAMP") {
                constraints(nullable: "false")
            }

            column(name: "USERNAME", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "TOKEN", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }

        // TODO Migrate plain text passwords to SHA256 encoded ones.
    }

    changeSet(author: "Peter", id: "Reply changes") {
        createTable(tableName: "REPLY") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_27")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "POST_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "IN_REPLY_TO_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "Peter", id: "HubbubUser addition") {
        addColumn(tableName: "USER") {
            column(name: "CLASS", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
        
        renameColumn(tableName: "USER_USER", oldColumnName: "USER_FOLLOWING_ID", newColumnName: "HUBBUB_USER_FOLLOWING_ID")
//        renameColumn(tableName: "USER_USER", oldColumnName: "USER_ID", newColumnName: "HUBBUB_USER_ID")
        renameTable(oldTableName: "USER_USER", newTableName: "HUBBUB_USER_USER")
    }
}
