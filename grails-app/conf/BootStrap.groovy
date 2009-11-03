import grails.util.Environment;

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

				
			} else {
				println "Existing admin user, skipping creation"
			}
		 
	 }
} 