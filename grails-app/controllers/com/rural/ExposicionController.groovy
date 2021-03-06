package com.rural
import grails.converters.JSON
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.FileOutputStream


class ExposicionController {
	

    def index = { redirect(action:list,params:params) }
    
    def create = {
    		
    }
    
    def list = {
    		
    }
    
    def edit = {
    	log.info("INGRESANDO AL METODO edit DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	return[id:params.id]
    }	
	
	def listjsonanioexpo = {
		log.info("INGRESANDO AL MENTODO listjsonanioexpo DEL CONTROLADOR ExposicionController")
		log.debug("PARAMETROS $params")
		def logos = Logo.createCriteria().list{
			expo {
				eq("id",Long.parseLong(params.expoId))
			}
			order("anio","desc")
		}
		log.debug "CANTIDAD DE LOGOS: "+logos.size()
		render(contentType:'text/json'){
			sucess 'true'
			total logos.size()
			rows{
				logos.each {
					row(anio: it.anio) 
				}
			}
		}
	}
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	def exposiciones
    	if(params.searchCriteria){
    		exposiciones = Exposicion.createCriteria().list{
    			like('nombre',"%"+params.searchCriteria+"%")
    		}
    	}else{
    		exposiciones = Exposicion.list()
    	}
    	
    	render(contentType:'text/json'){
    		success 'true'
    		total exposiciones.size()
    		rows{
    			exposiciones.each{
    				row(id: it.id,nombre: it.nombre)
    			}
    		}
    	}
    	
    }
    
    def showjson = {
    	log.info("INGRESANDO AL METODO showjson DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	def exposicionInstance = Exposicion.get(params.id)
    	String pathtofile = servletContext.getRealPath("/reports/images")+"\\"+exposicionInstance.nombre.trim()+".jpg";
    	if(exposicionInstance.image){
    		FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
    		foutput.write(exposicionInstance.image)
    		foutput.flush()
    	}
    	log.debug("NOMBRE DE ARCHIVO DE LA IMAGEN: "+"/expo/reports/images/"+exposicionInstance.nombre.trim()+".jpg")
    	if (exposicionInstance){
	    	render(contentType:"text/json"){
	    		success true
	    		pathfile "/expo/reports/images/"+exposicionInstance.nombre.trim()+".jpg"
	    		data(id:exposicionInstance.id,puntoVenta:exposicionInstance.puntoVenta,nombre:exposicionInstance.nombre)
	    	}
    	}else{
    		render(contentType:"text/json"){
    			success false
    		}
    	}
    }
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	/*def image =  request.getFile('image')
    	if (image?.empty || image?.size>1024*30){
    		render """{success:false,msg:'El tamaño máximo de la imagen es de 30 KB'}"""
    		return
    	}*/
    	def exposicionInstance=new Exposicion(params)
    	if(!exposicionInstance.hasErrors() && exposicionInstance.save()){
    		/*render(contentType:"text/json"){
    			success true
    			
    		}*/
    		render """{success:true}"""
    	}else{
    		/*render(contentType:"text/json"){
    			success false
    		}*/
    		render """{success:false,msg:'Se produjo un error al tratar de guardar los datos'}"""
    	}
    }
    
    def updatejson = {
    	log.info("INGRESANDO AL METODO updatejson DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	def exposicionInstance = Exposicion.get(params.id)
		exposicionInstance.properties = params
    	/*def image = request.getFile('image')
    	def imagesaved = exposicionInstance.image
    	
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
			exposicionInstance.image=imagesaved			    		
    	}*/
    	
    	
    	if(!exposicionInstance.hasErrors() && exposicionInstance.save()){
    		log.debug("LOS CAMBIOS SE GUARDARON CORRECTAMENTE")
    		render """{success:true}"""
    	}else{
    		log.debug("LOS CAMBIOS NO SE PUDIERON GUARDAR")
    		render """{success:false,msg:'Se produjo un error al tratar de guardar los datos'}"""
    	}
    }
    
    def delete = {
    	log.info("INGRESANDO AL METODO delete DEL CONTROLADOR ExposicionController")
    	log.debug("PARAMETROS $params")
    	def exposicionInstance = Exposicion.get(params.id)
    	if(exposicionInstance){
    		try{
    			exposicionInstance.delete(flush:true)
    			log.debug("Exposicion ELIMINADA")
    			render(contentType:"text/json"){
    				success true
    				msg "El registro se eliminó correctamente"
    			}
    		}catch(org.springframework.dao.DataIntegrityViolationException e){
    			log.debug("NO SE PUDO ELIMINAR LA Exposicion")
    			render(contentType:"text/json"){
    				success false
    				msg "Error de integridad al eliminar el registro"
    			}
    		}
    	}else{
    		log.debug("NO SE ENCONTRO LA EXPOSICION CON ID $params.id, IMPOSIBLE ELIMINAR")
    		render(contentType:"text/json"){
    			success false
    			msg "Error. Exposicion no encontrada"
    		}
    	}
    }
	
	def logopreview = {
		log.info "INGRESANDO AL METODO logopreview DEL CONTROLADOR ExposicionController"	
		log.debug "PARAMETROS $params"
		def exposicionInstance = Exposicion.get(params.id)
		def list = new ArrayList()
		if(exposicionInstance){
			log.debug("EXPOSICION ENCONTRADA")
			def pathtofile = servletContext.getRealPath("/reports/images")+"/"+exposicionInstance.nombre.trim()+".jpg"
			def reportDirPath = servletContext.getRealPath("/reports")
			if (exposicionInstance){
				FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
				if(exposicionInstance.image){
					foutput.write(exposicionInstance.image)
					foutput.flush()
				}
			}
			params.put("reportsDirPath",reportDirPath)
			params.put("logo",exposicionInstance.nombre.trim()+".jpg")	
			params.put("_format","PDF")
			params.put("_name","exposicion")
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
}
