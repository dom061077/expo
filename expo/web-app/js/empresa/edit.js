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
													           	  params:{'departamentonombre':Ext.getCmp('idDepartamento').getValue()}
													           });
													           loc.enable();
													           Ext.getCmp('idLocalidad').setValue(a.result.nombreLoc);
													           Ext.getCmp('idLocalidad').hiddenField.value=a.result.idLoc;
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
	        
				var dsexpoModel = Ext.data.Record.create([
						'id',
						'nombre'
				]);                		
				
				var vendedoresStore = new Ext.data.JsonStore({
					autoLoad:true,
					root:'rows',
					url: '../vendedor/listjson',
					fields: ['id','nombre']
				});
	        
	        	var exposStore = new Ext.data.JsonStore({
	        		    autoLoad:true,
	        			//totalProperty: 'total',
	        			root: 'rows',
	        			url:'../exposicion/listjson',
	        			fields:[
	        				'id','nombre'
	        			]
	        			
	        			
	        		}); 
	        	var exposdeempresaStoreDeleted = new Ext.data.Store({
	        			
	        		});	
	        	
	        	var exposdeempresaStore= new Ext.data.JsonStore({
	        			totalProperty: 'total',
	        			root: 'rows',
	        			
	        			url:'listexpos',
	        			fields:['id','nombre'],
	        			valueField:'id'
	        		});	
	        		
	        	var gridexpos = new Ext.grid.GridPanel({
	        		store:exposdeempresaStore,
	        		tbar:[{	icon: imagePath+'/skin/database_delete.png',
	        				cls: 'x-btn-text-icon',
	        				handler: function(){
	        					var sm = gridexpos.getSelectionModel();
	        					var sel = sm.getSelected();
	        					if (sm.hasSelection()){
	        						gridexpos.getStore().remove(sel);
	        					}
	        				}
	        			  }],
	        		title:'Exposiciones',
	        		columns: [
	        				{header:"id",dataIndex:"id",hidden:true},
	        				{header:"Nombre",width:250,dataIndex:"nombre"}
	        			],
	        		height:200,
	        		width:300
	        	});
	        		
	        	var comboExpo = new Ext.form.ComboBox({
														 layout:'form',
			        									 id:'idExposicionAddExpo',
			        									 allowBlank:true,
			        									 fieldLabel:'Exposición',
			        									 mode:'local',
			        									 name:'exposicionAddExpo',
			        									 displayField:'nombre',
			        									 forceSelection:true,
			        									 store:exposStore,
			        									 forceSelection:true,
			        									 listWidth:200,
			        									 valueField:'id',
			        									 hiddenName:'idComboAddExpo'
			        									 
	        									});
	        	
	        	var empresa_form = new Ext.FormPanel({
	        	url: 'update',
	        	id:'empresaFormId',
	        	tbar:toolbar,
	        	border:false,
	        	renderTo: 'formulario_extjs',
	        	frame: true,
	        	title: 'Alta de Empresa',
	        	height:400,
	        	width: 450,
	        	items: {	xtype:'tabpanel',
	        				activeItem:0,
	        				autoScroll:true,
	        				border:false,
	        				anchor:'100% 100%',
	        				deferredRender:false,			
	        				defaults:{
									  layout:'form'
									 
									 ,labelWidth:80
									 //,frame:true
									 ,autoHeight:true
									 ,anchor:'100% 100%'
									 ,defaultType:'textfield'
									 ,bodyStyle:'padding:5px'
									 ,border:true 
									 // as we use deferredRender:false we mustn't
									 // render tabs into display:none containers
									 //,hideMode:'offsets'
	        						},
	        				items:[	
	        						{
	        							title:'Datos Empresa',		
	        							autoScroll:true,
	        							defaults:{anchor:'-20'},
								        	items: [{xtype: 'textfield',
						        			 id:'empresaId',
						        			 hideLabel:true,
						        			 fieldLabel:'id de Empresa',
						        			 name:'id',
						        			 hidden:true
						        			},{xtype: 'textfield',
								        	id: 'nombreId',
								        	fieldLabel: 'Nombre',
								        	allowBlank: false,
								        	msgTarget:'under',
								        	layout:'form',
								        	width:260,
								        	name: 'nombre'
								        	},{
								        	xtype: 'textfield',
								        	fieldLabel: 'C.U.I.T',
								        	allowBlank: false,
								        	msgTarget:'under',
								        	layout:'form',
								        	name: 'cuit'
								        	},{
									        	xtype: 'textfield',
									            fieldLabel: 'Dirección',
									            allowBlank: false,
									            width:260,
									            msgTarget:'under',
									            layout:'form',
									            name:'direccion'
								        	},{
									        	xtype: 'textfield',
									        	fieldLabel:'Telefono 1',
									        	allowBlank:false,
									        	msgTarget:'under',
									        	layout:'form',
									        	name:'telefono1'
								        	},{
									        	xtype: 'textfield',
									        	fieldLabel: 'Telefono 2',
									        	allowBlank: true,
									        	name:'telefono2',
									        	layout:'form'
								        	},{
								        		xtype: 'combo',
								        		fieldLabel: 'Provincia',
								        		id:'idProvincia',
								        		name: 'provinciaLn',
								        		displayField:'nombre',
								        		allowBlank:false,
								        		layout:'form',
								        		msgTarget:'under',
								        		mode:'local',
								        		store: provinciasStore,
								        		forceSelection:true,
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
								        		msgTarget:'under',
								        		mode:'local',
								        		store: departamentosStore,
								        		forceSelection:true,
								        		layout:'form',
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
								        		layout:'form',
								        		valueField: 'id',
								        		mode:'local',
								        		store: localidadesStore,
								        		msgTarget:'under',
								        		forceSelection:true,
								        		width: 200
								        	},{
								        		xtype: 'combo',
								        		id: 'idVendedor',
								        		fieldLabel:'Vendedor',
								        		allowBlank: false,
								        		name: 'vendedor',
								        		hiddenName:'vendedor.id',
								        		displayField:'nombre',
								        		layout:'form',
								        		valueField: 'id',
								        		mode: 'local',
								        		store: vendedoresStore,
								        		msgTarget:'under',
								        		forceSelection:true,
								        		width: 200
								        	}
								        	]},
										{
	        							title:'Contacto',
	        							//frame:true,
	        							defaults:{anchor:'-20'},
	        							items:[{xtype: 'textfield',
								        		fieldLabel: 'Representante',
								        		allowBlank: false,
								        		msgTarget:'under',
								        		layout:'form',
								        		width:260,
								        		name: 'nombreRepresentante'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 1',
								        		allowBlank: true,
								        		msgTarget:'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante1'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 2',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout:'form',
								        		name: 'telefonoRepresentante2'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 3',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante3'
								        	}
	        							]
	        						},{
	        							title:'Exposiciones',
	        							//defaults:{anchor:'-20'},
	        							layout:'form',
	        							defaultType:'panel',
	        							
	        							items:[
	        								{
	        									layout:'column',
	        									anchor:'0',
	        									items:[
	        										{columnWidth: .8,
	        										 anchor:'0',
	        										 layout:'form',
	        										 items:comboExpo
	        										},{
	        										 //columnWidth:.5,
	        										 layout:'form',
	        										 items:[{
	        										 	 xtype:'button',
	        										 	 text:'Agregar',
	        										 	 handler: function(){
	        										 	 	if(exposdeempresaStore.find('id',comboExpo.hiddenField.value)<0)
		        										 	 	exposdeempresaStore.insert(0,
		        										 	 		new dsexpoModel({
		        										 	 			id:comboExpo.hiddenField.value,
		        										 	 			nombre:Ext.getCmp('idExposicionAddExpo').getRawValue()
		        										 	 		})
		        										 	 	);	
		        										 	else
		        										 		Ext.Msg.show({title:'Error'
		        										 						,msg:'Ya existe la Exposición'
		        										 						,icon:Ext.Msg.ERROR});
	        										 	 }
	        										 	},{
	        										 		xtype:'hidden',
	        										 		allowBlank:true,
	        										 		name:'exposempresajson',
	        										 		id:'exposempresajsonId'
	        										 		
	        										 		
	        										 	}]
	        										}
	        									
	        									]
	        								},gridexpos
	        							]
	        						}								        	
							]},
	        	buttons: [
	        	          	  {
		        	          	text:'Guardar',handler: function(){
        	          					var exposStoreArr=[];
        	          					var exposStoreJsonString="";
        	          					exposdeempresaStore.each(function(rec){
        	          						exposStoreArr.push(rec.data);
        	          					});
        	          					exposStoreJsonString=Ext.encode(exposStoreArr);
										Ext.getCmp('exposempresajsonId').setValue(exposStoreJsonString);
		        	          		
										
										
			        	          		empresa_form.getForm().submit({
				        	          			//waitMsg:'Guardando Datos...',
					        	          		success: function(f,a){
					        	          					Ext.Msg.alert('Mensaje','Los datos se guardaron correctamente',
																	function(btn,text){
																		if(btn=='ok'){
																			window.location='list';	
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
														    var msg="";
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
														    		for (var i=0; i<a.result.errors.length;i++){
														    			msg=msg+a.result.errors[i].title+'\r\n';	
												    				}
												    				Ext.Msg.alert(msg);
											    				}
														    }	
														    
			        	          						}
			        	          				});
	        	          	  			}
	        	          	  		 
		        	          },
		        	          {text:'Eliminar',handler:function(){
									Ext.Msg.show({title:'Mensaje',
													msg:'Está seguro/a de eliminar el registro?',
													buttons: {yes:true,no:true},
													icon: Ext.MessageBox.QUESTION,
													fn: function(btn){
														if (btn=='yes'){
															var conn = new Ext.data.Connection();
															conn.request({
																url:'delete',
																method:'POST',
																params:{
																	id:Ext.getCmp('empresaId').getValue()
																},
																success: function(resp,opt){
																	var respuesta = Ext.decode(resp.responseText);
																	
																	if(respuesta){
																		if(respuesta.loginredirect)
																			window.location='../logout/index';
																		else {
																			Ext.Msg.show({
																				title:"Error",
																				msg:respuesta.respuesta.title,
																				icon:Ext.MessageBox.ERROR,
																				buttons:Ext.MessageBox.OK
																			});
																			if(respuesta.respuesta.success)
																				window.location='list';
																		}
																	}
																	
																},
																failure: function(resp,opt){
																	Ext.Msg.show({
																			title:'Error',
																			msg:'Se produjo un error al intentar eliminar el registro',
																			icon:Ext.MessageBox.INFO,
																			buttons: Ext.MessageBox.OK
																	});						
																}
															});
														}
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
	        	empresa_form.load({
	            		url:'editempresajson',
	            		params:{
	        				'id':empresaId
	        			},
	        			success: function(f,a){
	            				Ext.getCmp('idProvincia').setValue(a.result.data.provinciaLn);
								Ext.getCmp('idDepartamento').store.load({
									params: {'provincianombre': Ext.getCmp('idProvincia').getValue() }
								});
								
	            				exposdeempresaStore.load({
	            					params:{'id':a.result.data.id}
	            				});
	            				Ext.getCmp('idDepartamento').setValue(a.result.data.departamentoLn);
	            				
	            				Ext.getCmp('idLocalidad').store.load({
	            					params: {'departamentonombre':Ext.getCmp('idDepartamento').getValue()}
	            				});
	            				
	            				Ext.getCmp('idLocalidad').setValue(a.result.data.localidadAux);
	            				Ext.getCmp('idLocalidad').hiddenField.value=a.result.data.localidadId;
	            				
	            				Ext.getCmp('idVendedor').setValue(a.result.data.vendedor);
	            				Ext.getCmp('idVendedor').hiddenField.value=a.result.data.vendedorId;
	            				
	            				
	        				},
	        			failure: function (thisform,action){
	        				alert(action);
	        			}
	                    		
			        });	
	        	
        	});
        	 




 