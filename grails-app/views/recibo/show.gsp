
<%@ page import="com.rural.Recibo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Recibo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Recibo List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Recibo</g:link></span>
        </div>
        <div class="body">
            <h1>Show Recibo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:reciboInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Numero:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:reciboInstance, field:'numero')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Cheques:</td>
                            
                            <td  valign="top" style="text-align:left;" class="value">
                                <ul>
                                <g:each var="c" in="${reciboInstance.cheques}">
                                    <li><g:link controller="cheque" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Fecha Alta:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:reciboInstance, field:'fechaAlta')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Orden Reserva:</td>
                            
                            <td valign="top" class="value"><g:link controller="ordenReserva" action="show" id="${reciboInstance?.ordenReserva?.id}">${reciboInstance?.ordenReserva?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Total:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:reciboInstance, field:'total')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${reciboInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
