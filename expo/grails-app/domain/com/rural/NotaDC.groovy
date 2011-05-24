package com.rural

import com.rural.enums.TipoNotaEnum

import java.sql.Date

class NotaDC {
	Long numero
	TipoNotaEnum tipo
	OrdenReserva ordenReserva
	Double monto
	Date fechaAlta
	static belongsTo = [ordenReserva:OrdenReserva]
    static constraints = {
		tipo(blank:true,nullable:true)
		monto(blank:true,nullable:true)
    }
	def sigNumero(){
		/*def c = OrdenReserva.createCriteria()
		def lastNum = c.get{
			projections{
				max("numero")
			}
			
		}
		return lastNum ? lastNum+1 : 1    */
		def max = OrdenReserva.executeQuery("select max(numero)+1 from NotaDC n where n.tipo = ?",[tipo])[0]
		if (max == null) {
			max = 1
		}
		return max

	}

	
	def beforeInsert={
		numero = sigNumero()
	}
	
}
