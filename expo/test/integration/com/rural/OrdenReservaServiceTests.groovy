package com.rural

import grails.test.*
import com.rural.*
import com.rural.seguridad.*


class OrdenReservaServiceTests extends GrailsUnitTestCase {
	def ordenReservaService
	def usuario = null
	def empresa = null
	def exposicion = null
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
    	
    	//empresa = Empresa.get(empresa.id)
    	//fail("EMPRESA: "+empresa.id+" - empresa nombre: "+empresa.nombre)
    	
    	def ordenReserva = new OrdenReserva(empresa:empresa,usuario:usuario,expo:exposicion,fechaAlta:new Date(),sector:'7G')
    	ordenReservaService.generarOrdenReserva(ordenReserva)
    }
}
