package com.rural

import grails.converters.JSON
import jxl.*
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableWorkbook
import jxl.write.WritableSheet
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.ByteArrayOutputStream

class EmpresaController {
    
    def index = { redirect(action:list,params:params) }
    def sessionFactory
    def authenticateService

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
    
    def search = {
    	log.info("INGRESANDO AL METODO search de EmpresaController")
    	log.debug("Parametros: "+params)
    	render(contentType:'text/json'){
    		success true
    		if(params.searchCriteria)
    			searchCriteria params.searchCriteria
    	}
    }

    def listjson = {
    	log.info("INGRESANDO AL METODO listjson de EmpresaController")
    	log.debug("Parametros: "+params)
	    def pagingConfig = [
	            max: params.limit as Integer ?: 10,
	            offset: params.start as Integer ?: 0
	    ]
    	def totalEmpresas = Empresa.createCriteria().count{
    			like('nombre','%'+params.searchCriteria+'%')
    	}
    	def empresas = Empresa.createCriteria().list(pagingConfig){
    		like('nombre','%'+params.searchCriteria+'%')
    	}
    	
    			
    	log.debug("Cantidad de Empresas consultadas: "+Empresa.count())
    	render(contentType:'text/json'){
    		total totalEmpresas
    		rows{
    			empresas.each{
    				row(id:it.id,nombre:it.nombre,nombreRepresentante:it.nombreRepresentante,telefono1:it.telefono1)
    			}
    			
    		}
    	}
    }
    
    def list = {
    	log.info("INGRESANDO AL METODO list DE EmpresaController")
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ empresaInstanceList: Empresa.list( params ), empresaInstanceTotal: Empresa.count() ]
    }

    def show = {
       	log.info("INGRESANDO AL METODO show DE EmpresaController")
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ empresaInstance : empresaInstance ] }
    }

    def delete = {
      	log.info("INGRESANDO AL METODO delete DE EmpresaController")
      	log.info("PARAMETROS: "+params)
        def empresaInstance = Empresa.get( params.id )
        def errorList
        if(empresaInstance) {
            try {
            	empresaInstance.exposaparticipar.each{
            		it.delete()
            	}
                empresaInstance.delete()

                log.info("EMPRESA CON ID: "+params.id+" ELIMINADA")
                //flash.message = "Empresa ${params.id} deleted"
                //redirect(action:list)
                render(contentType:"text/json"){
                	respuesta(success:true,title:"El registro se eliminÃ³ correctamente")
                }
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
            	log.info("ERROR DE INTEGRIDAD AL TRATAR DE ELIMINAR LA EMPRESA CON ID: "+params.id)
                //flash.message = "Empresa ${params.id} could not be deleted"
                //redirect(action:show,id:params.id)
                render(contentType:"text/json"){
                	success false
                	title "No se puede eliminar la empresa porque esta referenciada por otros datos"
                }
            }
        }
        else {
            //flash.message = "Empresa not found with id ${params.id}"
            //redirect(action:list)
            log.info("Empresa con id ${params.id} no encontrada")
            render(contentType:"text/json"){
            	respuesta(success:false,
            		title:"Empresa con id ${params.id} no encontrada")
            }
        }
    }

    def editempresajson = {
    	log.info("INGRESANDO AL METODO editJson DE EMPRESACONTROLLER")
    	log.debug("Params: "+params)
    	def empresaInstance = Empresa.get(params.id)
    	log.debug("nombre de la localidad: "+empresaInstance.localidad?.nombreLoc)
    	render(contentType:'text/json'){
    		success true
    		data(id:empresaInstance.id,nombre:empresaInstance.nombre
    			 ,nombreRepresentante:empresaInstance.nombreRepresentante
    			 ,telefono1:empresaInstance.telefono1
    			 ,telefono2:empresaInstance.telefono2
    			 ,telefonoRepresentante1:empresaInstance.telefonoRepresentante1
    			 ,telefonoRepresentante2:empresaInstance.telefonoRepresentante2
    			 ,telefonoRepresentante3:empresaInstance.telefonoRepresentante3
    			 ,cuit:empresaInstance.cuit
    			 ,direccion:empresaInstance.direccion
    			 ,provinciaLn:empresaInstance.provinciaFiscal
    			 ,localidadAux: empresaInstance.localidadFiscal
    			 ,vendedorId: empresaInstance.vendedor?.id
    			 ,vendedor:empresaInstance.vendedor?.nombre
    			 ,rubro:empresaInstance.subrubro?.rubro?.nombreRubro
    			 ,rubroId:empresaInstance.subrubro?.rubro?.id
    			 ,subrubro:empresaInstance.subrubro?.nombreSubrubro
    			 ,subrubroId: empresaInstance.subrubro?.id
    			)
    	}
    	
    }
    
    def edit = {
    	log.info("INGRESANDO AL METODO edit DE EmpresaController")    
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ id: params.id ]
        }
    }

    def update = {
    	log.info("INGRESANDO AL METODO update DE EmpresaController")
    	log.debug("Parametros: ${params}")
    	log.debug("Params.id: "+params.id)
        def empresaInstance = Empresa.get( params.id )
        //def expos = JSON.parse(params.exposempresajson)
        def exposaparticipar = JSON.parse(params.exposaparticiparjson)
        log.debug("EXPOS A PARTICIPAR JSON: "+params.exposaparticiparjson)
        def empIterator = null
        def exposJsonIterator = null
        def isnew
        def isdeleted
        def expoJson=null
        def e = null
        def errorList = null
        //aqui determino las exposaparticpar que se van a agregar

        exposaparticipar.each{
    		empIterator = empresaInstance.exposaparticipar.iterator()
    		e=null
    		isnew=true
    		while(empIterator.hasNext()){
    			e=empIterator.next()
    			if(e.id==it.id) isnew=false
    		}
    		if (isnew){
    			empresaInstance.addToExposaparticipar(Exposicion.get(it.id))
    			log.debug "SE AGREGO UNA EXPOSICION A LA EMPRESA EN exposaparticipar"
    		}
    	}
    	//aqui determino las exposaparticipar que se van a eliminar
    	
    	empIterator = empresaInstance.exposaparticipar.iterator()
    	while(empIterator.hasNext()){
    		e=empIterator.next()
    		exposJsonIterator = exposaparticipar.iterator()
    		expoJson=null
    		isdeleted=true
	    	while (exposJsonIterator.hasNext()){
	    		expoJson=exposJsonIterator.next()
	    		log.debug ("Id de expo JSON: "+expoJson.id+" Id de expo Empresa (expo a participar) "+ e.id)
	    		if((expoJson.id.toString()).compareTo(e.id.toString())==0){
	    			isdeleted=false
	    			log.debug("Para NO borrar (exposaparticipar) "+e)
	    		}
	    	}
    		if(isdeleted)
    			empIterator.remove()
    	}
    	//-----------fin logica para agregar y eliminar expos en que se participó y en las que se participará

    	
        if(empresaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(empresaInstance.version > version) {
                    
                    empresaInstance.errors.rejectValue("version", "empresa.optimistic.locking.failure", "Another user has updated this Empresa while you were editing.")
                    render(view:'edit',model:[empresaInstance:empresaInstance])
                    return
                }
            }
            empresaInstance.properties = params
            if(!empresaInstance.hasErrors() && empresaInstance.save()) {
                //flash.message = "Empresa ${params.id} updated"
                //redirect(action:show,id:empresaInstance.id)
                log.info("Intancia de empresa guardada, renderizando json")
                render(contentType:"text/json"){
                	success true
                }
            }
            else {
                //render(view:'edit',model:[empresaInstance:empresaInstance])
            	log.info("Error de validacion en Empresa")
            	empresaInstance.errors.allErrors.each{error->
            		error.codes.each{
            			if(g.message(code:it)!=it)
            				errorList.add(g.message(code:it))
            		}
            	}

                render(contentType:"text/json"){
                	success false
                	errorList.each{
                		errors{error(title:it)}
                	}
                } 
                
            }
        }
        else {
            //flash.message = "Empresa not found with id ${params.id}"
            log.info("Empresa no encontrada con id ${params.id}")
            render(contentType:'text/json'){
            	success false
            	errors{error(title:"Empresa con id ${params.id} no encontrada")}
            }
        }
    }

    def create = {
    	log.info "INGRESANDO AL METODO CREATE DE EmpresaController"
        def empresaInstance = new Empresa()
        empresaInstance.properties = params
        return ['empresaInstance':empresaInstance]
    }

    def save = {
    	log.info ("INGRESANDO AL METODO SAVE DE EmpresaController")

        def empresaInstance = new Empresa(params)
        
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            flash.message = "Empresa ${empresaInstance.id} created"
            redirect(action:show,id:empresaInstance.id)
        }
        else {
            render(view:'create',model:[empresaInstance:empresaInstance])
        }
    }
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DE EmpresaController")
    	log.debug("Parametros JSON: "+params)
    	log.debug("Usuario: "+authenticateService.userDomain().id)
        def empresaInstance = new Empresa(params)
        empresaInstance.usuario=authenticateService.userDomain()
        empresaInstance.fechaAlta= new Date()
    	log.debug("Valor de Instancia Empresa antes de salvar: "+empresaInstance)
		def expos = JSON.parse(params.exposempresajson)
		def exposaparticipar = JSON.parse(params.exposaparticiparjson)
		log.debug("Exposiciones cargadas: "+expos)
		log.debug("Exposiciones en las que puede participar: "+exposaparticipar)
		Exposicion expo
    	expos.each{
			log.debug("Cada item de las expos en json es: "+it)
			expo = Exposicion.get(it.id)
			empresaInstance.addToExpos(expo)
		}
    	
    	exposaparticipar.each{
    		log.debug("Cada item de las exposaparticiparjson es: "+it)
    		expo = Exposicion.get(it.id)
    		empresaInstance.addToExposaparticipar(expo)
    	}

		
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            render(contentType:"text/json") {
					success true
					idEmpresa empresaInstance.id
				}
        }
        else {
        	empresaInstance.errors.allErrors.each{
        		it.arguments.each{arg->
        			log.debug("Argumento: "+arg)
        		}
        		it.codes.each{cod->
        			log.debug("Codigos: "+cod)
        		}
        	}
            render(contentType:"text/json") {
					success false
					errors {
						empresaInstance.errors.allErrors.each {
							 title it.defaultMessage	
							 }
					}
				}
            
        }
    
    }
    
    def listexpos = {
    	log.info("INGRESANDO AL METODO listexpos DE EmpresaController")
    	log.debug("PARAMETROS: "+params)
    	
    	
    	def empresa = Empresa.get(params.id)
    	
    	render(contentType:"text/json"){
    		total empresa.expos.count()
    		rows{
    			empresa.expos.each{
    				row(id:it.id,nombre:it.nombre,isnew:false)
    			}
    		}
    	}
    	
    }

    def listexposaparticipar = {
    	log.info("INGRESANDO AL METODO listexposaparticipar DE EmpresaController")
    	log.debug("PARAMETROS: "+params)
    	
    	def empresa = Empresa.get(params.id)
    	render(contentType:"text/json"){
    		total empresa.exposaparticipar.size()
    		rows{
    			empresa.exposaparticipar.each{
    				row(id:it.id,nombre:it.nombre)
    				
    			}
    		}
    	}
    }

    def listempresassimilares = {
    	log.info("INGRESANDO AL METODO listempresassimilares DE EmpresaController")
    	log.debug("PARAMETROS: "+params)
    	
    	def empresassimilares = Empresa.get(params.id).empresas
    	render(contentType: "text/json"){
    		total empresassimilares.size()
    		rows{
    			empresassimilares.each{
    				row(id:it.id,nombre:it.nombre)
    			}
    		}
    	}
    }
    
    def listempresasconsimilitudes = {
    	log.info("INGRESANDO AL METODO listempresasconsimilares DE EMPRESACONTROLLER")
    	
		def empresas = Empresa.createCriteria().list{
		    or{
		         eq('totalsimilares',0)   
		         isNull('totalsimilares')
		    }
		}
    	
    	render(contentType: "text/json"){
    		total empresas.size()
    		rows{
    			empresas.each{
    				row(id:it.id,nombre:it.nombre)
    			}
    		}
    	}
    }
    
    def empresassimilares = {
    	log.info("INGRESANDO AL METODO empresassimilares DE EMPRESACONTROLLER")
    	log.debug("ESTE ACTION ES SIMPLEMENTE UN REDIRECT A LA VISTA empresassimilares.gsp")
    }
    
    //****************************métodos para el manejo inserción a partir de archivos excel********
    def uploadtest = {
    	log.info("INGRESANDO AL METODO uploadtest DELCONTROLADOR EmpresaController")
    	render(contentType:"text/json"){
    		success true
    	}
    }
    
    def upload  = {
    	log.info("INGRESANDO AL METODO upload DEL CONTROLADOR EmpresaController")
	    MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;  
		CommonsMultipartFile fileExcel = (CommonsMultipartFile) mpr.getFile("archivoExcel");
  	    ByteArrayOutputStream fileOutputExcel = new ByteArrayOutputStream()
  	    
		  // create our workbook
		  try{  
			  Workbook workbook = Workbook.getWorkbook(fileExcel.inputStream)
			  WritableWorkbook workbookCopiado = Workbook.createWorkbook(fileOutputExcel,workbook)
			  WritableSheet sheet = workbookCopiado.getSheet(0)
			  //Sheet sheetCopiado = workbookCopiado.getSheet(0)
			  log.info("LA LECTURA Y APERTURA DEL ARCHIVO EXCEL ES CORRECTO. NOMBRE DEL ARCHIVO: ${fileExcel.name}")
			  def cuit = null
			  def nombre = null
			  def rubro = null
			  def subrubro = null
			  def vendedor = null
			  
			  def empresasinsertadas=0
			  def empresassimilares = null
			  def token = ""
			  
			  StringTokenizer tokenizernombre=null
			  session.setAttribute("progressMapSave",["total":sheet.rows,"salvados":0])
			  Empresa empresa
			  int cantErrores = 0
			  //el archivo tendrá un fila con los nombres de columna por eso comienzo a leer desde la fila 1
			  for(int r = 1; r < sheet.rows; r++){
			  	subrubro=SubRubro.findByNombreSubrubro(sheet.getCell(1,r).contents)
				if (subrubro==null && sheet.getCell(1,r).contents!=""){
					log.debug("NO SE ECONTRO EL SUBRUBRO "+sheet.getCell(1,r).contents)
					rubro = new Rubro(nombreRubro:sheet.getCell(1,r).contents)
					rubro=rubro.save()
					subrubro = new SubRubro(nombreSubrubro:sheet.getCell(1,r).contents,rubro:rubro)
					subrubro=subrubro.save()
					log.debug("RUBRO y SUBRUBRO "+sheet.getCell(1,r).contents+" INSERTADO")
				}
				vendedor = Vendedor.findByNombre(sheet.getCell(12,r).contents)
				if (vendedor==null && sheet.getCell(12,r).contents!=""){
					log.debug("NO SE ENCONTRO EL VENDEDOR "+sheet.getCell(12,r).contents)
					vendedor = new Vendedor(nombre:sheet.getCell(12,r).contents)
					vendedor.save()
					log.debug("VENDEDOR "+sheet.getCell(12,r).contents+" INSERTADO")
				}
				log.debug("Valores de la planilla excel :"
						+sheet.getCell(0,r).contents+","+
						sheet.getCell(1,r).contents+","+
						sheet.getCell(2,r).contents+","+
						sheet.getCell(3,r).contents						
				)	
				
				empresa = new Empresa(fechaAlta:new Date()
						 ,nombre:sheet.getCell(0, r).contents
						 ,subrubro:subrubro
						 ,nombreRepresentante:sheet.getCell(2,r).contents
						 ,telefonoRepresentante1:sheet.getCell(3,r).contents
						 ,telefonoRepresentante12:sheet.getCell(4,r).contents
						 ,direccion:sheet.getCell(5,r).contents
						 ,provinciaFiscal:sheet.getCell(6,r).contents
						 ,localidadFiscal:sheet.getCell(7,r).contents
						 ,codigoPostal:sheet.getCell(8,r).contents
						 ,email:sheet.getCell(9,r).contents
						 ,sitioWeb:sheet.getCell(10,r).contents
						 ,observaciones:sheet.getCell(11,r).contents
						 ,vendedor:vendedor
						 ,usuario:authenticateService.userDomain()
				)
				//sheetCopiado.addCell(new Label(0,r,sheet.getCell(0,r).contents))
				//sheetCopiado.addCell(new Label(1,r,sheet.getCell(1,r).contents))
				empresa.validate()
				if(!empresa.hasErrors()){
					
					if (empresa.nombre.trim().equals("")){
						log.debug("ERROR DE CARGA, EMPRESA NO PUEDE TENER UN NOMBRE VACIO")
						sheet.addCell(new Label(13,r,"EMPRESA NO PUEDE TENER UN NOMBRE VACIO"))
						cantErrores++
					}else{
						tokenizernombre = new StringTokenizer(empresa.nombre)
						while(tokenizernombre.hasMoreTokens()){
							token = tokenizernombre.nextToken()
							if(token.toUpperCase()!="S.A" && token.toUpperCase()!="S.R.L"
								&&	token.length()>3 && token.toUpperCase()!="AGRO"
								&&  token!="S.A.")
							empresassimilares = Empresa.createCriteria().list{
								like('nombre',"%"+token+"%")
							}
							log.debug("SE ENCONTRARON "+empresassimilares?.size()+" SIMILARES PARA $empresa.nombre")
							if(empresassimilares){
								empresassimilares.each{
									it.token=token
									it.save()
									empresa.addToEmpresas(it)
								}
								empresa.totalsimilares=empresassimilares.size()
								log.debug("EMPRESAS SIMILARES: "+empresa.empresas)
							}
						}
						//if (Empresa.findByNombre(empresa.nombre)){
						//	log.debug("ERROR DE CARGA, EMPRESA YA EXISTE ")
						//	log.debug("VALORES DE INSERCION: "+empresa.nombre+" "+empresa.cuit )
						//	sheet.addCell(new Label(13,r,"YA EXISTE UNA EMPRESA CON EL MISMO NOMBRE"))
						//	cantErrores++
						//}else{	
							empresa.save()
							empresasinsertadas++
							log.debug("EMPRESA SALVADA")
							
						//}
					}
				}else{
					log.debug("ERRORES DE VALIDACION: $empresa.errors.allErrors")
					log.debug("VALORES DE INSERCION CON ERRORES: "+empresa.nombre+" "+empresa.cuit )
					def erroresstr=""
					def errorList = []
					empresa.errors.allErrors.each{error->
						error.codes.each{
							if(g.message(code:it)!=it)
								errorList.add(g.message(code:it))
						}
					}
					errorList.each{
						erroresstr=erroresstr+it+" "
						cantErrores++		
					}
					sheet.addCell(new Label(13,r,erroresstr))
					
				}		
			 }
			  	//session.setAttribute("progressMapSave",["total":sheet.rows,"salvados":r+1,"success":true])
			  	
			  		
			 workbookCopiado.write()
			 workbookCopiado.close()
			 log.debug("SALVANDO EMPRESA EN TRANSACCION")
			 
			 def cargaExcelInstance = new CargaExcel(fechaCarga:new Date(),nombreArchivo:fileExcel.name,archivo:fileOutputExcel.toByteArray())
			 cargaExcelInstance.save()
			 session.setAttribute("cargaexcelId",cargaExcelInstance.id)			 
			 if (cantErrores>0 || empresasinsertadas==0){
				 if(empresasinsertadas==0){
					 log.debug("NO SE INSERTO NINGUNA EMPRESA, RENDERIZANDO MENSAJE DE ERROR")
					 render """{success:false, responseText:"Se produjo algun problema con el archivo excel, ninguna linea fue guardada",idcargaexcel:$cargaExcelInstance.id}"""
				 }
				 else{
					 log.debug("ARCHIVO EXCEL PROCESADO CORRECTAMENTE PERO CON ALGUNOS ERRORES")
					 render """{success:true, responseText:"LA LECTURA Y APERTURA DEL ARCHIVO EXCEL ES CORRECTA", cantErrores:$cantErrores, idcargaexcel:$cargaExcelInstance.id}"""
				 }
			 }
			 else{
				 log.debug("ARCHIVO EXCEL PROCESADO CORRECTAMENTE Y SIN ERRORES")
				 render """{success:true, responseText:"LA LECTURA Y APERTURA DEL ARCHIVO EXCEL ES CORRECTA"}"""
			 }
  		  }catch(jxl.read.biff.BiffException ioe){
		   	 log.info("FALLO LA LECTURA Y APERTURA DEL ARCHIVO EXCEL")
		   	 render """{success:false, responseText:"FALLO LA LECTURA Y APERTURA DEL ARCHIVO EXCEL"}"""
		   	//recordar que para los taskrunner tengo que renderizar json de esta formarender """{success:false, responseText:"FALLO LA LECTURA Y APERTURA DEL ARCHIVO EXCEL"}"""
		   	 
		  }      	
    	
    }
    
    
    
    def uploadedFile = {
    	log.info("INGRESANDO AL METODO uploadedFile DEL CONTROLADOR EmpresaController")
    	
    }
    
    def uploadFile = {
    	log.info("INGRESANDO AL METODO uploadFile DEL CONTROLADOR EmpresaController")
    	log.debug("PARAMETROS: "+params)
    	
    }
    
    def uploadInfo = {
    		log.info("INGRESANDO AL METODO uploadInfo DEL CONTROLLADOR EmpresaController")
            def progressMap = session.getAttribute("progressMap")
            def progressStatus = session.getAttribute("progressStatus")
            def progressMapSave = session.getAttribute("progressMapSave")
            log.debug("CONTENIDO DE progressMapSave: "+progressMapSave)
            
            if (!progressMap) {
                render("No ProgressMap you Dweeb(tm)!")
            }
            
            //if we don't have progress info in the session, it could
            //indicate that the file upload was to small to require streaming
            //and possibly finished before we could check progress
            if (progressMap?.bytesRead == null){
                render(builder:'json'){
        		    bytesRead(1 )   
        			totalSize(1 )
        			status(AjaxProgressListener.STATUS_DONE)
        		}   
            }
                
            
            //Aahh.. JSON builders how I love thee :)
            def totalFilas = 0
            def filasSalvadas = 0
            if(progressMapSave){ 
            	totalFilas = progressMapSave['total']
            	filasSalvadas = progressMapSave['salvados']
            }
    		render(builder:'json'){
    		    bytesRead(progressMap['bytesRead'] )   
    			totalSize(progressMap['length'] )
    			status(progressStatus)
    			total(totalFilas)
    			salvados(filasSalvadas)
    		}
    
    }
    
    def downloadfileerrors = {
    	log.info("INGRESANDO AL METODO downloadfileerrors DEL CONTROLADOR EmpresaController")
    	log.debug("PARAMETROS DE INGRESO: "+params)
    	def cargaexcelId = session.getAttribute("cargaexcelId")
    	
    	log.debug("ID DE CARGA EXCEL: "+cargaexcelId)
    	def cargaexcel = CargaExcel.get(cargaexcelId)
    	response.outputStream << cargaexcel.archivo
    	 
    	def header = [:]  
    	header.id = "Id"  
    	header.investigator = "Investigator"  
    	header.hole = "Hole"  
    	header.top = "Interval Top (mbsf)"  
    	header.bottom = "Interval Bottom (mbsf)"  
    	header.samplesRequested = "Samples Requested"  
    	header.sampleSpacing = "Sample Spacing (m)"  
    	header.sampleType = "Volume/Type"  
    	header.sampleGroup = "Group/Discipline"  
    	header.notes = "Notes"  
    	header.status = "Status"  
    	header.priority = "Priority"
    	response.setHeader("Content-disposition", "attachment; filename=errores-requests.xls")  
    	response.contentType = "application/vnd.ms-excel"
    	
    	
    }
    
    static def writeExcel(out,map,objects){
	    	def workbook = Workbook.createWorkbook(out)
	    	def sheet = workbook.createSheet("Request",0)
	      // walk through our map and write out the headers  
	      def c = 0  
	      map.each() { k, v ->  
	          // write out our header  
	          sheet.addCell(new Label(c, 0, v.toString()))  
	        
	          // write out the value for each object  
	          def r = 1  
	          objects.each() { o ->  
	              if (o[k] != null) {  
	                  if (o[k] instanceof java.lang.Number) {  
	                      sheet.addCell(new Number(c, r, o[k]))  
	                  } else {  
	                      sheet.addCell(new Label(c, r, o[k].toString()))  
	                  }  
	              }  
	              r++  
	          }  
	          c++  
	      }  
	    
	      // close  
	      workbook.write()  
	      workbook.close()    	    	
    }

    def exportempresastoexcel = {
    	log.info("INGRESANDO AL METODO exportempresastoexcel DEL CONTROLADOR EmpresaController")
    	log.debug("PARAMETROS DE INGRESO: "+params)
    	def user=authenticateService.userDomain()
    	def empresas = Empresa.createCriteria().list(){like('nombre','%'+params.searchCriteria+'%')}
    	
	     // set our header and content type
	     response.setHeader("Content-disposition", "attachment; filename=${user}-requests.xls")
	     response.contentType = "application/vnd.ms-excel"
	 
	     // define our header map
	     def header = [:]
	     header.cuit = "C.U.I.T"
	     header.nombre = "Nombre"
	     /*header.top = "Interval Top (mbsf)"
	     header.bottom = "Interval Bottom (mbsf)"
	     header.samplesRequested = "Samples Requested"
	     header.sampleSpacing = "Sample Spacing (m)"
	     header.sampleType = "Volume/Type"
	     header.sampleGroup = "Group/Discipline"
	     header.notes = "Notes"*/
	 
	     writeExcel(response.outputStream, header, empresas)    	
    	
    }
    
    
}
