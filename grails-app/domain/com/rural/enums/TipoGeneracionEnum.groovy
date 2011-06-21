package com.rural.enums

public enum TipoGeneracionEnum {
	TIPOGEN_AUTOMATICA("Nota de Débito"),
	TIPOGEN_MANUAL("Nota de Crédito")
	
	String name
	
	public TipoGeneracionEnum(String name){
		this.name=name
	}
	
	static list(){
		[TIPOGEN_AUTOMATICA,TIPOGEN_MANUAL]
	}
}