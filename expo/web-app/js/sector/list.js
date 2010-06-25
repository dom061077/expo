Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var dslotemodel= Ext.data.Record.create([
		'id',
		'nombre'
	]);
	
	var loteStore = new Ext.data.Store({
			url: '../lote/listjson',
			reader: new Ext.data.JsonReader({
				root:'rows',
				totalProperty: 'total',
				id:'id'
			}, dslotemodel)
	});
	
	var gridlote = new Ext.grid.GridPanel({
		frame:true,
		store:loteStore,
		width:400,
		height:250,
		sm: new Ext.grid.RowSelectionModel({
				singleSelect: true
			}),
		tbar:[
			{
				text:'Agregar',
				handler: function(){
					var conn = new Ext.data.Connection();
					conn.request({
						url:'../lote/savejson',
						params:{
							nombre:'Lote Nuevo'
						},
						success:function(resp,opt){
							var loteid=Ext.util.JSON.decode(resp.responseText).loteid;
							gridlote.getStore().insert(0,new dslotemodel({id:loteid,nombre:'Lote Nuevo'}))
							gridlote.startEditing(0,0);
						},
						failure:function(resp,opt){
							Ext.MessageBox.show({
								title:'Error',
								msg:'No se puedo insertar el lote',
								buttons:Ext.MessageBox.OK,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					});
				}
			},{
				text:'Eliminar'
			}
		],
		columns:[
			{header:'Id de Lote',dataIndex:'id',hidden:true},
			{header:'Nro. de Lote',dataIndex:'nombre',width:150}
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
		height:260,
		items:[gridlote]
	});

	var sectorStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listtodosjson',
		fields:['id','nombre','exposicion']
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
					lotewin.show();	
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
		                  sectorStore.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;

		}
	);
	
});
