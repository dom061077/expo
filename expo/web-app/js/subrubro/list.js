Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var store = new Ext.data.JsonStore({
		url:'listjson',
		root:'rows',
		fields:['id','nombreSubrubro'],
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
	
	var grid = new Ext.grid.GridPanel({
		title:'Sub Rubros',
		store:store,
		columns:[
			{header:'id',dataIndex:'id',hidden:true},
			{header:'Nombre',dataIndex:'nombreSubrubro',width:200}
		],
		stripeRows:true,
		height:300,
		width:400,
		bbar: new Ext.PagingToolbar({
			pageSize:10,
			store:store,
			displayInfo:true,
			displayMsg:'Visualizando Sub Rubros {0} - {1} de {2}',
			emptyMsg:'No hay Sub Rubros para visualizar'
		})
	});
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		title:'Listado de Sub Rubros',
		renderTo:'formulario_extjs',
		frame:true,
		width:500,
		items:[
			{
				layout:'column',
				items:[
					{
						columnWidth: .5,
						layout:'form',
						items:{
							xtype:'textfield',
							name:'searchCriteria',
							id:'searchCriteriaId',
							fieldLabel:'Texto a Buscar'
						}
					},{
						columnWidth: .5,
						layout:'form',
						items:{
							xtype:'button',
							text:'Buscar',
							listeners:{
								click:function(){
									store.load({
										params:{'start':0,'limit':10,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()}
									});
								}
							}
						}
					}
				]
			},grid
		]
	});
	
	grid.on('rowdblclick',function(grid,rowIndex,e){
		var r = grid.getStore().getAt(rowIndex);
		var selectedId = r.get('id');
		window.location = 'edit?id='+selectedId;
	});
	
	
	
});