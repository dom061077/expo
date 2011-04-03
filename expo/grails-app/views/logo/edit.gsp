
<%@ page import="com.rural.Logo" %>
<html>
    <head>
		<% 
			out << "<script type='text/javascript'>";
			out << "var logoId = ${id};";
			

			out << "</script>";
		%>
    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type="text/javascript" src='${resource(dir:'js/logo',file:'edit.js')}'></script>
        <title>Modificación de Logo de Exposición</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado logos de Exposición</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Logo de Exposición</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
