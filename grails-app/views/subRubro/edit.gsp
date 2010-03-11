
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit SubRubro</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de SubRubros</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">Alta de SubRubro</g:link></span>
        </div>
        <div class="body">
            <h1>Edit SubRubro</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${subRubroInstance}">
            <div class="errors">
                <g:renderErrors bean="${subRubroInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${subRubroInstance?.id}" />
                <input type="hidden" name="version" value="${subRubroInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombreSubrubro">Nombre Subrubro:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:subRubroInstance,field:'nombreSubrubro','errors')}">
                                    <input type="text" id="nombreSubrubro" name="nombreSubrubro" value="${fieldValue(bean:subRubroInstance,field:'nombreSubrubro')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="empresas">Empresas:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:subRubroInstance,field:'empresas','errors')}">
                                    
<ul>
<g:each var="e" in="${subRubroInstance?.empresas?}">
    <li><g:link controller="empresa" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="empresa" params="['subRubro.id':subRubroInstance?.id]" action="create">Add Empresa</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="rubro">Rubro:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:subRubroInstance,field:'rubro','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Rubro.list()}" name="rubro.id" value="${subRubroInstance?.rubro?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Modificar" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Esta seguro?');" value="Eliminar" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
