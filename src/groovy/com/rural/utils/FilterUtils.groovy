package com.rural.utils

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.apache.log4j.Logger
import java.text.SimpleDateFormat;


class FilterUtils {
	private static Logger log = Logger.getLogger(FilterUtils.class)

	static def getNestedMetaProperty(def grailsApplication,def domainClass, String propertyName) {
        def nest = propertyName.tokenize('.')
        def thisDomainClass = grailsApplication.getDomainClass(domainClass.name)
        def thisDomainProp = null
        
        nest.each() {egg ->
            if(thisDomainClass){
                thisDomainProp =thisDomainClass.persistentProperties.find {
                    
                    it.name == egg
                }
                thisDomainClass = thisDomainProp?.referencedDomainClass
            }
     
        }
        return thisDomainProp
	}


	
	static java.util.Date parseDateFromDatePickerParams(/*def paramProperty, def params*/def fecha) {
		def df
		try{
			df=new SimpleDateFormat("dd/MM/yyyy")
			return df.parse(fecha)
		}catch(Exception e){
			return null
		}
		/*
		try {
			def year = params["${paramProperty}_year"]
			def month = params["${paramProperty}_month"]
			def day = params["${paramProperty}_day"]
			def hour = params["${paramProperty}_hour"]
			def minute = params["${paramProperty}_minute"]

//            if (log.isDebugEnabled()) {
//                log.debug("Parsing date from params: ${year} ${month} ${day} ${hour} ${minute}")
//            }

			String format = ''
			String value = ''
			if (year != null) {
				format = "yyyy"
				value = year
			}
			if (month != null) {
				format += 'MM'
				value += zeroPad(month)
			}
			if (day != null) {
				format += 'dd'
				value += zeroPad(day)
			}
			if (hour != null) {
				format += 'HH'
				value += zeroPad(hour)
			} else if (paramProperty.endsWith('To')) {
				format += 'HH'
				value += '23'
			}

			if (minute != null) {
				format += 'mm'
				value += zeroPad(minute)
			} else if (paramProperty.endsWith('To')) {
				format += 'mm:ss.SSS'
				value += '59:59.999'
			}

			if (value == '') { // Don't even bother parsing.  Just return null if blank.
				return null
			}

			if (log.isDebugEnabled()) log.debug("Parsing ${value} with format ${format}")
			return new java.text.SimpleDateFormat(format).parse(value)
		} catch (Exception ex) {
			log.error("${ex.getClass().simpleName} parsing date for property ${paramProperty}: ${ex.message}")
			return null
		}*/
	}

		
}
