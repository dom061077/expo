package com.rural

import grails.test.*

class RubroControllerTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
        def rubros = Rubro.listt()
        rubros.each{
        	it.delete()
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListRubroJSON() {
        def rubro=new Rubro(nombreRubro:"CONSTRUCCIONXXX")
        if (!rubro.validate()){
        	rubro.errors.each{
        		println it
        	}
        	fail("ERROR EN VALIDACION "+rubro.errors.allErrors)
        }
        rubro.save(flush:true)
        //def rubro=new Rubro(nombre:"CONSTRUCCIONES EDILICIAS",subRubro:srubro).save()
        //assertFalse("MENSAJE VALIDACION: "+subrubro.errors,1==1)	
    	assertTrue(Rubro.count()==1)
    	def rubroController = new RubroController()
    	rubroController.listrubrojson()
    	def respuesta = rubroController.response.contentAsString
    	def responseJSON = grails.converters.JSON.parse(respuesta)
    	if(responseJSON.total!=1)
    		fail("CANTIDAD DE RUBROS: $respuesta")
    		
    }
    
    void testListSubRubroJSON(){
    	//def rubro = new 
    }
    
    void testCRUD(){
    	def rubro = new Rubro(nombreRubro:"RUBRO 1").save(flush:true)
    	assertNotNull(rubro)
    	new SubRubro(nombreSubrubro:"SUBRUBRO 1",rubro:rubro).save(flush:true)
    	def subrubros = SubRubro.list(rubro:rubro)
    	assertTrue(subrubros.size()==1)
    }
}
