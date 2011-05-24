
<%@ page import="com.rural.NotaDC" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>NotaDC List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New NotaDC</g:link></span>
        </div>
        <div class="body">
            <h1>NotaDC List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="numero" title="Numero" />
                        
                   	        <g:sortableColumn property="tipo" title="Tipo" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${notaDCInstanceList}" status="i" var="notaDCInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${notaDCInstance.id}">${fieldValue(bean:notaDCInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:notaDCInstance, field:'numero')}</td>
                        
                            <td>${fieldValue(bean:notaDCInstance, field:'tipo')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${notaDCInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
