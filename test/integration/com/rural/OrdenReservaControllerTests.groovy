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
 


class OrdenReservaControllerTests extends GrailsUnitTestCase {
	def ordenReservaService
	def authenticateService
	def usuario = null
	def empresa = null
	def tipoconcepto = null
	def exposicion = null
	boolean transactional = false	
	
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
        
        
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.save()
        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)        
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerarOrden() {
    		assertNotNull(authenticateService.userDomain())
			def ordenreservaController = new OrdenReservaController()
			ordenreservaController.ordenReservaService=ordenReservaService
			ordenreservaController.authenticateService=authenticateService
			ordenreservaController.params.expo=exposicion
			ordenreservaController.params.usuario=usuario
	    	ordenreservaController.params.empresa=empresa
	    	ordenreservaController.params.empresa.nombre="empresa modificada"
	    	ordenreservaController.params.empresa.razonSocial="empresa modificada razon social"
	    	ordenreservaController.params.detallejson="[{sector:'emprendimientos',lote:'1',subtotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{id:tipoConcepto.id}]"			
			ordenreservaController.generarordenreserva()
			def respuesta = ordenreservaController.response.contentAsString
			def respuestaJson = grails.converters.JSON.parse(respuesta)
			def ordenreservaInstance = OrdenReserva.get(respuestaJson.id)
			assertNotNull(ordenreservaInstance)
			fail("nombre empresa: "+ordenReservaInstance.empresa.nombre+" - nombre fiscal "+empresa.razonSocial)
			assertTrue(ordenreservaInstance.empresa.nombre.equals("empresa modificada"))
			assertTrue(ordenreservaInstance.empresa.razonSocial.equals("empresa modificada razon social"))
			
    }
}
