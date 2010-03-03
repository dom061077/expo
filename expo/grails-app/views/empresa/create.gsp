
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Alta de Empresa</title>
    	<style type="text/css">
.list {list-style:square;width:500px;padding-left:16px;}
.list li{padding:2px;font-size:8pt;}

pre {
   font-size:11px; 
}

.x-tab-panel-body .x-panel-body {
    padding:10px;
}

/* default loading indicator for ajax calls */
.loading-indicator {
	font-size:8pt;
	background-image:url('../../resources/images/default/grid/loading.gif');
	background-repeat: no-repeat;
	background-position: left;
	padding-left:20px;
}

.new-tab {
    background-image:url(../feed-viewer/images/new_tab.gif) !important;
}


.tabs {
    background-image:url( ../desktop/images/tabs.gif ) !important;
}    	
		 </style>
        
    	<script type="text/javascript" src='${resource(dir:'js/empresa',file:'create.js')}'></script>
    	<style type="text/css">
    	
    	</style>
	                 
    </head>
    
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Listado de Empresas</g:link></span>
        </div>
        <div class="body">
			<div id="formulario_extjs">
			</div>        
        </div>
        
        
		<div id="dep-win" class="x-hidden">
			<div id="form-panel-departamento">
				
			</div>
		</div>
		<div id="loc-win" class="x-hidden">
			
		</div>
    </body>
    
    
</html>
