
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Listado Orden de Reserva</title>
		<script type="text/javascript" src='${resource(dir:'js/ordenreserva',file:'list.js')}'></script>        
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Alta de Orden de Reserva</g:link></span>
        </div>
        <div id="ordenreserva_form">
        </div>
    </body>
</html>
