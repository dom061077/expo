package com.rural.enums

public enum TipoNotaEnum {
	NOTA_DEBITO("Nota de Débito"),
	NOTA_CREDITO("Nota de Crédito")
	
	String name
	
	public TipoNotaEnum(String name){
		this.name=name
	}
	
	static list(){
		[NOTA_DEBITO,NOTA_CREDITO]
	}
}
