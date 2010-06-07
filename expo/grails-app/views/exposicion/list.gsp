
<%@ page import="com.rural.Exposicion" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Exposicion List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Exposicion</g:link></span>
        </div>
        <div class="body">
            <h1>Exposicion List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="imagen" title="Imagen" />
                        
                   	        <g:sortableColumn property="nombre" title="Nombre" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${exposicionInstanceList}" status="i" var="exposicionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${exposicionInstance.id}">${fieldValue(bean:exposicionInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:exposicionInstance, field:'imagen')}</td>
                        
                            <td>${fieldValue(bean:exposicionInstance, field:'nombre')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${exposicionInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
