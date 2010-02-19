import grails.util.Environment;
import com.rural.Provincia;
import com.rural.Departamento;
import com.rural.Localidad;
import com.rural.Exposicion;
import com.rural.Vendedor
import com.rural.Rubro
import com.rural.SubRubro

class BootStrap {
	 def authenticateService
	
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
	 
	 void createAdminUserIfRequired(){
			if (!Person.findByUsername("admin")) {
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
				new Requestmap(url:"/person/**",configAttribute:"ROLE_ADMIN").save()
				new Requestmap(url:"/authority/**",configAttribute:"ROLE_ADMIN").save()
				new Requestmap(url:"/requestmap/**",configAttribute:"ROLE_ADMIN").save()
				new Requestmap(url:"/vendedor/**",configAttribute:"ROLE_ADMIN").save()
				Provincia provincia
				Departamento departamento
				
				
				provincia = new Provincia(nombre:"TUCUMAN").save()
				//tucuman
				departamento = new Departamento(nombreDep:"CAPITAL",provincia:provincia).save()
				new Localidad(nombreLoc:"SEOC",departamento:departamento).save()
				departamento = new Departamento(nombreDep:"LEALES",provincia:provincia).save()
				new Localidad(nombreLoc:"ESQUINA - MANCOPA",departamento:departamento).save()
				
				//
				provincia = new Provincia(nombre:"SALTA").save()
				//salta
				
				
				provincia = new Provincia(nombre:"CATAMARCA").save()
				provincia = new Provincia(nombre:"BUENOS AIRES").save()
				
				new Exposicion(nombre:"EXPO CONSTRUCCION").save()
				new Exposicion(nombre:"EXPO TUCUMAN").save()
				
				new Vendedor(nombre:'PRUEBA').save()
				
				
				Rubro rubro=new Rubro(nombreRubro:"CONSTRUCCION").save()
				SubRubro srubro = new SubRubro(nombreSubrubro:"CONSTRUCCION DE ESCUELAS",rubro:rubro).save()
				
			} else {
				println "Existing admin user, skipping creation"
			}
		 
	 }
} 