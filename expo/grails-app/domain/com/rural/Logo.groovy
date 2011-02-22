package com.rural

class Logo {

	Integer anio
	byte[] image
	Exposicion expo
	static belongs = [expo:Exposicion]
    static constraints = {
		image(blank:true,nullable:true,maxSize:1024*30)
		anio(unique:'expo')
    }
}
