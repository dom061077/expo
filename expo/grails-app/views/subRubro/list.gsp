
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Listado de Subrubros</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Alta de SubRubro</g:link></span>
        </div>
        <div class="body">
            <h1>Listado de SubRubros</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="nombreSubrubro" title="Nombre Subrubro" />
                        
                   	        <th>Rubro</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${subRubroInstanceList}" status="i" var="subRubroInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${subRubroInstance.id}">${fieldValue(bean:subRubroInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:subRubroInstance, field:'nombreSubrubro')}</td>
                        
                            <td>${fieldValue(bean:subRubroInstance, field:'rubro')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${subRubroInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
