package com.rural

import grails.test.*
import com.rural.*
import com.rural.seguridad.*

class EmpresaServiceTests extends GrailsUnitTestCase {
	def empresaService
	def usuario = null
	def empresa = null
	def exposicion=null
	
    protected void setUp() {
        super.setUp()
        usuario = new Person(username:"admin",userRealName:"Administrador",passwd:"sdjflasf",email:"admin@noexiste.com.ar")
        usuario.save()
        
        empresa = new Empresa(nombre:"empresa de prueba",usuario:usuario)
        empresa=empresa.save()
        
        exposicion = new Exposicion(nombre:"Expo 2010")
        exposicion.save()
        
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
    	
    	def ordenReserva = new OrdenReserva(usuario:usuario,expo:exposicion,fechaAlta:new Date(),sector:'7G')
    	empresa.addToOrdenes(ordenReserva)
    	empresa=empresaService.generarOrdenReserva(empresa)
    	
    	
    	
    	assertTrue(empresa.ordenes?.size()==1)
    	
    }
}
