package com.rural.seguridad


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
		[personList: Person.list(params)]
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

		def person = Person.get(params.id)
		if (person) {
			def authPrincipal = authenticateService.principal()
			//avoid self-delete if the logged-in user is an admin
			if (!(authPrincipal instanceof String) && authPrincipal.username == person.username) {
				flash.message = "You can not delete yourself, please login as another admin and try again"
			}
			else {
				//first, delete this person from People_Authorities table.
				Authority.findAll().each { it.removeFromPeople(person) }
				person.delete()
				flash.message = "Person $params.id deleted."
			}
		}
		else {
			flash.message = "Person not found with id $params.id"
		}

		redirect action: list
	}

	def edit = {

		def person = Person.get(params.id)
		if (!person) {
			flash.message = "Person not found with id $params.id"
			redirect action: list
			return
		}

		return buildPersonModel(person)
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
			data{ username person.username
							password person.passwd
							newpassword ""
							retypenewpassword ""
			}
			
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
