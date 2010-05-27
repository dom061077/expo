
<%@ page import="com.rural.Recibo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Recibo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Recibo List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Recibo</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Recibo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reciboInstance}">
            <div class="errors">
                <g:renderErrors bean="${reciboInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${reciboInstance?.id}" />
                <input type="hidden" name="version" value="${reciboInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numero">Numero:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:reciboInstance,field:'numero','errors')}">
                                    <input type="text" id="numero" name="numero" value="${fieldValue(bean:reciboInstance,field:'numero')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="cheques">Cheques:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:reciboInstance,field:'cheques','errors')}">
                                    
<ul>
<g:each var="c" in="${reciboInstance?.cheques?}">
    <li><g:link controller="cheque" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="cheque" params="['recibo.id':reciboInstance?.id]" action="create">Add Cheque</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fechaAlta">Fecha Alta:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:reciboInstance,field:'fechaAlta','errors')}">
                                    <g:datePicker name="fechaAlta" value="${reciboInstance?.fechaAlta}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="ordenReserva">Orden Reserva:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:reciboInstance,field:'ordenReserva','errors')}">
                                    <g:select optionKey="id" from="${com.rural.OrdenReserva.list()}" name="ordenReserva.id" value="${reciboInstance?.ordenReserva?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="total">Total:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:reciboInstance,field:'total','errors')}">
                                    <input type="text" id="total" name="total" value="${fieldValue(bean:reciboInstance,field:'total')}" />
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
