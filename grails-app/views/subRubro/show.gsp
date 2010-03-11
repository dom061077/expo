
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>SubRubro</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de SubRubros</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de SubRubro</g:link></span>
        </div>
        <div class="body">
            <h1>SubRubro</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:subRubroInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Nombre Subrubro:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:subRubroInstance, field:'nombreSubrubro')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Empresas:</td>
                            
                            <td  valign="top" style="text-align:left;" class="value">
                                <ul>
                                <g:each var="e" in="${subRubroInstance.empresas}">
                                    <li><g:link controller="empresa" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Rubro:</td>
                            
                            <td valign="top" class="value"><g:link controller="rubro" action="show" id="${subRubroInstance?.rubro?.id}">${subRubroInstance?.rubro?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${subRubroInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Modificar" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Esta seguro?');" value="Eliminar" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
