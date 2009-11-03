<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'form.css')}" />        
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />				

</head>
<body>
<div id="wrap">

	<div id="top"></div>

	<div id="content">

		<div class="header">
			<h1><a href="#">Registro de Empresas</a></h1>
		</div>
		<div class="breadcrumbs">
		</div>

		<div class="middle">
			
			<g:layoutBody/>			 		
		</div>
		
		<div class="right">
		
			<h2>Navigation</h2>
			
			<g:isLoggedIn>
				<g:render template="/login/sidebar_loggedin"/>
			</g:isLoggedIn>
			
		
		</div>

		<div id="clear"></div>

	</div>

	<div id="bottom"></div>

</div>

<div id="footer">
Design by <a href="http://www.minimalistic-design.net">Minimalistic Design</a>
</div>

</body>
</html>
