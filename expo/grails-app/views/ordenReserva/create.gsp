
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Alta Orden de Reserva</title>
<%--        <ext:stylesheet dir="plugins/wizard/resources/css" file="ext-ux-wiz.css"/>--%>
        <script type="text/javascript" src="${resource(dir:"/js/ext/2.0.2/plugins/wizard",file:"CardLayout.js")}"></script>
        <script type="text/javascript" src="${resource(dir:"/js/ext/2.0.2/plugins/wizard",file:"Wizard.js")}"></script>
        <script type="text/javascript" src="${resource(dir:"/js/ext/2.0.2/plugins/wizard",file:"Header.js")}"></script>
        <script type="text/javascript" src="${resource(dir:"/js/ext/2.0.2/plugins/wizard",file:"Card.js")}"></script>


        
       	<!--script type="text/javascript" src='${resource(dir:'js/ordenreserva',file:'create.js')+'?id='+randomlink}'></script-->
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
