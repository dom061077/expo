package com.rural

import grails.test.*
import grails.converters.*
import grails.util.JSonBuilder
import groovy.xml.StreamingMarkupBuilder


//http://www.danilat.com/weblog/2009/11/20/testeando-respuestas-json-con-grails/
class ExposicionControllerItegrationTests extends GrailsUnitTestCase {
	 
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListJson() {
    	def controller = new ExposicionController()
    	controller.listjson()
    	def respuesta = controller.response.contentAsString
    	//assertFalse("respuesta: "+respuesta,true)
    	def responseJSON = grails.converters.JSON.parse(respuesta)
    	assertTrue(responseJSON.total==2)
    }
}
