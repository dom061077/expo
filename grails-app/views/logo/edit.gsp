
<%@ page import="com.rural.Logo" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Logo</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Logo List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Logo</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Logo</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${logoInstance}">
            <div class="errors">
                <g:renderErrors bean="${logoInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post"  enctype="multipart/form-data">
                <input type="hidden" name="id" value="${logoInstance?.id}" />
                <input type="hidden" name="version" value="${logoInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="image">Image:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logoInstance,field:'image','errors')}">
                                    <input type="file" id="image" name="image" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="anio">Anio:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logoInstance,field:'anio','errors')}">
                                    <input type="text" id="anio" name="anio" value="${fieldValue(bean:logoInstance,field:'anio')}" />
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
