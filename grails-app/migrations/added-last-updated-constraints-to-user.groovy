databaseChangeLog = {

	changeSet(author: "Andrea (generated)", id: "1308425642597-1") {
		createTable(tableName: "audit_log") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "actor", type: "VARCHAR(255)")

			column(name: "class_name", type: "VARCHAR(255)")

			column(name: "date_created", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "event_name", type: "VARCHAR(255)")

			column(name: "last_updated", type: "DATETIME") {
				constraints(nullable: "false")
			}

			column(name: "new_value", type: "VARCHAR(255)")

			column(name: "old_value", type: "VARCHAR(255)")

			column(name: "persisted_object_id", type: "VARCHAR(255)")

			column(name: "persisted_object_version", type: "BIGINT")

			column(name: "property_name", type: "VARCHAR(255)")

			column(name: "uri", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-2") {
		createTable(tableName: "lista_precios") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "anio", type: "INT")

			column(name: "expo_id", type: "BIGINT")

			column(name: "lote_id", type: "BIGINT")

			column(name: "precio", type: "DOUBLE") {
				constraints(nullable: "false")
			}

			column(name: "sector_id", type: "BIGINT")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-3") {
		createTable(tableName: "notadc") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "fecha_alta", type: "DATE") {
				constraints(nullable: "false")
			}

			column(name: "iva_gral", type: "DOUBLE") {
				constraints(nullable: "false")
			}

			column(name: "iva_rni", type: "DOUBLE") {
				constraints(nullable: "false")
			}

			column(name: "iva_suj_no_categ", type: "DOUBLE") {
				constraints(nullable: "false")
			}

			column(name: "numero", type: "BIGINT")

			column(name: "orden_reserva_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "sub_total", type: "DOUBLE") {
				constraints(nullable: "false")
			}

			column(name: "tipo", type: "VARCHAR(255)")

			column(name: "total", type: "DOUBLE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-4") {
		createTable(tableName: "notadc_detalle") {
			column(autoIncrement: "true", name: "id", type: "BIGINT") {
				constraints(nullable: "false", primaryKey: "true")
			}

			column(name: "version", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(defaultValue: "", name: "descripcion", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "notadc_id", type: "BIGINT") {
				constraints(nullable: "false")
			}

			column(name: "sub_total", type: "DOUBLE") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-5") {
		addColumn(tableName: "detalle_servicio_contratado") {
			column(name: "sub_totalsindesc", type: "DOUBLE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-6") {
		addColumn(tableName: "lote") {
			column(name: "precio", type: "DOUBLE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-7") {
		addColumn(tableName: "orden_reserva") {
			column(name: "fecha_vencimiento", type: "DATE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-8") {
		addColumn(tableName: "orden_reserva") {
			column(name: "sub_totalsindesc", type: "DOUBLE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-9") {
		addColumn(tableName: "orden_reserva") {
			column(name: "totalsindesc", type: "DOUBLE") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-10") {
		addColumn(tableName: "sector") {
			column(name: "porcentaje", type: "DOUBLE")
		}
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-11") {
		modifyDataType(columnName: "fecha_alta", newDataType: "DATE", tableName: "orden_reserva")
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-12") {
		addForeignKeyConstraint(baseColumnNames: "expo_id", baseTableName: "lista_precios", baseTableSchemaName: "expo", constraintName: "FK2A51931A02C7376", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "exposicion", referencedTableSchemaName: "expo", referencesUniqueColumn: "false")
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-13") {
		addForeignKeyConstraint(baseColumnNames: "lote_id", baseTableName: "lista_precios", baseTableSchemaName: "expo", constraintName: "FK2A5193174E2A25B", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "lote", referencedTableSchemaName: "expo", referencesUniqueColumn: "false")
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-14") {
		addForeignKeyConstraint(baseColumnNames: "sector_id", baseTableName: "lista_precios", baseTableSchemaName: "expo", constraintName: "FK2A51931686898DB", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sector", referencedTableSchemaName: "expo", referencesUniqueColumn: "false")
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-15") {
		addForeignKeyConstraint(baseColumnNames: "orden_reserva_id", baseTableName: "notadc", baseTableSchemaName: "expo", constraintName: "FKC20778ED4B763846", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "orden_reserva", referencedTableSchemaName: "expo", referencesUniqueColumn: "false")
	}

	changeSet(author: "Andrea (generated)", id: "1308425642597-16") {
		addForeignKeyConstraint(baseColumnNames: "notadc_id", baseTableName: "notadc_detalle", baseTableSchemaName: "expo", constraintName: "FKBD2E025EC26537B", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "notadc", referencedTableSchemaName: "expo", referencesUniqueColumn: "false")
	}
}
