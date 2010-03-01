package com.rural

class CargaExcel {
	Date fechaCarga
	String nombreArchivo
	byte[] archivo
    static constraints = {
		archivo(maxSize:10000000)
    }
}
