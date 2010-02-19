package com.rural

import grails.test.*

class RubroControllerTests extends GroovyTestCase {
	def rubroConsulta = null
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
    	rubroConsulta = rubro
		new SubRubro(nombreSubrubro:"CONSTRUCCION SUBRUBRO",rubro:rubro).save(flush:true)
		new SubRubro(nombreSubrubro:"CONSTRUCCION SUBRUBRO 2",rubro:rubro).save(flush:true)        
        
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
    	assertTrue(SubRubro.count()==2)
    	def rubroController = new RubroController()
    	rubroController.request.parameters = [rubroid:Long.toString(rubroConsulta.id.longValue())]
    	rubroController.listsubrubrojson()
    	def respuesta = rubroController.response.contentAsString
    	def responseJSON=grails.converters.JSON.parse(respuesta)
    	if(responseJSON.total!=2)
    		fail("CANTIDAD DE SUBRUBROS: $respuesta")
    }
    
    void testCRUD(){
    	def subrubros = SubRubro.list(rubro:rubroConsulta)
    	assertTrue(subrubros.size()==2)
    }
    
    void testSubRubroList(){
    	assertTrue(SubRubro.count()==2)
    	assertNotNull(rubroConsulta)
    	def subrubros = SubRubro.createCriteria().list{
    				eq('rubro.id',rubroConsulta.id)
    	}
    	
    	assertTrue(subrubros.size()==2)
    }
}
