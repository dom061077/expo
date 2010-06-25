Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var loteStore = new Ext.data.Store({
			url: 'movies.php',
			reader: new Ext.data.JsonReader({
				root:'rows',
				totalProperty: 'results',
				id:'id'
			}, ds_model)
	});
	
	loteStore.load();
	
	var gridLote = new Ext.form.FormPanel({
		title:'Modificacion de Lotes',
		frame:true
	});
	
	var dsmodel= Ext.data.Record.create([
		'id',
		'nombre'
	]);
	
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
					var lotewin = new Ext.Window({
						//applyTo:''
						title:'Modificando Lotes',
						resizable:true,
						modal:true,
						formPanel:null,
						width:400,
						height:400
					});
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
