<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="ext"  />
        	
        <ext:javascript dir="" file="ext-all.js"/>
        <ext:javascript dir="build/locale" file="ext-lang-es.js"/>
        <ext:stylesheet dir="resources/css" file="ext-all.css"/>
        <ext:stylesheet dir="resources/css" file="tabs.css"/>
        <ext:javascript dir="build/locale" file="ext-lang-es.js"/>
        
        
        <g:layoutHead />
        
        <script type="text/javascript">
			<%
				out << "var imagePath='"+"${resource(dir:'images')}';";
				
			%>        	

        </script>
        			

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
			<g:isLoggedIn>
				<g:render template="/login/sidebar_loggedin"/>
				<h2>Navegación</h2>
				<ul>
					<li class='controller'><g:link controller="empresa">Empresas </g:link></li>
					<li class='controller'><g:link controller="empresa" action="empresassimilares">Empresas Similares</g:link></li>
					<li class='controller'><g:link controller="ordenReserva" action="list">Orden de Reserva </g:link></li>
					<li class='controller'><g:link controller="recibo" action="list">Recibo </g:link></li>					
					<g:ifAllGranted role="ROLE_ADMIN">					
						<li class='controller'><g:link controller="empresa" action="uploadFile">Subir Archivos Excel</g:link></li>
					</g:ifAllGranted>
					<g:ifAllGranted role="ROLE_ADMIN">
						<li class='controller'><g:link controller="sector">Sectores</g:link> </li>
						<li class='controller'><g:link controller="vendedor">Vendedores </g:link> </li>
						<li class='controller'><g:link controller="rubro">Rubro </g:link> </li>
						<li class='controller'><g:link controller="subRubro">SubRubro </g:link> </li>
						<li class='controller'><g:link controller="exposicion">Exposición </g:link> </li>						
						<li class='controller'><g:link controller="person">Usuario</g:link> </li>												
					</g:ifAllGranted>
				</ul>
				
			</g:isLoggedIn>
			
			
			
		
		</div>

		<div id="clear"></div>

	</div>

	<div id="bottom"></div>
	
</div>

<div id="footer">
<a href="http://www.srt.org.ar">Sociedad Rural de Tucum&aacute;n</a>
</div>

</body>
</html>
