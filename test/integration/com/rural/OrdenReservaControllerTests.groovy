package com.rural

import grails.test.*
import com.rural.seguridad.*
import com.rural.*

class OrdenReservaControllerTests extends GrailsUnitTestCase {
	def usuario = null
	def empresa = null
	def tipoconcepto = null
	def exposicion = null
    protected void setUp() {
        super.setUp()
        usuario = new Person(username:"admin2",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
        usuario=usuario.save()
		Empresa.list().each{
		    it.delete(flush:true)
		}
        
        empresa = new Empresa(nombre:"empresa de prueba",usuario:usuario).save(flush:true)
        
        
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.save()
        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerarOrden() {
			def ordenreservaController = new OrdenReservaController()
	    	ordenreservaController.params.empresa=empresa
	    	ordenreservaController.params.empresa.nombre="empresa modificada"
	    	ordenreservaController.params.empresa.razonSocial="empresa modificada razon social"
	    	ordenreservaController.params.detallejson="[{sector:'emprendimientos',lote:'1',subtotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{id:tipoConcepto.id}]"			
			ordenreservaController.generarordenreserva()
    }
}
