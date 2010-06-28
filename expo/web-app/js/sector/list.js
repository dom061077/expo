Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var dslotemodel= Ext.data.Record.create([
		'id',
		'nombre'
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
									var loteid=Ext.util.JSON.decode(resp.responseText).loteid;
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
			}
		],
		columns:[
			{header:'Id de Lote',dataIndex:'id',hidden:true},
			{header:'Nro. de Lote',dataIndex:'nombre',width:150,editor:descEdit}
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
		totalProperty:'total',
		root:'rows',
		url:'listtodosjson',
		fields:['id','nombre','exposicion'],
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
	
	
	
	var gridsectores = new Ext.grid.GridPanel({
		store:sectorStore,
		columns:[
			{header:"id",dataIndex:"id",hidden:true},
			{header:"Nombre",dataIndex:"nombre",width:200},
			{header:"Exposición",dataIndex:"exposicion",width:200}			
		],
		stripRows:true,
		height:250,
		width:460,
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
			}
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
										params:{'start':0,'limit':10,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()}
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
	
});
