Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var logosStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listjson',
		fields:['id','nombre']
		
	});
	
	var gridlogos = new Ext.grid.GridPanel({
		store:logosStore,
		columns:[
			{header:"id",dataIndex:'id',hidden:true},
			{header:"Año",dataIndex:'anio'}
		],
		height:250,
		width:460,
		tbar:[
			/*{
				text:'Agregar',
				handler:function(){
					var sm = gridlogos.getSelectionModel();
					var sel = sm.geSelected();
					if(sm.hasSelection()){
						
					}
				}
			}*/
		]
	});
	
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
				hidden:true,
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