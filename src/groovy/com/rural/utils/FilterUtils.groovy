package com.rural.utils

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.apache.log4j.Logger


class FilterUtils {
	private static Logger log = Logger.getLogger(FilterUtils.class)
	

	static def getNestedMetaProperty(groovy.lang.MetaClass mc, String propertyName) {
		log.info "INGRESANDO AL METODO ESTATICO getNestedMetaProperty DE LA CLASE FilterUtils"
		log.info " propertyName: ${propertyName}, mc metaclass: ${mc}"
		def nest = propertyName.tokenize('.')
		//groovy.lang.MetaClass currMc = mc
		def mp = null
		if (log.isDebugEnabled())
			log.debug("Getting nested meta properties for mc ${mc} and prop ${propertyName}.  Nest is ${nest}")
		nest.each() {egg ->
			try{
				log.info "TRATANDO DE DEVOLVER UNA PROPIEDAD"
				mp = mc.getMetaProperty(egg)
			}catch(Exception e){
				log.info "Exception al obtener getMetaProperty: "+e.getMessage()
			}
			log.debug("${egg} mp is ${mp}")
			if (mp != null) {
				mc = mp.type.getMetaClass()
				log.info "${egg} mp is ${mp}. type is ${mp.type.name} metaclass is ${mp.type.metaClass}"
			}
		}
		log.info "ESTA RETORNANDO UNA METACLASS: "+mc
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
