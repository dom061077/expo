
<%@ page import="com.rural.Recibo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Recibo List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Recibo</g:link></span>
        </div>
        <div class="body">
            <h1>Recibo List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="numero" title="Numero" />
                        
                   	        <g:sortableColumn property="fechaAlta" title="Fecha Alta" />
                        
                   	        <th>Orden Reserva</th>
                   	    
                   	        <g:sortableColumn property="total" title="Total" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${reciboInstanceList}" status="i" var="reciboInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${reciboInstance.id}">${fieldValue(bean:reciboInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:reciboInstance, field:'numero')}</td>
                        
                            <td>${fieldValue(bean:reciboInstance, field:'fechaAlta')}</td>
                        
                            <td>${fieldValue(bean:reciboInstance, field:'ordenReserva')}</td>
                        
                            <td>${fieldValue(bean:reciboInstance, field:'total')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${reciboInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
