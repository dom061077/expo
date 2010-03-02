

<html>
    <head>
    
    	<%
    		out << "<script type='text/javascript'>";
    		/*out << "var success = ${success};";
    		out << "var msgupload = '${msgupload}';";*/
    		out << "var patherroresapp='"+"${createLink(action:'downloadfileerrors',controller:'empresa')}"+"';";
    		out << "</script>";
    	%>
    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <script type='text/javascript' src='${resource(dir:'js/empresa',file:'uploadFile.js')}'> </script>
        <title>Modificar Empresa</title>
        
    </head>
    <body>
        <div class="nav">
        </div>
        
        <div id='progressBar'>
        </div>
        <div class="body">
			<div id="formularioupload">
			</div>        
        </div>
        
        

    </body>
</html>
