package com.rural

import grails.test.*
import com.rural.*
import com.rural.seguridad.*


class OrdenReservaServiceTests extends GrailsUnitTestCase {
	def ordenReservaService
	def usuario = null
	def empresa = null
	def exposicion = null
	def tipoconcepto = null
    protected void setUp() {
        super.setUp()
        usuario = new Person(username:"admin2",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
        usuario.save()
        
        empresa = new Empresa(nombre:"empresa de prueba",usuario:usuario).save()
        
        
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.save()
        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)
        
        
		Empresa.list().each{
		    it.delete(flush:true)
		}
		Person.list().each{
		    it.delete(flush:true)
		}        
        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerarOrdenReserva() {
    	
    	//empresa = Empresa.get(empresa.id)
    	//fail("EMPRESA: "+empresa.id+" - empresa nombre: "+empresa.nombre)
    	assertNotNull(usuario)
    	assertNotNull(empresa)
    	assertNotNull(tipoconcepto)
    	
    	empresa.nombre="empresa modificada"
    	
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date(),sector:'7G')
    	ordenReserva.addToDetalle(new DetalleServicioContratado(sector:"emprendimientos",lote:"uno",subtotal:1900))
    	ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subtotal:500))
    	ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
    	asertNotNull(ordenReserva)
    }
}
