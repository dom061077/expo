
<%@ page import="com.rural.Exposicion" %>
<html>
    <head>
		<% 
			out << "<script type='text/javascript'>";
			out << "var exposicionId = ${id};";
			

			out << "</script>";
		%>
    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type="text/javascript" src='${resource(dir:'js/exposicion',file:'edit.js')}'></script>
        <title>Modificación Exposición</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Exposición</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Exposición</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
