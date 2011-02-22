
<%@ page import="com.rural.Logo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type="text/javascript" src="${resource(dir:'js/logo',file:'create.js')}"> </script>
        <script type="text/javascript">
        	var exposicionId=<%out << "${exposicionId}" %>;
        	var exposicionNombre='<%out << "${exposicionNombre}" %>';
        </script>
        <title>Alta de Logo</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Logos</g:link></span>
        </div>
        <div class="body">
        	<div id="formulario_extjs">
        	</div>
        </div>
    </body>
</html>
