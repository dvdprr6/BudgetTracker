package com.migrator.models

case class MigratorOptions(postgresUrl: String,
                           postgresUsername: String,
                           postgresPassword: String,
                           mongoDbUrl: String)
