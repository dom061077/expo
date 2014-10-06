import org.apache.log4j.net.SMTPAppender
import org.apache.log4j.RollingFileAppender
import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

grails.json.legacy.builder = true



// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}


mail.error.server = 'smtp.gmail.com'
mail.error.port = 587
mail.error.username = 'miuusario'
mail.error.password = 'miclave'
mail.error.to = 'dom061077@gmail.com'
mail.error.from = 'dom061077@gmail.com'
mail.error.subject = '[Error en sistema de ordenes de reserva]'
mail.error.starttls = true
mail.error.debug = false


// log4j configuration
log4j = {

    System.setProperty 'mail.smtp.port', mail.error.port.toString()
    System.setProperty 'mail.smtp.starttls.enable', mail.error.starttls.toString()

    appenders {

        appender new SMTPAppender(name: 'smtp', to: mail.error.to, from: mail.error.from,
                subject: mail.error.subject, threshold: Level.ERROR,
                SMTPHost: mail.error.server, SMTPUsername: mail.error.username,
                SMTPDebug: mail.error.debug.toString(), SMTPPassword: mail.error.password,
                layout: pattern(conversionPattern:
                        '%d{[ dd.MM.yyyy HH:mm:ss.SSS]} [%t] %n%-5p %n%c %n%C %n %x %n %m%n'))

        appenders {
            file name:'file', file:'expo.log', append: false
            console name:'stdout'
        }
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    info  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate',
            'grails.app.controllers'

    debug  'grails.app.controllers'//,  //  controllers
           // 'org.codehaus.groovy.grails.web.pages', //  GSP
           // 'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           // 'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           // 'org.codehaus.groovy.grails.web.mapping', // URL mapping
           // 'org.codehaus.groovy.grails.commons', // core / classloading
           // 'org.codehaus.groovy.grails.plugins', // plugins
           // 'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           // 'org.springframework',
           // 'org.hibernate',
           // 'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'

    root {
        error 'stdout','file', 'smtp'
        info 'stdout','file'
        //debug 'stdout'
        additivity = true
    }
}


