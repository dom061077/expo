
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Empresa</title>
        
        <script type="text/javascript">
	        Ext.onReady(function(){
	        	var provinciasStore = new Ext.data.JsonStore({
	        			autoLoad:true,
	        			url:'../provincia/listjson',
	        			root:'rows',
	        			fields: ['id','nombre']
	        		});
	        		
	        	var departamentosStore = new Ext.data.JsonStore({
	        			autoLoad:true,
	        			url:'../departamento/listjson',
	        			root:'rows',
	        			fields:['id','nombre']
	        		});	
	        	var localidadesStore = new Ext.data.JsonStore({
	        			autoLoad:true,
	        			url:'../localidad/listjson',
	        			root:'rows',
	        			fields:['id','nombre']
	        		});	
	        		
	        	
	        	var empresa_form = new Ext.FormPanel({
	        	url: 'movie-form-submit.php',
	        	renderTo: 'formulario_extjs',
	        	frame: true,
	        	title: 'Alta de Empresa',
	        	width: 400,
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
	        	},{
	        		xtype: 'combo',
	        		fieldLabel: 'provincia',
	        		id:'idProvincia',
	        		name: 'provincia',
	        		displayField:'nombre',
	        		mode:'local',
	        		store: provinciasStore,
	        		width: 120,
					listeners: {
					       'select' : function(cmb, rec, idx) {
					           var dep = Ext.getCmp('idDepartamento');
					           
					           dep.clearValue();
					           dep.store.load({
					              params: {'provinciaid': Ext.getCmp('idProvincia').getValue() }
					           });
					           dep.enable();
					           var loc = Ext.getCmp('idLocalidad');
					           loc.clearValue();
					           loc.store.removeAll();
					           loc.disable();
					       }
					    }	        		
	        	},{
	        		xtype: 'combo',
	        		fieldLabel: 'Departamento',
	        		id: 'idDepartamento',
	        		name: 'departamento',
	        		displayField:'nombre',
	        		mode:'local',
	        		store: departamentosStore,
	        		width: 120
	        	},{
	        		xtype: 'combo',
	        		id: 'idLocalidad',
	        		fieldLabel: 'Localidad',
	        		name: 'localidad',
	        		displayField:'nombre',
	        		mode:'local',
	        		store: localidadesStore,
	        		width: 200
	        		
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
