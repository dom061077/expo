
<%@ page import="com.rural.Rubro" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Rubro</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Rubro List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Rubro</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Rubro</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${rubroInstance}">
            <div class="errors">
                <g:renderErrors bean="${rubroInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${rubroInstance?.id}" />
                <input type="hidden" name="version" value="${rubroInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombreRubro">Nombre Rubro:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:rubroInstance,field:'nombreRubro','errors')}">
                                    <input type="text" id="nombreRubro" name="nombreRubro" value="${fieldValue(bean:rubroInstance,field:'nombreRubro')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="subRubros">Sub Rubros:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:rubroInstance,field:'subRubros','errors')}">
                                    
<ul>
<g:each var="s" in="${rubroInstance?.subRubros?}">
    <li><g:link controller="subRubro" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="subRubro" params="['rubro.id':rubroInstance?.id]" action="create">Add SubRubro</g:link>

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
