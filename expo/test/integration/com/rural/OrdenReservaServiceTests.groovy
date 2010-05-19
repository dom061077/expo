package com.rural

import grails.test.*
import com.rural.*
import com.rural.seguridad.*
import grails.converters.*


class OrdenReservaServiceTests extends GrailsUnitTestCase {
	def ordenReservaService
	def reciboService
	def usuario = null
	def empresa = null
	def exposicion = null
	def tipoconcepto = null
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

    void testGenerarOrdenReserva() {
    	
    	//empresa = Empresa.get(empresa.id)
    	//fail("EMPRESA: "+empresa.id+" - empresa nombre: "+empresa.nombre)
    	assertNotNull(usuario)
    	assertNotNull(empresa)
    	assertNotNull(tipoconcepto)
    	
    	empresa.nombre="empresa modificada"
    	
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date(),anio:2010)
    	ordenReserva.addToDetalle(new DetalleServicioContratado(sector:"emprendimientos",lote:"uno",subtotal:1900))
    	ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subtotal:500))
    	ordenReserva.addToProductos(new ProductoExpuesto(descripcion:'Producto Expuesto'))
    	ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
    	assertNotNull(ordenReserva)
    	assertTrue(ordenReserva.detalle.size()==1)
    	assertTrue(ordenReserva.otrosconceptos.size()==1)
    	assertTrue(ordenReserva.empresa.nombre.equals("empresa modificada"))
    	
    	Recibo rec = reciboService.generarRecibo(ordenReserva.id)
    	assertNotNull(rec)
    	assertTrue(rec.numero==1)
    	
    }
}
