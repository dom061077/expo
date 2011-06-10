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
import java.text.SimpleDateFormat

import org.apache.commons.logging.LogFactory


class ReciboControllerTests extends GrailsUnitTestCase {
	def log = LogFactory.getLog(getClass())
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
        sector = new Sector(nombre:"EMPRENDIMIENTOS PRODUCTIVOS",porcentaje:15)
        lote = new Lote(nombre:"LOTE 8",precio:4000)
        sector.addToLotes(lote)
        exposicion = new Exposicion(nombre:"Expo 2010")
		if(!exposicion.validate())
			fail("ERROR EN VALIDACION DE exposicion, "+exposicion.errors.allErrors)
        exposicion.addToSectores(sector)
        exposicion = exposicion.save(flush:true)
        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)              
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateJson() {
		
		assertNotNull(exposicion)
		assertNotNull(sector)
		assertNotNull(lote)
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		
		java.sql.Date fechavence = new java.sql.Date((df.parse("2011-12-25")).getTime()) 
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new java.sql.Date((new Date()).getTime())
    		,observacion:"TEXTO DE PRUEBA",fechaVencimiento:null
	    	,anio:2010,porcentajeResIns:21,porcentajeResNoIns:0)
		//log.debug "VALIDACION: ${ordenReserva.validate().toString()}" 
    	ordenReserva.addToDetalle(new DetalleServicioContratado(subTotal:lote.precio,sector:sector,lote:lote))
    	ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subTotal:500))
    	ordenReserva.addToProductos(new ProductoExpuesto(descripcion:'Producto Expuesto'))
    	ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
    	assertNotNull(ordenReserva)
		
		assertTrue(ordenReserva.nombre.equals("empresa de prueba".toUpperCase()))
    	assertTrue(ordenReserva.detalle.size()==1)
    	assertTrue(ordenReserva.otrosconceptos.size()==1)
    	assertTrue(ordenReserva.empresa.nombre.equals("empresa de prueba".toUpperCase()))
    	//fail("TOTAL DE LA ORDEN: "+ordenReserva.total)
    	assertTrue(ordenReserva.total==5445)	
    	def reciboController = new ReciboController()
    	reciboController.reciboService = reciboService
    	reciboController.params.ordenreservaid=ordenReserva.id
    	reciboController.params.concepto=''
    	reciboController.params.efectivo=4
    	reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000,vencimiento:'2012-10-26'},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900.24,vencimiento:'2011-12-30'}]"
    	reciboController.createjson()
    	def respuesta = reciboController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
		def ordenPos = OrdenReserva.load(ordenReserva.id)
		assertTrue(ordenPos.notas.size()==0)
    	assertTrue(respuestaJson.success)
    	//fail("TOTAL EN PESOS ES: "+respuestaJson.totalletras+", numero de recibo: "+respuestaJson.numero+", nombre empresa: "+respuestaJson.empresa_nombre+", importe: "+respuestaJson.total)
    }
}
