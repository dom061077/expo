
<%@ page import="com.rural.Vendedor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Modificar Vendedor</title>
        <script type="text/javascript">
        	<%
        		out << "var vendedorId = ${id};";
        		
        	%>
        </script>
        <script type="text/javascript" src='${resource(dir:'js/vendedor',file:'edit.js?4')}'></script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Vendedores</g:link></span>
            <span class="menuButton"><g:link class="list" action="create">Agregar Vendedor</g:link></span>            
        </div>
    
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
