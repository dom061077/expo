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

    void testDeleteJson(){
    	//testeo de un delete simple
    	assertTrue(Vendedor.count()==0)
    	def vendedorInstance = new Vendedor(nombre:"DIEGO MARADONA").save()
    	def vendedorController = new VendedorController()
    	vendedorController.params.id = vendedorInstance.id
    	vendedorController.deletejson()
    	assertTrue(Vendedor.count()==0)
    	def respuesta = vendedorController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	//fail(respuestaJson)
    	assertTrue(respuestaJson.success)
    }
    
    void testDeleteJsonNotFound(){
    	//testeo de un delete con error de vendendor not found
    	def vendedorController = new VendedorController()
    	vendedorController.params.id=0
    	vendedorController.deletejson()
    	def respuesta = vendedorController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	assertTrue(respuestaJson.msg.equals("EL VENDEDOR CON ID 0 NO PUDO SER ENCONTRADO"))
    	
    }
    
    void testDeleteJsonIntegrityError(){
    	//testeo de un delete donde haya error de integridad
    	def vendedor = new Vendedor(nombre:"VENDEDOR 1").save(flush:true)
    	def empresa = new Empresa(nombre:"EMPRESA A BORRAR",cuit:"22222",vendedor:vendedor)
    	assertTrue(empresa.validate())
    	fail(empresa?.errors?.allErrors)
    	empresa=empresa.save(flush:true)
    	assertNotNull(empresa)
    	def vendedorController = new VendedorController()
    	vendedorController.params.id=vendedor.id
    	vendedorController.deletejson()
    	def respuesta=vendedorController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertFalse(respuestaJson.success)
    	
    	assertTrue(respuestaJson.msg.equals("No se puede eliminar el vendedor porque es referenciado en otros datos"))
    }
    
}
