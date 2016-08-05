//import com.lowagie.text.pdf.AcroFields.Item;

package com.rural
import com.rural.seguridad.Person
import com.rural.enums.TipoNotaEnum
import com.rural.enums.TipoGeneracionEnum
import com.rural.utils.Util

class ReciboException extends RuntimeException{
	String message
	Recibo recibo
	
	public ReciboException(String message, Recibo rec){
		super(message)
		this.message=message
		this.recibo=rec
	}
}


class ReciboService {

    def authenticateService

    boolean transactional = true

    Recibo generarRecibo(Long ordId,String concepto,Double efectivo,cheques,Person user) {
    		def ord = OrdenReserva.get(ordId)
    		Double saldo=0
			Double totalDebitos=0
			Double totalCreditos=0
			Double totalRecibos=0
			/*
			//-------obtengo total recibos---
    		ord.recibos.each{
    			if(it.anulado==false)
    				 totalRecibos = totalRecibos + it.total
    		}
			
			//--------obtengo total debitos y creditos----
			ord.notas.each{
				if(it.anulada==false){
					if(it.tipo == TipoNotaEnum.NOTA_CREDITO)
						totalCreditos = totalCreditos + it.total
					if(it.tipo == TipoNotaEnum.NOTA_DEBITO)
						totalDebitos = totalDebitos + it.total	
				}
			}
			*/
            saldo = ord.saldoConDescuento
    		
    		if(ord){
                //if (ord.anulada==false)
                //    throw new ReciboException('La orden está anulada y no se puede generar ningún recibo',null)
    			def recibo = new Recibo(fechaAlta:new Date(),ordenReserva:ord,efectivo:efectivo,concepto:concepto,total:0,usuario:user)
    			
    			cheques.each{
    				recibo.addToCheques(it)
    				recibo.total=recibo.total+it.importe	
    			}
    			recibo.total=recibo.total+efectivo
    			recibo.total=Math.round(recibo.total*Math.pow(10,2))/Math.pow(10,2);
                recibo.ordenReserva = ord
				
				/*totalDebitos = Math.round(totalDebitos*Math.pow(10,2))/Math.pow(10,2);
				totalCreditos = Math.round(totalCreditos*Math.pow(10,2))/Math.pow(10,2);
				totalRecibos = Math.round(totalRecibos*Math.pow(10,2))/Math.pow(10,2);
				saldo = ord.total + totalDebitos - totalCreditos - totalRecibos
				 */
				saldo = Math.round(saldo*Math.pow(10,2))/Math.pow(10,2);     
                recibo.total = Math.round(recibo.total*Math.pow(10,2))/Math.pow(10,2);
                
                def difParaNotaDC =  Math.round((ord.total-ord.totalConDescuentos)*Math.pow(10,2))/Math.pow(10,2)

				
    			if (recibo.total>saldo)
    				throw new ReciboException('El total ('+recibo.total+') de recibo supera el total ('+saldo+') pendiente de pago de la orden de reserva',recibo)
    			if(recibo.save()){
					//verificarVencimiento(ord,recibo,user)
                    //Genero nota de crédito por la diferencia del descuento
                    log.info "DIFERENCIA PARA LA NOTA DE CREDITO DE DESCUENTO: "+difParaNotaDC
                    log.info "Saldo de Orden xxx: "+saldo
                    log.info "Total del recibo yyyyyy: "+recibo.total
                    if((saldo-recibo.total<1)&& (difParaNotaDC>1)){
                        log.info "INGRESA A LA GENERACION DE LA NOTA DE CREDITO"
                        def notaDCInstance = new NotaDC()
                        notaDCInstance.usuario = authenticateService.userDomain()
                        notaDCInstance.ordenReserva = ord
                        notaDCInstance.fechaAlta = new java.sql.Date((new java.util.Date()).getTime())
                        notaDCInstance.tipoGen = TipoGeneracionEnum.TIPOGEN_AUTOMATICA
                        notaDCInstance.tipo = TipoNotaEnum.NOTA_CREDITO
                        notaDCInstance.addToDetalle(new NotadcDetalle(descripcion:"Descuento en sector por pago antes de vencimiento",subTotal:difParaNotaDC))
                        /*
		nota.ivaGral = nota.subTotal*(ordenReservaInstance.porcentajeResIns > 0 ? ordenReservaInstance.porcentajeResIns : ordenReservaInstance.porcentajeResNoIns)/100
		nota.ivaRni = nota.subTotal+nota.ivaGral
		if(ordenReservaInstance.ivaRniCheck && ordenReservaInstance.porcentajeResIns>=0)
			nota.ivaSujNoCateg=nota.ivaRni*10.5/100

                        * */
                        notaDCInstance.subTotal = 100*difParaNotaDC/121
                        notaDCInstance.ivaGral = notaDCInstance.subTotal*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
                        notaDCInstance.ivaRni = notaDCInstance.subTotal+notaDCInstance.ivaGral
                        if(ord.ivaRniCheck && ord.porcentajeResIns>=0)
                            notaDCInstance.ivaSujNoCateg=notaDCInstance.ivaRni*10.5/100


                        notaDCInstance.total = difParaNotaDC
                        def totalRedondeado = (Double)Util.redondear(notaDCInstance.total,0)
                        notaDCInstance.redondeo = totalRedondeado - notaDCInstance.total
                        notaDCInstance.redondeo = Math.round(notaDCInstance.redondeo*Math.pow(10,2))/Math.pow(10,2);
                        notaDCInstance.total = totalRedondeado

                        if(notaDCInstance.save()){
                            recibo.concepto=recibo.concepto+". Se generó nota de Credito Nro.:${notaDCInstance.numero} por descuento"
                            recibo.save()
                            log.info "Se generó nota de crédito por descuentos"
                            return recibo
                        }else
                            throw new ReciboException('El recibo no se pudo crear por un error en la generación de Nota de Crédito Automática de descuentos',recibo)
                    } else
                        return recibo
    			}else{
    				throw new ReciboException('El recibo no se pudo crear por un error de validacion',recibo)
    			}
    		}else{
    			throw new ReciboException('No se puede crear un recibo con una orden de reserva inexistente',recibo)
    		}
    }
    
    boolean anularRecibo(Long id){ 
    	def reciboInstance = Recibo.get(id)
    	if (reciboInstance){
    		reciboInstance.anulado = true
    		reciboInstance.save()
    	}else
    		throw new ReciboException("No se pudo anular el recibo. Recibo inexistente, $id",reciboInstance)
    	
    }    
	
	private void verificarVencimiento(def orden,def recibo,def user){
		def notad
		def notadDetalle 
		def today = new Date()
		def todaysql = new java.sql.Date(today.getTime())
		boolean debitoCreado = false
		
		//= new NotaDC(ordenReserva:orden,tipo:TipoNotaEnum.NOTA_DEBITO)
		log.info "INGRESANDO AL METODO PRIVADO verificarVencimiento"
		if(orden.fechaVencimiento){
			orden.notas.each{
				if(it.tipo==TipoNotaEnum.NOTA_DEBITO && it.tipoGen== TipoGeneracionEnum.TIPOGEN_AUTOMATICA )
					debitoCreado=true
			}
			if(orden.fechaVencimiento<todaysql && orden.detalle.size() && debitoCreado==false){
					notad = new NotaDC(fechaAlta:todaysql, ordenReserva:orden,tipoGen:TipoGeneracionEnum.TIPOGEN_AUTOMATICA,tipo:TipoNotaEnum.NOTA_DEBITO,monto:"0".toDouble()
						,usuario:user,subTotal:"0".toDouble(),ivaGral:"0".toDouble(),ivaRni:"0".toDouble(),ivaSujNoCateg:"0".toDouble())
					orden.detalle.each {
						if(it.subTotalsindesc-it.subTotal >0){
							 notadDetalle = new NotadcDetalle(descripcion:"Quita de Descuento del ${it.sector.porcentaje} por Sector ${it.sector.nombre}",subTotal:it.subTotalsindesc-it.subTotal)
							 notad.addToDetalle(notadDetalle)
							 notad.subTotal = notad.subTotal + notadDetalle.subTotal
						}
					}
					notad.ivaGral =  notad.subTotal*(orden.porcentajeResIns > 0 ? orden.porcentajeResIns : orden.porcentajeResNoIns)/100
					if(orden.ivaRniCheck)
						notad.ivaSujNoCateg=notad.ivaRni*10.5/100
					notad.total=notad.subTotal+notad.ivaGral+notad.ivaSujNoCateg
					notad.total=Math.round(notad.total*Math.pow(10,2))/Math.pow(10,2)
					if(notad.subTotal>0){
						if(notad.save()){
							orden.addToNotas(notad)
							if(orden.save())
								log.info "SE GENERO UNA NOTA DE DEBITO de ${notad.total} PARA LA ORDEN CON ID: ${orden.id}"
							else
								throw new ReciboException("Se produjo un error al agregar una nota como detalle de la orden de reserva",recibo)	
						}else{
							log.info "ERRORES DE RECIBO: "+notad.errors.allErrors
							throw new ReciboException("Se produjo un error al generar una nota de débito, operación cancelada",recibo)
						}
					}
			}
		}
		log.info "VERIFICACION CONCLUIDA CORRECTAMENTE"
	}
    
}
