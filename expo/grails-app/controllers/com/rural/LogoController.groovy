

package com.rural

class LogoController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
    	log.info "INGRESANDO AL CLOSURE list DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS: $params"
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ logoInstanceList: Logo.list( params ), logoInstanceTotal: Logo.count(),exposicionId:params.expoid,exposicionNombre:params.exponombre ]
    }

    def show = {
        def logoInstance = Logo.get( params.id )

        if(!logoInstance) {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ logoInstance : logoInstance ] }
    }

    def delete = {
        def logoInstance = Logo.get( params.id )
        if(logoInstance) {
            try {
                logoInstance.delete(flush:true)
                flash.message = "Logo ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Logo ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def logoInstance = Logo.get( params.id )

        if(!logoInstance) {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ id : logoInstance.id ]
        }
    }

    def update = {
        def logoInstance = Logo.get( params.id )
        if(logoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(logoInstance.version > version) {
                    
                    logoInstance.errors.rejectValue("version", "logo.optimistic.locking.failure", "Another user has updated this Logo while you were editing.")
                    render(view:'edit',model:[logoInstance:logoInstance])
                    return
                }
            }
            logoInstance.properties = params
            if(!logoInstance.hasErrors() && logoInstance.save()) {
                flash.message = "Logo ${params.id} updated"
                redirect(action:show,id:logoInstance.id)
            }
            else {
                render(view:'edit',model:[logoInstance:logoInstance])
            }
        }
        else {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
    	log.info "INGRESANDO AL CLOSURE create DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS $params"
        def logoInstance = new Logo()
        logoInstance.properties = params
        return ['logoInstance':logoInstance,exposicionId:params.exposicionId,exposicionNombre:params.exposicionNombre]
    }

    def save = {
        def logoInstance = new Logo(params)
        if(!logoInstance.hasErrors() && logoInstance.save()) {
            flash.message = "Logo ${logoInstance.id} created"
            redirect(action:show,id:logoInstance.id)
        }
        else {
            render(view:'create',model:[logoInstance:logoInstance])
        }
    }
    
    def savejson = {
    	log.info "INGRESANDO AL CLOSURE savejson DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS $params"
		def image = request.getFile('image')
		if (image?.empty || image?.size>1024*30){
			log.error "LA IMAGEN  SUPERA EL LIMITE PERMITIDO O ES UNA IMAGEN VACIA"
			render """{success:false,msg:'Recuerde que el archivo subido debe ser una imagen y que su tamaño máximo debe se de 30 KB'}"""
			return
		}
		def logoInstance = new Logo(params)
		
    	if(!logoInstance.hasErrors() && logoInstance.save()){
			log.info "LA INSTANCIA DE LA CLASE Logo SE GUARDO CORRECTAMENTE"
			render """{success:true,msg:'Logo guardado'}"""
    	}else{
			log.error "ERROR AL GUARDAR LA INSTACIA DE LA CLASE Logo "+logoInstance.errors.allErrors
			render """{success:false,msg:'Se produjo un error al tratar de guardar los datos. Verifique que el archivo sea una imagen y que no esta repitiendo el a�o'}"""
		}
    }

	def listjson={
		log.info "INGRESANDO AL CLOSURE listjson DEL CONTROLLER LogoController"
		log.info "PARAMETROS $params"
		def list = Logo.createCriteria().list(){
			expo{
				eq("id",new Long(params.exposicionId))
			}
		}
		render(contentType:'text/json'){
			success 'true'
			total list.size()
			rows{
				list.each{
					row(id: it.id,anio:it.anio,exponombre: it.expo.nombre)
				}
			}
		}
	}	
	
	def updatejson={
		log.info "INGRESANDO AL CLOSURE updatejson DEL CONTROLLER LogoController"
		log.info "PARAMETROS $params"
		def logoInstance = Logo.get(params.id)
		def image = request.getFile('image')
		def imagesaved = logoInstance.image
		logoInstance.properties = params
		if (!image?.empty ){
			if(image.size>1024*30){
				log.debug("Error de tama�o de archivo")
				render """{success:false,msg:'El tamaño máximo de la imagen es de 30 KB'}"""
				return
			}else{
				log.debug("Imagen de Exposicion Asignada desde el upload")
				//exposicionInstance.image=image
			   }
		}else{
			log.debug("Imagen de Exposicion recuperada")
			logoInstance.image=imagesaved
		}
		
		
		if(!logoInstance.hasErrors() && logoInstance.save()){
			log.debug("LOS CAMBIOS SE GUARDARON CORRECTAMENTE")
			render """{success:true}"""
		}else{
			log.debug("LOS CAMBIOS NO SE PUDIERON GUARDAR")
			render """{success:false,msg:'Se produjo un error al tratar de guardar los datos'}"""
		}

	}
	
	def showjson = {
		log.info("INGRESANDO AL METODO showjson DEL CONTROLADOR LogoController")
		log.debug("PARAMETROS $params")
		def logoInstance = Logo.get(params.id)
		String pathtofile = servletContext.getRealPath("/reports/images")+"\\"+logoInstance.expo.nombre.trim()+logoInstance.anio+".jpg";
		if(logoInstance.image){
			FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
			foutput.write(logoInstance.image)
			foutput.flush()
		}
		log.debug("NOMBRE DE ARCHIVO DE LA IMAGEN: "+"/expo/reports/images/"+logoInstance.expo.nombre.trim()+logoInstance.anio+".jpg")
		if (logoInstance){
			render(contentType:"text/json"){
				success true
				pathfile "/expo/reports/images/"+logoInstance.expo.nombre.trim()+".jpg"
				data(id:logoInstance.id,anio:logoInstance.anio,exposicionId:logoInstance.expo.id,exposicionNombre:logoInstance.expo.nombre)
			}
		}else{
			render(contentType:"text/json"){
				success false
			}
		}
	}
	
	def logopreview = {
		log.info "INGRESANDO AL METODO logopreview DEL CONTROLADOR LogoController"	
		log.debug "PARAMETROS $params"
		def logoInstance = Logo.get(params.id)
		def list = new ArrayList()
		if(logoInstance){
			log.debug("LOGO ENCONTRADO")
			def pathtofile = servletContext.getRealPath("/reports/images")+"/"+logoInstance.expo.nombre.trim()+logoInstance.anio+".jpg"
			def reportDirPath = servletContext.getRealPath("/reports")
			if (logoInstance){
				FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
				if(logoInstance.image){
					foutput.write(logoInstance.image)
					foutput.flush()
				}
			}
			params.put("reportsDirPath",reportDirPath)
			params.put("logo",logoInstance.expo.nombre.trim()+logoInstance.anio+".jpg")
			params.put("_format","PDF")
			params.put("_name","logoexposicion")
			params.put("_file","previewlogo")
			chain(controller:'jasper',action:'index',model:[data:list],params:params)
		}else{
			log.debug("EXPOSICION NO ENCONTRADA")
			render(contentType:"text/json"){
				success false
				msg "Exposición no encontrada."
			}
		}
	}
	
	def deletejson ={
		log.info "INGRESANDOAL METODO deletejson DEL CONTROLADOR LogoController"
		log.debug "PARAMETROS $params"
		
		def logoInstance = Logo.get(params.id)
		def list
		if(logoInstance){
			list = OrdenReserva.createCriteria().list(){
				and{
					expo{
						eq("id",logoInstance.expo.id)
					}
					eq("anio",logoInstance.anio)
				}
			}
			if(list.size()>0){
				log.debug "ERROR DE INTEGRIDAD CON ORDENES DE RESERVA"
				render(contentType:"text/json"){
					success false
					msg "Error de integridad al eliminar el registro"
				}
			}
			try{
				logoInstance.delete(flush:true)
				log.debug("Logo de Exposicion ELIMINADA")
				render(contentType:"text/json"){
					success true
					msg "El registro se eliminó correctamente"
				}
			}catch(org.springframework.dao.DataIntegrityViolationException e){
				log.debug("NO SE PUDO ELIMINAR EL Logo de Exposicion")
				render(contentType:"text/json"){
					success false
					msg "Error de integridad al eliminar el registro"
				}
			}
		}else{
			log.debug("NO SE ENCONTRO EL Logo de EXPOSICION CON ID $params.id, IMPOSIBLE ELIMINAR")
			render(contentType:"text/json"){
				success false
				msg "Error. Logo de Exposicion no encontrada"
			}
		}

	}

	
}
