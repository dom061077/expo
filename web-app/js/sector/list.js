Ext.onReady(function(){
	Ext.QuickTips.init();
	var sectorStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listtodosjson',
		fields:['id','nombre','exposicion']
	});
	
	var lotewin = new Ext.Window({
		//applyTo:''
		title:'Modificando Lotes',
		resizable:true,
		modal:true
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
	
});
