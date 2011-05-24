
<%@ page import="com.rural.NotaDC" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit NotaDC</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">NotaDC List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New NotaDC</g:link></span>
        </div>
        <div class="body">
            <h1>Edit NotaDC</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${notaDCInstance}">
            <div class="errors">
                <g:renderErrors bean="${notaDCInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${notaDCInstance?.id}" />
                <input type="hidden" name="version" value="${notaDCInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numero">Numero:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:notaDCInstance,field:'numero','errors')}">
                                    <input type="text" id="numero" name="numero" value="${fieldValue(bean:notaDCInstance,field:'numero')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tipo">Tipo:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:notaDCInstance,field:'tipo','errors')}">
                                    <g:select  from="${com.rural.enums.TipoNotaEnum?.values()}" value="${notaDCInstance?.tipo}" name="tipo" ></g:select>
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
