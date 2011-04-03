package com.rural

import com.rural.utils.N2t
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.Date
import jxl.*
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableWorkbook
import jxl.write.DateFormat
import jxl.write.DateTime
import jxl.write.WritableCellFormat
import jxl.write.WritableSheet



class ReciboController {
	def reciboService
    def index = { redirect(action:list,params:params) }
	def authenticateService
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
        return ['reciboInstance':reciboInstance,ordenreservaId:params.ordenreservaId]
    }

	def reporte = {
		log.info("INGREANDO AL METODO reporte DEL CONTROLADOR ReciboController")
		log.debug("PARAMETROS: $params")
		def recibo = Recibo.get(new Long(params.id))
		recibo.cheques.each{
			log.debug("Numero del cheque: "+it.numero)
			log.debug("Vencimiento del cheque: "+it.vencimiento)
		}
		log.debug("DETALLE DE ORDEN DE RESERVA")
		recibo.ordenReserva.detalle.each{
			log.debug(it.lote?.sector?.nombre)
			log.debug(it.sector?.nombre)
		}
		recibo.ordenReserva.otrosconceptos.each{
			log.debug(it.descripcion)
		}
		log.debug(recibo.usuario.userRealName)

		//-------recuperacion de desde la instancia de logo correspondiente a la exposicion y al año de la Orden
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
	
	
    def createjson = {
		log.info("INGRESANDO AL METODO createjson DEL CONTROLADOR ReciboController")
		log.debug("PARAMETROS: $params")
		def chequesjson = grails.converters.JSON.parse(params.chequesjson)
		def cheques = [] 
		Date fechaVence 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
		chequesjson.each{
			log.debug(it)
			log.debug("Vencimiento: "+it.vencimiento)
			try{
				fechaVence = df.parse(it.vencimiento.substring(0,10))
				log.debug("Fecha convertida correctamente "+it.vencimiento.substring(0,10))
			}catch(ParseException e){
				log.debug("Error al convertir la fecha")
			} 
			
			log.debug("Vence convertido: "+fechaVence.toString())
			cheques.add(new Cheque(numero:it.numero,banco:it.banco,importe:it.importe,vencimiento:fechaVence))
		}
		
		Double efectivo = new Double((params.efectivo)) 
		
		def recibo = reciboService.generarRecibo(new Long(params.ordenreservaid),params.concepto,efectivo,cheques,authenticateService.userDomain())
		int entero = recibo.total.intValue()
		Double totalaux = (recibo.total - entero)*100
		int decimal = totalaux.intValue()
		log.debug("XXXXXXXXXXXXXXXXXXXXXXXANTES DE CREAR EL CONVERTIDOR DE NUMEROS A LETRAS")
		N2t num2letra = new N2t()
		log.debug("DESPUES DE CREAR EL CONVERTIDOR DE NUMEROS A LETRAS")
		//String totalenletras=num2letra.convertirLetras(entero)+" PESOS CON "+((num2letra.convertirLetras(0)).trim()=="" ? "CERO" : num2letra.convertirLetras(decimal))+" CENTAVOS"
		String totalenletras=num2letra.convertirLetras(entero)+" CON "+decimal+"/100"
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
	
	def listjson = {
		log.info("INGRESANDO AL METODO listjson DEL CONTROLADOR ReciboController")
		log.debug("PARAMETROS $params")
		def totalRecibos=0
		def recibos=null
		def pagingconfig = [
    		max: params.limit as Integer ?:10,
    		offset: params.start as Integer ?:0
    	]
    	
		
		recibos = Recibo.createCriteria().list(){
			and{
				if(params.fieldSearch=='empresa.nombre'){
					ordenReserva{ 
						empresa{
							like('nombre','%'+params.searchCriteria+'%')
						}
					}
				}
				if(params.fieldSearch=='numero'){
						eq('numero',new Long(params.searchCriteria))
				}
				eq('anulado',Boolean.parseBoolean(params.anulada))
			}
			ordenReserva{
				if(params.sort=="nombre"){
						empresa{
							order("nombre",params.dir.toLowerCase())
						}
				}
				if(params.sort=="numeroordenreserva"){
						order("numero",params.dir.toLowerCase())
				}
			}
			if(params.sort=="fechaAlta"){
				order("fechaAlta",params.dir.toLowerCase())
			}
			if(params.sort=="numero"){
				order("numero",params.dir.toLowerCase())
			}
		} 		
		
		log.debug("Cantidad de recibos consultados: $totalRecibos")
		int entero
		Double totalaux
		int decimal
		N2t num2letra = new N2t()
		String totalenletras=""
		
		render(contentType:"text/json"){
			total	totalRecibos
			rows{
				recibos.each{
					entero = it.total.intValue()
					totalaux = (it.total - entero)*100
					decimal = totalaux.intValue()
					
					totalenletras= num2letra.convertirLetras(entero)+" CON "+decimal+"/100"
					totalenletras = totalenletras.toUpperCase()
					row(id:it.id,fechaAlta:it.fechaAlta,nombre:it.ordenReserva.empresa.nombre,numero:it.numero,total:it.total,numeroordenreserva:it.ordenReserva.numero,totalletras:totalenletras)
				}
			}
		}
	}
	
	def anularrecibo = {
		log.info("INGRESANDO EL METODO anularrecibo DEL CONTROLLER ReciboController" )
		log.debug("PARAMETROS: $params")
		try{
			reciboService.anularRecibo(new Long(params.id))
			render(contentType:"text/json"){
				success true
				
			}
			
		}catch(ReciboException e){
			render(contentType:"text/json"){
				success false
				msg e.message
			}
		}
		
	}
	
	def excel = {
		log.info("INGRESANDO AL METODO excel DEL CONTROLLER ReciboController")
		log.debug("PARAMETROS: $params")
	     response.setHeader("Content-disposition", "attachment")
	     response.contentType = "application/vnd.ms-excel"		
		def recibos = Recibo.createCriteria().list(){
			and{
				if(params.fieldSearch=='empresa.nombre'){
					ordenReserva{ 
						empresa{
							like('nombre','%'+params.searchCriteria+'%')
						}
					}
				}
				if(params.fieldSearch=='numero'){
						eq('numero',new Long(params.searchCriteria))
				}
				eq('anulado',Boolean.parseBoolean(params.anulada))
			}
			ordenReserva{
				if(params.sort=="nombre"){
						empresa{
							order("nombre",params.dir.toLowerCase())
						}
				}
				if(params.sort=="numeroordenreserva"){
						order("numero",params.dir.toLowerCase())
				}
			}
			if(params.sort=="fechaAlta"){
				order("fechaAlta",params.dir.toLowerCase())
			}
			if(params.sort=="numero"){
				order("numero",params.dir.toLowerCase())
			}
		} 		

		def workbook = Workbook.createWorkbook(response.outputStream)
    	 def sheet = workbook.createSheet("Request",0)
		 DateFormat customDateFormat = new DateFormat ("d/m/yy h:mm") 
		 WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat)  	     
	 	 boolean falgdetalle=false
	 	 def fil=0
	 	 if(Boolean.parseBoolean(params.anulada)){
	 		 sheet.addCell(new Label(0,fil,"SOLO RECIBOS ANULADOS"))
	 		 fil=fil+1
	 	 }
	 	 sheet.addCell(new Label(0, fil, "Empresa"))
	 	 sheet.addCell(new Label(1, fil, "Fecha Alta"))
	 	 sheet.addCell(new Label(2, fil, "Nro.Recibo"))
	 	 sheet.addCell(new Label(3, fil, "Total"))	
	 	 sheet.addCell(new Label(4, fil, "Nro.Orden de Reserva"))
	 	 fil=fil+1
	 	 recibos.each{
	 		 sheet.addCell(new Label(0,fil,it.ordenReserva.empresa.nombre))
	 		 sheet.addCell(new DateTime(1,fil,it.fechaAlta,dateFormat))
	 		 sheet.addCell(new Number(2,fil,it.numero))
	 		 sheet.addCell(new Number(3,fil,it.total))
	 		 sheet.addCell(new Number(4,fil,it.ordenReserva.numero))
	 		 fil=fil+1
	 	 }
		 
      	 workbook.write()  
	     workbook.close()  		
	}
	
}


/*------------------CONVERTIDOR DE NUMERO EN LETRAS------------------------------*/










