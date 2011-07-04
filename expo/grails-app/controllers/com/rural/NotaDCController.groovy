

package com.rural

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
			render(contentType:"text/json"){
				success false
				errors{
					retorno.
				}	
			}
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
		def totalNotas = NotaDC.createCriteria().count(){
			and{
				if(params.fieldSearch.equals('empresa.nombre')){
					ordenReserva{
						ilike("nombre","%"+params.searchCriteria+"%")
					}
				}else{
					eq("numero",new Long(params.searchCriteria))
				}
				eq("anulada",Boolean.parseBoolean(params.anulada))
			}
		}
		
		def notas = NotaDC.createCriteria().list(pagingconfig){
			and{
				if(params.fieldSearch.equals('empresa.nombre')){
					ordenReserva{
						ilike("nombre","%"+params.searchCriteria+"%")
					}
				}else{
					eq("numero",new Long(params.searchCriteria))
				}
				eq("anulada",Boolean.parseBoolean(params.anulada))
			}
			
			if(params.sort.equals("fechaAlta")){
				order("fechaAlta",params.dir.toLowerCase())
			}
			if(params.sort.equals("numero")){
				order("numero",params.dir.toLowerCase())
			}
		}
		
		render(contentType:"text/json"){
			total	totalNotas
			rows{
				notas.each{
					row(id:it.id,fechaAlta:it.fechaAlta,nombre:it.ordenReserva.nombre,numero:it.numero,monto:it.monto)
				}
			} 
		}

	}
	
	def updatejson = {
		log.info "INGRESANDO AL CLOSURE updatejson DEL CONTROLLER NotaDCController"
		log.info "PARAMETROS ${params}"

	}
}
