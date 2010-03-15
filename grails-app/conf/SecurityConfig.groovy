security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "com.rural.seguridad.Person"
	authorityDomainClass = "com.rural.seguridad.Authority"
	requestMapClass = "com.rural.seguridad.Requestmap"
	
	loginFormUrl = "/login"
	defaultTargetUrl = "/"
	userRequestMapDomainClass = true 
	

}
