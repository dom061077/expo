package com.rural

import com.rural.seguridad.*

class OrdenReserva {
	Empresa empresa
	Person usuario
	Exposicion expo
	
	Date fechaAlta
	static belongs = [empresa:Empresa,usuario:Person,expo:Exposicion] 
			
	
    static constraints = {
    }
}
