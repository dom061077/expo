
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Empresa</title>
        
        <script type="text/javascript">
	        Ext.onReady(function(){
	        	var win;
	        	var button = Ext.get('show-btn');
				var formLocalidad =  new Ext.FormPanel({
										url:'../localidad/create',
				                		renderTo: 'form-panel-localidad',
							        	frame: true,
							        	//title: 'Alta de Empresa',
							        	width: 500,
							        	items: [{
							        	xtype: 'textfield',
							        	id: 'nombreId',
							        	fieldLabel: 'Nombre',
							        	allowBlank: false,
							        	name: 'nombre'
							        	}]							        	
				                		});
	        
				   button.on('click', function(){
				        // create the window on the first click and reuse on subsequent clicks
				        if(!win){
				            win = new Ext.Window({
				                applyTo:'hello-win',
				                layout:'fit',
				                width:500,
				                height:300,
				                closeAction:'hide',
				                plain: true,
				                buttons: [{
				                    text:'Submit',
				                    disabled:true
				                },{
				                    text: 'Close',
				                    handler: function(){
				                        win.hide();
				                    }
				                }]
				            });
				        }
				        win.show(this);
				    });
	        
	        
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
	        	url: 'save',
	        	id:'empresaFormId',
	        	renderTo: 'formulario_extjs',
	        	frame: true,
	        	title: 'Alta de Empresa',
	        	width: 400,
	        	items: [{
	        	xtype: 'textfield',
	        	id: 'nombreId',
	        	fieldLabel: 'Nombre',
	        	allowBlank: false,
	        	name: 'nombre'
	        	},{
	        	xtype: 'textfield',
	        	fieldLabel: 'C.U.I.T',
	        	allowBlank: false,
	        	name: 'cuit'
	        	},{
	        	xtype: 'textfield',
	        	fieldLabel: 'Representante',
	        	allowBlank: false,
	        	
	        	name: 'nombreRepresentante'
	        	},{
		        	xtype: 'textfield',
		            fieldLabel: 'Direcci&#243;n',
		            allowBlank: false,
		            name:'direccion'
	        	},{
		        	xtype: 'textfield',
		        	fieldLabel:'Telefono 1',
		        	allowBlank:false,
		        	name:'telefono1'
	        	},{
		        	xtype: 'textfield',
		        	fieldLabel: 'Telefono 2',
		        	allowBlank: true,
		        	name:'telefono2'
	        	},{
	        		xtype: 'combo',
	        		fieldLabel: 'Provincia',
	        		id:'idProvincia',
	        		name: 'provinciaLn',
	        		displayField:'nombre',
	        		mode:'local',
	        		store: provinciasStore,
	        		width: 120,
					listeners: {
					       'select' : function(cmb, rec, idx) {
					           var dep = Ext.getCmp('idDepartamento');
					           
					           dep.clearValue();
					           dep.store.load({
					              params: {'provincianombre': Ext.getCmp('idProvincia').getValue() }
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
	        		name: 'departamentoLn',
	        		displayField:'nombre',
	        		mode:'local',
	        		store: departamentosStore,
	        		width: 120,
	        		listeners: {
	        				'select' : function (cmb,rec,idx){
    							var loc = Ext.getCmp('idLocalidad');
    							loc.clearValue();
    							loc.store.load({
        							params: {'departamentonombre':Ext.getCmp('idDepartamento').getValue()}
    							});
    							loc.enable();
	        				}
	        		}
	        	},{
	        		xtype: 'combo',
	        		id: 'idLocalidad',
	        		fieldLabel: 'Localidad',
	        		name: 'localidadAux',
	        		hiddenName:'localidad.id',
	        		displayField:'nombre',
	        		valueField: 'id',
	        		mode:'local',
	        		store: localidadesStore,
	        		width: 200
	        		
	        	}],
	        	buttons: [
	        	          	  {
		        	          	text:'Guardar',handler: function(){
			        	          		empresa_form.getForm().submit({
				        	          			//waitMsg:'Guardando Datos...',
					        	          		success: function(f,a){
				        	          					Ext.Msg.alert('Mensaje','Los datos se guardaron correctamente',
																function(btn,text){
																	if(btn=='ok'){
																		window.location='create';	
																	}
				        	          							}
								        	          	);
				        	          					
			        	          						},
			        	          				failure: function(f,a){
						        	          				//Ext.Msg.alert('Warning',(a.result.errors[1]).title);
						        	          				//Ext.Msg.alert('Warning',(a.result.errors[0]).title);
														    //for(var i=0;i<a.result.errors.length;i++){
															//    Ext.Msg.alert('Advertencia',a.result.errors[i].title);
														    //}
						        							Ext.Msg.alert('Advertencia','Se produjo un error en la carga');          				
				        	          						/*Ext.each(a.result.errors,function(error,index){
					        	          								Ext.Msg.alert('Advertencia',error.title)
			        	          								}
						        	          				);*/
			        	          						}
			        	          				});
	        	          	  			}
	        	          	  		 
		        	          },
	        	          {text:'Cancelar'}
	      	        ]
	        	});
	        	Ext.getCmp('nombreId').focus('', 10);	
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
        
        <input type="button" id="show-btn" value="Mostrar Windows" />
        
		<div id="hello-win" class="x-hidden">
			<div id="form-panel-localidad">
				
			</div>
		</div>
    </body>
    
    
</html>
