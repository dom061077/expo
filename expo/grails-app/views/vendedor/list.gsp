
<%@ page import="com.rural.Vendedor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Listado de Vendedores</title>
        <script type="text/javascript" src='${resource(dir:'js/vendedor',file:'list.js')}'>
        </script>
    </head>
    
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Listado de Vendedores</g:link></span>
        </div>
        <div id="vendedor-grid">
        
        </div>
    </body>
</html>
