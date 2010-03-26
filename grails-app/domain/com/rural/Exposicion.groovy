package com.rural

class Exposicion {
	String nombre
	Empresa empresaParent	

	static hasMany = [empresas:Empresa]

    static constraints = {
    	empresaParent(blank:true,nullable:true)
    }
	
	static belongsTo = [Empresa]

	
/*	def beforeDelete = {
		empresaParent.exposaparticipar.remove(this)
		
	}*/
	
}
