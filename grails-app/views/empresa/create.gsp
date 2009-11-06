
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Empresa</title>
        
        <script type="text/javascript">
	        Ext.onReady(function(){
	        	var empresa_form = new Ext.FormPanel({
	        	url: 'movie-form-submit.php',
	        	renderTo: 'formulario_extjs',
	        	frame: true,
	        	title: 'Alta de Empresa',
	        	width: 250,
	        	items: [{
	        	xtype: 'textfield',
	        	fieldLabel: 'Nombre',
	        	name: 'nombre'
	        	},{
	        	xtype: 'textfield',
	        	fieldLabel: 'C.U.I.T',
	        	name: 'cuit'
	        	},{
	        	xtype: 'textfield',
	        	fieldLabel: 'Representante',
	        	name: 'nombreRepresentante'
	        	}],
	        	buttons:  	[
	        				 { 	text: 'Guardar',
	        				 	handler:	function(){
	        				 					empresa_form.getForm().submit({
	        				 							success: function(f,a){
	        				 									Ext.Msg.alert('Success','Funciona');
	        				 								},
	        				 							failure: function(f,a){
	        				 									Ext.Msg.alert('Advertencia',a.result.errors);
	        				 								}	
	        				 							
	        				 						});
	        				 				}
	        				  },
	        				 {	text: 'Cancelar'
	        				 }
	        	
	        				]
	        	});
	        	
        	});
        </script>
                 
    </head>
    
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Empresa List</g:link></span>
        </div>
        <div class="body">
			<div id="formulario_extjs">
			</div>        
        </div>
    </body>
    
    
</html>
