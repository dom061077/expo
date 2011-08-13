package com.rural.utils


import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.gpars.GParsPool


class GParsSQL{
	void parsear(){
		def sql = Sql.newInstance(ConfigurationHolder.config.db as String,
			ConfigurationHolder.config.dbUsername as String,
			ConfigurationHolder.config.dbPassword as String,
			"com.mysql.jdbc.Driver")
  
		/* go through each row of photos */
		  GParsPool.withPool {
			sql.rows("""SELECT * from photos""").eachParallel  { photo ->
			}
		  }
	}
		  
}






