
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Empresa List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Empresa</g:link></span>
        </div>
        <div class="body">
            <h1>Empresa List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="cuit" title="Cuit" />
                        
                   	        <g:sortableColumn property="direccion" title="Direccion" />
                        
                   	        <th>Localidad</th>
                   	    
                   	        <g:sortableColumn property="nombre" title="Nombre" />
                        
                   	        <g:sortableColumn property="nombreRepresentante" title="Nombre Representante" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${empresaInstanceList}" status="i" var="empresaInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${empresaInstance.id}">${fieldValue(bean:empresaInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:empresaInstance, field:'cuit')}</td>
                        
                            <td>${fieldValue(bean:empresaInstance, field:'direccion')}</td>
                        
                            <td>${fieldValue(bean:empresaInstance, field:'localidad')}</td>
                        
                            <td>${fieldValue(bean:empresaInstance, field:'nombre')}</td>
                        
                            <td>${fieldValue(bean:empresaInstance, field:'nombreRepresentante')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${empresaInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
