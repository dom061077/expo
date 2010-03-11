
<%@ page import="com.rural.SubRubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Alta de SubRubro</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Subrubros</g:link></span>
        </div>
        <div class="body">
            <h1>Create SubRubro</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${subRubroInstance}">
            <div class="errors">
                <g:renderErrors bean="${subRubroInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
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
                    <span class="button"><input class="save" type="submit" value="Guardar" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
