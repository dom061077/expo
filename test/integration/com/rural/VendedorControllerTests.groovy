package com.rural

import grails.test.*



class VendedorControllerTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
        Vendedor.list().each{
        	it.delete(flush:true)
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSaveJson() {
		assertTrue (Vendedor.count()==0)
		def vendedorController = new VendedorController()
		vendedorController.params.nombre='ZINEDINE ZIDANE'
		vendedorController.savejson()
		def respuesta = vendedorController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		def vendedor = Vendedor.get(respuestaJson.idVendedor)
		assertTrue(respuestaJson.success)
		assertNotNull(vendedor)
    }
    
    void testUpdateJson(){ 
    	def vendedor = new Vendedor(nombre:"PABLO RODRIGUEZ GAY").save(flush:true)
    	assertTrue(Vendedor.count()>0)
    	def vendedorController = new VendedorController()
    	vendedorController.params.id = vendedor.id
    	vendedorController.params.nombre = "GAY RODRIGUEZ"
    	vendedorController.updatejson()
    	def respuesta = vendedorController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
   		vendedor = Vendedor.get(vendedor.id)
   		assertTrue(respuestaJson.success)
    	assertTrue(vendedor.nombre=="GAY RODRIGUEZ")
    }
    
}
