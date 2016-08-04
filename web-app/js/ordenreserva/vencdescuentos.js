Ext.onReady(function(){
		var store = new Ext.data.JsonStore({
			//totalProperty:'total',
			root:'rows'
			
			,url:'listvencdescuentos'
			,fields:['id','vecimiento','porcentaje']
		});
		
		store.load();
		
		var grid = new Ext.grid.GridPanel({
				store:store
				,columns:[
					{header:"id",dataIndex:"id",hidden:true}
					,{header:"Vencimiento",dataIndex:"vencimiento"}
                                        ,{header:"Porcentaje",dataIndex:"porcentaje"}
				]
                ,height:250
                ,width:420
				,stripeRows:true
				,title:'Porcentaje de Descuentos'
                ,iconCls: 'icon-grid' 
                ,bbar: new Ext.PagingToolbar({
                        	pageSize: 10,
                        	store: store,
                        	displayInfo:true,
                        	displayMsg: 'Visualizando registros {0} - {1} de {2}',
                        	emptyMsg: 'No hay registros para visualizar'
        		})
		});
		
    			var formSearch = new Ext.form.FormPanel({
        				url:'search',
    					renderTo: 'vencimientos-grid',
    					id:'formSearchId',
    					title:'Listado de Vencimientos',
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