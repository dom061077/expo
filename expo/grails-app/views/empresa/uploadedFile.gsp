

<html>
    <head>
    
    	<%
    		out << "<script type='text/javascript'>";
    		out << "var success = ${success};";
    		out << "var msgupload = '${msgupload}';";
    		out << "</script>";
    	%>
    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Procesando archivo subido</title>
        
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