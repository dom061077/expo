
<%@ page import="com.rural.Exposicion" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Exposicion</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Exposicion List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Exposicion</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Exposicion</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${exposicionInstance}">
            <div class="errors">
                <g:renderErrors bean="${exposicionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post"  enctype="multipart/form-data">
                <input type="hidden" name="id" value="${exposicionInstance?.id}" />
                <input type="hidden" name="version" value="${exposicionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="empresas">Empresas:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exposicionInstance,field:'empresas','errors')}">
                                    
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="imagen">Imagen:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exposicionInstance,field:'imagen','errors')}">
                                    <input type="file" id="imagen" name="imagen" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombre">Nombre:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exposicionInstance,field:'nombre','errors')}">
                                    <input type="text" id="nombre" name="nombre" value="${fieldValue(bean:exposicionInstance,field:'nombre')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sectores">Sectores:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:exposicionInstance,field:'sectores','errors')}">
                                    
<ul>
<g:each var="s" in="${exposicionInstance?.sectores?}">
    <li><g:link controller="sector" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="sector" params="['exposicion.id':exposicionInstance?.id]" action="create">Add Sector</g:link>

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
