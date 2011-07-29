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
		def filtros
		java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy")
		java.util.Date fecha
		java.sql.Date fechasql

		try{
			filtros = JSON.parse(params.filter)
		}catch(Exception e){
			log.debug "ERROR EN PARSEO DE FILTROS: "+e.message
		}
		def criteria = NotaDC.createCriteria()
		def criteriacount = NotaDC.createCriteria()
		
		def closurenotadc = {
			firstResult(params.start.toInteger())
			maxResults(params.limit.toInteger())
			criteria.eq("anulada",Boolean.parseBoolean(params.soloanuladas))
			filtros.each{filtro->
				if(filtro["field"].equals("numero")){
					criteria.eq("numero",filtro["value"].toLong())
				}
				if(filtro["field"].equals("fechaAlta")){
					fecha = df.parse(filtro["value"])
					fechasql = new java.sql.Date(fecha.getTime())
					criteria."${filtro.comparison}"(filtro["field"],fechasql)
				}
				if(filtro["field"].equals("nombre")){
					criteria.ordenReserva(){
						criteria.ilike("nombre","%${filtro["value"]}%")
					}
				}
				if(filtro["field"].equals("expo")){
					criteria.ordenReserva(){
						criteria.expo(){
							criteria.ilike("nombre","%${filtro["value"]}%")
						}
					}
				}
			}
		}
		
		def closurecount = {
			criteriacount.eq("anulada",Boolean.parseBoolean(params.soloanuladas))
			filtros.each{filtro->
				if(filtro["field"].equals("numero")){
					criteriacount.eq("numero",filtro["value"].toLong())
				}
				if(filtro["field"].equals("fechaAlta")){
					fecha = df.parse(filtro["value"])
					fechasql = new java.sql.Date(fecha.getTime())
					criteriacount."${filtro.comparison}"(filtro["field"],fechasql)
				}
				if(filtro["field"].equals("nombre")){
					criteriacount.ordenReserva(){
						criteriacount.ilike("nombre","%${filtro["value"]}%")
					}
				}
				if(filtro["field"].equals("expo")){
					criteriacount.ordenReserva(){
						criteriacount.expo(){
							criteriacount.ilike("nombre","%${filtro["value"]}%")
						}
					}
				}
			}

			criteriacount.projections{
				rowCount()
			}
		}
		
		
		def totalNotas = criteriacount.get(closurecount)
		def notas = criteria.list(closurenotadc)
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
					eq("id",notadc.ordenReserva.expo.id)
				}
				eq("anio",notadc.ordenReserva.anio)
			}
		}
		def logo
		listlogos.each{
			logo = it
		}
		
		notadc.detalle.each{
			
		}
		//------------------------------------------------------------------------------------

		String pathtofile = servletContext.getRealPath("/reports/images")+"/"+notadc.ordenReserva.expo.nombre.trim()+notadc.ordenReserva.anio+".jpg"
		if(logo?.image){
			FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
			foutput.write(logo?.image)
			foutput.flush()
		}
		
		List notadcList = new ArrayList()
		notadcList.add(notadc)
		String reportsDirPath = servletContext.getRealPath("/reports/");
		params.put("reportsDirPath", reportsDirPath);
		params.put("logo",notadc.ordenReserva.expo.nombre.trim()+notadc.ordenReserva.anio+".jpg")
		log.debug("Parametros: $params")
		chain(controller:'jasper',action:'index',model:[data:notadcList],params:params)

	}
	
}

