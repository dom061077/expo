package com.rural.enums

public enum TipoNotaEnum {
	NOTA_DEBITO("Comprobante de Débito"),
	NOTA_CREDITO("Comprobante de Crédito")
	
	String name
	
	public TipoNotaEnum(String name){
		this.name=name
	}
	
	static list(){
		[NOTA_DEBITO,NOTA_CREDITO]
	}
}
