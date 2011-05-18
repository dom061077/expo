
<%@ page import="com.rural.ListaPrecios" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit ListaPrecios</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">ListaPrecios List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New ListaPrecios</g:link></span>
        </div>
        <div class="body">
            <h1>Edit ListaPrecios</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${listaPreciosInstance}">
            <div class="errors">
                <g:renderErrors bean="${listaPreciosInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${listaPreciosInstance?.id}" />
                <input type="hidden" name="version" value="${listaPreciosInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sector">Sector:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:listaPreciosInstance,field:'sector','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Sector.list()}" name="sector.id" value="${listaPreciosInstance?.sector?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lote">Lote:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:listaPreciosInstance,field:'lote','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Lote.list()}" name="lote.id" value="${listaPreciosInstance?.lote?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expo">Expo:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:listaPreciosInstance,field:'expo','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Exposicion.list()}" name="expo.id" value="${listaPreciosInstance?.expo?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="precio">Precio:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:listaPreciosInstance,field:'precio','errors')}">
                                    <input type="text" id="precio" name="precio" value="${fieldValue(bean:listaPreciosInstance,field:'precio')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="vigencia">Vigencia:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:listaPreciosInstance,field:'vigencia','errors')}">
                                    <g:datePicker name="vigencia" value="${listaPreciosInstance?.vigencia}" precision="day" ></g:datePicker>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
