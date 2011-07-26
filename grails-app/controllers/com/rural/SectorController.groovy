//ja cvf expo.war

package com.rural

import grails.converters.JSON
import org.springframework.transaction.TransactionStatus
import java.util.Calendar

class SectorController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

	
	def listdescuentos = {
		log.info "INGRESANDO AL CLOSURE listdescuentos DEL CONTROLLER SectorController"
	}
	
	def listjsondescuentos= {
		log.info "INGRESANDO AL CLOSURE listprecios DEL CONTROLLER SectorController"
		log.info "PARAMETROS $params"
		def list = ListaDescuentos.createCriteria().list(){
				sector{
					eq("id",params.sectorId?.toLong())
				}
			order("fechaVencimiento","asc")
		}
		
		render(contentType:"text/json"){
			total 0
			rows{
				list.each{
					row(id:it.id,porcentaje:it.porcentaje,fechaVencimiento:it.fechaVencimiento)
				}
			}
		}
	}
	
	def addjsondescuentos = {
		log.info "INGRESANDO AL CLOSURE addjsondescuentos DEL CONTROLLER SectorController"
		log.info "PARAMEETROS $params"
		def sectorInstance
		def listaDescuentosInstance 
		Sector.withTransaction(){TransactionStatus status ->
			sectorInstance = Sector.get(params.sectorId)
			if(sectorInstance){
				listaDescuentosInstance = new ListaDescuentos(params)
				listaDescuentosInstance.sector = sectorInstance
				if(listaDescuentosInstance.validate()){
					listaDescuentosInstance=listaDescuentosInstance.save()
					log.info "SE GUARDO CORRECTAMENTE EL SECTOR CON SU DESCUENTO"
					render(contentType:"text/json"){
						success true
						id listaDescuentosInstance.id
					}
				}else{
					status.setRollbackOnly()
					log.info "NO SE PUDO GUARDAR EL SECTOR CON SU DESCUENTO"
					log.debug "ERRORES DE VALIDACION: "+listaDescuentosInstance.errors.allErrors
					render(contentType:"text/json"){
						success false
						errors{
							g.eachError(bean:listaDescuentosInstance){
								log.debug "${it.class}"
								title g.message(error:it)
							}
						}
					}
				}
			}else{
				status.setRollbackOnly()
				log.info "NO SE ENCONTRO EL SECTOR PARA GUARDAR EL DESCUENTO"
				render(contentType:"text/json"){
					success false
					errors{
						title g.message(code:"com.rural.sector.noexiste",args:[params.sectorId])
					}
				}
			}
		}
	}
	
	def listjsonprecios = {
		log.info "INGRESANDO AL CLOSURE listjsonprecios DEL CONTROLLER SectorController"
		log.info "PARAMETROS $params"
		def filtros
		
		try{
			filtros = JSON.parse(params.filter)
			log.debug "FILTROS CONVERTIDO EN JSON"
		}catch(Exception e){
			log.debug "ERROR EN CONVERSION DE FILTRO: ${e.message}"
		}

		def sectores = Sector.createCriteria().list(){
			filtros.each{filtro->
				
				if(filtro["field"].equals("expoNombre")){
					log.debug "INGRESANDO POR EL FILTRO expoNombre"
					expo{
							ilike("nombre","%${filtro["value"]}%")
						}
				}
				if(filtro["field"].equals("nombre")){
					log.debug "INGRESANDO POR EL FILTRO nombre DE SECTOR"
					ilike("nombre","%${filtro["value"]}%")
				}
			}
			if(params.sort.equals("expoNombre")){
					expo{
						order("nombre",params.dir)
					}
			}

			if(params.sort.equals("nombre")){
				order("nombre",params.dir)
			}

		}
		render(contentType:"text/json"){
			total sectores.size()
			rows{
				sectores.each{
					row(id:it.id,expoNombre:it.expo.nombre,nombre:it.nombre,precio:it.precio)
				}
			}
		}

	}
	    
    def listtodosjson = {
    	log.info("INGRESANDO AL METODO listtodosjson DEL CONTROLLER SectorController")
    	log.debug("PARAMETROS INGRESADOS: $params")
		def pagingConfig = [
			max: params.limit as Integer ?: 10,
			offset: params.start as Integer ?: 0,
			sort: 'nombre',
			order: 'asc'
		]
		
    	def c = Sector.createCriteria()
    	def sectores = c.list(pagingConfig){
    		or{
    			expo{
    				like('nombre','%'+params.searchCriteria+'%')
    			}
    			like('nombre','%'+params.searchCriteria+'%')
    		}
    	}
		def totalSectores = Sector.createCriteria().get{
					projections{
						rowCount()
					}
					or{
						expo{
							like('nombre','%'+params.searchCriteria+'%')
						}
						like('nombre','%'+params.searchCriteria+'%')
					}
				}
		log.debug("TOTAL SECTORES: "+totalSectores)
    	render(contentType:"text/json"){
    		total totalSectores
    		rows{
    			sectores.each{
    				row(id:it.id,nombre:it.nombre,expoId:it.expo.id,exposicion:it.expo.nombre)
    			}
    		}
    	}
    }
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLLER SectorController")
    	log.debug("PARAMETROS INGRESADOS: $params")
    	def c = Sector.createCriteria()
    	def sectores = c.list(sort:'nombre',order:'asc'){
    		expo{
    			eq('id',new Long(params.exposicion_id))
    		}
    	}
		def totalSectores = Sector.createCriteria().count(){
    		expo{
    			eq('id',new Long(params.exposicion_id))
    		}

		}
    	render(contentType:"text/json"){
    		total totalSectores
    		rows{
    			sectores.each{
    				row(id:it.id,nombre:it.nombre)
    			}
    		}
    	}
    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ sectorInstanceList: Sector.list( params ), sectorInstanceTotal: Sector.count() ]
    }

    def show = {
        def sectorInstance = Sector.get( params.id )

        if(!sectorInstance) {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ sectorInstance : sectorInstance ] }
    }

    def delete = {
        def sectorInstance = Sector.get( params.id )
        if(sectorInstance) {
            try {
                sectorInstance.delete(flush:true)
                render(contentType:"text/json"){
                	success true
                	msg "El registro se eliminó correctamente"
                }
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
    			render(contentType:"text/json"){
    				success false
    				msg "Error de integridad al eliminar el registro"
    			}
            }
        }
        else {
    		render(contentType:"text/json"){
    			success false
    			msg "Error. Exposicion no encontrada"
    		}
        }
    }

    def edit = {
        /*def sectorInstance = Sector.get( params.id )

        if(!sectorInstance) {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ sectorInstance : sectorInstance ]
        }*/
        return[id:params.id]
    }

    def update = {
        def sectorInstance = Sector.get( params.id )
        if(sectorInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(sectorInstance.version > version) {
                    
                    sectorInstance.errors.rejectValue("version", "sector.optimistic.locking.failure", "Another user has updated this Sector while you were editing.")
                    render(view:'edit',model:[sectorInstance:sectorInstance])
                    return
                }
            }
            sectorInstance.properties = params
            if(!sectorInstance.hasErrors() && sectorInstance.save()) {
                flash.message = "Sector ${params.id} updated"
                redirect(action:show,id:sectorInstance.id)
            }
            else {
                render(view:'edit',model:[sectorInstance:sectorInstance])
            }
        }
        else {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def sectorInstance = new Sector()
        sectorInstance.properties = params
        return ['sectorInstance':sectorInstance]
    }

    def save = {
        def sectorInstance = new Sector(params)
        if(!sectorInstance.hasErrors() && sectorInstance.save()) {
            flash.message = "Sector ${sectorInstance.id} created"
            redirect(action:show,id:sectorInstance.id)
        }
        else {
            render(view:'create',model:[sectorInstance:sectorInstance])
        }
    }
    
    def showjson = {
    	log.info("INGRESANDO AL METODO showjson DEL CONTROLLER SectorController")
    	log.debug("PARAMETROS $params")
    	def sectorInstance = Sector.get(params.id)
    	render(contentType:"text/json"){
    		success true
    		data(id: sectorInstance.id,
    		nombre: sectorInstance.nombre,
    		exposicionId: sectorInstance.expo.id)
    	}
    	
    }
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLLER LoteController")
    	log.debug("PARAMETROS $params")
    	def lotesjson = grails.converters.JSON.parse(params.lotesjson)
    	def exposicionInstance=Exposicion.get(params.expo.id)
    	def sectorInstance = new Sector(nombre:params.nombre,expo:exposicionInstance)
    	lotesjson.each{
    		sectorInstance.addToLotes(new Lote(nombre:it.nombre))
    	}
    	
    	if(!sectorInstance.hasErrors() && sectorInstance.save()){
    		render(contentType:"text/json"){
    			success true
    		}
    	}else{
    		log.debug("NO SE GUARDARON LOS DATOS VERIFIQUE ESTOS ERRORES: "
    			+sectorInstance.errors.allErrors)
    		render(contentType:"text/json"){
    			success false
    		}
    	}
    }
	
	def deletejsondescuentos = {
		log.info "INGRESANDO AL CLOSURE deletejsondescuentos DEL CONTROLLER SectorController"
		log.info "PARAMETROS: $params"
		def listaDescuentosInstance = ListaDescuentos.get(params.id)
		if(listaDescuentosInstance){
			try {
				listaDescuentosInstance.delete(flush:true)
				render(contentType:"text/json"){
					success true
					msg "El registro se eliminó correctamente"
				}
			}catch(org.springframework.dao.DataIntegrityViolationException e) {
				render(contentType:"text/json"){
					success false
					errors{
						title "Error de Integridad al eliminar el registro"
					}
				}
			}
		}else{
			log.error (g.message(code:"com.rural.noexiste",args:["El Descuento",params.id]))
			render(contentType:"text/json"){
				success false
				errors{
					title g.message(code:"com.rural.noexiste",args["El Descuento",params.id])
				}
			}
		}
	}
	
	def updatejsondescuentos = {
		log.info ("INGRESANDO AL CLOSURE updatejsondescuentos DEL CONTROLLER SectorController")
		log.info "PARAMETROS: $params"
		def listaDescuentosInstance = ListaDescuentos.get(params.id)
		if(listaDescuentosInstance){
			listaDescuentosInstance.properties = params
			if(!listaDescuentosInstance.hasErrors() && listaDescuentosInstance.save()){
				render(contentType:"text/json"){
					success true
				}	
			}else{
				render(contentType:"text/json"){
					success false
					errors{
						g.eachError(bean:listaDescuentosInstance){
							title g.message(error:it)
						}
					}
				}
			}
		}else{
			log.error g.message(code:"com.rural.noexiste",args:["El Descuento",params.id])
			render(contentType:"text/json"){
				success false
				errors{
					title g.message(code:"com.rural.noexiste",args:["El Descuento",params.id])
				}
			}
		}
	}
    
    def updatejson = {
    	log.info("INGRESANDO AL METODO updatejson DEL CONTROLLER SectorController")
    	log.info("PARAMETROS $params")
    	def sectorInstance=Sector.get(params.id)
		params.precio = params.precio?.replace(".",",")
		if(sectorInstance){
	    	sectorInstance.properties=params
	    	if(!sectorInstance.hasErrors() && sectorInstance.save()){
	    		render(contentType:"text/json"){
	    			success true
	    		}
	    	}else{
	    		render(contentType:"text/json"){
	    			success false
					errors{
						g.eachError(bean:sectorInstance){
							title g.message(error:it)
						}
					}
	    		}
	    	}
		}else{
			render(contentType:"text/json"){
				success false
				errors{
					title g.message(code:"com.rural.noexiste",args:["El Sector",params.id])
				}
			}
		}
    }
	
	def getdescuento = {
		log.info("INGRESANDO AL CLOSURE getdescuento DEL CONTROLLER SectorController")
		log.info("PARAMETROS $params")
		def calendar = Calendar.getInstance()
		def listDescuentos = ListaDescuentos.createCriteria().list(){
			and{
				sector{
					eq("id",new Long(params.id.toLong()))
				}
				ge("fechaVencimiento",new java.sql.Date(calendar.getTime().getTime()))
			}
			order("fechaVencimiento","asc")
			
		}
		def porc = 0
		def vencimiento
		def precioSector
		if(listDescuentos){
			porc = listDescuentos[0].porcentaje
			vencimiento = listDescuentos[0].fechaVencimiento
			precioSector = listDescuentos[0].sector.precio
		}
		render(contentType:"text/json"){
			success true
			porcentaje porc
			fechaVencimiento vencimiento
			precio precioSector
		}
	}
}
