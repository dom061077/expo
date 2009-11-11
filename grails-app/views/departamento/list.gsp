
<%@ page import="com.rural.Departamento" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Departamento List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Departamento</g:link></span>
        </div>
        <div class="body">
            <h1>Departamento List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="nombre" title="Nombre" />
                        
                   	        <th>Provincia</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${departamentoInstanceList}" status="i" var="departamentoInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${departamentoInstance.id}">${fieldValue(bean:departamentoInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:departamentoInstance, field:'nombre')}</td>
                        
                            <td>${fieldValue(bean:departamentoInstance, field:'provincia')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${departamentoInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
