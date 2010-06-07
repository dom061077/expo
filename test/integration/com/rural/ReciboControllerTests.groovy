package com.rural

import grails.test.*
import com.rural.seguridad.*
import com.rural.*
import org.springframework.security.GrantedAuthority
import org.springframework.security.GrantedAuthorityImpl
import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.security.providers.UsernamePasswordAuthenticationToken
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserImpl 
import org.springframework.security.providers.UsernamePasswordAuthenticationToken

class ReciboControllerTests extends GrailsUnitTestCase {
	def ordenReservaService
	def authenticateService
	def reciboService
	def usuario = null
	def empresa = null
	def tipoconcepto = null
	def exposicion = null
	def sector = null
	def lote = null
	boolean transactional = true	
	
    protected void setUp() {
        super.setUp()
        usuario = new Person(username:"admin2",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
        usuario=usuario.save()
        
	   def authorities = usuario.authorities.collect { new GrantedAuthorityImpl(it.authority) }
	   def userDetails = new GrailsUserImpl(usuario.username, usuario.passwd, usuario.enabled, usuario.enabled,
	        usuario.enabled, usuario.enabled, authorities as GrantedAuthority[], usuario)
	   SCH.context.authentication = new UsernamePasswordAuthenticationToken(
	        userDetails, usuario.passwd, userDetails.authorities) 
       authenticateService.clearCachedRequestmaps() 
        
		Empresa.list().each{
		    it.delete(flush:true)
		}
        
        empresa = new Empresa(nombre:"empresa de prueba",usuario:usuario).save(flush:true)
        sector = new Sector(nombre:"EMPRENDIMIENTOS PRODUCTIVOS")
        lote = new Lote(nombre:"LOTE 8")
        sector.addToLotes(lote)
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.addToSectores(sector)
        exposicion.save()
        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)              
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateJson() {
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date()
    		,observacion:"TEXTO DE PRUEBA"
	    	,anio:2010,porcentajeResIns:21,porcentajeResNoIns:0)
    	ordenReserva.addToDetalle(new DetalleServicioContratado(subTotal:1900,lote:lote))
    	ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subTotal:500.20))
    	ordenReserva.addToProductos(new ProductoExpuesto(descripcion:'Producto Expuesto'))
    	ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
    	assertNotNull(ordenReserva)
    	//fail("IVA GRAL DE LA ORDEN DE RESERVA: "+ordenReserva.porcentajeResIns+" IVA EN PESOS: "+ordenReserva.ivaGral)
    	assertTrue(ordenReserva.detalle.size()==1)
    	assertTrue(ordenReserva.otrosconceptos.size()==1)
    	assertTrue(ordenReserva.empresa.nombre.equals("empresa de prueba"))
    //	assertTrue(ordenReserva.total==2904.242)	
    	
    	def reciboController = new ReciboController()
    	reciboController.reciboService = reciboService
    	reciboController.params.ordenreservaid=ordenReserva.id
    	reciboController.params.concepto=''
    	reciboController.params.efectivo=4
    	reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900}]"
    	reciboController.createjson()
    	def respuesta = reciboController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	assertTrue(respuestaJson.success)
    	fail("TOTAL EN PESOS ES: "+respuestaJson.totalletras+", numero de recibo: "+respuestaJson.numero+", nombre empresa: "+respuestaJson.empresa_nombre+", importe: "+respuestaJson.total)
    		
    }
}
