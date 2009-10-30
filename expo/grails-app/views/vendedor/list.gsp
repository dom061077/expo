
<%@ page import="com.rural.Vendedor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Vendedor List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Vendedor</g:link></span>
        </div>
        <div class="body">
            <h1>Vendedor List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="nombre" title="Nombre" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${vendedorInstanceList}" status="i" var="vendedorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${vendedorInstance.id}">${fieldValue(bean:vendedorInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:vendedorInstance, field:'nombre')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${vendedorInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
