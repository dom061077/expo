package com.rural.enums

public enum TipoGeneracionEnum {
	TIPOGEN_AUTOMATICA("Automatica"),
	TIPOGEN_MANUAL("Manual")
	
	String name
	
	public TipoGeneracionEnum(String name){
		this.name=name
	}
	
	static list(){
		[TIPOGEN_AUTOMATICA,TIPOGEN_MANUAL]
	}
}