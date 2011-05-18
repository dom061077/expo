
<%@ page import="com.rural.ListaPrecios" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show ListaPrecios</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">ListaPrecios List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New ListaPrecios</g:link></span>
        </div>
        <div class="body">
            <h1>Show ListaPrecios</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:listaPreciosInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Sector:</td>
                            
                            <td valign="top" class="value"><g:link controller="sector" action="show" id="${listaPreciosInstance?.sector?.id}">${listaPreciosInstance?.sector?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Lote:</td>
                            
                            <td valign="top" class="value"><g:link controller="lote" action="show" id="${listaPreciosInstance?.lote?.id}">${listaPreciosInstance?.lote?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Expo:</td>
                            
                            <td valign="top" class="value"><g:link controller="exposicion" action="show" id="${listaPreciosInstance?.expo?.id}">${listaPreciosInstance?.expo?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Precio:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:listaPreciosInstance, field:'precio')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Vigencia:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:listaPreciosInstance, field:'vigencia')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${listaPreciosInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
