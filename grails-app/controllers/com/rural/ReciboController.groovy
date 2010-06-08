package com.rural

import com.rural.utils.N2t

class ReciboController {
	def reciboService
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ reciboInstanceList: Recibo.list( params ), reciboInstanceTotal: Recibo.count() ]
    }

    def show = {
        def reciboInstance = Recibo.get( params.id )

        if(!reciboInstance) {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ reciboInstance : reciboInstance ] }
    }

    def delete = {
        def reciboInstance = Recibo.get( params.id )
        if(reciboInstance) {
            try {
                reciboInstance.delete(flush:true)
                flash.message = "Recibo ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Recibo ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def reciboInstance = Recibo.get( params.id )

        if(!reciboInstance) {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ reciboInstance : reciboInstance ]
        }
    }

    def update = {
        def reciboInstance = Recibo.get( params.id )
        if(reciboInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(reciboInstance.version > version) {
                    
                    reciboInstance.errors.rejectValue("version", "recibo.optimistic.locking.failure", "Another user has updated this Recibo while you were editing.")
                    render(view:'edit',model:[reciboInstance:reciboInstance])
                    return
                }
            }
            reciboInstance.properties = params
            if(!reciboInstance.hasErrors() && reciboInstance.save()) {
                flash.message = "Recibo ${params.id} updated"
                redirect(action:show,id:reciboInstance.id)
            }
            else {
                render(view:'edit',model:[reciboInstance:reciboInstance])
            }
        }
        else {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def reciboInstance = new Recibo()
        reciboInstance.properties = params
        return ['reciboInstance':reciboInstance]
    }
    
    def createjson = {
		log.info("INGRESANDO LA METODO createjson DEL CONTROLADOR ReciboController")
		log.debug("PARAMETROS: $params")
		def chequesjson = grails.converters.JSON.parse(params.chequesjson)
		def cheques = []  
		
		chequesjson.each{
			cheques.add(new Cheque(numero:it.numero,banco:it.banco,importe:it.importe))
		}
		def recibo = reciboService.generarRecibo(new Long(params.ordenreservaid),params.concepto,params.efectivo,cheques)
		int entero = recibo.total.intValue()
		Double totalaux = (recibo.total - entero)*100
		int decimal = totalaux.intValue()
		log.debug("XXXXXXXXXXXXXXXXXXXXXXXANTES DE CREAR EL CONVERTIDOR DE NUMEROS A LETRAS")
		N2t num2letra = new N2t()
		log.debug("DESPUES DE CREAR EL CONVERTIDOR DE NUMEROS A LETRAS")
		String totalenletras="SON "+num2letra.convertirLetras(entero)+" PESOS CON "+((num2letra.convertirLetras(0)).trim()=="" ? "CERO" : num2letra.convertirLetras(decimal))+" CENTAVOS"
		totalenletras = totalenletras.toUpperCase()
		if(recibo){
			render(contentType:"text/json"){
				success true
				id recibo.id
				numero	recibo.numero
				empresa_nombre recibo.ordenReserva.empresa.nombre
				total recibo.total 
				totalletras totalenletras 
			}
		}else{
			render(contentType:"text/json"){
				success false
			}
				 
		}
    }

    def save = {
        def reciboInstance = new Recibo(params)
        if(!reciboInstance.hasErrors() && reciboInstance.save()) {
            flash.message = "Recibo ${reciboInstance.id} created"
            redirect(action:show,id:reciboInstance.id)
        }
        else {
            render(view:'create',model:[reciboInstance:reciboInstance])
        }
    }
	
    def listchequesjson = {
    	log.info("INGRESANDO AL METODO listchequesjson DEL CONTROLADOR ReciboController")
    	log.debug("PARAMETROS $params")
    	def recibo = Recibo.get(params.id)
    	render(contentType:"text/json"){
    		total recibo.cheques.size()
    		rows{
    			recibo.cheques.each{
    				row(numero:it.numero,banco,importe:it.importe)
    			}
    		}
    	}
    }
	
	
}


/*------------------CONVERTIDOR DE NUMERO EN LETRAS------------------------------*/










