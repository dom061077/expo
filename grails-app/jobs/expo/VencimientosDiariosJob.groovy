package expo

import com.rural.DetalleServicioContratadoDescuentos
import java.text.SimpleDateFormat
import com.rural.OrdenReservaService


class VencimientosDiariosJob {
	def ordenReservaService
    def timeout = 24 * 60 * 60 * 1000 // execute job once in 5 seconds
	def startDelay = 30 * 1000

    def execute() {
/*
		 log.debug "VERIFICANDO VENCIMIENTOS DE DESCUENTOS DE ORDENES DE RESERVA"
		 java.util.Date today = new java.util.Date()
		 def sdf = new SimpleDateFormat("yyyy-MM-dd")
		 
		 def descuentosVencidos = DetalleServicioContratadoDescuentos.createCriteria().list(){
			 and{
				 lt("fechaVencimiento",java.sql.Date.valueOf(sdf.format(today)))
				 detalleServicioContratado{
					 ordenReserva{
						 eq("anulada",Boolean.parseBoolean("false"))
					 }
				 }
				 gt("porcentaje","0".toDouble())
				 gt("subTotal","0".toDouble())
				 or{
					 isNull("notadcDetalle")
					 notadcDetalle{
						 notadc{
							 eq("anulada",Boolean.parseBoolean("true"))
						 }
					 }
				 }
			 }
		 }
		 log.info "ORDENES DE RESERVA CON VECIMIENTOS: "+descuentosVencidos.size()

		
		 descuentosVencidos.each {
			  
		 	ordenReservaService.verificarVencimiento(it)
			log.info "SE EJECUTO EL SERVICIO... "
		 }
		 log.info "TERMINO LA EJECUCION"
		 */
    }
}
