package com.rural

import grails.test.*

class RubroControllerTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListRubroJson() {
        def srubro=new Rubro(nombre:"CONSTRUCCION")
        if (!srubro.validate()){
        	srubro.errors.each{
        		println it
        	}
        	fail("ERROR EN VALIDACION "+srubro.errors.allErrors)
        }
        assertFalse srubro.validate()
        def rubro=new Rubro(nombre:"CONSTRUCCIONES EDILICIAS",subRubro:srubro).save()
        //assertFalse("MENSAJE VALIDACION: "+subrubro.errors,1==1)	
    	assertTrue(Rubro.count()==1)
    	def rubroController = new RubroController()
    	rubroController.listrubrojson()
    	def respuesta = rubroController.response.contentAsString
    	def responseJSON = grails.converters.JSON.parse(respuesta)
    	assertTrue(responseJSON.total==2)
    		
    }
    
    private void validateAndPrintErrors(Object object) {
    	   if (!object.validate()) {
    	       object.errors.allErrors.each {error ->
    	           println error
    	      }
    	      fail("failed to save object ${object}")
    	   }
    }    
    
}
