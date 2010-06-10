
<%@ page import="com.rural.Recibo" %>
<html>
    <head>
    	<%
    		out << "<script type='text/javascript'>";
    		out << "var ordenreservaId=${ordenreservaId};"
    		out << "</script>";
    	%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Alta de Recibo</title>
        
        <script type="text/javascript" src='${resource(dir:'js/recibo',file:'create.js')}'></script>         
    </head>
    <body>
    	<div class="nav">
    		<span class="menuButton"><g:link class="list" action="list">Listado de Recibos </g:link></span>
    	</div>
    	<div class="body">
    		<div id='recibo_form'>
    		
    		</div>
    	</div>
    </body>
</html>
