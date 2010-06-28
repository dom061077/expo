
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        	<%
    		out << "<script type='text/javascript'>"
    		out << "var subrubroId=${id};"
    		out << "</script>"
    	%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Modificar Sub Rubro</title>
        <script type="text/javascript" src='${resource(dir:'js/subrubro',file:'edit.js')}'></script>  
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Sub Rubros</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Sub Rubro</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
