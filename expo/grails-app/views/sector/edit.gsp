
<%@ page import="com.rural.Sector" %>
<html>
    <head>
		<% 
			out << "<script type='text/javascript'>";
			out << "var sectorId = ${id};";
			

			out << "</script>";
		%>    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Modificación de Sector</title>
		<script type="text/javascript" src='${resource(dir:'js/sector',file:'edit.js')}'></script>        
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Sectores</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Sector</g:link></span>
        </div>
        
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
