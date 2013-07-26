Ext.onReady(function(){
	Ext.QuickTips.init();
	var sectorId;
	var expoId;
	var loteId;
	var dslotemodel= Ext.data.Record.create([
		'id',
		'nombre',
		'habilitado'
	]);
	
	var descEdit = new Ext.form.TextField({
		allowBlank:false,
		maxLength:20
	});
	
	var loteStore = new Ext.data.Store({
			url: '../lote/listjson',
			reader: new Ext.data.JsonReader({
				root:'rows',
				totalProperty: 'total',
				id:'id'
			}, dslotemodel)
	});
	var gridlote = new Ext.grid.EditorGridPanel({
		frame:true,
		store:loteStore,
		width:400,
		clicksToEdit: 2,
		height:250,
		sm: new Ext.grid.RowSelectionModel({
				singleSelect: true
			}),
			listeners: {
				afteredit: function(e){
					var conn = new Ext.data.Connection();
					conn.request({
						url: '../lote/updatejson',
						params: {
							id: e.record.data.id,
							nombre: e.record.data.nombre
						},
						success: function(resp,opt) {
							//e.commit();
						},
						failure: function(resp,opt) {
							//e.reject();
							Ext.MessageBox.show({
								title:'Error',
								msg:'Error al modificar los datos',
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					});
				}
			},
		tbar:[
			{
				text:'Agregar',
				handler: function(){
					var sm = gridsectores.getSelectionModel();
					var sel = sm.getSelected();
					var conn = new Ext.data.Connection();
					var sector_id
					if(sm.hasSelection()){
							sector_id=sel.data.id;
							conn.request({
								url:'../lote/savejson',
								params:{
									nombre:'Lote Nuevo',
									'sector.id':sector_id
								},
								success:function(response,opt){
									var loteid=Ext.util.JSON.decode(response.responseText).loteid;
									gridlote.getStore().insert(0,new dslotemodel({id:loteid,nombre:'Lote Nuevo'}))
									gridlote.startEditing(0,0);
								},
								failure:function(response,opt){
									
		                                var jsonObject = Ext.util.JSON.decode(response.responseText);
					                    if (response.status==0)
						                    	Ext.MessageBox.show({
							                    		title:'Error',
							                    		msg:'Error de comunicación con el servidor',
							                    		icon:Ext.MessageBox.ERROR,
							                    		buttons:Ext.MessageBox.OK
						                    	});
						                else{    	
							                    if (jsonObject.loginredirect == true)
						                    		window.location='../logout/index';
						                    	else
													Ext.MessageBox.show({
														title:'Error',
														msg:'No se pudo insertar el lote',
														buttons:Ext.MessageBox.OK,
														icon:Ext.MessageBox.ERROR,
														buttons:Ext.MessageBox.OK
													});
							               }                                    
								}
							});
					}
				}
			},{

				text:'Eliminar',
				handler:function(){
					var sm = gridlote.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						Ext.MessageBox.show({
							title:'Mensaje',
							msg:'Está seguro de eliminar este lote?',
							icon:Ext.MessageBox.QUESTION,
							buttons:Ext.MessageBox.YESNO,
							fn:function(btn){
								if(btn=='yes'){
									var conn = new Ext.data.Connection();
									conn.request({
										url:'../lote/deletejson',
										params:{
											id:sel.data.id
										},
										success:function(resp,opt){
											gridlote.getStore().remove(sel);
										},
										failure:function(response,opt){
		                                var jsonObject = Ext.util.JSON.decode(response.responseText);
							                    if (response.status==0)
								                    	Ext.MessageBox.show({
									                    		title:'Error',
									                    		msg:'Error de comunicación con el servidor',
									                    		icon:Ext.MessageBox.ERROR,
									                    		buttons:Ext.MessageBox.OK
								                    	});
								                else{    	
									                    if (jsonObject.loginredirect == true)
								                    		window.location='../logout/index';
								                    	else
															Ext.MessageBox.show({
																title:'Error',
																msg:'No se pudo eliminar el lote',
																buttons:Ext.MessageBox.OK,
																icon:Ext.MessageBox.ERROR,
																buttons:Ext.MessageBox.OK
															});
									           }                                    
											
										}
									});
								}
							}
						});
					}
				}
			},{
				text:'Lista de Precios',
				handler: function(){
					var sm = gridlote.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						loteId=sel.data.id;
						sectorId=null;
						listapreciosStore.load({
							params:{'loteId':loteId,'expoId':expoId}
						});
						sectorlistapreciowin.title='Tarifario de Lotes';
						sectorlistapreciowin.show();
					}else{
						Ext.MessageBox.show({
                    		title:'Mensaje',
                    		msg:'Seleccione un lote para cargar los precios',
                    		icon:Ext.MessageBox.INFORMATION,
                    		buttons:Ext.MessageBox.OK
                    	});
					}
				}
				
			}
		],
		columns:[
			{header:'Id de Lote',dataIndex:'id',hidden:true},
			{header:'Nro. de Lote',dataIndex:'nombre',width:150,editor:descEdit},
			{header:'Habilitado',dataIndex:'habilitado',width:60,xtype}ESTABA AQUI
		]
	});
	 
	var lotewin = new Ext.Window({
		applyTo:'lotewin_extjs',
		title:'Modificando Lotes',
		resizable:true,
		closeAction:'hide',
		modal:true,
		formPanel:null,
		resizable:false,
		width:450,
		height:280,
		items:[gridlote]
	});

	var sectorStore = new Ext.data.JsonStore({
		autoLoad:true,
		remoteSort:true,
		totalProperty:'total',
		root:'rows',
		url:'listtodosjson',
		fields:['id','nombre','expoId','exposicion','habilitado'],
		listeners: {
            loadexception: function(proxy, store, response, e) {
	                    var jsonObject = Ext.util.JSON.decode(response.responseText);
	                    if (response.status==0)
	                    	Ext.MessageBox.show({
	                    		title:'Error',
	                    		msg:'Error de comunicación con el servidor',
	                    		icon:Ext.MessageBox.ERROR,
	                    		buttons:Ext.MessageBox.OK
	                    	});
	                    else{
		                    if (jsonObject.loginredirect == true)
		                    		window.location='../logout/index';
	                    }
	                   }
				
			}							
		
	});
	
	sectorStore.on("beforeload",function(){
		
		sectorStore.baseParams={
				searchCriteria:Ext.getCmp('searchCriteriaId').getValue()
		}
	});
	
	var gridsectores = new Ext.grid.GridPanel({
		store:sectorStore,
		columns:[
			{header:"id",dataIndex:"id",hidden:true},
			{header:"Nombre",dataIndex:"nombre",width:200,sortable:true},
			{header:"Id Expo",dataIndex:"expoId",width:200,hidden:true},
			{header:"Exposición",dataIndex:"exposicion",width:180,sortable:true},
			{xtype: 'checkcolumn',header:"Habilitado",dataIndex:"habilitado",width:60,sortable:false,processEvent:function(){return false;}}
		],
		stripRows:true,
		height:250,
		width:460,
		loadMask:true,
		title:"Sectores",
		tbar:[
			{
				text:'Modificar Lotes'
				,handler:function(){
					var sm = gridsectores.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						loteStore.load({
							params:{'sector_id':sel.data.id}
						});
						expoId = sel.data.expoId;
						loteId = null;
						lotewin.show();
					}else{
						Ext.MessageBox.show({
							title:'Mensaje',
							msg:'Seleccione un sector para modificar los lotes',
							icon:Ext.MessageBox.INFO,
							buttons:Ext.MessageBox.OK
						});
					}
				}
			}/*,{
				text:'Lista de precios'
				,handler: function(){
					loteId=null;
					var sm = gridsectores.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						expoId=sel.data.expoId;
						sectorId=sel.data.id;
						loteId=null;
						listapreciosStore.load({
							params:{'sectorId':sectorId,'expoId':expoId}
						});
						sectorlistapreciowin.show();
					}else{
						
					}
				}
			}*/
		],
		bbar: new Ext.PagingToolbar({
				pageSize:10,
				store:sectorStore,
				displayInfo:true,
				displayMsg:'Visualizando Sector {0} - {1} de {2}',
				emptyMsg:'No hay Sectores para visualizar'
		})
	});
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'formulario_extjs',
		width:470,
		frame:true,
		title:'Búsqueda de Sector',
		items:[{
				layout:'column',
				anchor:'0',
				items:[{
						columnWidth:.5,
						layout:'form',
						items:{
							xtype:'textfield',
							id:'searchCriteriaId',
							fieldLabel:'Texto a Buscar',
							anchor:'0'
						}
					},{
						columnWidth:.5,
						layout:'form',
						items:{
							xtype:'button',
							text:'Buscar',
							listeners:{
								click:function(){
									
									sectorStore.load({
										params:{'start':0,'limit':10}
									});									
								}
							}
						}
					}]
			},gridsectores
			]
	});
	gridsectores.on('rowdblclick',function(grid,rowIndex,e){
		                  var r = grid.getStore().getAt(rowIndex);
		                  var selectedId = r.get('id');
		                  //sectorStore.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;

		}
	);
	
	//roweditor para la lista de precios
	
	var ListaPrecioSector = Ext.data.Record.create([{
			name: 'id',
			type: 'int'
		},{
			name: 'vigencia',
			type: 'date',
			dateFormat:'d/m/Y'
		},{
			name:'precio',
			type:'float'
	}]);
	
	var editor = new Ext.ux.grid.RowEditor({
		saveText:'Guardar'
	});
	
	var listapreciosStore = new Ext.data.JsonStore({
			autoLoad:false,
			totalProperty:'total',
			root:'rows',
			url:'../listaPrecios/listjson',
			fields:['id','vigencia','precio'],
			listeners: {
	            loadexception: function(proxy, store, response, e) {
		                     var jsonObject = Ext.util.JSON.decode(response.responseText);
		                   if (response.status==0)
		                    	Ext.MessageBox.show({
		                    		title:'Error',
		                    		msg:'Error de comunicación con el servidor',
		                    		icon:Ext.MessageBox.ERROR,
		                    		buttons:Ext.MessageBox.OK
		                    	});
		                    else{
			                    if (jsonObject.loginredirect == true)
			                    		window.location='../logout/index';
		                    }
		                   } 
				}							
		});

	listapreciosStore.on('add',function(store,records,index){
			var conn = new Ext.data.Connection();
			conn.request({
				url:'../listaPrecios/savejson',
				method:'POST',
				params:{
					'precio':records[0].data.precio,
					'vigencia':records[0].data.vigencia,
					'sector.id':sectorId,
					'lote.id':loteId,
					'expo.id':expoId
				},
				success:function(resp,opt){
						var respuesta=Ext.decode(resp.responseText);
						if(respuesta.result){
							if(respuesta.result.loginredirect==true)
								Ext.MessageBox.show({
									title:'Mensaje',
									icon:Ext.MessageBox.INFO,
									buttons:Ext.MessageBox.OK,
									fn:function(btn){
										window.location='../logout/index';
									}
								});
						}else{
							if(respuesta.success){
								
							}else{
								var msg="";
								for(var i=0;i<respuesta.errors.length;i++){
									msg=msg+respuesta.errors[i].title+'\r\n';
								}
								Ext.MessageBox.show({
									title:'Error',
									msg:msg,
									icon:Ext.MessageBox.ERROR,
									buttons:Ext.MessageBox.OK
								});
							}
						}			
						listapreciosStore.load({
							params:{'sectorId':sectorId,'loteId':loteId,'expoId':expoId}
						});
				},
				failure:function(resp,opt){
						var respuesta = Ext.decode(resp.responseText);
						if(respuesta.result){
							if(respuesta.result.loginredirect==true)
								Ext.MessageBox.show({
									title:'Mensaje',
									icon:Ext.MessageBox.INFO,
									buttons:Ext.MessageBox.OK,
									fn:function(btn){
										window.location='../logout/index';
									}
								});
						}else{
							Ext.MessageBox.show({
								title:'Error',
								msg:'Se produjo al intentar guardar el registro'
								,buttons:Ext.MessageBox.OK
								,fn:function(btn){
								}
							});
						}
					
				}
			});
	});
	
	listapreciosStore.on('update',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'../listaPrecios/updatejson',
			method:'POST',
			params:{
				'id':records.data.id,
				'vigencia':records.data.vigencia,
				'precio':records.data.precio
			},
			success:function(resp,opt){
					var respuesta=Ext.decode(resp.responseText);
					if(respuesta.result){
						if(respuesta.result.loginredirect==true)
							Ext.MessageBox.show({
								title:'Mensaje',
								icon:Ext.MessageBox.INFO,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									window.location='../logout/index';
								}
							});
					}else{
						if(respuesta.success){
							
						}else{
							var msg="";
							for(var i=0;i<respuesta.errors.length;i++){
								msg=msg+respuesta.errors[i].title+'\r\n';
							}
							Ext.MessageBox.show({
								title:'Error',
								msg:msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					}					
			},
			failure:function(resp,opt){
					var respuesta = Ext.decode(resp.responseText);
					if(respuesta.result){
						if(respuesta.result.loginredirect==true)
							Ext.MessageBox.show({
								title:'Mensaje',
								icon:Ext.MessageBox.INFO,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									window.location='../logout/index';
								}
							});
					}else{
						Ext.MessageBox.show({
							title:'Error',
							msg:'Se produjo un error al intentar modificar el registro'
							,buttons:Ext.MessageBox.OK
							,fn:function(btn){
							}
						});
					}
				
			}
		});
		
	});
	
	listapreciosStore.on('remove',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'../listaPrecios/deletejson',
			method:'POST',
			params:{
				'id':records.data.id
			},
			success:function(resp,opt){
					var respuesta=Ext.decode(resp.responseText);
					if(respuesta.result){
						if(respuesta.result.loginredirect==true)
							Ext.MessageBox.show({
								title:'Mensaje',
								icon:Ext.MessageBox.INFO,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									window.location='../logout/index';
								}
							});
												
					}else{
						if(respuesta.success){
							
						}else{
							var msg="";
							for(var i=0;i<respuesta.errors.length;i++){
								msg=msg+respuesta.errors[i].title+'\r\n';
							}
							Ext.MessageBox.show({
								title:'Error',
								msg:msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					}					
			},
			failure:function(resp,opt){
					var respuesta = Ext.decode(resp.responseText);
					if(respuesta.result){
						if(respuesta.result.loginredirect==true)
							Ext.MessageBox.show({
								title:'Mensaje',
								icon:Ext.MessageBox.INFO,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									window.location='../logout/index';
								}
							});
					}else{
						Ext.MessageBox.show({
							title:'Error',
							msg:'Se produjo un error al intentar eliminar el registro'
							,buttons:Ext.MessageBox.OK
							,fn:function(btn){
							}
						});
					}
				
			}
		});
	});
		
	var gridprecios = new Ext.grid.GridPanel({
		id:'gridpreciosId',
		plugins: [editor],
		header:true,
		footer:true,
		selModel: new Ext.grid.RowSelectionModel(),
		stripeRows:true,
		store:listapreciosStore,
		width:400,
		height:400,
		tbar:[{
				text:'Eliminar',
				handler:function(){
					editor.stopEditing();
	                var s = gridprecios.getSelectionModel().getSelections();
    	            for(var i = 0, r; r = s[i]; i++){
        	            listapreciosStore.remove(r);
            	    }
				}
			},{
				text:'Agregar',
				handler:function(){
					var e = new ListaPrecioSector({
						id:0,
		                vigencia:(new Date()).clearTime(),
		                precio:0
		            });
	                //editor.stopEditing();
	                listapreciosStore.insert(0, e);
	                //grid.getView().refresh();
	                //grid.getSelectionModel().selectRow(0);
	                //editor.startEditing(0);
				}
			}
		],
		columns:[
		    {header:'id',dataIndex:'id',hidden:true},
		    //{header:"Fecha",dataIndex:'fechaAlta',width:80,renderer: Ext.util.Format.dateRenderer('d/m/y'),sortable:true}
		    {header:'Vigencia',dataIndex:'vigencia',width:150,renderer: Ext.util.Format.dateRenderer('d/m/y')
				    ,editor:{
				    	xtype: 'datefield',
				    	allowBlank:false
				    },sortable:false},
		    {header:'Precio',dataIndex:'precio',width:150
		    		,editor:{
		    			xtype:'textfield',
		    			allowBlank:false
		    		}
		    }
        ]
	
	});
	
	var sectorlistapreciowin = new Ext.Window({
		applyTo:'listaprecios_extjs',
		title:'Tarifario de Sectores',
		closeAction:'hide',
		modal:true,
		formPanel:null,
		resizable:false,
		width:450,
		height:280,
		items:[gridprecios]
	});
	

	
});
