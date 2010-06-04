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
	def tipoConcepto = null
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
        
        tipoConcepto=new TipoConcepto(nombre:"descuento").save(flush:true)        
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGenerarOrdenAddEmpresa(){
    		assertNotNull(authenticateService.userDomain())
	    	assertNotNull(empresa)
			def ordenreservaController = new OrdenReservaController()
			ordenreservaController.ordenReservaService=ordenReservaService
			ordenreservaController.authenticateService=authenticateService
			ordenreservaController.params.expo=exposicion
			ordenreservaController.params.anio=2010
			//ordenreservaController.params.usuario=usuario
			ordenreservaController.params.empresa=new Empresa()
	    	ordenreservaController.params.empresa.nombre="empresa nueva"
	    	ordenreservaController.params.empresa.razonSocial="empresa nueva razon social"
	    	ordenreservaController.params.detallejson="[{lote_id:$lote.id,subTotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]"
	    	ordenreservaController.params.observacion="OBSERVACION" 
	    	ordenreservaController.params.porcentajeResIns=21
	    	ordenreservaController.params.porcentajeResNoIns=0
	    	ordenreservaController.params.productosjson="[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]"
			
	    	/*
	    	ordenreservaController.params.put("empresa.nombre","empresa nueva")
	    	ordenreservaController.params.put("empresa.razonSocial","empresa nueva razon social")
	    	ordenreservaController.params.put("detallejson","[{lote_id:$lote.id,subTotal:1900}]")
	    	ordenreservaController.params.put("otrosconceptosjson","[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]")
	    	ordenreservaController.params.put("observacion","OBSERVACION ")
	    	ordenreservaController.params.put("porcentajeResIns",21)
	    	ordenreservaController.params.put("porcentajeResNoIns",0)
	    	ordenreservaController.params.put("productosjson","[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]")
	    	*/
	    	//fail("ERROR "+ordenreservaController.class)
	    	
	    	ordenreservaController.generarordenreserva()
			def respuesta = ordenreservaController.response.contentAsString
			def respuestaJson = grails.converters.JSON.parse(respuesta)
			def ordenreservaInstance = OrdenReserva.get(respuestaJson.ordenid)
			assertTrue(ordenreservaInstance.empresa.nombre.equals("empresa nueva"))
			assertNotNull(ordenreservaInstance)
			assertTrue(ordenreservaInstance.detalle.size()==1)
			assertTrue(ordenreservaInstance.otrosconceptos.size()==1)
			assertTrue(ordenreservaInstance.empresa.razonSocial.equals("empresa nueva razon social"))
			assertTrue(ordenreservaInstance.numero==1)
			if(ordenreservaInstance.subTotal!=1805)
				fail("SubTotal erroneo: deberia ser 1805 pero resultó en: $ordenreservaInstance.subTotal")
		
	}

    void testGenerarOrden() {
    		assertNotNull(authenticateService.userDomain())
	    	assertNotNull(empresa)
			def ordenreservaController = new OrdenReservaController()
			ordenreservaController.ordenReservaService=ordenReservaService
			ordenreservaController.authenticateService=authenticateService
			ordenreservaController.params.id=empresa.id
			ordenreservaController.params.expo=exposicion
			ordenreservaController.params.anio=2010
			//ordenreservaController.params.usuario=usuario
	    	ordenreservaController.params.empresa=empresa
	    	ordenreservaController.params.empresa.nombre="empresa modificada"
	    	ordenreservaController.params.empresa.razonSocial="empresa modificada razon social"
	    	ordenreservaController.params.detallejson="[{lote_id:$lote.id,subTotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]"
	    	ordenreservaController.params.observacion="OBSERVACION "
	    	ordenreservaController.params.porcentajeResIns=21
	    	ordenreservaController.params.porcentajeResNoIns=0
	    	ordenreservaController.params.observacion='NINGUNA'
	    	ordenreservaController.params.productosjson="[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]"
	    	ordenreservaController.request.getAttribute("org.codehaus.groovy.grails.WEB_REQUEST").informParameterCreationListeners()
			ordenreservaController.generarordenreserva()
			def empresaInstance=Empresa.get(empresa.id)
			assertTrue(empresaInstance.nombre.equals("empresa modificada"))
			def respuesta = ordenreservaController.response.contentAsString
			def respuestaJson = grails.converters.JSON.parse(respuesta)
			def ordenreservaInstance = OrdenReserva.get(respuestaJson.ordenid)
			assertNotNull(ordenreservaInstance)
			assertTrue(ordenreservaInstance.detalle.size()==1)
			assertTrue(ordenreservaInstance.otrosconceptos.size()==1)
			assertTrue(ordenreservaInstance.empresa.nombre.equals("empresa modificada"))
			assertTrue(ordenreservaInstance.empresa.razonSocial.equals("empresa modificada razon social"))
			assertTrue(ordenreservaInstance.numero==1)
			if(ordenreservaInstance.subTotal!=1805)
				fail("SubTotal erroneo: deberia ser 1805 pero resultó en: $ordenreservaInstance.subTotal")
			
    }
    
    void testAnularOrden(){
    	def ordenReservaInstance = new OrdenReserva(usuario:usuario,empresa:empresa,expo:exposicion,fechaAlta:new Date(),anio:2010,observacion:"OBSERVANDO")
    	if (ordenReservaInstance.validate())
    		ordenReservaInstance.save(flush:true)
    	else
    		fail("Error de validacion: "+ordenReservaInstance.errors.allErrors)
    	assertNotNull(ordenReservaInstance)
    	def ordenReservaController = new OrdenReservaController()
    	ordenReservaController.params.id=ordenReservaInstance.id
    	ordenReservaController.ordenReservaService = ordenReservaService
    	ordenReservaController.anularordenreserva(ordenReservaInstance.id)
    	def respuesta = ordenReservaController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
    	ordenReservaInstance = OrdenReserva.get(ordenReservaInstance.id)
    	assertTrue(respuestaJson.success)
    	assertTrue(ordenReservaInstance.anulada)
    }
    
   void testFailAnularOrden(){
	   def ordenReservaController = new OrdenReservaController()
	   ordenReservaController.params.id=0
	   ordenReservaController.ordenReservaService = ordenReservaService
	   ordenReservaController.anularordenreserva()
	   def respuesta = ordenReservaController.response.contentAsString
	   def respuestaJson = grails.converters.JSON.parse(respuesta)
	   assertFalse(respuestaJson.success)
   }
}
