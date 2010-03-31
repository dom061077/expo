
<%@ page import="com.rural.OrdenReserva" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit OrdenReserva</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">OrdenReserva List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New OrdenReserva</g:link></span>
        </div>
        <div class="body">
            <h1>Edit OrdenReserva</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${ordenReservaInstance}">
            <div class="errors">
                <g:renderErrors bean="${ordenReservaInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${ordenReservaInstance?.id}" />
                <input type="hidden" name="version" value="${ordenReservaInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="empresa">Empresa:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ordenReservaInstance,field:'empresa','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Empresa.list()}" name="empresa.id" value="${ordenReservaInstance?.empresa?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expo">Expo:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ordenReservaInstance,field:'expo','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Exposicion.list()}" name="expo.id" value="${ordenReservaInstance?.expo?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fechaAlta">Fecha Alta:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ordenReservaInstance,field:'fechaAlta','errors')}">
                                    <g:datePicker name="fechaAlta" value="${ordenReservaInstance?.fechaAlta}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="usuario">Usuario:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ordenReservaInstance,field:'usuario','errors')}">
                                    <g:select optionKey="id" from="${com.rural.seguridad.Person.list()}" name="usuario.id" value="${ordenReservaInstance?.usuario?.id}" ></g:select>
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
