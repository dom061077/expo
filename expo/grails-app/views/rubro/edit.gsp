
<%@ page import="com.rural.Rubro" %>
<html>
    <head>
    	<%
    		out << "<script type='text/javascript'>"
    		out << "var rubroId=${id};"
    		out << "</script>"
    	%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Modificar Rubro</title>
        <script type="text/javascript" src='${resource(dir:'js/rubro',file:'edit.js')}'></script>  
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Rubros</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Rubro</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
