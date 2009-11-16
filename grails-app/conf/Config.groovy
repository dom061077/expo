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

// log4j configuration
log4j {
        appender.stdout = "org.apache.log4j.ConsoleAppender"
        appender.'stdout.layout'="org.apache.log4j.PatternLayout"
        appender.'stdout.layout.ConversionPattern'='[%r] %c{2} %m%n'
        //if you want stdout to go to a file rather than the console, use the following two lines - but then it won't go to the console  
        //appender.stdout = "org.apache.log4j.FileAppender"   
        //appender.'stdout.File'="stdout.log"   //will go to approotdirectory/stdout.log

        appender.errors = "org.apache.log4j.FileAppender"
        appender.'errors.layout'="org.apache.log4j.PatternLayout"
        appender.'errors.layout.ConversionPattern'='[%r] %c{2} %m%n'
        appender.'errors.File'="stacktrace.log"   //goes to approotdirectory/stacktrace.log

        rootLogger="error,stdout"
        logger {
          grails="error,stdout"
          org {
            codehaus.groovy.grails.web.servlet="error,stdout"  //  controllers
            codehaus.groovy.grails.web.errors="error,stdout"  //  web layer errors
            codehaus.groovy.grails.web.pages="error,stdout" //  GSP
            codehaus.groovy.grails.web.sitemesh="error,stdout" //  layouts
            codehaus.groovy.grails."web.mapping.filter"="error,stdout" // URL mapping
            codehaus.groovy.grails."web.mapping"="error,stdout" // URL mapping
            codehaus.groovy.grails.commons="info,stdout" // core / classloading
            codehaus.groovy.grails.plugins="error,stdout" // plugins
            codehaus.groovy.grails.orm.hibernate="error,stdout" // hibernate integration
            springframework="off,stdout"
            hibernate="off,stdout"
        }
    }
        additivity.'default' = false
        additivity {
                grails=false
                org {
                      codehaus.groovy.grails=false
                      springframework=false
                      hibernate=false
                }
    }
}