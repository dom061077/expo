
<%@ page import="com.rural.Lote" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Lote List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Lote</g:link></span>
        </div>
        <div class="body">
            <h1>Lote List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="nombre" title="Nombre" />
                        
                   	        <th>Sector</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${loteInstanceList}" status="i" var="loteInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${loteInstance.id}">${fieldValue(bean:loteInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:loteInstance, field:'nombre')}</td>
                        
                            <td>${fieldValue(bean:loteInstance, field:'sector')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${loteInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
