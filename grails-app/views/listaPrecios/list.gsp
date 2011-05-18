
<%@ page import="com.rural.ListaPrecios" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>ListaPrecios List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New ListaPrecios</g:link></span>
        </div>
        <div class="body">
            <h1>ListaPrecios List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <th>Sector</th>
                   	    
                   	        <th>Lote</th>
                   	    
                   	        <th>Expo</th>
                   	    
                   	        <g:sortableColumn property="precio" title="Precio" />
                        
                   	        <g:sortableColumn property="vigencia" title="Vigencia" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${listaPreciosInstanceList}" status="i" var="listaPreciosInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${listaPreciosInstance.id}">${fieldValue(bean:listaPreciosInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:listaPreciosInstance, field:'sector')}</td>
                        
                            <td>${fieldValue(bean:listaPreciosInstance, field:'lote')}</td>
                        
                            <td>${fieldValue(bean:listaPreciosInstance, field:'expo')}</td>
                        
                            <td>${fieldValue(bean:listaPreciosInstance, field:'precio')}</td>
                        
                            <td>${fieldValue(bean:listaPreciosInstance, field:'vigencia')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${listaPreciosInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
