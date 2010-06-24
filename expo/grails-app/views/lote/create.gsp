
<%@ page import="com.rural.Lote" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Lote</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Lote List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Lote</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${loteInstance}">
            <div class="errors">
                <g:renderErrors bean="${loteInstance}" as="list" />
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
                                <td valign="top" class="value ${hasErrors(bean:loteInstance,field:'nombre','errors')}">
                                    <input type="text" id="nombre" name="nombre" value="${fieldValue(bean:loteInstance,field:'nombre')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sector">Sector:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:loteInstance,field:'sector','errors')}">
                                    <g:select optionKey="id" from="${com.rural.Sector.list()}" name="sector.id" value="${loteInstance?.sector?.id}" ></g:select>
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
