package com.rural

import grails.test.*

class EmpresaControllerTests extends GrailsUnitTestCase {
 	def expo = null
 	def expoaparticipar = null
 	def subrubro = null
    protected void setUp() {
        super.setUp()
        expo=new Exposicion(nombre:"EXPOSICION NUEVA").save(flush:true)
        expoaparticipar = new Exposicion(nombre:"EXPOSICION A PARTICIPAR").save(flush:true)
        def rubro = new Rubro(nombreRubro:"AUTOMOTOR").save(flush:true)
        subrubro=new SubRubro(nombreSubrubro:"AUTOS",rubro:rubro).save(flush:true)
        
    }

    protected void tearDown() {
        super.tearDown()
        
    }
    
    void testSaveJson(){
    	def empresaController = new EmpresaController()
    	assertTrue(expoaparticipar.id>0)
    	empresaController.params.nombre="ANDRES CAJAL"
    	empresaController.params.cuit="1234"
    	empresaController.params.exposaparticiparjson="[{id:$expoaparticipar.id,nombre:'expoaparticipar.nombre'}]"
    	empresaController.params.exposempresajson="[]"
    	empresaController.savejson()
    	def respuesta = empresaController.response.contentAsString
    	def respuestaJSON = grails.converters.JSON.parse(respuesta)
    	if(!respuestaJSON.success){
    		fail("NO SE REGISTRO LA EMPRESA. RESPUESTA JSON: "+respuestaJSON)
    	}
    	def empresa = Empresa.get(respuestaJSON.idEmpresa)
    	assertNotNull(empresa)
    	assertTrue(empresa.exposaparticipar.size()==1)
    	assertTrue(empresa.nombre.compareTo("ANDRES CAJAL")==0)
    }

    void testUpdateJson() {
    	if(expo == null)
    		fail("NO HAY NINGUNA EXPOSICION DE PRUEBA PARA EL TEST")
    	subrubro = SubRubro.get(subrubro.id)
    	assertNotNull subrubro.rubro
        Empresa empresa = new Empresa(nombre:"ROGELIO FUNES MORIS",cuit:"123")
        if(!empresa.validate()){
        	fail("ERROR EN VALIDACION: "+empresa.errors.allErrors)
        }
        assertFalse empresa.hasErrors()
        
        empresa.save(flush:true)
        
        
    	
    	assertTrue Empresa.count()==1
    	empresa = Empresa.get(empresa.id)
    	
    	def empresaController = new EmpresaController()
    	
    	empresaController.params.id=empresa.id
    	empresaController.params.nombre="TAQUICO"
    	empresaController.params.cuit="012345"
    	empresaController.params.exposempresajson="[{id:$expo.id,nombre:'$expo.nombre'}]"
    	empresaController.params.subrubro=subrubro
    	empresaController.params.exposaparticiparjson="[{id:$expoaparticipar.id,nombre:'$expoaparticipar.nombre'}]"
        //fail("probando como van los params $empresaController.params")
    	empresaController.update()
    	
    	def respuesta = empresaController.response.contentAsString
    	def respuestaJSON = grails.converters.JSON.parse(respuesta)
    	assertNotNull subrubro.rubro
    	if (!respuestaJSON.success)
    		fail("FALLO LA RESPUESTA JSON $respuestaJSON")
    	empresa = Empresa.get(empresa.id)
    	assertTrue empresa.expos.size()==1
    	assertTrue empresa.exposaparticipar.size()==1
    	assertNotNull empresa.subrubro.id
    	assertNotNull empresa.subrubro.rubro
    }
    
    void testCRUD(){
        Empresa empresa = new Empresa(nombre:"ROGELIO FUNES MORIS",cuit:"123",subrubro:subrubro)
        if(!empresa.validate()){
        	fail("ERROR EN VALIDACION: "+empresa.errors.allErrors)
        }
        assertFalse empresa.hasErrors()
        
        empresa.save(flush:true)
        
        
    	
    	assertTrue Empresa.count()==1
    	empresa = Empresa.get(empresa.id)
    	assertNotNull empresa
    	assertNotNull empresa.subrubro
    	assertNotNull empresa.subrubro.rubro
    	
    }
    
    void testUpdateJsonDeletedRowExpo(){
    	def expoaparticiparUdp = new Exposicion(nombre:"CONSTRUCCION 2011").save(flush:true)
    	def expoparticipo = new Exposicion(nombre:"CONSTRUCCION 2007").save(flush:true)
    	def empresaController = new EmpresaController()
    	Empresa empresa = new Empresa(nombre:"COCA COLA COMPANY",cuit:"4444")
    	empresa.addToExpos(expoparticipo)
    	empresa.addToExposaparticipar(expoaparticiparUdp)
    	empresa.save(flush:true)
    	assertTrue(Empresa.count()==1)
    	empresa = Empresa.get(empresa.id)
    	assertTrue(empresa.expos.size()==1)
    	assertTrue(empresa.exposaparticipar.size()==1)
    	empresaController.params.id=empresa.id
    	empresaController.params.nombre="COCA"
    	empresaController.params.cuit="5555"
    	empresaController.params.exposempresajson="[{id:$expoparticipo.id,nombre:'$expoparticipo.nombre'},{id:$expo.id,nombre:'$expo.nombre'}]"
    	empresaController.params.exposaparticiparjson="[{id:$expoaparticiparUdp.id,nombre:'$expoaparticiparUdp.nombre'},{id:$expoaparticipar.id,nombre:'$expoaparticipar.nombre'}]"
    	empresaController.update()
    											 
    	def respuesta=empresaController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertTrue(respuestaJson.success)
    	
    }
    
    void testListExposaParticipar(){
    	def empresa = new Empresa(nombre:'RIVER PLATE',cuit:'12345')
    	empresa.addToExposaparticipar(expoaparticipar)
    	empresa.save(flush:true)
    	empresa = Empresa.get(empresa.id)
    	assertTrue(empresa.exposaparticipar.size()==1)
    	def empresacontroller = new EmpresaController()
    	empresacontroller.params.id = empresa.id
    	empresacontroller.listexposaparticipar()
    	def respuesta = empresacontroller.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertTrue(respuestaJson.total==empresa.exposaparticipar.size())
    	assertTrue(respuestaJson.rows.size()==empresa.exposaparticipar.size())
    }
}
