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
import org.apache.log4j.*


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
	def sectorSinDesc = null
	def iva = null
	def lote = null
	boolean transactional = true
	def df
	def logger
	def rubro
	def subrubro
	def ordenreservaInstance

	
    protected void setUp() {
        super.setUp()
		logger = LogManager.getLogger(OrdenReservaControllerTests.getClass())
		usuario = Person.findByUsername("admin2")
		if(!usuario){
	        usuario = new Person(username:"admin2",
					passwd: authenticateService.encodePassword('esquina'),email:"dom061077@yahoo.com.ar",enabled:true,userRealName:"Usuario Administrador")
			usuario=usuario.save(failOnError:true)
			
		    //println(usuario.username.toUppercase())
			def authority = new Authority(authority: "ROLE_USER", description: "Usuario comun")
			authority.addToPeople(usuario)
			authority.save()
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

		/*sector = new Sector(nombre:"EMPRENDIMIENTOS PRODUCTIVOS",porcentaje:15)
		sectorSinDesc = new Sector(nombre:"PABELLON 2",porcentaje:0)
		
        lote = new Lote(nombre:"LOTE 8",precio:4000)
        sector.addToLotes(lote)
		iva = new Iva(descripcion:"21 %",porcentaje:21).save(flush:true)
		lote = new Lote(nombre:"LOTE 2",precio:3500)
		sectorSinDesc.addToLotes(lote)
		
        exposicion = new Exposicion(nombre:"Expo 2010",puntoVenta:new Integer(1))
		if(!exposicion.validate())
			fail("ERROR EN VALIDACION DE exposicion, "+exposicion.errors.allErrors)
        exposicion.addToSectores(sector)
		exposicion.addToSectores(sectorSinDesc)
        exposicion = exposicion.save(flush:true)*/
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
		
		//tipoConcepto=new TipoConcepto(nombre:"descuento").save(flush:true)
		
		iva = new Iva(descripcion:"21 %",porcentaje:21).save(flush:true)
		rubro = new Rubro(nombreRubro:"RUBRO PRUEBA").save(flush:true)
		subrubro = new SubRubro(nombreSubrubro:"SUBRUBRO NUEVO",rubro:rubro).save()
		
		if(exposicion==null)
			fail("EXPOSICION NULA, NO SE GUARDO CORRECTAMENTE")
		sector = Sector.findByNombre("EMPRENDIMIENTOS PRODUCTIVOS")
		assertNotNull(sector)
		assertTrue(sector.descuentos.size()==3)

        
        tipoconcepto=new TipoConcepto(nombre:"descuento").save(flush:true)              
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	private void ordenReservaControllerCall(){
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
		ordenreservaController.params.otrosconceptosjson="[{descripcion:'interes',subTotal:500}]"
		ordenreservaController.params.observacion="OBSERVACION"
		ordenreservaController.params.porcentajeResIns=iva.id
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
		//fail("RESPUESTA JSON "+ordenreservaController.response.contentAsString)
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		ordenreservaInstance = OrdenReserva.get(respuestaJson.ordenid)
		
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

    void testCreateJsonSinDebito() {
		
		/*assertNotNull(exposicion)
		
		def sector = Sector.findByNombre("EMPRENDIMIENTOS PRODUCTIVOS")
		def lote = Lote.findByNombre("LOTE 8")
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		
		java.sql.Date fechavence = new java.sql.Date((df.parse("2011-12-25")).getTime()) 
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new java.sql.Date((new Date()).getTime())
    		,observacion:"TEXTO DE PRUEBA",fechaVencimiento:fechavence
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
		
		
		if (ordenReserva.total==4719)	
   	       assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA: "+ordenReserva.total)
		if (ordenReserva.totalsindesc==5445)	
   	       assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA sin descuento: "+ordenReserva.totalsindesc)
		*/  
		
		ordenReservaControllerCall()
		
    	def reciboController = new ReciboController()
    	reciboController.reciboService = reciboService
    	reciboController.params.ordenreservaid=ordenreservaInstance.id
    	reciboController.params.concepto=''
    	reciboController.params.efectivo=4
    	reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000,vencimiento:'2012-10-26'},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900.24,vencimiento:'2011-12-30'}]"
    	reciboController.createjson()
    	def respuesta = reciboController.response.contentAsString
    	def respuestaJson = grails.converters.JSON.parse(respuesta)
		def ordenPos = OrdenReserva.get(ordenreservaInstance.id)
		//if(ordenPos.total - ordenPos.credito + ordenPos.debito - ordenPos.recibo != 1814.76)
		//fail("DATOS: ${ordenPos.total} ${ordenPos.credito} ${ordenPos.debito} ${ordenPos.recibo}")
		assertNull(ordenPos.notas)
		assertTrue(NotaDC.count()==0)
    	assertTrue(respuestaJson.success)
    	//fail("TOTAL EN PESOS ES: "+respuestaJson.totalletras+", numero de recibo: "+respuestaJson.numero+", nombre empresa: "+respuestaJson.empresa_nombre+", importe: "+respuestaJson.total)
    }
	
	void testCreateJsonSinDebitoMontoMayorSaldo(){
		assertNotNull(exposicion)
		def sector = Sector.findByNombre("EMPRENDIMIENTOS PRODUCTIVOS")
		def lote = Lote.findByNombre("LOTE 8")
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		
		java.sql.Date fechavence = new java.sql.Date((df.parse("2011-12-25")).getTime())
		def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new java.sql.Date((new Date()).getTime())
			,observacion:"TEXTO DE PRUEBA",fechaVencimiento:fechavence
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
		if (ordenReserva.total==4719)
			  assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA: "+ordenReserva.total)
		if (ordenReserva.totalsindesc==5445)
			  assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA sin descuento: "+ordenReserva.totalsindesc)
		  
		def reciboController = new ReciboController()
		reciboController.reciboService = reciboService
		reciboController.params.ordenreservaid=ordenReserva.id
		reciboController.params.concepto=''
		reciboController.params.efectivo=40000
		reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000,vencimiento:'2012-10-26'},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900.24,vencimiento:'2011-12-30'}]"
		reciboController.createjson()
		def respuesta = reciboController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		def ordenPos = OrdenReserva.get(ordenReserva.id)
		//if(ordenPos.total - ordenPos.credito + ordenPos.debito - ordenPos.recibo != 1814.76)
		//fail("DATOS: ${ordenPos.total} ${ordenPos.credito} ${ordenPos.debito} ${ordenPos.recibo}")
		assertNull(ordenPos.notas)
		assertTrue(NotaDC.count()==0)
		assertFalse(respuestaJson.success)
		//if(!respuestaJson.success)
		//	fail(respuestaJson.message)

	}
	
	void testCreateJsonConDebito() {
		
		assertNotNull(exposicion)
		def sector = Sector.findByNombre("EMPRENDIMIENTOS PRODUCTIVOS")
		def lote = Lote.findByNombre("LOTE 8")

		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		
		java.sql.Date fechavence = new java.sql.Date((df.parse("2011-06-11")).getTime())
		def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new java.sql.Date((new Date()).getTime())
			,observacion:"TEXTO DE PRUEBA",fechaVencimiento:fechavence
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
		if (ordenReserva.total==4719)	
   	       assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA: "+ordenReserva.total)
		if (ordenReserva.totalsindesc==5445)	
   	       assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA sin descuento: "+ordenReserva.totalsindesc)

		def reciboController = new ReciboController()
		reciboController.reciboService = reciboService
		reciboController.params.ordenreservaid=ordenReserva.id
		reciboController.params.concepto=''
		reciboController.params.efectivo=4
		reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000,vencimiento:'2012-10-26'},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900.24,vencimiento:'2011-12-30'}]"
		reciboController.createjson()
		
		
		def respuesta = reciboController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		assertTrue(respuestaJson.success)
		def ordenPos = OrdenReserva.get(ordenReserva.id)
		assertNotNull(ordenPos.notas)
		//fail("DATOS: ${ordenPos.total} ${ordenPos.credito} ${ordenPos.debito} ${ordenPos.recibo} ${ordenPos.subtotalDetalle}")

		reciboController.createjson()
		respuesta = reciboController.response.contentAsString
		respuestaJson = grails.converters.JSON.parse(respuesta)
		assertTrue(respuestaJson.success)

		
		assertTrue(NotaDC.count()==1)
		assertTrue(ordenPos.notas.size()==1)
		//fail("TOTAL EN PESOS ES: "+respuestaJson.totalletras+", numero de recibo: "+respuestaJson.numero+", nombre empresa: "+respuestaJson.empresa_nombre+", importe: "+respuestaJson.total)
	}
	
	
	void testCreateJsonSinDescuento(){
		def sectorSinDesc = Sector.findByNombre("PABELLON 2")
		def loteSinDesc = Lote.findByNombre("LOTE 2")
		assertNotNull(exposicion)
		assertNotNull(sectorSinDesc)
		assertNotNull(loteSinDesc)
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		
		java.sql.Date fechavence = new java.sql.Date((df.parse("2010-12-25")).getTime())
		def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new java.sql.Date((new Date()).getTime())
			,observacion:"TEXTO DE PRUEBA",fechaVencimiento:fechavence
			,anio:2010,porcentajeResIns:21,porcentajeResNoIns:0)
		//log.debug "VALIDACION: ${ordenReserva.validate().toString()}"
		ordenReserva.addToDetalle(new DetalleServicioContratado(subTotal:lote.precio,sector:sectorSinDesc,lote:loteSinDesc))
		ordenReserva.addToOtrosconceptos(new OtrosConceptos(descripcion:"DESCUENTO",tipo:tipoconcepto,subTotal:500))
		ordenReserva.addToProductos(new ProductoExpuesto(descripcion:'Producto Expuesto'))
		ordenReserva=ordenReservaService.generarOrdenReserva(ordenReserva,empresa)
		assertNotNull(ordenReserva)
		
		assertTrue(ordenReserva.nombre.equals("empresa de prueba".toUpperCase()))
		assertTrue(ordenReserva.detalle.size()==1)
		assertTrue(ordenReserva.otrosconceptos.size()==1)
		assertTrue(ordenReserva.empresa.nombre.equals("empresa de prueba".toUpperCase()))
		//fail("TOTAL DE LA ORDEN: "+ordenReserva.total)
		if (ordenReserva.total==4840)
			  assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA: "+ordenReserva.total)
		if (ordenReserva.totalsindesc==4840)
			  assertTrue(true)
		else
			fail("ERROR EN EL CALCULO DEL TOTAL DE ORDEN DE RESERVA sin descuento: "+ordenReserva.totalsindesc)
		  
		def reciboController = new ReciboController()
		reciboController.reciboService = reciboService
		reciboController.params.ordenreservaid=ordenReserva.id
		reciboController.params.concepto=''
		reciboController.params.efectivo=4
		reciboController.params.chequesjson="[{numero:'0000789',banco:'BANCO DEL TUCUMAN',importe:2000,vencimiento:'2012-10-26'},{numero:'0123456',banco:'BANCO DE LA NACION ARGENTINA',importe:900.24,vencimiento:'2011-12-30'}]"
		reciboController.createjson()
		def respuesta = reciboController.response.contentAsString
		def respuestaJson = grails.converters.JSON.parse(respuesta)
		def ordenPos = OrdenReserva.get(ordenReserva.id)
		//fail("DATOS: ${ordenPos.total} ${ordenPos.credito} ${ordenPos.debito} ${ordenPos.recibo}")
		assertNull(ordenPos.notas)
		assertTrue(NotaDC.count()==0)
		assertTrue(respuestaJson.success)

		
	}
	
	
	

}
