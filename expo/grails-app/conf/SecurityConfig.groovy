security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "Person"
	authorityDomainClass = "Authority"
	requestMapClass = "Requestmap"
	
	loginFormUrl = "/login"
	defaultTargetUrl = "/"
	userRequestMapDomainClass = true 
}
