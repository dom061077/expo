package com.rural.enums

public enum TipoNotaEnum {
	NOTA_DEBITO("Comprobante de Crédito"),
	NOTA_CREDITO("Comprobante de Débito")
	
	String name
	
	public TipoNotaEnum(String name){
		this.name=name
	}
	
	static list(){
		[NOTA_DEBITO,NOTA_CREDITO]
	}
}
