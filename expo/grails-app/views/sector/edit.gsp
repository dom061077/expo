
<%@ page import="com.rural.Sector" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Sector</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Sector List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Sector</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Sector</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sectorInstance}">
            <div class="errors">
                <g:renderErrors bean="${sectorInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${sectorInstance?.id}" />
                <input type="hidden" name="version" value="${sectorInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expo">Expo:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sectorInstance,field:'expo','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Exposicion.list()}" name="expo.id" value="${sectorInstance?.expo?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lotes">Lotes:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sectorInstance,field:'lotes','errors')}">
                                    
<ul>
<g:each var="l" in="${sectorInstance?.lotes?}">
    <li><g:link controller="lote" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="lote" params="['sector.id':sectorInstance?.id]" action="create">Add Lote</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nombre">Nombre:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sectorInstance,field:'nombre','errors')}">
                                    <input type="text" id="nombre" name="nombre" value="${fieldValue(bean:sectorInstance,field:'nombre')}"/>
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
