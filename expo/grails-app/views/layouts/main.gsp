<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
		<META HTTP-EQUIV="Cache-Control" CONTENT ="no-cache"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/resources/css',file:'ext-all.css')}" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/resources/css',file:'tabs.css')}" />

        <link rel="stylesheet" href="${resource(dir:'css/menu',file:'dropdown.css')}" type="text/css" media="screen, projection"/>        

        <link rel="stylesheet" href="${resource(dir:'css/menu',file:'default.advanced.css')}" type="text/css" media="screen, projection"/>
        
                
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

            <div>
				<g:isLoggedIn>
					<ul class="dropdown dropdown-horizontal">
						<li><a href="#" class="dir">Actualizaciones</a>
							<ul>
								<li><g:link controller="empresa">Empresas </g:link></li>
								<li><g:link controller="empresa" action="empresassimilares">Empresas Similares</g:link></li>
								<g:ifAllGranted role="ROLE_ADMIN">					
									<li><g:link controller="empresa" action="uploadFile">Subir Archivos Excel</g:link></li>
								</g:ifAllGranted>
							</ul>
						</li>
						<li><a href="#" class="dir">Ventas</a>
							<ul>
								<li><g:link controller="ordenReserva" action="list">Orden de Reserva </g:link></li>
								<li><g:link controller="recibo" action="list">Recibo </g:link></li>					
							</ul>
						</li>
						<g:ifAllGranted role="ROLE_ADMIN">
							<li><a href="#" class="dir">Operaciones de Administrador</a>
								<ul>
									<li><g:link controller="lote" action="listprecios">Lista de precios</g:link>
									<li><g:link controller="sector" action="listdescuentos">Descuentos por Sectores</g:link>						
									<li><g:link controller="sector">Sectores</g:link> </li>
									<li><g:link controller="vendedor">Vendedores </g:link> </li>
									<li><g:link controller="rubro">Rubro </g:link> </li>
									<li><g:link controller="subRubro">SubRubro </g:link> </li>
									<li><g:link controller="exposicion">Exposición </g:link> </li>						
									<li><g:link controller="person">Usuario</g:link> </li>
								</ul>
							</li>												
						</g:ifAllGranted>
						<li><a href="#" class="dir">Usuario</a>
							<ul>
								<li><g:link controller="person" action="editpassw">Cambiar Contraseña</g:link></li>
								<li><g:link controller="logout" action="index">Cerrar sesión de <g:loggedInUserInfo field="userRealName">Guest</g:loggedInUserInfo></g:link></li>
							</ul>
						</li>
					</ul>
					
				</g:isLoggedIn>
            
            
            </div>
		
		<div class="breadcrumbs">
		</div>

		<div class="middle">
			
			<g:layoutBody/>
						 		
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
