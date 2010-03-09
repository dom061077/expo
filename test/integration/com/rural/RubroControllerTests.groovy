package com.rural

import grails.test.*

class RubroControllerTests extends GroovyTestCase {
	def rubroConsulta = null
	def messageSource
    protected void setUp() {
        super.setUp()
        def subrubros = SubRubro.list()
        subrubros.each{
        	it.delete()
        }
        
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
    
    void testSaveJson() {
		def rubroController = new RubroController()
		rubroController.params.nombreRubro='ZINEDINE ZIDANE'
		rubroController.savejson()
		def respuesta = rubroController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		def rubro = Rubro.get(respuestaJson.idRubro)
		
		assertTrue(respuestaJson.success)
		assertNotNull(rubro)
    }
    
    
    void testUpdateJson(){ 
    	def rubro = new Rubro(nombreRubro:"PABLO RODRIGUEZ GAY").save(flush:true)
    	assertNotNull(rubro)
    	def rubroController = new RubroController()
    	rubroController.params.id = rubro.id
    	rubroController.params.nombreRubro = "GAY RODRIGUEZ"
    	rubroController.updatejson()
    	def respuesta = rubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
   		rubro = Rubro.get(rubro.id)
   		assertTrue(respuestaJson.success)
    	assertTrue(rubro.nombreRubro=="GAY RODRIGUEZ")
    }
    
    void testDeleteJson(){
    	def rubro = new Rubro(nombreRubro:"RUBRO DE PRUEBA").save(flush:true)
    	assertNotNull(rubro)
    	def rubroController = new RubroController()
    	rubroController.params.id=rubro.id
    	rubroController.deletejson()
    	def respuesta = rubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	rubro = Rubro.get(rubro.id)
    	assertTrue(respuestaJson.success)
    	assertNull(rubro)
    }
    
    void testShowJson(){
    	def rubro = new Rubro(nombreRubro:"RUBRO DE PRUEBA").save(flush:true)
    	assertNotNull(rubro)
    	def rubroController = new RubroController()
    	rubroController.params.id=rubro.id
    	rubroController.showjson()
    	def respuesta = rubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertTrue(respuestaJson.success)
    	//assertTrue(respuestaJson.data.id==rubro.id)
    	assertTrue(respuestaJson.data.nombreRubro.equals(rubro.nombreRubro))
    	rubro = Rubro.get(rubro.id)
    	assertNotNull(rubro)
    }
    
    void testConstraints(){
    	def rubro = new Rubro(nombreRubro:"RIVERPLATENSE").save(flush:true)
    	def rubroController = new RubroController()
    	Object[] testArgs = {}
    	rubroController.params.nombreRubro="RIVERPLATENSE"
    	rubroController.savejson()
    	def respuesta = rubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	def flag = false
    	def msg = messageSource.resolveCode("com.rural.Rubro.nombreRubro.unique.error", new java.util.Locale("ES")).format(testArgs)
    	respuestaJson.errors.each{
    		if(msg.equals(it.title))
    			flag=true
    	}
    	assertTrue(flag)
    }
    
    void testShowJsonNotFound(){
    	def rubroController = new RubroController()
    	rubroController.params.id=0
    	rubroController.showjson()
    	def respuesta = rubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	
    }    
    
}
