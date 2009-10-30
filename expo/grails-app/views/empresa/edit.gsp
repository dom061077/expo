
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Empresa</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Empresa List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Empresa</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Empresa</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${empresaInstance}">
            <div class="errors">
                <g:renderErrors bean="${empresaInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${empresaInstance?.id}" />
                <input type="hidden" name="version" value="${empresaInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="cuit">Cuit:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'cuit','errors')}">
                                    <input type="text" id="cuit" name="cuit" value="${fieldValue(bean:empresaInstance,field:'cuit')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="direccion">Direccion:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'direccion','errors')}">
                                    <input type="text" id="direccion" name="direccion" value="${fieldValue(bean:empresaInstance,field:'direccion')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="localidad">Localidad:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'localidad','errors')}">
                                    <input type="text" id="localidad" name="localidad" value="${fieldValue(bean:empresaInstance,field:'localidad')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombre">Nombre:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'nombre','errors')}">
                                    <input type="text" id="nombre" name="nombre" value="${fieldValue(bean:empresaInstance,field:'nombre')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombreRepresentante">Nombre Representante:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'nombreRepresentante','errors')}">
                                    <input type="text" id="nombreRepresentante" name="nombreRepresentante" value="${fieldValue(bean:empresaInstance,field:'nombreRepresentante')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="provincia">Provincia:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'provincia','errors')}">
                                    <input type="text" id="provincia" name="provincia" value="${fieldValue(bean:empresaInstance,field:'provincia')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="telefono1">Telefono1:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'telefono1','errors')}">
                                    <input type="text" id="telefono1" name="telefono1" value="${fieldValue(bean:empresaInstance,field:'telefono1')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="telefono2">Telefono2:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:empresaInstance,field:'telefono2','errors')}">
                                    <input type="text" id="telefono2" name="telefono2" value="${fieldValue(bean:empresaInstance,field:'telefono2')}"/>
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
