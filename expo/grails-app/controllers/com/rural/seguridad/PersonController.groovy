package com.rural.seguridad

//http://jlorenzen.blogspot.com/2009/11/learning-with-grails-security-extjs.html#rest

/**
 * User controller.
 */

 
class PersonController {

	def authenticateService

	// the delete, save and update actions only accept POST requests
	static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']



	def index = {
		redirect action: list, params: params
	}

	def list = {
		if (!params.max) {
			params.max = 10
		}
		log.debug("TOTAL DE USUARIOS: "+Person.count())
		[personList: Person.list(params), personInstanceTotal: Person.count() ]
	}
	
	def savejson = {
		log.info("INGREANDO AL METODO savejson DEL CONTROLADOR PersonController")
		log.debug("PARAMETROS INGRESADOS $params")
		params.passwd=authenticateService.encodePassword(params.passwd)
		def usuarioInstance = new Person(params)
		def authority = Authority.get(params.rol)
		authority.addToPeople(usuarioInstance)
		if (!usuarioInstance.hasErrors() && usuarioInstance.save()){
			render(contentType:"text/json"){
				success true
				idUsuario usuarioInstance.id
			}
		}else{
			render(contentType:"text/json"){
				success false
				errors{
					usuarioInstance.errors.allErrors.each{
						title it.defaultMessage
					}
				}
			}
		}
		
	}
	
	def listjson = {
		log.info("INGRESANDO AL METODO listjson DEL CONTROLADOR PersonController")
		log.debug("PARAMETROS INGRESADOS $params")
		def pagingConfig = [
			max: params.limit as Integer ?: 10,
			offset: params.start as Integer ?: 0
		]
		
		def totalUsuarios = Person.createCriteria().count{
			if(params.searchCriteria)
				or{
					ilike('username','%'+params?.searchCriteria+'%')
					ilike('userRealName','%'+params?.searchCriteria+'%')
				}
		}		
		
		def usuarios = Person.createCriteria().list(pagingConfig){
			if(params.searchCriteria)
				or{
					ilike('username','%'+params?.searchCriteria+'%')
					ilike('userRealName','%'+params?.searchCriteria+'%')
				}				
		}
		
		render(contentType:"text/json"){
			total totalUsuarios
			rows{
				usuarios.each{
					row(id:it.id,username:it.username,userRealName:it.userRealName)
				}
			}
		}
	}
	
	def showjson={
		log.info("INGRESANDO AL METODO showjson del CONTROLADOR PersonController")
		log.debug("Parametros: $params")
		def usuarioInstance = Person.get(params.id);
		def authority = null
		usuarioInstance.authorities.each{
			authority = it
		}
		if(usuarioInstance){
			log.debug("Instancia de usuario encontrada, renderizando json")
			render(contentType:"text/json"){
				success true
				data(id:usuarioInstance.id,username:usuarioInstance.username,userRealName:usuarioInstance.userRealName
						,enabled:usuarioInstance.enabled,email:usuarioInstance.email,emailShow:usuarioInstance.emailShow
						,authorityId:authority?.id
						,description:usuarioInstance.description
						,authorityDesc:authority?.description
						,passwd:'')
			}
		}else{
			log.debug("Instancia de usuario no encontrada, renderizando json");
			render(contentType:"text/json"){
				success false
			}
		}
	}
	
	def updatejson = {
		log.info("INGRESANDO EL METODO updatejson DEL CONTROLLER updatejson")
		log.debug("PARAMETROS: $params")
		if(params.enabled.equals('on'))
			params.enabled=true
		else
			params.enabled=false
		def person = Person.get(params.id)
		if (!person) {
			render(contentType:"text/json"){
				success false
				errors{
					title "Usuario no encontrado con Id: $params.id"
				}
			}
			return
		}

		/*
		long version = params.version.toLong()
		if (person.version > version) {
			render(contentType:"text/json"){
				success false
				errors{
					title "Otro usuario esta ha modificado este registro mientras estaba intentandolo Ud."
				}
			}
			
		}*/
		def oldPassword = person.passwd
		person.properties = params
		if (!params.passwd.equals(oldPassword)) {
			person.passwd = authenticateService.encodePassword(params.passwd)
		}
		if (person.save()) {
			Authority.findAll().each { it.removeFromPeople(person) }
			Authority.get(params.rol).addToPeople(person)
			render(contentType:"text/json"){
				success true
			}
		}
		else {
			render(contentType:"text/json"){
				success false
				errors{
					title "Error de validacion en el registro"
				}
			}
		}
		
	}

	def show = {
		def person = Person.get(params.id)
		if (!person) {
			flash.message = "Person not found with id $params.id"
			redirect action: list
			return
		}
		List roleNames = []
		for (role in person.authorities) {
			roleNames << role.authority
		}
		roleNames.sort { n1, n2 ->
			n1 <=> n2
		}
		[person: person, roleNames: roleNames]
	}

	/**
	 * Person delete action. Before removing an existing person,
	 * he should be removed from those authorities which he is involved.
	 */
	def delete = {
		try{
		Person.withTransaction{

			def person = Person.get(params.id)
			if (person) {
				def authPrincipal = authenticateService.principal()
				//avoid self-delete if the logged-in user is an admin
				if (!(authPrincipal instanceof String) && authPrincipal.username == person.username) {
					//flash.message = "You can not delete yourself, please login as another admin and try again"
					render(contentType:"text/json"){
						success false
						msg "No puede borrarse a si mismo"
					}
				}
				else {
					//first, delete this person from People_Authorities table.
							try{
									Authority.findAll().each { it.removeFromPeople(person) }
									person.delete(flush:true)
									//flash.message = "Person $params.id deleted."
									render(contentType:"text/json"){
										success true 
										msg "Usuario Eliminado"
									}
									
							}catch(Exception e){
								
								
							}
							
				}
			}
			else {
				//flash.message = "Person not found with id $params.id"
				render(contentType:"text/json"){
					success false
					msg "Usuario no encontrado"
				}
			}
		}
		//redirect action: list
		}catch(org.springframework.dao.DataIntegrityViolationException e){
			render(contentType:"text/json"){
				success false
				msg "No se puede eliminar el Usuario porque es referenciado en otro registro"
			}
		}
	}

	def edit = {

		/*def person = Person.get(params.id)
		if (!person) {
			flash.message = "Person not found with id $params.id"
			redirect action: list
			return
		}

		return buildPersonModel(person)*/
		log.info("INGRESANDO AL METODO edit DEL CONTROLLER PersonController")
		log.debug("Parametros $params")
		return [id:params.id]
	}

	/**
	 * Person update action.
	 */
	def update = {

		def person = Person.get(params.id)
		if (!person) {
			flash.message = "Person not found with id $params.id"
			redirect action: edit, id: params.id
			return
		}

		long version = params.version.toLong()
		if (person.version > version) {
			person.errors.rejectValue 'version', "person.optimistic.locking.failure",
				"Another user has updated this Person while you were editing."
				render view: 'edit', model: buildPersonModel(person)
			return
		}

		def oldPassword = person.passwd
		person.properties = params
		if (!params.passwd.equals(oldPassword)) {
			person.passwd = authenticateService.encodePassword(params.passwd)
		}
		if (person.save()) {
			Authority.findAll().each { it.removeFromPeople(person) }
			addRoles(person)
			redirect action: show, id: person.id
		}
		else {
			render view: 'edit', model: buildPersonModel(person)
		}
	}

	def create = {
		[person: new Person(params), authorityList: Authority.list()]
	}

	/**
	 * Person save action.
	 */
	def save = {

		def person = new Person()
		person.properties = params
		person.passwd = authenticateService.encodePassword(params.passwd)
		if (person.save()) {
			addRoles(person)
			redirect action: show, id: person.id
		}
		else {
			render view: 'create', model: [authorityList: Authority.list(), person: person]
		}
	}

	private void addRoles(person) {
		for (String key in params.keySet()) {
			if (key.contains('ROLE') && 'on' == params.get(key)) {
				Authority.findByAuthority(key).addToPeople(person)
			}
		}
	}

	private Map buildPersonModel(person) {

		List roles = Authority.list()
		roles.sort { r1, r2 ->
			r1.authority <=> r2.authority
		}
		Set userRoleNames = []
		for (role in person.authorities) {
			userRoleNames << role.authority
		}
		LinkedHashMap<Authority, Boolean> roleMap = [:]
		for (role in roles) {
			roleMap[(role)] = userRoleNames.contains(role.authority)
		}

		return [person: person, roleMap: roleMap]
	}
	
	def updatepsswJson = {PersonCommand pc ->
		def errorList = []
	
		log.info("INGRESANDO AL METODO updatepsswJson")
		log.debug("PersonCommand username: "+pc.username)
		log.debug("PersonCommand password: "+pc.password)
		log.debug("PersonCommand newpassword: "+pc.newpassword)
		log.debug("PersonCommand newpasswordrepeat: "+pc.newpasswordrepeat)
		log.debug("Params: "+params)
		Person user = authenticateService?.userDomain()
		if (authenticateService.encodePassword(pc.password).equals(user.passwd)){
			log.debug("El password es correcto")
		}else{
			log.debug("El password es incorrecto")
			pc.errors.rejectValue("password","personcommand.password.authentication.failed","Contraseña")
		}
			
		if(pc.hasErrors()){
			pc.errors.allErrors.each{error->
        		error.codes.each{
        			log.debug("Codigo de error: "+it)
        			if(g.message(code:it)!=it){
        				log.debug("ENCONTRO EL CODIGO DE MENSAJE")
        				errorList.add(g.message(code:it))
        			}
        		}
			}
		
			render(contentType:"text/json"){
				success false
				errors{
					errorList.each{
						title it
					}
				} 
			}
		}else{
			user = Person.findByUsername(pc.username)
			user.passwd=authenticateService.encodePassword(pc.newpassword)
			if (user.save()){
				log.debug("PASSWORD MODIFICADA CON EXITO")
				render(contentType:"text/json"){
					success true
				}
				
			}else{
				log.debug("NO SE GUARDO EL CAMBIO DE PASSWORD DEBIDO A ALGUN ERROR")
				render(contentType:"text/json"){
					success false
					errors{
						title "Se produjo un error al guardar los datos."
					}
				}
			}
		}
		
		
	}

	def editpasswJson = {
		log.info("INGRESANDO AL METODO editpasswJson EN PersonController")
		log.debug("Params: "+params)
		Person person = Person.get(params.id)
		log.debug("Usuario: "+person.username)
		render(contentType:'text/json'){
			success true
			//data(id:exposicionInstance.id,nombre:exposicionInstance.nombre)
			data( username : person.username,
				  newpassword : "",
   				  retypenewpassword : ""
			)
			
		}
	}
	
	def editpassw = {
		log.info("INGRESANDO AL METODO editpasswJson EN PersonController")
		Long id = authenticateService.userDomain().id
		log.debug("ID DE USUARIO: "+id) 
		return [id:id]
	}
	
	
}

class PersonCommand {
	String username
	String password
	String newpassword
	String newpasswordrepeat
	
	static constraints = {
		newpassword(blank: false, size: 6..15,
				validator: { newpswd, pc ->
					if(newpswd != pc.password){
						return newpswd != pc.password
					}else{
						return newpswd != pc.username
					}							
				}
			)
		newpasswordrepeat(blank: false, size: 6..15,
				validator: { repeatpswd, pc ->
					return repeatpswd==pc.newpassword
				}
			)	
	}
	
}
