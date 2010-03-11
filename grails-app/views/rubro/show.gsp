
<%@ page import="com.rural.Rubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Rubro</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Rubros</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de Rubro</g:link></span>
        </div>
        <div class="body">
            <h1>Show Rubro</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:rubroInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Nombre Rubro:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:rubroInstance, field:'nombreRubro')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Sub Rubros:</td>
                            
                            <td  valign="top" style="text-align:left;" class="value">
                                <ul>
                                <g:each var="s" in="${rubroInstance.subRubros}">
                                    <li><g:link controller="subRubro" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${rubroInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Modificar" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Esta seguro?');" value="Borrar" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
