package com.rural

import grails.test.*

class EmpresaControllerTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSaveJson() {
        Empresa empresa = new Empresa(nombre:"ROGELIO FUNES MORIS",cuit:"123").save();
        assertFalse empresa.hasErrors()
        //new Exposicion(nombre:"EXPO CONSTRUCCION").save()
        def expo = Exposicion.get(1)
        empresa.addToExpos(expo)
        empresa.save()
    	
    	assertTrue Empresa.count()>1
    	empresa = Empresa.get(empresa.id)
    	//assertTrue empresa.expos.size()==1
    	def empresaController = new EmpresaController()
    	empresaController.params.id=1
    	empresaController.params.nombre="TAQUICO"
    	empresaController.params.cuit="012345"
    	empresaController.params.exposempresajson="[{id:2,nombre:'EXPO TUCUMAN'},{id:1,nombre:'EXPO TUCUMAN'}]"
    	empresaController.update()
    	assertTrue empresa.expos.size()==1
    	
    	assertTrue empresa.expos.size()==2
    }
}
