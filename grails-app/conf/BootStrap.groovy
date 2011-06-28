import grails.util.Environment;
import com.rural.Provincia;
import com.rural.Localidad;
import com.rural.Exposicion;
import com.rural.Lote
import com.rural.Sector
import com.rural.Vendedor
import com.rural.Rubro
import com.rural.SubRubro
import com.rural.Iva
import com.rural.OrdenReserva
import com.rural.seguridad.Person
import com.rural.seguridad.Authority
import com.rural.seguridad.Requestmap
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.apache.commons.io.FileUtils;
import java.text.SimpleDateFormat

/*   class BootStrap {
esto es para fixear el error:
groovy.lang.MissingPropertyException: No such property: save
      def grailsApplication

      def init = { servletContext ->

         for (dc in grailsApplication) {
            dc.clazz.count()
         }
      }
   }
*/


class BootStrap {
	 def authenticateService
	 def ordenReservaService
	
     def init = { servletContext ->
     		switch (Environment.current){
     				
     				case Environment.DEVELOPMENT:
     					createUserIfRequired()			 
     					createAdminUserIfRequired()
     					break;
     				case Environment.PRODUCTION:
     					createAdminUserIfRequired()
     					break;
     				//case Environment.TEST:
     				//	createAdminUserIfRequired()
     				//	break;
     		}
     }
     def destroy = {
     }
	 
	 void vencimientoOrdenes(){
		 log.info "VERIFICANDO VENCIMIENTOS DE ORDENES DE RESERVA"
		 java.util.Date today = new java.util.Date()
		 def sdf = new SimpleDateFormat("yyyy-MM-dd")
		 
		 def ordenes = OrdenReserva.createCriteria().list(){
			 and{
				 lt("fechaVencimiento",java.sql.Date.valueOf(sdf.format(today)))
				 eq("anulada",Boolean.parseBoolean("false"))
			 }
		 }
		 log.info "ORDENES DE RESERVA CON VECIMIENTOS: "+ordenes.size()
		 ordenes.each { 
		 	ordenReservaService.verificarVencimiento(it)
		 }
	 }
     
     void createUserIfRequired(){
     		if (!Person.findByUsername("usuario")){
     			log.debug "Creating usuario user";
     			def authority = new Authority(authority: "ROLE_USER", description: "Usuario comun")
     			def person = new Person(username:"usuario",
     				passwd: authenticateService.encodePassword('usuario'),email:'usuario@noexiste.com.ar'
     				,enabled:true,userRealName:"Usuario comun de prueba").save()
     			authority.addToPeople(person)
     			authority.save()	
     		}
     }
	 
	 void inicializaTablas(){

		 
		 String sqlFilePath = 'src/templates/provincias.sql'
		 String sqlString =  FileUtils.readFileToString(new File(sqlFilePath)); 
		 def sql = Sql.newInstance(ConfigurationHolder.config.dataSource.url, ConfigurationHolder.config.dataSource.username, ConfigurationHolder.config.dataSource.password, ConfigurationHolder.config.dataSource.driverClassName)
		 List<String> lines = FileUtils.readLines (new File(sqlFilePath))
		 lines.each{
			 sql.execute(it)
		 }
		 
		 sqlFilePath = 'src/templates/localidades.sql'
		 sqlString = new File(sqlFilePath).text
		 sql = Sql.newInstance(ConfigurationHolder.config.dataSource.url, ConfigurationHolder.config.dataSource.username, ConfigurationHolder.config.dataSource.password, ConfigurationHolder.config.dataSource.driverClassName)
		 sql.execute(sqlString)
	 	 println "Inicializacion de tablas completa"
 }
	 
	 void createAdminUserIfRequired(){
			if (!Person.findByUsername("admin")) {
				//inicializaTablas()
				println "Fresh Database. Creating ADMIN user."
				def authority = new Authority(authority: "ROLE_ADMIN", description: "Super usuario")
				def person = new Person(username:"admin",
				passwd: authenticateService.encodePassword('esquina'),email:"dom061077@yahoo.com.ar",enabled:true,userRealName:"Usuario Administrador").save()
				authority.addToPeople(person)
				authority.save()
				//new Requestmap(url:"/captcha/**",configAtribute:"ROLE_ADMIN").save()
				
				
				//new Requestmap(url:"/",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/login/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()				
				//new Requestmap(url:"/register/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/js/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/css/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/image/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/images/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/plugins/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
				new Requestmap(url:"/person/editpassw",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
				new Requestmap(url:"/person/editpasswJson",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
				
				new Requestmap(url:"/person/updatepsswJson",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
				
				new Requestmap(url:"/person/**",configAttribute:"ROLE_ADMIN").save()
				new Requestmap(url:"/authority",configAttribute:"ROLE_A DMIN").save()
				new Requestmap(url:"/requestmap/**",configAttribute:"ROLE_ADMIN").save()
				new Requestmap(url:"/vendedor/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
				new Requestmap(url:"/vendedor/create",configAttribute:"ROLE_ADMIN").save()
				/*Provincia provincia
				
				
				provincia = new Provincia(nombre:"TUCUMAN").save()
				//tucuman
				new Localidad(nombreLoc:"SEOC",provincia:provincia).save()
				new Localidad(nombreLoc:"ESQUINA - MANCOPA",provincia:provincia).save()
				
				//
				provincia = new Provincia(nombre:"SALTA").save()
				//salta
				
				
				provincia = new Provincia(nombre:"CATAMARCA").save()
				provincia = new Provincia(nombre:"BUENOS AIRES").save()
				*/
				def expo = new Exposicion(nombre:"EXPO TUCUMAN")
				def lote = new Lote(nombre:"EMPRENDIMIENTOS")
				def sector = new Sector(nombre:"SECTOR Z")
				sector.addToLotes(lote)
				
				expo.addToSectores(sector)
				expo.save()
				
				expo=new Exposicion(nombre:"EXPO CONSTRUCCION")
				lote = new Lote(nombre:"GANADERO")
				sector = new Sector(nombre:"SECTOR A")
				sector.addToLotes(lote)
				expo.addToSectores(sector)
				expo.save()
				
				new Vendedor(nombre:'PRUEBA').save()
				new Vendedor(nombre:"MARCELA").save()

				Rubro rubro=new Rubro(nombreRubro:"CONSTRUCCION").save()
				SubRubro srubro = new SubRubro(nombreSubrubro:"CONSTRUCCION DE ESCUELAS",rubro:rubro).save()
				new Iva(descripcion:"21 %",porcentaje:21).save()
				new Iva(descripcion:"10,5",porcentaje:10.5).save()
			} else {
				println "Existing admin user, skipping creation"
			}
		 
	 }
} 