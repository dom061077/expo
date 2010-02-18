package com.rural

import grails.test.*

class RubroControllerTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
        def rubros = Rubro.list()
        rubros.each{
        	it.delete()
        }
        
        def rubro=new Rubro(nombreRubro:"CONSTRUCCIONXXX")
        if (!rubro.validate()){
        	rubro.errors.each{
        		println it
        	}
        	fail("ERROR EN VALIDACION "+rubro.errors.allErrors)
        }
        rubro.save(flush:true)
    	
		def subrubro = new SubRubro(nombreSubrubro:"CONSTRUCCION SUBRUBRO",rubro:rubro).save(flush:true)        
        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListRubroJSON() {
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
    	assertTrue(SubRubro.count()==1)
    	def rubroController = new RubroController()
    	rubroController.listsubrubrojson()
    	def respuesta = rubroController.response.contentAsString
    	def responseJSON=grails.converters.JSON.parse(respuesta)
    	if(responseJSON.total!=1)
    		fail("CANTIDAD DE SUBRUBROS: $respuesta")
    }
    
    void testCRUD(){
    	new SubRubro(nombreSubrubro:"SUBRUBRO 1",rubro:rubro).save(flush:true)
    	def subrubros = SubRubro.list(rubro:rubro)
    	assertTrue(subrubros.size()==1)
    }
}
