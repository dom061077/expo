
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Empresa</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Empresa List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Empresa</g:link></span>
        </div>
        <div class="body">
            <h1>Show Empresa</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Cuit:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'cuit')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Direccion:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'direccion')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Localidad:</td>
                            
                            <td valign="top" class="value"><g:link controller="localidad" action="show" id="${empresaInstance?.localidad?.id}">${empresaInstance?.localidad?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Nombre:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'nombre')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Nombre Representante:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'nombreRepresentante')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Provincia:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'provincia')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Telefono1:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'telefono1')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Telefono2:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:empresaInstance, field:'telefono2')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Vendedor:</td>
                            
                            <td valign="top" class="value"><g:link controller="vendedor" action="show" id="${empresaInstance?.vendedor?.id}">${empresaInstance?.vendedor?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${empresaInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
