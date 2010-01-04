
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
    	<%
    		out << "<script type='text/javascript'>";
    		out << "var empresaId = ${id};";
    		out << "</script>";
    	%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type='text/javascript' src='${resource(dir:'js/empresa',file:'edit.js')}'> </script>
        <title>Modificar Empresa</title>
        
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Empresa List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Empresa</g:link></span>
        </div>
        <div class="body">
			<div id="formulario_extjs">
			</div>        
        </div>
        
        
		<div id="dep-win" class="x-hidden">
			<div id="form-panel-departamento">
				
			</div>
		</div>
		<div id="loc-win" class="x-hidden">
			
		</div>
    </body>
</html>
