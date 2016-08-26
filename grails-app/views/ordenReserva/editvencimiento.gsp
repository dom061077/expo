<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="main" />
        <script type="text/javascript">
            var idVencimiento = <%out << "${vencInstance?.id}"%>;
            var ordenId = <%out << vencInstance.detalleServicioContratado.ordenReserva.id%>;
        </script>
        <script type="text/javascript" src="${resource(dir:'js/ordenreserva',file:'editvencimiento.js')}"></script>
        <title>Modificar Vencimiento</title>
    </head>
    <body>
        <div id='formulario_extjs'>
	</div>
    </body>
</html>
