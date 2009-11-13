
<%@ page import="com.rural.Departamento" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Departamento</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Departamento List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Departamento</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Departamento</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${departamentoInstance}">
            <div class="errors">
                <g:renderErrors bean="${departamentoInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${departamentoInstance?.id}" />
                <input type="hidden" name="version" value="${departamentoInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="localidades">Localidades:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:departamentoInstance,field:'localidades','errors')}">
                                    
<ul>
<g:each var="l" in="${departamentoInstance?.localidades?}">
    <li><g:link controller="localidad" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="localidad" params="['departamento.id':departamentoInstance?.id]" action="create">Add Localidad</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombreDep">Nombre Dep:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:departamentoInstance,field:'nombreDep','errors')}">
                                    <input type="text" id="nombreDep" name="nombreDep" value="${fieldValue(bean:departamentoInstance,field:'nombreDep')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="provincia">Provincia:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:departamentoInstance,field:'provincia','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Provincia.list()}" name="provincia.id" value="${departamentoInstance?.provincia?.id}" ></g:select>
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
