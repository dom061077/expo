Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var logosStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listjson',
		fields:['id','anio','exponombre'],
		baseParams:{exposicionId:exposicionId}
		
	});
	
	var gridlogos = new Ext.grid.GridPanel({
		store:logosStore,
		columns:[
			{header:"id",dataIndex:'id',hidden:true},
			{header:"Año",dataIndex:'anio'},
			{header:"Expo nombre",dataIndex:'exponombre'}
		],
		height:250,
		width:460,
		tbar:[
			{
				text:'Pre-visualizar',
				handler:function(){
					var sm = gridlogos.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						open('logopreview?id='+sel.data.id,'_blank');
					}
				}
			}
		]
	});
	
	gridlogos.on('rowdblclick',function(grid,rowIndex,e){
		                  var r = grid.getStore().getAt(rowIndex);
		                  var selectedId = r.get('id');
		                  logosStore.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;

		}
	);
	
	var formSearch = new Ext.form.FormPanel({
		title:'Logos de Exposición',
		renderTo:'formulario_extjs',
		width:470,
		frame:true,
		items:[
			{
				xtype:'textfield',
				id:'exposicionId',
				name:'exposicionId',
				hidden:true
				,
				value:exposicionId,
				hideLabel:true
			},{
				xtype:'textfield',
				fieldLabel:'Exposición',
				id:'exposicionNombreId',
				name:'exposicionNombre',
				value:exposicionNombre,
				disabled:true
			},gridlogos
		]
	});
	
	
});