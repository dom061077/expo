package com.rural

import com.rural.enums.TipoNotaEnum
import com.rural.enums.TipoGeneracionEnum
import com.rural.seguridad.Person

import java.sql.Date

class NotaDC {
	static auditable = true
	
	Integer puntoVenta
	Person usuario
	Long numero
	TipoNotaEnum tipo
	TipoGeneracionEnum tipoGen
	OrdenReserva ordenReserva
	Double total
	Date fechaAlta
	Boolean anulada = false
	
	Double subTotal=0
	Double ivaGral=0
	Double ivaRni=0/*es el resultado subtotal neto + ivaGral*/
	Double ivaSujNoCateg=0 /*es mayor a cero cuando la condicion de IVA es ivaRniCheck true*/
        Double redondeo=0
	static belongsTo = [ordenReserva:OrdenReserva]
	static hasMany = [detalle:NotadcDetalle]
    static constraints = {
		tipo(blank:true,nullable:true)
		total(blank:true,nullable:true)
		numero(blank:true,nullable:true)
		usuario(blank:true,nullable:true)
		puntoVenta(blank:true,nullable:true)
    }
	def sigNumero(){
		/*def c = NotaDC.createCriteria()
		def lastNum = c.get{
			projections{
				max("numero")
			}
			
		}
		return lastNum ? lastNum+1 : 1
		    */
		def max = NotaDC.executeQuery("select max(numero)+1 from NotaDC n where  n.tipo=?",[tipo])[0]
		if (max == null) {
			max = 1
		}
		return max

	}

	
	def beforeInsert={
		numero = sigNumero()
		puntoVenta = ordenReserva.puntoVenta
	}
	
}
