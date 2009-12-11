
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Empresa</title>
        
        <script type="text/javascript">
        	function loginForm(){
        		//alert('Que pasa vieja!!!');
        	}
        
	        Ext.onReady(function(){
		        Ext.QuickTips.init();
	        	var winDep;
	        	var winLoc;
	        	var showAddLoc = function(){
	        			if(!winLoc){
	        				winLoc = new Ext.Window({
	        					applyTo: 'loc-win',
	        					title:'Carga de Localidades',
	        					resizable:false,
	        					modal:true,
	        					formPanel:null,
	        					width:400,
	        					height:200,
	        					closeAction:'hide',
	        					plain:true,
	        					items:[formLocalidad]	
	        				});
	        			}
	        			winLoc.show(this);
	        	}
	        	
	        	var showAddDep = function(){
				        if(!winDep){
				            winDep = new Ext.Window({
				                applyTo:'dep-win',
				                title:'Carga de Departamentos',
				                resizable:false,
				                modal:true,
				                formPanel: null,
				                width:400,
				                height:200,
				                closeAction:'hide',
				                plain: true,
				                items:[formDepartamento]
				            });
				        }
				        winDep.show(this);
	        	}
	        	
	        	var toolbar = new Ext.Toolbar({
	        		items: [{
	        			xtype:'tbbutton',
	        			text: 'Agregar Dep.',
	        			handler: showAddDep
	        		},{
	        			xtype:'tbbutton',
	        			text: 'Agregar Loc.',
	        			handler: showAddLoc
	        		}]
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
	        			fields:['id','nombreDep'],
						listeners: {
                            loadexception: function(proxy, store, response, e) {
                                //alert("Response Text>>"+Ext.util.JSON.encode(response.responseText));
                                var jsonObject = Ext.util.JSON.decode(response.responseText);
                                if (jsonObject.loginredirect == true)
                                		window.location='../logout/index';
                                
                               }
	        			}
	        			 
	        		});	
	        	var localidadesStore = new Ext.data.JsonStore({
	        			autoLoad:true,
	        			url:'../localidad/listjson',
	        			root:'rows',
	        			fields:['id','nombreLoc'],
	        			listeners: {
		                    loadexception: function(proxy, store, response, e) {
					                    var jsonObject = Ext.util.JSON.decode(response.responseText);
					                    if (jsonObject.loginredirect == true)
					                    		window.location='../logout/index';
                    
					                   }
        						
	        				}
	        		});	
	        		
	        	var formLocalidad = new Ext.FormPanel({
	        							url:'../localidad/savejson',
	        							id:'formLocalidadId',
	        							frame:true,
	        							width: 400,
	        							height:150,
	        							items:[{
	        									xtype: 'textfield',
	        									id:'nombreFormLocId',
	        									fieldLabel:'Localidad',
	        									allowBlank: false,
	        									name: 'nombreLoc',
	        									width: 200
	        								  },{
	        								  	xtype:'combo',
	        								  	id:'idProvinciaAddLoc',
	        								  	fieldLabel:'Provincia',
	        								  	allowBlank: false,
	        								  	mode:'local',
	        								  	name:'provinciaAddLoc',
												listeners: {
												       'select' : function(cmb, rec, idx) {
												           var dep = Ext.getCmp('idDepartamentoAddLoc');
												           
												           dep.clearValue();
												           dep.store.load({
												              params: {'provincianombre': Ext.getCmp('idProvincia').getValue() }
												           });
												           dep.enable();
												       }
												    },	        		
	        								  	
	        								  	displayField:'nombre',
	        								  	store:provinciasStore,
	        								  	valueField:'id'
	        								  },{
	        								  	xtype:'combo',
	        								  	id:'idDepartamentoAddLoc',
	        								  	fieldLabel:'Departamento',
	        								  	allowBlank: false,
	        								  	mode:'local',
	        								  	name:'departamentoAddLoc',
	        								  	displayField:'nombreDep',
	        								  	store: departamentosStore,
	        								  	valueField:'id',
	        								  	hiddenName:'departamento.id',
	        								  	width: 200
	        								  }],
	        							buttons: [{
	        									text:'Guardar',
	        									handler:function(){
	        									
		        										formLocalidad.getForm().submit({
			        										success: function(f,a){
													           var loc = Ext.getCmp('idLocalidad');
													           loc.clearValue();
													           loc.store.removeAll();
													           loc.store.load({
													           	  params:{'departamentonombre':Ext.getCmp('idLocalidad').getValue()}
													           });
													           loc.enable();
								                    		   winLoc.hide();
			        											
			        										},
			        										failure: function(f,a){
						                    					var msg="";
						                    					if (a.result)
															    	if (a.result.errors){
															    		 for (var i=0; i<a.result.errors.length;i++){
															    			msg=msg+a.result.errors[i].title+'\r\n';	
													    				}
																		Ext.Msg.show({
																			title:'Errores',
																			msg:msg,
																			icon: Ext.MessageBox.ERROR	
																		});	
													    				
												    				}

			        										}
		        										})
	        										}
		        								},{
		        									text:'Cerrar',
		        									handler:function(){winLoc.hide();}
		        								}]
	        								  
	        	});	
	        	
				var formDepartamento =  new Ext.FormPanel({
										url:'../departamento/savejson',
										id:'formDepartamentoId',
				                		//renderTo: 'form-panel-departamento',
							        	frame: true,
							        	//title: 'Alta de Empresa',
							        	width: 400,
							        	height:150,
							        	items: [{
									        	xtype: 'textfield',
									        	id: 'nombreFormDepId',
									        	fieldLabel: 'Departamento',
									        	allowBlank: false,
									        	name: 'nombreDep',
									        	width:200
										        	
								        	},{
							        		xtype: 'combo',
							        		fieldLabel: 'Provincia',
							        		allowBlank: false,
							        		mode:'local',
							        		id:'idProvinciaAddDep',
							        		name: 'provinciaAddDep',
							        		displayField:'nombre',
							        		store: provinciasStore,
							        		
								        	valueField:'id',
							        		hiddenName:'provincia.id',
							        		width: 120
							        	}],
						                buttons: [{
						                    text:'Guardar',
						                    handler: function(){
						                    	formDepartamento.getForm().submit({
						                    		success: function(f,a){
											           var dep = Ext.getCmp('idDepartamento');
											           
											           dep.clearValue();
											           dep.store.load({
											              params: {'provincianombre': Ext.getCmp('idProvincia').getValue() }
											           });
											           dep.enable();
											           var loc = Ext.getCmp('idLocalidad');
											           loc.clearValue();
											           loc.store.removeAll();
											           loc.enable();

													   Ext.getCmp('idDepartamento').setValue(a.result.nombreDep);						                    		
						                    		   winDep.hide();
						                    		},
						                    		failure: function(f,a){
					                    					var msg="";
					                    					if (a.result){
														    	if (a.result.loginredirect==true){
														    		Ext.Msg.show({
															    		title:'Mensaje',
																		msg:'Su sesion de usuario a caducado, ingrese nuevamente',
																		buttons: Ext.MessageBox.OK,																		
														    			icon: Ext.MessageBox.WARNING,
														    			fn: function(btn){
														    				window.location='../logout/index';
														    			}
														    			
														    		});
														    		
														    	}
						                    					
														    	if (a.result.errors){
														    		 for (var i=0; i<a.result.errors.length;i++){
														    			msg=msg+a.result.errors[i].title+'\r\n';	
												    				}
																	Ext.Msg.show({
																		title:'Errores',
																		msg:msg,
																		icon: Ext.MessageBox.ERROR	
																	});	
												    				
											    				}
					                    					}
						                    		}
						                    	});
						                    }
						                },{
						                    text: 'Cerrar',
						                    handler: function(){
						                        winDep.hide();
						                    }
						                }]
				                		});
	        
	        
	        
	        		
	        		
	        	
	        	var empresa_form = new Ext.FormPanel({
	        	url: 'savejson',
	        	id:'empresaFormId',
	        	tbar:toolbar,
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
	        		allowBlank:false,
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
	        		displayField:'nombreDep',
	        		allowBlank:false,
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
	        		allowBlank: false,
	        		name: 'localidadAux',
	        		hiddenName:'localidad.id',
	        		displayField:'nombreLoc',
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
														    var msg;
														    if (a.failureType==Ext.form.Action.CONNECT_FAILURE ||
														    	a.failureType==Ext.form.Action.SERVER_INVALID){
														    		Ext.Msg.alert('Error','El servidor no Responde')
														    	}
														    if (a.result){
														    	if (a.result.loginredirect==true){
														    		Ext.Msg.alert('Su sesion de usuario a caducado, ingrese nuevamente');
														    		window.location='../logout/index';
														    		}
														    	if (a.result.errors){
														    		for (a.result.errors in error){
														    			msg=msg+error.title;	
												    				}
												    				Ext.Msg.alert(msg);
											    				}
														    }	
														    
						        							//Ext.Msg.alert('Advertencia','Se produjo un error en la carga');          				
				        	          						/*Ext.each(a.result.errors,function(error,index){
					        	          								Ext.Msg.alert('Advertencia',error.title)
			        	          								}
						        	          				);*/
			        	          						}
			        	          				});
	        	          	  			}
	        	          	  		 
		        	          },
	        	          {text:'Cancelar',handler: function (){
			        	          window.location='list';
		        	          }
		        	      }
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
        
        
		<div id="dep-win" class="x-hidden">
			<div id="form-panel-departamento">
				
			</div>
		</div>
		<div id="loc-win" class="x-hidden">
			
		</div>
    </body>
    
    
</html>
