package com.rural

import com.rural.enums.TipoNotaEnum
import com.rural.enums.TipoGeneracionEnum
import grails.converters.JSON

class NotaDCController {
	def ordenReservaService
	    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ notaDCInstanceList: NotaDC.list( params ), notaDCInstanceTotal: NotaDC.count() ]
    }

    def show = {
        def notaDCInstance = NotaDC.get( params.id )

        if(!notaDCInstance) {
            flash.message = "NotaDC not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ notaDCInstance : notaDCInstance ] }
    }

    def delete = {
        def notaDCInstance = NotaDC.get( params.id )
        if(notaDCInstance) {
            try {
                notaDCInstance.delete(flush:true)
                flash.message = "NotaDC ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "NotaDC ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "NotaDC not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def notaDCInstance = NotaDC.get( params.id )

        if(!notaDCInstance) {
            flash.message = "NotaDC not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ notaDCInstance : notaDCInstance ]
        }
    }

    def update = {
        def notaDCInstance = NotaDC.get( params.id )
        if(notaDCInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(notaDCInstance.version > version) {
                    
                    notaDCInstance.errors.rejectValue("version", "notaDC.optimistic.locking.failure", "Another user has updated this NotaDC while you were editing.")
                    render(view:'edit',model:[notaDCInstance:notaDCInstance])
                    return
                }
            }
            notaDCInstance.properties = params
            if(!notaDCInstance.hasErrors() && notaDCInstance.save()) {
                flash.message = "NotaDC ${params.id} updated"
                redirect(action:show,id:notaDCInstance.id)
            }
            else {
                render(view:'edit',model:[notaDCInstance:notaDCInstance])
            }
        }
        else {
            flash.message = "NotaDC not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def notaDCInstance = new NotaDC()
        notaDCInstance.properties = params
        return ['notaDCInstance':notaDCInstance]
    }

    def save = {
        def notaDCInstance = new NotaDC(params)
        if(!notaDCInstance.hasErrors() && notaDCInstance.save()) {
            flash.message = "NotaDC ${notaDCInstance.id} created"
            redirect(action:show,id:notaDCInstance.id)
        }
        else {
            render(view:'create',model:[notaDCInstance:notaDCInstance])
        }
    }
	
	def savejson = {
		log.info "INGRESANDO AL CLOSURE savejson DEL CONTROLLER NotaDCController"
		log.info "PARAMETROS ${params}"
		
		def retorno = ordenReservaService(params.ordenReservaId.toLong(),Enum.valueOf(TipoNotaEnum,params.tipo))
		if(retorno instanceof OrdenReserva){

			render retorno.errors.allErrors.collect {
				message(error:it,encodeAs:'HTML')
			} as JSON
			
			
		}else{
			render(contentType:"text/json"){
				success true
				notaId retorno
			}
		}
	}
	
	def showjson = {
		log.info "INGRESANDO AL CLOSURE showjson DEL CONTROLLER NotaDCController"
		log.info "PARAMETROS ${params}"
	
	}
	
	def listjson = {
		log.info "INGRESANDO AL CLOSURE listjson DEL CONTROLLER NotaDCController"
		log.info "PARAMETROS ${params}"
		def pagingconfig = [
			max: params.limit as Integer ?:10,
			offset: params.start as Integer ?:0
		]
		
		def notas = NotaDC.list(pagingconfig)
		
		
		//fields:['id','fechaAlta','numero','nombre','total','numeroordenreserva','expo'],
		
		def totalNotas = notas.size()
		
		render(contentType:"text/json"){
			total	totalNotas
			rows{
				notas.each{
					row(id:it.id,fechaAlta:it.fechaAlta,nombre:it.ordenReserva.nombre,numero:it.numero,total:it.total,tipo:it.tipo.name,tipoGen:it.tipoGen.name,numeroordenreserva:it.ordenReserva.numero,expo:it.ordenReserva.expo.nombre)
				}
			} 
		}

	}
	
	def updatejson = {
		log.info "INGRESANDO AL CLOSURE updatejson DEL CONTROLLER NotaDCController"
		log.info "PARAMETROS ${params}"

	}
	
	def listtiponotajson = {
		log.info "INGRESANDO AL CLOSURE listtiponota"
		log.info "PARAMETROS $params"
		def tipoNotaList = TipoNotaEnum.list()
		render(contentType:"text/json"){
			success true
			rows{
				tipoNotaList.each {
					row(id:it,name:it.name)	 
				}
			}
		}
	}
	
	def reporte = {
		log.info"INGRESANDO AL CLOSURE reporte"
		log.info "PARAMETROS: $params"
		def notadc = NotaDC.load(params.id.toLong())
		def listlogos = Logo.createCriteria().list(){
			and{
				expo{
					eq("id",recibo.ordenReserva.expo.id)
				}
				eq("anio",recibo.ordenReserva.anio)
			}
		}
		def logo
		listlogos.each{
			logo = it
		}
		//------------------------------------------------------------------------------------

		String pathtofile = servletContext.getRealPath("/reports/images")+"/"+recibo.ordenReserva.expo.nombre.trim()+recibo.ordenReserva.anio+".jpg"
		if(logo?.image){
			FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
			foutput.write(logo?.image)
			foutput.flush()
		}
		
		log.debug("Empresa del Recibo: "+recibo.ordenReserva.empresa.nombre)
		List reciboList = new ArrayList()
		reciboList.add(recibo)
		String reportsDirPath = servletContext.getRealPath("/reports/");
		params.put("reportsDirPath", reportsDirPath);
		params.put("logo",recibo.ordenReserva.expo.nombre.trim()+recibo.ordenReserva.anio+".jpg")
		log.debug("Parametros: $params")
		chain(controller:'jasper',action:'index',model:[data:reciboList],params:params)

	}
	
}

