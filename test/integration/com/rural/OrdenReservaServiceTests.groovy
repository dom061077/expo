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
    	
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date(),anio:2010,porcentajeResIns:21,porcentajeResNoIns:0)
    	ordenReserva.addToDetalle(new DetalleServicioContratado(sector:"emprendimientos",lote:"uno",subTotal:1900))
    	ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subTotal:500))
    	ordenReserva.addToProductos(new ProductoExpuesto(descripcion:'Producto Expuesto'))
    	ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
    	assertNotNull(ordenReserva)
    	//fail("IVA GRAL DE LA ORDEN DE RESERVA: "+ordenReserva.porcentajeResIns+" IVA EN PESOS: "+ordenReserva.ivaGral)
    	assertTrue(ordenReserva.total==2904)
    	assertTrue(ordenReserva.detalle.size()==1)
    	assertTrue(ordenReserva.otrosconceptos.size()==1)
    	assertTrue(ordenReserva.empresa.nombre.equals("empresa modificada"))
    	
    	//Recibo de pago parcial con efectivo solamente, sin cheque
    	Recibo rec1 = reciboService.generarRecibo(ordenReserva.id,"Cancelación parcial de deuda",1000,[])
    	assertNotNull(rec1)
    	assertTrue(rec1.numero==1)
    	assertNull(rec1.cheques)
    	
    	//Recibo de pago parcial con efectivo y cheque
    	Recibo rec2 = reciboService.generarRecibo(ordenReserva.id,"Cancelación parcial de deuda"
				    		,1000
				    		,[new Cheque(numero:"00001",banco:"BANCO MACRO",importe:500)
				    		,new Cheque(numero:"00001",banco:"BANCO MACRO",importe:404)])
    	assertNotNull(rec2)
    	assertTrue(rec2.numero==2)
    	assertTrue(rec2.cheques.size()==2)		
    	assertTrue(rec2.total==1904)	
    	
    	try{
		    	Recibo rec3 = reciboService.generarRecibo(ordenReserva.id,"Cancelación parcial de deuda"
						    		,1000
						    		,[new Cheque(numero:"00001",banco:"BANCO MACRO",importe:500)
						    		,new Cheque(numero:"00001",banco:"BANCO MACRO",importe:404)])
										    		
				//fail("FALLO LA PRUEBA DEL RECIBO ERRONEO")						    		
    	}catch(ReciboException e){
				assertNull(rec3)    	
    	}	
    }
}
