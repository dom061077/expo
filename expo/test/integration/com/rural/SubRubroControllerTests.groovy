package com.rural

import grails.test.*

class SubRubroControllerTests extends GrailsUnitTestCase {
	def rubro = null
    protected void setUp() {
        super.setUp()
        def subrubros = SubRubro.list()
        subrubros.each{
        	it.delete()
        }
        rubro = new Rubro(nombreRubro:"RUBRO PRUEBA").save(flush:true)
        
    }

    protected void tearDown() {
        super.tearDown()
        
    }


    void testSaveJson() {
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
    	def subrubro = new SubRubro(nombreSubrubro:"PABLO RODRIGUEZ GAY",rubro:rubro).save(flush:true)
    	def rubroTest = new Rubro(nombreRubro:'rubro test').save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id = subrubro.id
    	subrubroController.params.nombreSubrubro = "GAY RODRIGUEZ"
    	subrubroController.params.rubro = rubroTest
    	subrubroController.updatejson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
   		subrubro = SubRubro.get(subrubro.id)
   		assertTrue(respuestaJson.success)
    	assertTrue(subrubro.nombreSubrubro=="GAY RODRIGUEZ")
    	assertTrue(subrubro.rubro.equals(rubroTest))
    }
    
    void testDeleteJson(){
    	def subrubro = new SubRubro(nombreRubro:"RUBRO DE PRUEBA",rubro:rubro).save(flush:true)
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
    	def subrubro = new SubRubro(nombreRubro:"RUBRO DE PRUEBA",rubro:rubro).save(flush:true)
    	assertNotNull(subrubro)
    	def subrubroController = new SubRubroController()
    	subrubroController.params.id=subrubro.id
    	subrubroController.showjson()
    	def respuesta = subrubroController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertTrue(respuestaJson.success)
    	subrubro = Rubro.get(subrubro.id)
    	assertNotNull(subrubro)
    }
    
    void testConstraints(){
    	def subrubro = new SubRubro(nombreRubro:"RIVERPLATENSE").save(flush:true)
    	def subrubroController = new SubRubroController()
    	Object[] testArgs = {}
    	subrubroController.params.nombreRubro="RIVERPLATENSE"
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
