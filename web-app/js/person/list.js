Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var usuariosStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listjson',
		root:'rows',
		fields:['id','username','userRealName']
	});
	
	var gridusuarios = new Ext.grid.GridPanel({
		store:usuariosStore,
		columns:[
			{header:"id",dataIndex:'id',hidden:true},
			{header:"Nombre de Usuario",width:200,dataIndex:"username"},
			{header:"Nombre Real del Usuario",width:200,dataIndex:"userRealName"}
		],
		stripRows:true,
		height:250,
		width:460,
		title:"Usuarios",
		bbar: new Ext.PagingToolbar({
				pageSize:10,
				store:usuariosStore,
				displayInfo:true,
				displayMsg:'Visualizando usuarios {0} - {1} de {2}',
				emptyMsg:'No hay empresas para visualizar'
		})
	});
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'usuarios-grid',
		width:470,
		frame:true,
		title:'Búsqueda de Usuarios',
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
									usuariosStore.load({
										params:{'start':0,'limit':10,'searchCriteria':Ext.getCmp('searchCriteriaId').value}
									});									
								}
							}
						}
					}]
			},gridusuarios
		
		]
	});
	
	gridusuarios.on('rowdblclick',function(grid,rowIndex,e){
		                  var r = grid.getStore().getAt(rowIndex);
		                  var selectedId = r.get('id');
		                  usuariosStore.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;

		}
	);
	
});