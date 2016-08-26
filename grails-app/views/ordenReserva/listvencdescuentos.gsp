<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="layout" content="main" />
        <title>Vencimientos de Descuentos</title>
        <script type="text/javascript" src='${resource(dir:'js/ordenreserva',file:'vencdescuentos.js')}'>
        </script>        
        <script type="text/javascript">
            var ordenId = '<% out << "${ordenInstance.id}";%>';
        </script>    
    </head>
    <body>
        Vencimientos de la orden N°: ${ordenInstance.numero}
        <div id="vencimientos-grid">
            <a href="listvencdescuentos.gsp"></a>
        </div>
    </body>
</html>
