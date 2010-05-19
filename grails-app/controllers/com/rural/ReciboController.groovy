

package com.rural

class ReciboController {
	def reciboService
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ reciboInstanceList: Recibo.list( params ), reciboInstanceTotal: Recibo.count() ]
    }

    def show = {
        def reciboInstance = Recibo.get( params.id )

        if(!reciboInstance) {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ reciboInstance : reciboInstance ] }
    }

    def delete = {
        def reciboInstance = Recibo.get( params.id )
        if(reciboInstance) {
            try {
                reciboInstance.delete(flush:true)
                flash.message = "Recibo ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Recibo ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def reciboInstance = Recibo.get( params.id )

        if(!reciboInstance) {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ reciboInstance : reciboInstance ]
        }
    }

    def update = {
        def reciboInstance = Recibo.get( params.id )
        if(reciboInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(reciboInstance.version > version) {
                    
                    reciboInstance.errors.rejectValue("version", "recibo.optimistic.locking.failure", "Another user has updated this Recibo while you were editing.")
                    render(view:'edit',model:[reciboInstance:reciboInstance])
                    return
                }
            }
            reciboInstance.properties = params
            if(!reciboInstance.hasErrors() && reciboInstance.save()) {
                flash.message = "Recibo ${params.id} updated"
                redirect(action:show,id:reciboInstance.id)
            }
            else {
                render(view:'edit',model:[reciboInstance:reciboInstance])
            }
        }
        else {
            flash.message = "Recibo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def reciboInstance = new Recibo()
        reciboInstance.properties = params
        return ['reciboInstance':reciboInstance]
    }
    
    def createJson = {
		log.Info("INGRESANDO LA METODO createjson DEL CONTROLADOR ReciboController")
		log.Info("PARAMETROS: $params")
		def recibo = reciboService.generarRecibo(new Long(params.id))
		int entero = recibo.total.intValue()
		Double totalaux = (recibo.total - entero)*100
//		int decimal = totalaux.intValue()
		 
		
		n2t numero2Letra = new n2t()
		
		

		if(recibo){
			render(contentType="text/json"){
				success true
				id recibo.id
				numero	recibo.numero
				empresa_nombre recibo.ordenReserva.empresa.nombre
				total recibo.total
				totalletras "SON "+numeroLetra.convertirLetras(entero)+" PESOS CON "
					+numeroLetra.convertirLetras(decimal)+" CENTAVOS" 
			}
		}
    }

    def save = {
        def reciboInstance = new Recibo(params)
        if(!reciboInstance.hasErrors() && reciboInstance.save()) {
            flash.message = "Recibo ${reciboInstance.id} created"
            redirect(action:show,id:reciboInstance.id)
        }
        else {
            render(view:'create',model:[reciboInstance:reciboInstance])
        }
    }
}


/*------------------CONVERTIDOR DE NUMERO EN LETRAS------------------------------*/

class n2t {
	private int flag;
	public int numero;
	public String importe_parcial;
	public String num;
	public String num_letra;
	public String num_letras;
	public String num_letram;
	public String num_letradm;
	public String num_letracm;
	public String num_letramm;
	public String num_letradmm;
	
	public n2t(){
		numero = 0;
		flag=0;
	}
	public n2t(int n){
		numero = n;
		flag=0;
	}

	
	private String unidad(int numero){
		
		switch (numero){
		case 9:
				num = "nueve";
				break;
		case 8:
				num = "ocho";
				break;
		case 7:
				num = "siete";
				break;
		case 6:
				num = "seis";
				break;
		case 5:
				num = "cinco";
				break;
		case 4:
				num = "cuatro";
				break;
		case 3:
				num = "tres";
				break;
		case 2:
				num = "dos";
				break;
		case 1:
				if (flag == 0)
					num = "uno";
				else 
					num = "un";
				break;
		case 0:
				num = "";
				break;
		}
		return num;
	}
	
	private String decena(int numero){
	
		if (numero >= 90 && numero <= 99)
		{
			num_letra = "noventa ";
			if (numero > 90)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 90));
		}
		else if (numero >= 80 && numero <= 89)
		{
			num_letra = "ochenta ";
			if (numero > 80)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 80));
		}
		else if (numero >= 70 && numero <= 79)
		{
			num_letra = "setenta ";
			if (numero > 70)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 70));
		}
		else if (numero >= 60 && numero <= 69)
		{
			num_letra = "sesenta ";
			if (numero > 60)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 60));
		}
		else if (numero >= 50 && numero <= 59)
		{
			num_letra = "cincuenta ";
			if (numero > 50)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 50));
		}
		else if (numero >= 40 && numero <= 49)
		{
			num_letra = "cuarenta ";
			if (numero > 40)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 40));
		}
		else if (numero >= 30 && numero <= 39)
		{
			num_letra = "treinta ";
			if (numero > 30)
				num_letra = num_letra.concat("y ").concat(unidad(numero - 30));
		}
		else if (numero >= 20 && numero <= 29)
		{
			if (numero == 20)
				num_letra = "veinte ";
			else
				num_letra = "veinti".concat(unidad(numero - 20));
		}
		else if (numero >= 10 && numero <= 19)
		{
			switch (numero){
			case 10:

				num_letra = "diez ";
				break;

			case 11:

				num_letra = "once ";
				break;

			case 12:

				num_letra = "doce ";
				break;

			case 13:

				num_letra = "trece ";
				break;

			case 14:

				num_letra = "catorce ";
				break;

			case 15:
			
				num_letra = "quince ";
				break;
			
			case 16:
			
				num_letra = "dieciseis ";
				break;
			
			case 17:
			
				num_letra = "diecisiete ";
				break;
			
			case 18:
			
				num_letra = "dieciocho ";
				break;
			
			case 19:
			
				num_letra = "diecinueve ";
				break;
			
			}	
		}
		else
			num_letra = unidad(numero);

	return num_letra;
	}	

	private String centena(int numero){
		if (numero >= 100)
		{
			if (numero >= 900 && numero <= 999)
			{
				num_letra = "novecientos ";
				if (numero > 900)
					num_letra = num_letra.concat(decena(numero - 900));
			}
			else if (numero >= 800 && numero <= 899)
			{
				num_letra = "ochocientos ";
				if (numero > 800)
					num_letra = num_letra.concat(decena(numero - 800));
			}
			else if (numero >= 700 && numero <= 799)
			{
				num_letra = "setecientos ";
				if (numero > 700)
					num_letra = num_letra.concat(decena(numero - 700));
			}
			else if (numero >= 600 && numero <= 699)
			{
				num_letra = "seiscientos ";
				if (numero > 600)
					num_letra = num_letra.concat(decena(numero - 600));
			}
			else if (numero >= 500 && numero <= 599)
			{
				num_letra = "quinientos ";
				if (numero > 500)
					num_letra = num_letra.concat(decena(numero - 500));
			}
			else if (numero >= 400 && numero <= 499)
			{
				num_letra = "cuatrocientos ";
				if (numero > 400)
					num_letra = num_letra.concat(decena(numero - 400));
			}
			else if (numero >= 300 && numero <= 399)
			{
				num_letra = "trescientos ";
				if (numero > 300)
					num_letra = num_letra.concat(decena(numero - 300));
			}
			else if (numero >= 200 && numero <= 299)
			{
				num_letra = "doscientos ";
				if (numero > 200)
					num_letra = num_letra.concat(decena(numero - 200));
			}
			else if (numero >= 100 && numero <= 199)
			{
				if (numero == 100)
					num_letra = "cien ";
				else
					num_letra = "ciento ".concat(decena(numero - 100));
			}
		}
		else
			num_letra = decena(numero);
		
		return num_letra;	
	}	

	private String miles(int numero){
		if (numero >= 1000 && numero <2000){
			num_letram = ("mil ").concat(centena(numero%1000));
		}
		if (numero >= 2000 && numero <10000){
			flag=1;
			num_letram = unidad(numero/1000).concat(" mil ").concat(centena(numero%1000));
		}
		if (numero < 1000)
			num_letram = centena(numero);
		
		return num_letram;
	}		

	private String decmiles(int numero){
		if (numero == 10000)
			num_letradm = "diez mil";
		if (numero > 10000 && numero <20000){
			flag=1;
			num_letradm = decena(numero/1000).concat("mil ").concat(centena(numero%1000));		
		}
		if (numero >= 20000 && numero <100000){
			flag=1;
			num_letradm = decena(numero/1000).concat(" mil ").concat(miles(numero%1000));		
		}
		
		
		if (numero < 10000)
			num_letradm = miles(numero);
		
		return num_letradm;
	}		

	private String cienmiles(int numero){
		if (numero == 100000)
			num_letracm = "cien mil";
		if (numero >= 100000 && numero <1000000){
			flag=1;
			num_letracm = centena(numero/1000).concat(" mil ").concat(centena(numero%1000));		
		}
		if (numero < 100000)
			num_letracm = decmiles(numero);
		return num_letracm;
	}		

	private String millon(int numero){
		if (numero >= 1000000 && numero <2000000){
			flag=1;
			num_letramm = ("Un millon ").concat(cienmiles(numero%1000000));
		}
		if (numero >= 2000000 && numero <10000000){
			flag=1;
			num_letramm = unidad(numero/1000000).concat(" millones ").concat(cienmiles(numero%1000000));
		}
		if (numero < 1000000)
			num_letramm = cienmiles(numero);
		
		return num_letramm;
	}		
	
	private String decmillon(int numero){
		if (numero == 10000000)
			num_letradmm = "diez millones";
		if (numero > 10000000 && numero <20000000){
			flag=1;
			num_letradmm = decena(numero/1000000).concat("millones ").concat(cienmiles(numero%1000000));		
		}
		if (numero >= 20000000 && numero <100000000){
			flag=1;
			num_letradmm = decena(numero/1000000).concat(" milllones ").concat(millon(numero%1000000));		
		}
		
		
		if (numero < 10000000)
			num_letradmm = millon(numero);
		
		return num_letradmm;
	}		

	
	public String convertirLetras(int numero){
		num_letras = decmillon(numero);
		return num_letras;
	} 	
}










