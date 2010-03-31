
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create OrdenReserva</title>
        <ext:javascript dir="plugins" file="Ext.ux.PowerWizard.js"/>
        
       	<script type="text/javascript" src='${resource(dir:'js/ordenreserva',file:'create.js')}'></script>
                 
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">OrdenReserva List</g:link></span>
        </div>
        <div class="body">
        	<div id='formulario_extjs'>
        		
        	</div>
        </div>
	</body>
</html>
