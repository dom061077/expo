package com.rural

import grails.test.*

class RubroTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSave() {
    	def rubro = new Empresa(nombre:"CONSTRUCCION")
    	assertTrue rubro.validate()
    	
    }
}
