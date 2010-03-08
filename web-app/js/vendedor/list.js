Ext.onReady(function(){
		var store = new Ext.data.JsonStore({
			//totalProperty:'total',
			root:'rows'
			
			,url:'listjson'
			,fields:['id','nombre']
		});
		
		store.load();
		
		var grid = new Ext.grid.GridPanel({
				store:store
				,columns:[
					{header:"id",dataIndex:"id",hidden:true}
					,{header:"Nombre",dataIndex:"nombre"}
				]
                ,height:250
                ,width:420
				,stripeRows:true
				,title:'Vendedores'
                ,iconCls: 'icon-grid' 
                ,bbar: new Ext.PagingToolbar({
                        	pageSize: 10,
                        	store: store,
                        	displayInfo:true,
                        	displayMsg: 'Visualizando vendedores {0} - {1} de {2}',
                        	emptyMsg: 'No hay vendedores para visualizar'
        		})
		});
		
    			var formSearch = new Ext.form.FormPanel({
        				url:'search',
    					renderTo: 'vendedor-grid',
    					id:'formSearchId',
    					title:'Listado de Vendedores',
    					width:450,
    					height:300,
    					frame:true,
    					items: [grid]
    				});
		grid.on('rowdblclick',function(grid,rowIndex,e) {
		                  var r = grid.getStore().getAt(rowIndex);
		                  var selectedId = r.get('id');
		                  store.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;
		});	
});