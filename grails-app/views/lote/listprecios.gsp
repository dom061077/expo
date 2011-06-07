<%@ page import="com.rural.Lote" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/plugins/ux/css',file:'RowEditor.css')}" />
        <script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux',file:'RowEditor.js')}'></script>

		<link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/css',file:'GridFilters.css')}" />				
		<link rel="stylesheet" href="${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/css',file:'RangeMenu.css')}" />		
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/menu',file:'RangeMenu.js')}'></script>		
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/menu',file:'ListMenu.js')}'></script>		
        
        
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters',file:'GridFilters.js')}'></script>
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'Filter.js')}'></script>		
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'BooleanFilter.js')}'></script>		
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'DateFilter.js')}'></script>
		
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'ListFilter.js')}'></script>
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'NumericFilter.js')}'></script>
		<script type="text/javascript" src='${resource(dir:'js/ext/3.3.1/plugins/ux/gridfilters/filter',file:'StringFilter.js')}'></script>
        
        
        <script type="text/javascript" src='${resource(dir:'js/lote',file:'listprecios.js')}'></script>
        <title>Alta de Precios de Lotes</title>         
        
    </head>
    <body>
    	<div id="listaprecios_extjs">
    	
    	</div>

    
    </body>
</html>
