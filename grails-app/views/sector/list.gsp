
<%@ page import="com.rural.Sector" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		<script type="text/javascript" src='${resource(dir:'js/sector',file:'list.js')}'></script>        
        <title>Listado de Sectores</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Alta de Sector</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
		<div id="lotewin_extjs">
        </div        
    </body>
</html>
