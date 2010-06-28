
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Listado de Sub Rubros</title>
        <script type="text/javascript" src='${resource(dir:'js/subrubro',file:'list.js')}'></script>          
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Alta de SubRubro</g:link></span>
        </div>
        <div class="body">
        	<div id='formulario_extjs'>
        	</div>
        </div>
    </body>
</html>
