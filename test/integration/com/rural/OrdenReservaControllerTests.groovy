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
import java.text.SimpleDateFormat;
import org.apache.log4j.*



class OrdenReservaControllerTests extends GrailsUnitTestCase {
	
	
	def ordenReservaService
	def authenticateService
	def usuario = null
	def empresa = null
	def tipoConcepto = null
	def exposicion = null
	def sector = null
	def lote = null
	
	def iva = null
	def rubro = null
	def subrubro = null
	def df
	boolean transactional = true	
	def logger
	
    protected void setUp() {
       super.setUp()
	   logger = LogManager.getLogger(OrdenReservaControllerTests.getClass())

	   // use groovy metaClass to put the log into your class
	   
	   
	   usuario = Person.findByUsername("admin2")
       if(!usuario){
		    usuario = new Person(username:"admin2",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
			if(!usuario.validate())
				fail("ERROR DE USUARIO: "+usuario.errors.allErrors)
		    usuario=usuario.save()
       }
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
		df = new SimpleDateFormat("dd/MM/yy")
		def date 
		
        sector = new Sector(nombre:"EMPRENDIMIENTOS PRODUCTIVOS",precio: new Double(4500))

		date = df.parse("30/10/2011")
		def listaDescuentosInstance = new ListaDescuentos(porcentaje:new Double(20)
				,fechaVencimiento:new java.sql.Date(date.getTime()))
		sector.addToDescuentos(listaDescuentosInstance)
		
		
		date = df.parse("20/08/2011")
		listaDescuentosInstance = new ListaDescuentos(porcentaje:new Double(25)
				,fechaVencimiento:new java.sql.Date(date.getTime()))
		sector.addToDescuentos(listaDescuentosInstance)
		
		date = df.parse("30/07/2011")
		listaDescuentosInstance = new ListaDescuentos(porcentaje:new Double(30)
				,fechaVencimiento:new java.sql.Date(date.getTime()))
		sector.addToDescuentos(listaDescuentosInstance)
				
        lote = new Lote(nombre:"LOTE 8",precio:4000)
        sector.addToLotes(lote)
        exposicion = new Exposicion(nombre:"Expo 2010",puntoVenta:new Integer(1))
        exposicion.addToSectores(sector)
        exposicion=exposicion.save()
        
        tipoConcepto=new TipoConcepto(nombre:"descuento").save(flush:true)
        
        iva = new Iva(descripcion:"21 %",porcentaje:21).save(flush:true)
        rubro = new Rubro(nombreRubro:"RUBRO PRUEBA").save(flush:true)
        subrubro = new SubRubro(nombreSubrubro:"SUBRUBRO NUEVO",rubro:rubro).save()
		
		if(exposicion==null)
			fail("EXPOSICION NULA, NO SE GUARDO CORRECTAMENTE")
		sector = Sector.findByNombre("EMPRENDIMIENTOS PRODUCTIVOS")	
		assertNotNull(sector)
		assertTrue(sector.descuentos.size()==3)
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGenerarOrdenAddEmpresa(){
    		assertNotNull(authenticateService.userDomain())
	    	assertNotNull(empresa)
	    	assertNotNull(iva)

			def ordenreservaController = new OrdenReservaController()
			ordenreservaController.ordenReservaService=ordenReservaService
			ordenreservaController.authenticateService=authenticateService
			ordenreservaController.params.expo=exposicion
			ordenreservaController.params.anio=2010
			//ordenreservaController.params.usuario=usuario
			ordenreservaController.params.empresa=new Empresa(subrubro:subrubro)
	    	ordenreservaController.params.empresa.nombre="empresa nueva"
	    	ordenreservaController.params.empresa.razonSocial="empresa nueva razon social"
	    	ordenreservaController.params.detallejson="[{lote_id:$lote.id,sector_id:$sector.id,subTotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{descripcion:'interes',subTotal:500,id:$tipoConcepto.id}]"
	    	ordenreservaController.params.observacion="OBSERVACION" 
	    	ordenreservaController.params.porcentajeResIns=iva.id
	    	ordenreservaController.params.porcentajeResNoIns=0
	    	ordenreservaController.params.productosjson="[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]"
			
	    	
	    	ordenreservaController.generarordenreserva()
			def respuesta = ordenreservaController.response.contentAsString
			def respuestaJson = grails.converters.JSON.parse(respuesta)
			def ordenreservaInstance = OrdenReserva.get(respuestaJson.ordenid)
			
			assertTrue(ordenreservaInstance.empresa.nombre.equals("EMPRESA NUEVA"))
			assertNotNull(ordenreservaInstance)
			assertEquals(ordenreservaInstance.detalle.size(),1)
			assertEquals(ordenreservaInstance.otrosconceptos.size(),1)
			assertEquals(ordenreservaInstance.empresa.razonSocial,"EMPRESA NUEVA RAZON SOCIAL")
			assertEquals(ordenreservaInstance.numero,1)
			assertEquals(ordenreservaInstance.total,3993)
			def listPorcentaje = [5,5,20]
			def listSubtotales = [200,200,800]
			def listVencimientos = [
				 new java.sql.Date(df.parse("30/07/2011").getTime())
				,new java.sql.Date(df.parse("20/08/2011").getTime())
				,new java.sql.Date(df.parse("30/10/2011").getTime())]
			def i=0
			ordenreservaInstance.detalle.each{
				it.descuentos.each{desc->
					logger.info("PORCENTAJE: "+desc.porcentaje)
					logger.info("SUBTOTAL: "+desc.subTotal)
					logger.info("FECHA DE VENCIMIENTO: "+desc.fechaVencimiento)
					//assertEquals(desc.porcentaje,listPorcentaje[i])
					//assertEquals(desc.subTotal,listSubtotales[i])
					//assertEquals(desc.fechaVencimiento,listVencimientos[i])
					i++
				}
			}
			
		
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
			empresa.subrubro=subrubro
	    	ordenreservaController.params.empresa=empresa
	    	ordenreservaController.params.empresa.nombre="empresa modificada"
	    	ordenreservaController.params.empresa.razonSocial="empresa modificada razon social"
	    	ordenreservaController.params.detallejson="[{lote_id:$lote.id,sector_id:$sector.id,subTotal:1900}]"
	    	ordenreservaController.params.otrosconceptosjson="[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]"
	    	ordenreservaController.params.observacion="OBSERVACION "
	    	ordenreservaController.params.porcentajeResIns=iva.id
			//ordenreservaController.params.ivaRniCheck=true
			ordenreservaController.params.fechaVencimiento="10/10/2009"
			ordenreservaController.params.fechaVencimiento_year="2009"
			ordenreservaController.params.fechaVencimiento_month="10"
			ordenreservaController.params.fechaVencimiento_day="10"
						
	    	
	    	ordenreservaController.params.porcentajeResNoIns=0
	    	ordenreservaController.params.observacion='NINGUNA'
	    	ordenreservaController.params.productosjson="[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]"
	    	ordenreservaController.request.getAttribute("org.codehaus.groovy.grails.WEB_REQUEST").informParameterCreationListeners()
			ordenreservaController.generarordenreserva()
			def empresaInstance=Empresa.get(empresa.id)
			
			assertTrue(empresaInstance.nombre.equals("EMPRESA MODIFICADA"))
			def respuesta = ordenreservaController.response.contentAsString
			
			def respuestaJson = grails.converters.JSON.parse(respuesta)
			def ordenreservaInstance = OrdenReserva.get(respuestaJson.ordenid)
			assertNotNull(ordenreservaInstance)
			assertTrue(ordenreservaInstance.detalle.size()==1)
			assertTrue(ordenreservaInstance.otrosconceptos.size()==1)
			assertTrue(ordenreservaInstance.empresa.nombre.equals("EMPRESA MODIFICADA"))
			assertTrue(ordenreservaInstance.empresa.razonSocial.equals("EMPRESA MODIFICADA RAZON SOCIAL"))
			assertTrue(ordenreservaInstance.numero==1)
			if(ordenreservaInstance.subTotal!=3305)
				fail("SubTotal erroneo: deberia ser 3305 pero resultó en: $ordenreservaInstance.subTotal")
			if(ordenreservaInstance.total!=3999.05)
				fail("SubTotal erroneo: deberia ser 3999.05 pero resultó en: $ordenreservaInstance.total")    
	}		
    
    /*void testGenerarOrdenAddSubRubro(){
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
	    	ordenreservaController.params.empresa.subrubro = new SubRubro(nombreSubrubro:"NUEVO SUBRUBRO",rubro:rubro)
	    	
	    	ordenreservaController.params.put("detallejson","[{lote_id:$lote.id,subTotal:1900}]")
	    	ordenreservaController.params.otrosconceptosjson="[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]"
	    	ordenreservaController.params.observacion="OBSERVACION "
	    	ordenreservaController.params.porcentajeResIns=iva.id
	    	
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
			assertNotNull(ordenreservaInstance.empresa.subrubro)
			assertTrue(ordenreservaInstance.numero==1)
			if(ordenreservaInstance.subTotal!=1805)
				fail("SubTotal erroneo: deberia ser 1805 pero resultó en: $ordenreservaInstance.subTotal")
			
    
    }*/
    
    void testAnularOrden(){
    	def ordenReservaInstance = new OrdenReserva(usuario:usuario,empresa:empresa,expo:exposicion,fechaAlta:new java.sql.Date((new java.util.Date()).getTime()),anio:2010,observacion:"OBSERVANDO")
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
   
   void testConsultaAvanzada(){
    	def ordenReservaInstance = new OrdenReserva(usuario:usuario,empresa:empresa,expo:exposicion,fechaAlta:new java.sql.Date((new java.util.Date()).getTime()),anio:2010,observacion:"OBSERVANDO")
    	if (ordenReservaInstance.validate())
    		ordenReservaInstance.save(flush:true)
    	else
    		fail("Error de validacion: "+ordenReservaInstance.errors.allErrors)
    	assertNotNull(ordenReservaInstance)
   
   		def ordenReservaController = new OrdenReservaController()
   		ordenReservaController.params.filtroempresa=true
   		ordenReservaController.params.empresa=new Empresa()
   		ordenReservaController.params.empresa.nombre="prueba"
   		ordenReservaController.avancedlist()
   		def respuesta = ordenReservaController.response.contentAsString
   		def respuestaJson = grails.converters.JSON.parse(respuesta)
   		assertTrue(respuestaJson.success)
   		assertTrue(respuestaJson.rows.size()>0)
   }
   
   
   void testGenerarNotaDC(){
	   def ordenreservaController = new OrdenReservaController()
	   ordenreservaController.ordenReservaService=ordenReservaService
	   ordenreservaController.authenticateService=authenticateService
	   ordenreservaController.params.id=empresa.id
	   ordenreservaController.params.expo=exposicion
	   ordenreservaController.params.anio=2010
	   //ordenreservaController.params.usuario=usuario
	   empresa.subrubro=subrubro
	   ordenreservaController.params.empresa=empresa
	   ordenreservaController.params.empresa.nombre="empresa modificada"
	   ordenreservaController.params.empresa.razonSocial="empresa modificada razon social"
	   ordenreservaController.params.detallejson="[{lote_id:$lote.id,sector_id:$sector.id,subTotal:1900}]"
	   ordenreservaController.params.otrosconceptosjson="[{descripcion:'descuento 5%',subTotal:-95,id:$tipoConcepto.id}]"
	   ordenreservaController.params.observacion="OBSERVACION "
	   ordenreservaController.params.porcentajeResIns=iva.id
	   //ordenreservaController.params.ivaRniCheck=true
	   ordenreservaController.params.fechaVencimiento="10/10/2009"
	   ordenreservaController.params.fechaVencimiento_year="2009"
	   ordenreservaController.params.fechaVencimiento_month="10"
	   ordenreservaController.params.fechaVencimiento_day="10"
				   
	   
	   ordenreservaController.params.porcentajeResNoIns=0
	   ordenreservaController.params.observacion='NINGUNA'
	   ordenreservaController.params.productosjson="[{descripcion:'QUESOS Y QUESILLOS'},{descripcion:'MEMBRILLO'}]"
	   ordenreservaController.request.getAttribute("org.codehaus.groovy.grails.WEB_REQUEST").informParameterCreationListeners()
	   ordenreservaController.generarordenreserva()
   	   def notaDCController = new NotaDCController()
	   notaDCController.ordenReservaService = ordenReservaService
	   notaDCController.authenticateService = authenticateService
	   def ordenReservaInstance = OrdenReserva.findByNombre("empresa modificada")
	   assertNotNull(ordenReservaInstance)
	   notaDCController.params.ordenReserva=new OrdenReserva()
	   notaDCController.params.ordenReserva.id = ordenReservaInstance.id
	   notaDCController.params.detallejson = "[{descripcion:'descuento de prueba',importe:250,cantidad:1}]"
	   notaDCController.params.tipo = "NOTA_DEBITO"  
	   notaDCController.savejson()
	   assertEquals(NotaDC.count(),1)
   }
}
