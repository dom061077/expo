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
	        			//autoLoad:true,
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
	        			//autoLoad:true,
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
				
				var rubroStore = new Ext.data.JsonStore({
					autoLoad:true,
					root:'rows',
					url:'../rubro/listrubrojson',
					fields: ['id','nombreRubro']
				});
				
				var subrubroStore = new Ext.data.JsonStore({
					root:'rows',
					url:'../rubro/listsubrubrojson',
					fields: ['id','nombreSubrubro']
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
	        		
	        	var empresassimilaresStore = new Ext.data.JsonStore({
	        			totalProperty: 'total',
	        			root: 'rows',
	        			url:'listempresassimilares',
	        			fields:['id','nombre'],
	        			valueField:'id'
	        	});	
	        		
	        	var exposdeempresaStore= new Ext.data.JsonStore({
	        			totalProperty: 'total',
	        			root: 'rows',
	        			url:'listexpos',
	        			fields:['id','nombre'],
	        			valueField:'id'
	        		});	
	        		
	        	var exposaparticiparStore = new Ext.data.JsonStore({
	        			totalProperty: 'total',
	        			root: 'rows',
	        			url:'listexposaparticipar',
	        			fields:['id','nombre'],
	        			valueField:'id'
	        	});	
	        		
	        		
	        	var gridexpos = new Ext.grid.GridPanel({
	        		store:exposdeempresaStore,
	        		title:'Exposiciones',
	        		columns: [
	        				{header:"id",dataIndex:"id",hidden:true},
	        				{header:"Nombre",width:250,dataIndex:"nombre"}
	        			],
	        		height:200,
	        		width:300
	        	});
	        	
	        	var gridsimilares = new Ext.grid.GridPanel({
	        		store:empresassimilaresStore,
	        		title:'Empresas con Nombres Similares',
	        		columns:[
	        			{header:"id",dataIndex:"id",hidden:true},
	        			{header:"Nombre",width:250, dataIndex:"nombre"}
	        		],
	        		height:200,
	        		width:300
	        	});
	        	
	        	var gridexposaparticipar = new Ext.grid.GridPanel({
	        		store: exposaparticiparStore,
	        		tbar:[{
	        				icon: imagePath+'/skin/database_delete.png',
	        				cls: 'x-btn-text-icon',
	        				handler: function(){
	        					var sm = gridexposaparticipar.getSelectionModel();
	        					var sel = sm.getSelected();
	        					if (sm.hasSelection()){
	        						gridexposaparticipar.getStore().remove(sel);
	        					}
	        				}
	        			}],
	        		title: 'Exposiciones',
	        		columns:[
	        			{header:"id",dataIndex:"id",hidden:true},
	        			{header:"Nombre",width:250,dataIndex:"nombre"}
	        		],
	        		height:200,
	        		width:300
	        	});
	        	
	        	
	        	var comboExpoaParticipar = new Ext.form.ComboBox({
	        														layout:'form',
	        														id:'idExposicionaParticiparAddExpo',
	        														allowBlank:true,
	        														fieldLabel: 'Exposición',
	        														mode:'local',
	        														name:'exposicionaParticiparAddExpo',
	        														displayField:'nombre',
	        														forceSelection:true,
	        														store:exposStore,
	        														listWidth:200,
	        														valueField:'id',
	        														hiddenName:'idComboAddExpoaParticipar'
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
	        	border:false,
	        	renderTo: 'formulario_extjs',
	        	frame: true,
	        	title: 'Modificar Empresa',
	        	height:400,
	        	width: 550,
	        	items: {	xtype:'tabpanel',
	        				activeItem:0,
	        				autoScroll:true,
	        				enableTabScroll:true,
	        				border:false,
	        				anchor:'100% 100%',
	        				deferredRender:false,			
	        				defaults:{
									  layout:'form'
									 ,labelWidth:80
									 ,autoHeight:true
									 ,anchor:'100% 100%'
									 ,defaultType:'textfield'
									 ,bodyStyle:'padding:5px'
									 ,border:true 
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
								        	//allowBlank: false,
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
								        		name: 'provinciaFiscal',
								        		mode:'local',
								        		displayField:'nombre',
								        		valueField:'id',
								        		hiddenName:'localidad.provincia.id',
								        		store:provinciasStore,
								        		allowBlank:false,
								        		forceSelection:true,
								        		layout:'form',
								        		msgTarget:'under',
								        		width: 120
								        	},{
								        		xtype: 'combo',
								        		id: 'idLocalidad',
								        		fieldLabel: 'Localidad',
								        		store:localidadesStore,
								        		valueField:'id',
								        		displayField:'nombreLoc',
								        		hiddenName:'localidad.id',
								        		mode:'local',
								        		allowBlank: false,
								        		name: 'localidadFiscal',
								        		layout:'form',
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
								        	},{
								        		xtype: 'combo',
								        		fieldLabel:'Rubro',
								        		id:'idRubro',
								        		allowBlank:false,
								        		msgTarget: 'under',
								        		name: 'rubro',
								        		hiddenName:'rubro.id',
								        		displayField:'nombreRubro',
								        		valueField:'id',
								        		mode : 'local',
								        		forceSelection:true,
								        		store:rubroStore,
								        		layout: 'form',
								        		width:200,
								        		listeners:{
								        			'select': function(cmd,rec,idx){
								        				var subrubroCmb = Ext.getCmp('idSubrubro');
								        				subrubroCmb.clearValue();
								        				subrubroCmb.store.load({
								        					params:{'rubroid':Ext.getCmp('idRubro').hiddenField.value}
								        				});
								        				subrubroCmb.enable();
								        			}
								        		}
								        	},{
								        		xtype: 'combo',
								        		fieldLabel:'Sub-Rubro',
								        		id:'idSubrubro',
								        		store:subrubroStore,
								        		allowBlank:false,
								        		msgTarget:'under',
								        		layout: 'form',
								        		name: 'subrubro',
								        		hiddenName:'subrubro.id',
								        		displayField:'nombreSubrubro',
								        		valueField:'id',
								        		mode:'local',
								        		forceSelection:true,
								        		store:subrubroStore,
								        		width:200
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
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'E-mail',
								        		allowBlank:true,
								        		msgTarget: 'under',
								        		layout:'form',
								        		name:'email',
								        		vtype:'email'
								        	}
	        							]
	        						},{
	        							title:'Expos. en las que participó',
	        							//defaults:{anchor:'-20'},
	        							layout:'form',
	        							defaultType:'panel',
	        							
	        							items:gridexpos
	        						},{
	        							title:'Expos a participar',
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
		        									 items:comboExpoaParticipar
		        									},{
		        									 layout:'form',
		        									 items:[{
		        									 	xtype:'button',
		        									 	text:'Agregar',
		        									 	handler: function(){
		        											if(exposaparticiparStore.find('id',comboExpoaParticipar.hiddenField.value)<0)
		        												exposaparticiparStore.insert(0,
		        													new dsexpoModel({
		        														id:comboExpoaParticipar.hiddenField.value,
		        														nombre:Ext.getCmp('idExposicionaParticiparAddExpo').getRawValue()
		        													})
		        												);
		        											else
		        												Ext.MessageBox.show({
		        													title:'Error',
		        													msg:'Ya existe la Exposición',
		        													icon:Ext.MessageBox.ERROR,
		        													buttons:Ext.MessageBox.OK
		        												});
		        									 	}
		        									 },{
		        									 	xtype:'hidden',
		        									 	allowBlank:true,
		        									 	name:'exposaparticiparjson',
		        									 	id:'exposaparticiparjsonid'
	        										 		
	        										 		
	        										 	}]
		        									}
	        									]
	        								},gridexposaparticipar
	        							]
	        						},{
	        							title:'Empresas Similares',
	        							layout:'form',
	        							defaultType:'panel',
	        							items:gridsimilares
	        						}
							]},
	        	buttons: [
	        	          	  {
		        	          	text:'Guardar',handler: function(){
        	          					var exposStoreArr=[];
        	          					var exposStoreJsonString="";
        	          					var exposaParticiparJsonString="";
        	          					var exposaParticiparArr=[]; 
        	          					exposdeempresaStore.each(function(rec){
        	          						exposStoreArr.push(rec.data);
        	          					});
        	          					
        	          					exposaparticiparStore.each(function(rec){
											exposaParticiparArr.push(rec.data)        	          						
        	          					}
        	          					);
        	          					
        	          					exposStoreJsonString=Ext.encode(exposStoreArr);
        	          					exposaParticiparJsonString=Ext.encode(exposaParticiparArr);
										//Ext.getCmp('exposempresajsonId').setValue(exposStoreJsonString);
		        	          			Ext.getCmp('exposaparticiparjsonid').setValue(exposaParticiparJsonString);
		        	          		
										
										
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
	            				Ext.getCmp('idProvincia').setValue(a.result.data.provinciaNombre);
								Ext.getCmp('idProvincia').hiddenField.value=a.result.data.provinciaId;
								var localidadCmb = Ext.getCmp('idLocalidad');
								localidadCmb.clearValue();
								localidadCmb.store.load({
										params:{'provincia_id':Ext.getCmp('idProvincia').hiddenField.value}
								});
								Ext.getCmp('idLocalidad').setValue(a.result.data.localidadNombre);
								Ext.getCmp('idLocalidad').hiddenField.value=a.result.data.localidadId;

								
	            				exposdeempresaStore.load({
	            					params:{'id':a.result.data.id}
	            				});
	            				
	            				exposaparticiparStore.load({
	            					params:{'id':a.result.data.id}
	            				});
	            				
									            				
	            				Ext.getCmp('idVendedor').setValue(a.result.data.vendedor);
	            				Ext.getCmp('idVendedor').hiddenField.value=a.result.data.vendedorId;
	            				
	            				Ext.getCmp('idRubro').setValue(a.result.data.rubro);
	            				Ext.getCmp('idRubro').hiddenField.value=a.result.data.rubroId;

	            				empresassimilaresStore.load({
	            					params:{'id':a.result.data.id}
	            				});
	            				
	            				empresassimilaresStore.load({
	            					params:{'id':a.result.data.id}	
	            				});
	            				
		        				var subrubroCmb = Ext.getCmp('idSubrubro');
		        				subrubroCmb.clearValue();
		        				subrubroCmb.store.load({
		        					params:{'rubroid':a.result.data.rubroId}
		        				});
		        				subrubroCmb.enable();
		        				
										            				
	            				Ext.getCmp('idSubrubro').setValue(a.result.data.subrubro);
	            				Ext.getCmp('idSubrubro').hiddenField.value=a.result.data.subrubroId;
	            				
	        				},
	        			failure: function (thisform,action){
	        				alert(action);
	        			}
	                    		
			        });	
	        	
        	});
        	 




 