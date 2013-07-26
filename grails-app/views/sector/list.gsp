
<%@ page import="com.rural.Sector" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/plugins/ux/css',file:'RowEditor.css')}" />
        <script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux',file:'RowEditor.js')}'></script>
        <script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/ux',file:'CheckColumn.js')}'></script>
        
		<script type="text/javascript" src='${resource(dir:'js/sector',file:'list.js')+'?id='+randomlink}'></script>        
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
        </div>  
        <div id="listaprecios_extjs"></div>      
    </body>
</html>
