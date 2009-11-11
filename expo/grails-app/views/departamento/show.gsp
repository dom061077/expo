
<%@ page import="com.rural.Departamento" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Departamento</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Departamento List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Departamento</g:link></span>
        </div>
        <div class="body">
            <h1>Show Departamento</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:departamentoInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Localidades:</td>
                            
                            <td  valign="top" style="text-align:left;" class="value">
                                <ul>
                                <g:each var="l" in="${departamentoInstance.localidades}">
                                    <li><g:link controller="localidad" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Nombre:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:departamentoInstance, field:'nombre')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Provincia:</td>
                            
                            <td valign="top" class="value"><g:link controller="provincia" action="show" id="${departamentoInstance?.provincia?.id}">${departamentoInstance?.provincia?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${departamentoInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
