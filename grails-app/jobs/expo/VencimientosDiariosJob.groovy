package expo

import com.rural.OrdenReserva
import java.text.SimpleDateFormat
import com.rural.OrdenReservaService


class VencimientosDiariosJob {
	def ordenReservaService
    def timeout = 24 * 60 * 60 * 1000 // execute job once in 5 seconds
	def startDelay = 60 * 1000

    def execute() {
		def orden
		//ordenReservaService.verificarVencim(orden)

		 log.debug "VERIFICANDO VENCIMIENTOS DE ORDENES DE RESERVA"
		 java.util.Date today = new java.util.Date()
		 def sdf = new SimpleDateFormat("yyyy-MM-dd")
		 
		 def ordenes = OrdenReserva.createCriteria().list(){
			 and{
				 lt("fechaVencimiento",java.sql.Date.valueOf(sdf.format(today)))
				 eq("anulada",Boolean.parseBoolean("false"))
			 }
		 }
		 log.debug "ORDENES DE RESERVA CON VECIMIENTOS: "+ordenes.size()
		 log.debug "ORDENES: $ordenes"
		
		 /*ordenes.each {
			  
		 	retorno=ordenReservaService.verificarVencimiento(it)
			log.info "SE EJECUTO EL SERVICIO: "+retorno
		 }*/
		 log.info "TERMINO LA EJECUCION"
    }
}
