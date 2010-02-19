package com.rural

import grails.test.*

class EmpresaControllerTests extends GrailsUnitTestCase {
 	def expo = null
    protected void setUp() {
        super.setUp()
        expo=new Exposicion(nombre:"EXPOSICION NUEVA").save(flush:true)
        
    }

    protected void tearDown() {
        super.tearDown()
        
    }

    void testSaveJson() {
    	if(expo == null)
    		fail("NO HAY NINGUNA EXPOSICION DE PRUEBA PARA EL TEST")
        Empresa empresa = new Empresa(nombre:"ROGELIO FUNES MORIS",cuit:"123")
        if(!empresa.validate()){
        	fail("ERROR EN VALIDACION: "+empresa.errors.allErrors)
        }
        assertFalse empresa.hasErrors()
        
        empresa.save(flush:true)
        empresa.addToExpos(expo)
        empresa.save(flush:true)
    	
    	assertTrue Empresa.count()==1
    	empresa = Empresa.get(empresa.id)
    	assertTrue empresa.expos.size()==1
    	def empresaController = new EmpresaController()
    	
    	empresaController.params.id=empresa.id
    	empresaController.params.nombre="TAQUICO"
    	empresaController.params.cuit="012345"
    	empresaController.params.exposempresajson="[{id:$expo.id,nombre:'$expo.nombre'}]"
    	empresaController.params.subrubro=[id:1234]
    	empresaController.update()
    	
    	assertTrue empresa.expos.size()==1
    	assertNotNull empresa.subrubro
    	assertNotNull empresa.subrubro.rubro
    }
}
