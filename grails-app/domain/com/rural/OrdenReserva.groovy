package com.rural




import com.rural.seguridad.*
import java.sql.Date
import java.text.SimpleDateFormat

import com.rural.enums.TipoNotaEnum
import com.rural.util.DescuentoPorFecha
import com.rural.utils.Util

class OrdenReserva {
	static auditable = true
	
	Empresa empresa
	Person usuario
	Exposicion expo
	Boolean anulada = false
	Double ivaGral=0
	Double ivaRni=0/*es el resultado subtotal neto + ivaGral*/
	Double ivaSujNoCateg=0 /*es mayor a cero cuando la condicion de IVA es ivaRniCheck true*/
	Double subTotal=0
	Double subTotalsindesc=0
	Double total=0
	Double totalsindesc=0
    Float redondeo
	Integer anio
	Boolean ivaGralCheck = false
	Boolean ivaRniCheck = false
	Boolean exentoCheck = false
	Boolean consFinalCheck = false
	Boolean monotributoCheck = false
	Float porcentajeResIns=0
	Float porcentajeResNoIns=0
	String observacion
	Double subtotalDetalle=0
	Double subtotalOtrosConceptos=0
	Double debito=0
	Double credito=0
	//Double saldo
	Double recibo=0
	
	Long numero
	Date fechaAlta
	Date fechaVencimiento
	
	Integer puntoVenta
	
	//-------datos persistidos del expositor----
	String nombreRepresentante
	String direccion
	String email
	String nombre//es el nombre comercial
	String telefono1
	String telefono2
	String cargoRep//cargo representante
	String dniRep
	
	
	//datos fiscales para la facturaci�n
	String cuit
	String razonSocial //apellido y nombre del expositor o raz�n social
	String direccionFiscal
	String localidadFiscal
	String provinciaFiscal
	String codigoPostal
	String pais
	String telefonoFiscal
	
	
	Vendedor vendedor
	Localidad localidad
	
	
	String telefonoRepresentante1;
	String telefonoRepresentante2;
	String telefonoRepresentante3;
	
	
	
	
	//-------fin datos persistidos del expositor-----
	
	//-------------------datos trasient--------------
	static transients = ['totalConDescuentos','subTotalConDescuento','descAplicadosEnFecha','difTotalDesc','ivaGralCalc'
                        ,'ivaRniCalc','ivaSujNoCategCalc','descuentosPorFecha','saldo','saldoConDescuento']


    

    Double getIvaSujNoCategCalc(){
        if(ivaRniCheck && porcentajeResIns>=0)
            return ivaRni*10.5/100
        return 0
    }

    Double getIvaRniCalc(){
        return subTotal + ivaGralCalc
    }

    Double getIvaGralCalc(){
        def ivaReturn = subTotal*(porcentajeResIns > 0 ? porcentajeResIns : porcentajeResNoIns)/100

        return ivaReturn
    }

    Double getSubTotalConDescuento(){
        def todayCal = Calendar.getInstance()
        def sf = new SimpleDateFormat("yyyy-MM-dd")
        def fecha =  java.sql.Date.valueOf(sf.format(todayCal.getTime()))
        def subTotalOrden = 0
        if(detalle?.size()>0){
            List<DetalleServicioContratadoDescuentos> porcentajesDesc = new ArrayList<DetalleServicioContratadoDescuentos>();
            detalle.each{ det->
                det.descuentos.sort{a,b-> a.fechaVencimiento.compareTo(b.fechaVencimiento)}

                det.descuentos.each{ desc->
                    if(!porcentajesDesc.find {it.detalleServicioContratado.equals(desc.detalleServicioContratado)}){
                       if(desc.fechaVencimiento.compareTo(fecha)>=0){
                          porcentajesDesc.add(desc);
                       }
                    }
                }
            }

            subTotalOrden = subTotal

            porcentajesDesc.each{desc ->
                subTotalOrden = subTotalOrden - desc.detalleServicioContratado.subTotalsindesc*desc.porcentaje/100
            }
        }

        return subTotalOrden

    }
    
    /*Double getSubTotalConDescuento(){
        def todayCal = Calendar.getInstance()
        def sf = new SimpleDateFormat("yyyy-MM-dd")
        def fecha =  java.sql.Date.valueOf(sf.format(todayCal.getTime()))


        def subTotalOrden = 0
        if(detalle.size()>0){
            List<DetalleServicioContratadoDescuentos> porcentajesDesc = new ArrayList<DetalleServicioContratadoDescuentos>();
            detalle.each{ det->
                det.descuentos.sort{a,b-> a.fechaVencimiento.compareTo(b.fechaVencimiento)}
                det.descuentos.each{ desc->
                    if(desc.fechaVencimiento.compareTo(fecha)>=0){
                        porcentajesDesc.add(desc);
                        return
                    }
                }
            }

            subTotalOrden = subTotal

            porcentajesDesc.each{desc ->
                subTotalOrden = subTotalOrden - desc.detalleServicioContratado.subTotalsindesc*desc.porcentaje/100
            }
        }
        return subTotalOrden
    }*/

    Double getSaldo(){
        return total - recibo - credito + debito
    }
    
    Double getSaldoConDescuento(){
        return totalConDescuentos - recibo - credito + debito
    }
    
    List<DescuentoPorFecha> getDescuentosPorFecha(){
        List<DescuentoPorFecha> descPorFechaList = new ArrayList<DescuentoPorFecha>()
        def descPorFecha
        detalle.each {det->
           det.descuentos.each{desc->
               descPorFecha = descPorFechaList.find{it.fecha.equals(det.fechaVencimiento)}
               if(descPorFecha){
                   descPorFecha.porcentaje+=desc.porcentaje
                   descPorFecha.montoDescontado+=desc.montoDescontado
                   descPorFecha.descripcion+=det.sector.nombre+" (${String.format("%.2f",desc.porcentaje)} %); "
               }else{
                   descPorFechaList.add(
                           new DescuentoPorFecha(fecha: desc.fechaVencimiento, porcentaje: desc.porcentaje,montoDescontado: desc.montoDescontado,descripcion: det.sector.nombre+" (${desc.porcentaje}); ")
                   )
               }

           }
        }

        descPorFechaList.sort{a,b-> a.fecha.compareTo(b.fecha)}
        return descPorFechaList
    }
	
	
	Double getDifTotalDesc(){
		return total - totalConDescuentos 
	}
	
	
	List<DetalleServicioContratadoDescuentos> getDescAplicadosEnFecha(){
		List<DetalleServicioContratadoDescuentos> porcentajesDesc = new ArrayList<DetalleServicioContratadoDescuentos>();
		detalle.each{ det->
			det.descuentos.each{ desc->
				if(desc.fechaVencimiento.compareTo(fecha)>=0){
					porcentajesDesc.add(desc);
				}
			}
		}
		return porcentajesDesc
	}
	
	Double getTotalConDescuentos(){
		/*def todayCal = Calendar.getInstance()
		def sf = new SimpleDateFormat("yyyy-MM-dd")
		def fecha =  java.sql.Date.valueOf(sf.format(todayCal.getTime()))

		
		def totalOrden = total
        if(detalle.size()>0){
            def subTotalOrden = 0
            List<DetalleServicioContratadoDescuentos> porcentajesDesc = new ArrayList<DetalleServicioContratadoDescuentos>();
            detalle.each{ det->
                det.descuentos.sort{a,b-> a.fechaVencimiento.compareTo(b.fechaVencimiento)}
                det.descuentos.each{ desc->
                    if(desc.fechaVencimiento.compareTo(fecha)>=0){
                        porcentajesDesc.add(desc);
                        return
                    }
                }
            }

            subTotalOrden = subTotal

            porcentajesDesc.each{desc ->
                subTotalOrden = subTotalOrden - desc.detalleServicioContratado.subTotalsindesc*desc.porcentaje/100
            }


         def ivaGralLocal = subTotalOrden *(porcentajeResIns > 0 ? porcentajeResIns : porcentajeResNoIns)/100

            def ivaRniLocal= subTotalOrden + ivaGralLocal
            def ivaSujNoCategLocal=0

            if(ivaRniCheck && porcentajeResIns>=0)//modificacion clave para determinar el porcentaje de iva cuando la expo es exenta
                ivaSujNoCategLocal=ivaRniLocal*10.5/100
            totalOrden=subTotalOrden+ivaGralLocal+ivaSujNoCategLocal
        }*/
        def ivaGralLocal = subTotalConDescuento *(porcentajeResIns > 0 ? porcentajeResIns : porcentajeResNoIns)/100

        def ivaRniLocal= subTotalConDescuento + ivaGralLocal
        def ivaSujNoCategLocal=0

        if(ivaRniCheck && porcentajeResIns>=0)//modificacion clave para determinar el porcentaje de iva cuando la expo es exenta
            ivaSujNoCategLocal=ivaRniLocal*10.5/100
        def totalOrden = subTotalConDescuento+ivaGralLocal+ivaSujNoCategLocal

		//totalOrden = Math.round(totalOrden*Math.pow(10,2))/Math.pow(10,2);
		return (Double) Util.redondear(totalOrden,0)
	}
	
	static belongsTo = [empresa:Empresa,usuario:Person,expo:Exposicion] 

	static hasMany = [detalle:DetalleServicioContratado,otrosconceptos:OtrosConceptos,productos:ProductoExpuesto,recibos:Recibo,notas:NotaDC]
	
    static constraints = {
    	numero(blank:true,nullable:true)
		puntoVenta(blank:true,nullable:true)
		nombreRepresentante(blank:true,nullable:true)
		direccion(blank:true,nullable:true)
		email(blank:true,nullable:true)
		nombre(blank:true,nullable:true)
		telefono1(blank:true,nullable:true)
		telefono2(blank:true,nullable:true)
		cargoRep(blank:true,nullable:true)
		dniRep(blank:true,nullable:true)
		cuit(blank:true,nullable:true)
		razonSocial(blank:true,nullable:true) //apellido y nombre del expositor o raz�n social
		direccionFiscal(blank:true,nullable:true)
		localidadFiscal(blank:true,nullable:true)
		provinciaFiscal(blank:true,nullable:true)
		codigoPostal(blank:true,nullable:true)
		pais(blank:true,nullable:true)
		telefonoFiscal(blank:true,nullable:true)
		
		puntoVenta(blank:true,nullable:true)
		
		vendedor(blank:true,nullable:true)
		localidad(blank:true,nullable:true)
		
		
		telefonoRepresentante1(blank:true,nullable:true)
		telefonoRepresentante2(blank:true,nullable:true)
		telefonoRepresentante3(blank:true,nullable:true)
		/*--propiedades agregadas para aplicar los descuentos y el tarifario--*/
		subTotalsindesc(blank:true,nullable:true)
		fechaVencimiento(blank:true,nullable:true)
    }
		   
    
    static mapping = {
    	subtotalDetalle formula:"(select IF(sum(d.sub_total) IS NULL,0,sum(d.sub_total)) from detalle_servicio_contratado d where d.orden_reserva_id=id)"
		subtotalOtrosConceptos formula:"(select IF(sum(o.sub_total) IS NULL,0,sum(o.sub_total)) from otros_conceptos o where o.orden_reserva_id=id)"
		debito formula:"(select IF(sum(ndc.total) IS NULL,0,sum(ndc.total)) from notadc ndc where ndc.orden_reserva_id=id and ndc.anulada=0 and ndc.tipo='"+TipoNotaEnum.NOTA_DEBITO+"')"
		credito formula:"(select IF(sum(ndc.total) IS NULL,0,sum(ndc.total)) from notadc ndc where ndc.orden_reserva_id=id and ndc.anulada=0 and ndc.tipo='"+TipoNotaEnum.NOTA_CREDITO+"')"
		recibo formula:"(select IF(sum(r.total) IS NULL,0,sum(r.total)) from recibo r where r.anulado=0 and r.orden_reserva_id=id)"
		
    }
    
    def sigNumero(){
    	/*def c = OrdenReserva.createCriteria()
    	def lastNum = c.get{
    		projections{
    			max("numero")
    		}
    		
    	}
		return lastNum ? lastNum+1 : 1    */
		def max = OrdenReserva.executeQuery("select max(numero)+1 from OrdenReserva o where o.expo = ?",[expo])[0]
		if (max == null) {
			max = 1
		}
		return max

    }
    
    def beforeInsert={
    	numero = sigNumero()
		
		nombreRepresentante=empresa.nombreRepresentante
		direccion=empresa.direccion
		email=empresa.email
		nombre=empresa.nombre//es el nombre comercial
		telefono1=empresa.telefono1
		telefono2=empresa.telefono2
		cargoRep=empresa.cargoRep//cargo representante
		dniRep=empresa.dniRep
		
		
		//datos fiscales para la facturaci�n
		cuit=empresa.cuit
		razonSocial=empresa.razonSocial //apellido y nombre del expositor o raz�n social
		direccionFiscal=empresa.direccionFiscal
		localidadFiscal=empresa.localidadFiscal
		provinciaFiscal=empresa.provinciaFiscal
		codigoPostal=empresa.codigoPostal
		pais=empresa.pais
		telefonoFiscal=empresa.telefonoFiscal
		vendedor=empresa.vendedor
		localidad=empresa.localidad
		telefonoRepresentante1=empresa.telefonoRepresentante1
		telefonoRepresentante2=empresa.telefonoRepresentante2
		telefonoRepresentante3=empresa.telefonoRepresentante3
		puntoVenta=expo.puntoVenta
		
		
		
		
    }
    
}
