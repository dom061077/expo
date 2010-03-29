Ext.onReady(function(){
	Ext.QuickTips.init();
	var empresassimilaresStore = new Ext.data.JsonStore({
			autoLoad:true,
			url:'listempresasconsimilitudes',
			root:'rows',
			fields: ['id','nombre']
	});
	
	var empresassimilaresDetalleStore = new Ext.data.JsonStore({
			autoLoad:true,
			url:'listempresasconsimilitudes',
			root:'rows',
			fields: ['id','nombre']
	});
	
	var gridCabecera = new Ext.grid.GridPanel({
			store:empresassimilaresStore,
			columns: [
    		          {header: "id",dataIndex:'id',hidden:true},		  	
    		          {header: "Nombre",width:200,sortable:true,dataIndex:'nombre'},
			],
			stripRows:true,
			height:150,
			width:400,
			title:'Empresas con nombres similares'
	});
	
	var gridDetalle = new Ext.grid.GridPanel({
		 store:empresassimilaresDetalleStore,
		 columns:[
		          {header:'id',dataIndex:'id',hidden:true},
		          {header:'Nombre',width:200,sortable:true,dataIndex:'nombre'}
		          ],
		 stripRows:true,
		 height:250,
		 width:400,
		 title:'Detalle de Empresas Similares'
	});
	
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'empresassimilares-grid',
		width:470,
		frame:true,
		title:'Empresas Similares',
		items:[{
				layout:'column',
				anchor: '0',
				items: [{
							columnWidth:.5,
							layout:'form',
								items:{
								       xtype:'textfield',
								       id:'searchCriteriaId',
								       name:'searchCriteria',
								       fieldLabel: 'Texto a Buscar',
								       anchor: '0'
								}
						},{
							columnWidth:.5,
							layout:'form',
								items:{
										xtype:'button',
										text:'Buscar'
								}
						}
				]
		},gridCabecera,gridDetalle
		]
	});
});