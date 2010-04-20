
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>OrdenReserva List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Alta de Orden de Reserva</g:link></span>
        </div>
        <div class="body">
            <h1>OrdenReserva List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <th>Empresa</th>
                   	    
                   	        <th>Expo</th>
                   	    
                   	        <g:sortableColumn property="fechaAlta" title="Fecha Alta" />
                        
                   	        <th>Usuario</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${ordenReservaInstanceList}" status="i" var="ordenReservaInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${ordenReservaInstance.id}">${fieldValue(bean:ordenReservaInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:ordenReservaInstance, field:'empresa')}</td>
                        
                            <td>${fieldValue(bean:ordenReservaInstance, field:'expo')}</td>
                        
                            <td>${fieldValue(bean:ordenReservaInstance, field:'fechaAlta')}</td>
                        
                            <td>${fieldValue(bean:ordenReservaInstance, field:'usuario')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${ordenReservaInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
