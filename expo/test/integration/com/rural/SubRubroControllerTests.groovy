package com.rural

import grails.test.*

class SubRubroControllerTests extends GrailsUnitTestCase {
	def rubro = null
	def messageSource
    protected void setUp() {
        super.setUp()
        def subrubros = SubRubro.list()
        subrubros.each{
        	it.delete()
        }
        
		//def rubro = new Rubro(nombreRubro:"RUBRO PRUEBA")
        
    }

    protected void tearDown() {
        super.tearDown()
        
    }


    void testSaveJson() {
    	Rubro.count()
		rubro = new Rubro(nombreRubro:"AUTOMOTOR").save(flush:true)
		assertNotNull(rubro)
		def subrubroController = new SubRubroController()
    		
		subrubroController.params.nombreSubrubro='ZINEDINE ZIDANE'
		subrubroController.params.rubro=rubro
		subrubroController.savejson()
		def respuesta = subrubroController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		def subrubro = SubRubro.get(respuestaJson.idSubRubro)
		
		assertTrue(respuestaJson.success)
		assertNotNull(subrubro)
    }
    
    
    void testUpdateJson(){ 
    	def rubroTest = new Rubro(nombreRubro:'rubro test').save(flush:true)
    	rubro = new Rubro(nombreRubro:"rubro update test").save(flush:true)
    	assertNotNull(rubro)
    	assertNotNull(rubroTest)
    	def subrubro = new SubRubro(nombreSubrubro:"PABLO RODRIGUEZ GAY",rubro:rubroTest).save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id = subrubro.id
    	subrubroController.params.nombreSubrubro = "GAY RODRIGUEZ"
    	subrubroController.params.rubro = rubro
    	subrubroController.updatejson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
   		subrubro = SubRubro.get(subrubro.id)
   		assertTrue(respuestaJson.success)
    	assertTrue(subrubro.nombreSubrubro=="GAY RODRIGUEZ")
    	assertTrue(subrubro.rubro.equals(rubro))
    }
    
    void testDeleteJson(){
    	rubro = new Rubro(nombreRubro:"RUBRO DE PRUEBA").save(flush:true)
    	def subrubro = new SubRubro(nombreSubrubro:"SUBRUBRO DE PRUEBA",rubro:rubro).save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id=subrubro.id
    	subrubroController.deletejson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	subrubro = SubRubro.get(subrubro.id)
    	assertTrue(respuestaJson.success)
    	assertNull(subrubro)
    }
    
    void testShowJson(){
    	rubro = new Rubro(nombreRubro:"Rubro de Prueba").save(flush:true)
    	assertNotNull(rubro)
    	def subrubro = new SubRubro(nombreSubrubro:"SUBRUBRO DE PRUEBA",rubro:rubro).save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id=subrubro.id
    	subrubroController.showjson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	
    	assertTrue(respuestaJson.success)
    	assertTrue(respuestaJson.data.nombreSubrubro.equals(subrubro.nombreSubrubro))
    	assertTrue(respuestaJson.data.id==subrubro.id)
    	assertTrue(respuestaJson.data.rubroId==subrubro.rubro.id)
    	subrubro = SubRubro.get(subrubro.id)
    	assertNotNull(subrubro)
    }
    
    void testConstraints(){
    	rubro = new Rubro(nombreRubro:"Rubro prueba").save(flush:true)
    	assertNotNull(rubro)
    	def subrubro = new SubRubro(nombreSubrubro:"RIVERPLATENSE",rubro:rubro).save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	Object[] testArgs = {}
    	subrubroController.params.nombreSubrubro="RIVERPLATENSE"
    	subrubroController.savejson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	def flag = false
    	def msg = messageSource.resolveCode("com.rural.SubRubro.nombreSubrubro.unique.error", new java.util.Locale("ES")).format(testArgs)
    	respuestaJson.errors.each{
    		if(msg.equals(it.title))
    			flag=true
    	}
    	assertTrue(flag)
    }
    
    void testShowJsonNotFound(){
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id=0
    	subrubroController.showjson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	
    }    


}
