
<%@ page import="com.rural.Rubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Rubro List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Rubro</g:link></span>
        </div>
        <div class="body">
            <h1>Rubro List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="nombreRubro" title="Nombre Rubro" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${rubroInstanceList}" status="i" var="rubroInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${rubroInstance.id}">${fieldValue(bean:rubroInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:rubroInstance, field:'nombreRubro')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${rubroInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
