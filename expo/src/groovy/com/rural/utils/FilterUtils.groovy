package com.rural.utils

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.apache.log4j.Logger


class FilterUtils {
	private static Logger log = Logger.getLogger(FilterUtils.class)
	

	static def getNestedMetaProperty(MetaClass mc, String propertyName) {
		def nest = propertyName.tokenize('.')
		MetaClass currMc = mc
		def mp = null
		if (log.isDebugEnabled())
			log.debug("Getting nested meta properties for mc ${mc} and prop ${propertyName}.  Nest is ${nest}")
		nest.each() {egg ->
			mp = currMc.getMetaProperty(egg)
			log.debug("${egg} mp is ${mp}")
			if (mp != null) {
				currMc = mp.type.getMetaClass()
				//println "${egg} mp is ${mp}. type is ${mp.type.name} metaclass is ${mp.type.metaClass}"
			}
		}
		return mp
	}

	static java.util.Date parseDateFromDatePickerParams(def paramProperty, def params) {
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
		}
	}

		
}
