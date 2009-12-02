import grails.util.Environment;
import com.rural.Provincia;
import com.rural.Departamento;
import com.rural.Localidad;

class BootStrap {
	 def authenticateService
	
     def init = { servletContext ->
     		switch (Environment.current){
     				
     				case Environment.DEVELOPMENT:
     								 
     					createAdminUserIfRequired()
     					break;
     				case Environment.PRODUCTION:
     					createAdminUserIfRequired()
     					break;
     		}
     }
     def destroy = {
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
				
				new Requestmap(url:"/register/**",configAttribute:"ROLE_ADMIN").save()
				println "Requestmap creado"
				
				new Requestmap(url:"/",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/login/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()				
				new Requestmap(url:"/register/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/js/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/css/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/image/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/images/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/plugins/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
				new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
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
				
				
			} else {
				println "Existing admin user, skipping creation"
			}
		 
	 }
} 