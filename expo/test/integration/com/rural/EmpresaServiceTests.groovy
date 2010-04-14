package com.rural

import grails.test.*
import com.rural.*
import com.rural.seguridad.*

class EmpresaServiceTests extends GrailsUnitTestCase {
	def empresaService
	def usuario = null
	def empresa = null
	def exposicion=null
	
	//static transactional = false
	
    protected void setUp() {
        super.setUp()
        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerarOrdenReserva() {
        usuario = new Person(username:"admin6",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
        usuario=usuario.save(flush:true)        
        empresa = new Empresa(nombre:"empresa de prueba5",usuario:usuario)
        /*if(empresa.validate())
        	
        	empresa=empresa.save(flush:true)
        else{
        	
        	fail("FALLO LA CREACION DE LA EMPRESA "+empresa.errors.allErrors)	
        }*/
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.save(flush:true)
        
        
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date(),sector:'7G')
        ordenReserva.addToDetalle(new DetalleServicioContratado(sector:"x 2 stands de emprendimiento",lote:"lote 1",subtotal:1900))
        ordenReserva.addToOtrosconceptos(new OtrosConceptos(descrpcion:"",subtotal:))
    	
    	empresa.addToOrdenes(ordenReserva)
    	empresa=empresaService.generarOrdenReserva(empresa)
    	
    	def ord = OrdenReserva.get(ordenReserva.id)
    	
    	assertNotNull(ord)
    	assertTrue(ord.detalle.size()==1)	
    	
    	
    }
}
