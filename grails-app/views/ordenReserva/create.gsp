
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create OrdenReserva</title>
        <ext:stylesheet dir="plugins/wizard/resources/css" file="ext-ux-wiz.css"/>
        <ext:javascript dir="plugins/wizard" file="CardLayout.js"/>
        <ext:javascript dir="plugins/wizard" file="Wizard.js"/>
        <ext:javascript dir="plugins/wizard" file="Header.js"/>        
        <ext:javascript dir="plugins/wizard" file="Card.js"/>


        
       	<script type="text/javascript" src='${resource(dir:'js/ordenreserva',file:'create.js')}'></script>

                 
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado Ordenes de Reserva</g:link></span>
        </div>
        <div class="body">
        	<div id='formulario_extjs'>
        		
        	</div>
        	
        </div>
	</body>
</html>
