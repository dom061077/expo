Ext.onReady(function(){
	Ext.QuickTips.init();
	var exposicionStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listjson',
		root:'rows',
		fields:['id','nombre']
	});
	
	var gridexposiciones = new Ext.grid.GridPanel({
		store:exposicionStore,
		columns:[
			{header:"id",dataIndex:'id',hidden:true},
			{header:"Nombre de Exposición",width:200,dataIndex:"nombre"}
		],
		tbar:[/*{
				text:'Pre-visualizar',
				handler:function(){
					var sm = gridexposiciones.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						open('logopreview?id='+sel.data.id,'_blank');
					}
				}
			},*/{
				text:'Logos',
				handler:function(){
					var sm = gridexposiciones.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						window.location='../logo/list?expoid='+sel.data.id+'&exponombre='+sel.data.nombre	
					}else{
						Ext.MessageBox.show({
								title:'Error',
								msg:'Seleccione una Exposición para cargar logos',
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
					}
				}
			}],
		stripRows:true,
		height:250,
		width:460,
		title:"Exposiciones",
		bbar: new Ext.PagingToolbar({
				pageSize:10,
				store:exposicionStore,
				displayInfo:true,
				displayMsg:'Visualizando exposiciones {0} - {1} de {2}',
				emptyMsg:'No hay exposiciones para visualizar'
		})
	});
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'formulario_extjs',
		width:470,
		frame:true,
		title:'Búsqueda de Exposición',
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
									
									exposicionStore.load({
										params:{'start':0,'limit':10,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()}
									});									
								}
							}
						}
					}]
			},gridexposiciones
		
		]
	});
	
	gridexposiciones.on('rowdblclick',function(grid,rowIndex,e){
		                  var r = grid.getStore().getAt(rowIndex);
		                  var selectedId = r.get('id');
		                  exposicionStore.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;

		}
	);
		
});