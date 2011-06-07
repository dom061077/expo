<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
		<META HTTP-EQUIV="Cache-Control" CONTENT ="no-cache"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/resources/css',file:'ext-all.css')}" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/resources/css',file:'tabs.css')}" />        
        <link rel="shortcut icon" href="${resource(dir:'images',file:'srt.ico')}" type="image/x-icon" />

        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'jquery-1.5.1.min.js')}"></script>         
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'jquery.maphilight.min.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/ext/3.3.1/adapter/jquery',file:'ext-jquery-adapter.js')}"></script>

		<script type="text/javascript" src="${resource(dir:'js/ext/3.3.1/adapter/ext',file:'ext-base.js')}"></script>
        <script type="text/javascript" src="${resource(dir:'js/ext/3.3.1',file:'ext-all.js')}"></script>        
        <script type="text/javascript" src="${resource(dir:'js/ext/3.3.1/src/locale',file:'ext-lang-es.js')}"></script>
        
        <script type="text/javascript">
			<%
				out << "var imagePath='"+"${resource(dir:'images')}';";
				
			%>        	
			<%
				out << "var blankimagePath='"+"${resource(dir:'js')}';";
				
			%>   			
				//Ext.BLANK_IMAGE_URL =blankimagePath+ '/ext/2.0.2/resources/images/default/s.gif'; 
				Ext.BLANK_IMAGE_URL =blankimagePath+ '/ext/3.3.1/resources/images/default/s.gif';
        </script>        
        <g:layoutHead />        

        

        			

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
						<!-- li class='controller'><g:link controller="notaDC">Notas Débito/Crédito</g:link> </li -->
						<li class='controller'><g:link controller="lote" action="listprecios">Lista de precios</g:link>
						<li class='controller'><g:link controller="sector" action="listdescuentos">Descuentos por Sectores</g:link>						
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
