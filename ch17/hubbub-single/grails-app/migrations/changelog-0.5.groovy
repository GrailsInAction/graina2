databaseChangeLog = {

    changeSet(author: "Peter", id: "Quartz changes") {

        createTable(tableName: "QRTZ_JOB_DETAILS") {
            column(name: "SCHED_NAME", type: "VARCHAR(120)") {
                constraints(nullable: "false")
            }
            column(name: "JOB_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "JOB_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR (250)")
            column(name: "JOB_CLASS_NAME", type: "VARCHAR (250)") {
                constraints(nullable: "false")
            }
            column(name: "IS_DURABLE", type: "BIT") {
                constraints(nullable: "false")
            }
            column(name: "IS_NONCONCURRENT", type: "BIT") {
                constraints(nullable: "false")
            }
            column(name: "IS_UPDATE_DATA", type: "BIT") {
                constraints(nullable: "false")
            }
            column(name: "REQUESTS_RECOVERY", type: "BIT") {
                constraints(nullable: "false")
            }
            column(name: "JOB_DATA", type: "BLOB")
        }

        createTable(tableName: "QRTZ_TRIGGERS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "JOB_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "JOB_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "DESCRIPTION", type: "VARCHAR (250)")
            column(name: "NEXT_FIRE_TIME", type: "BIGINT")
            column(name: "PREV_FIRE_TIME", type: "BIGINT")
            column(name: "PRIORITY", type: "INTEGER")
            column(name: "TRIGGER_STATE", type: "VARCHAR (16)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_TYPE", type: "VARCHAR (8)") {
                constraints(nullable: "false")
            }
            column(name: "START_TIME", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "END_TIME", type: "BIGINT")
            column(name: "CALENDAR_NAME", type: "VARCHAR (200)")
            column(name: "MISFIRE_INSTR", type: "INTEGER")
            column(name: "JOB_DATA", type: "BLOB")
        }

        createTable(tableName: "QRTZ_PAUSED_TRIGGER_GRPS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "QRTZ_FIRED_TRIGGERS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "ENTRY_ID", type: "VARCHAR (95)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "INSTANCE_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "FIRED_TIME", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "SCHED_TIME", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "PRIORITY", type: "INTEGER") {
                constraints(nullable: "false")
            }
            column(name: "STATE", type: "VARCHAR (16)") {
                constraints(nullable: "false")
            }
            column(name: "JOB_NAME", type: "VARCHAR (200)")
            column(name: "JOB_GROUP", type: "VARCHAR (200)")
            column(name: "IS_NONCONCURRENT", type: "BIT")
            column(name: "REQUESTS_RECOVERY", type: "BIT")
        }

        createTable(tableName: "QRTZ_SIMPLE_TRIGGERS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "REPEAT_COUNT", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "REPEAT_INTERVAL", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "TIMES_TRIGGERED", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "QRTZ_CRON_TRIGGERS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "TRIGGER_GROUP", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "CRON_EXPRESSION", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "TIME_ZONE_ID", type: "VARCHAR (80)")
        }

        createTable(tableName: "QRTZ_SCHEDULER_STATE") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "INSTANCE_NAME", type: "VARCHAR (200)") {
                constraints(nullable: "false")
            }
            column(name: "LAST_CHECKIN_TIME", type: "BIGINT") {
                constraints(nullable: "false")
            }
            column(name: "CHECKIN_INTERVAL", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }

        createTable(tableName: "QRTZ_LOCKS") {
            column(name: "SCHED_NAME", type: "VARCHAR (120)") {
                constraints(nullable: "false")
            }
            column(name: "LOCK_NAME", type: "VARCHAR (40)") {
                constraints(nullable: "false")
            }
        }
    }
}
