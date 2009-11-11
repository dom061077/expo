
<%@ page import="com.rural.Departamento" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Departamento</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Departamento List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Departamento</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${departamentoInstance}">
            <div class="errors">
                <g:renderErrors bean="${departamentoInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombre">Nombre:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:departamentoInstance,field:'nombre','errors')}">
                                    <input type="text" id="nombre" name="nombre" value="${fieldValue(bean:departamentoInstance,field:'nombre')}"/>
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
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
