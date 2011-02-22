
<%@ page import="com.rural.Logo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type="text/javascript" src='${resource(dir:'js/logo' ,file:'list.js' )}' ></script>
        
        
        <script type="text/javascript">
        	var exposicionId = '<%out << "${expocisionId}"%>';
        	var exposicionNombre = '<% out << "${exposicionNombre}"%>';
        </script>
        <title>Logo List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create" params="[exposicionId:exposicionId,exposicionNombre:exposicionNombre]">Agregar Logo</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        		
        	</div>
        </div>
    </body>
</html>
