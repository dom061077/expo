package com.rural

class Logo {

	Integer anio
	byte[] image
    static constraints = {
		image(blank:true,nullable:true,maxSize:1024*30)
		
    }
}
