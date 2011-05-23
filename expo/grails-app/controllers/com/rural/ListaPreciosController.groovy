

package com.rural

import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.Calendar

class ListaPreciosController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ listaPreciosInstanceList: ListaPrecios.list( params ), listaPreciosInstanceTotal: ListaPrecios.count() ]
    }

    def show = {
        def listaPreciosInstance = ListaPrecios.get( params.id )

        if(!listaPreciosInstance) {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ listaPreciosInstance : listaPreciosInstance ] }
    }

    def delete = {
        def listaPreciosInstance = ListaPrecios.get( params.id )
        if(listaPreciosInstance) {
            try {
                listaPreciosInstance.delete(flush:true)
                flash.message = "ListaPrecios ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "ListaPrecios ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def listaPreciosInstance = ListaPrecios.get( params.id )

        if(!listaPreciosInstance) {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ listaPreciosInstance : listaPreciosInstance ]
        }
    }

    def update = {
        def listaPreciosInstance = ListaPrecios.get( params.id )
        if(listaPreciosInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(listaPreciosInstance.version > version) {
                    
                    listaPreciosInstance.errors.rejectValue("version", "listaPrecios.optimistic.locking.failure", "Another user has updated this ListaPrecios while you were editing.")
                    render(view:'edit',model:[listaPreciosInstance:listaPreciosInstance])
                    return
                }
            }
            listaPreciosInstance.properties = params
            if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()) {
                flash.message = "ListaPrecios ${params.id} updated"
                redirect(action:show,id:listaPreciosInstance.id)
            }
            else {
                render(view:'edit',model:[listaPreciosInstance:listaPreciosInstance])
            }
        }
        else {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def listaPreciosInstance = new ListaPrecios()
        listaPreciosInstance.properties = params
        return ['listaPreciosInstance':listaPreciosInstance]
    }

    def save = {
        def listaPreciosInstance = new ListaPrecios(params)
        if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()) {
            flash.message = "ListaPrecios ${listaPreciosInstance.id} created"
            redirect(action:show,id:listaPreciosInstance.id)
        }
        else {
            render(view:'create',model:[listaPreciosInstance:listaPreciosInstance])
        }
    }
	
	def listjson = {
		log.info "INGRESANDO AL CLOSURE listjson DEL CONTROLLER ListaPreciosController"
		log.info "PARAMETROS: ${params}"
		def pagingConfig = [
			max: params.limit as Integer ?: 10,
			offset: params.start as Integer ?: 0
		]
		
		
		def totalPrecios = ListaPrecios.createCriteria().count(){
			expo{
				eq("id", params.expoId.toLong())
			}
			sector{
				eq("id", params.sectorId.toLong())
			}
		}
		
		log.debug "TOTAL REGISTROS DE LISTA DE PRECIOS: ${totalPrecios}"
		
		def list = ListaPrecios.createCriteria().list(pagingConfig){
			expo{
				eq("id", params.expoId.toLong())
			}
			sector{
				eq("id", params.sectorId.toLong())
			}
		}
		
		log.debug "LISTA DE PRECIOS: ${list}"
		
		render(contentType:"text/json"){
			total totalPrecios
			rows{
				list.each{
					row(id:it.id,vigencia:it.vigencia,precio:it.precio)
				}
			}
		}
		
		
	}
	
	def savejson={
		log.info "INGRESANDO AL CLOSURE savejson DEL CONTROLLER ListaPreciosController"
		log.info "PARAMETROS ${params}"
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		Date vigencia
		def flagdate=false
		
		
		try{
			log.debug("VENCIMIENTO A PARSEAR: "+params.vigencia.substring(0,10))
			vigencia = df.parse(params.vigencia.substring(0,10))
			def gc = Calendar.getInstance()
			gc.setTime(vigencia)
			params.vigencia_year = gc.get(Calendar.YEAR).toString()
			params.vigencia_month = (gc.get(Calendar.MONTH)+1).toString()
			params.vigencia_day = gc.get(Calendar.DATE).toString()
		}catch(Parse){
			log.debug("ERROR AL PARSEAR LA VIGENCIA")
			flagdate=true
		}
		def listaPreciosInstance= new ListaPrecios(params)
		if(flagdate)
			listaPreciosInstance.errors.rejectValue("vigencia","typeMismatch.java.util.Date")

			

		if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()){ 
			render(contentType:"text/json") {
				success true
				id listaPreciosInstance.id
			}
		}else{
			g.eachError(bean:listaPreciosInstance){
				log.debug "ERROR: ${it}"
			}
			log.debug "ERRORES ENCONTRADOS: "+listaPreciosInstance.errors.allErrors
			render(contentType:"text/json"){
				success false
				errors{
					g.eachError(bean:listaPreciosInstance){
						title g.message(error:it)
					}
				}
			}
	
		}
		
	}
	
	def deletejson={
		log.info "INGRESANDO AL CLOSURE deletejson DEL CONTROLLER ListaPreciosController"
		log.info "PARAMETROS: ${params}"
		def listaPreciosInstance = ListaPrecios.get(params.id)
		if(listaPreciosInstance){
			try{
				listaPreciosInstance.delete(flush:true)	
				render(contentType:"text/json"){
					success true 
					title	"El registro se eliminó correctamente"
					
				}
			}catch(org.springframework.dao.DataIntegrityViolationException e){
				render(contentType:"text/json"){
					success false
					title: "No se puede eliminar el precio del tarifario porque es referenciado por otros datos"
				}
			}	
		}else{
			render(contentType:"text/json"){
				success false
				errors{
					title "La lista de precios con id ${params.id} no existe"
				}
			}
		}
		
	}
	
	def updatejson = {
		log.info "INGRESANDO AL CLOSURE updatejson DEL CONTROLLER ListaPreciosController"
		log.info "PARAMETROS: ${params}"
		def listaPreciosInstance = ListaPrecios.get(params.id)
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		Date vigencia
		def flagdate=false
		
		
		try{
			log.debug("VENCIMIENTO A PARSEAR: "+params.vigencia.substring(0,10))
			vigencia = df.parse(params.vigencia.substring(0,10))
			def gc = Calendar.getInstance()
			gc.setTime(vigencia)
			params.vigencia_year = gc.get(Calendar.YEAR).toString()
			params.vigencia_month = (gc.get(Calendar.MONTH)+1).toString()
			params.vigencia_day = gc.get(Calendar.DATE).toString()
		}catch(Parse){
			log.debug("ERROR AL PARSEAR LA VIGENCIA")
			flagdate=true
		}

		if(listaPreciosInstance){
			listaPreciosInstance.properties = params
			if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()){
				render(contentType:"text/json"){
					success true
					
				}
			}else{
				render(contentType:"text/json"){
					success false
					errors{
						g.eachError(bean:listaPreciosInstance){
							title g.message(error:it)
						}
					}
				}
			}
		}else{
			render(contentType:"text/json"){
				success false
				errors{
					title "La lista de precios con id ${params.id} no existe"
				}
			}

		}
	}
}
