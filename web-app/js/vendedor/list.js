Ext.onReady(function(){
		var store = new Ext.data.JsonStore({
		
		});
		
		store.load({params:{start:0, limit:10}});
		
		var grid = new Ext.grid.GridPanel({
				store:store,
				columns:[
					{header:"id",dataIndex:"id",hidden:true}
					,{header:"Nombre",dataIndex:"nombre"}
				],
				stripeRows:true
				,title:'Vendedores'
		});
		
    			var formSearch = new Ext.form.FormPanel({
        				url:'search',
    					renderTo: 'vendedor-grid',
    					id:'formSearchId',
    					title:'Listado de Empresas',
    					width:450,
    					frame:true,
    					items: [{
    							layout: 'column',
    							anchor: '0',
    							items: [{
    										columnWidth: .5,
    										layout: 'form',
    										items: {
    											xtype: 'textfield',
    											name: 'searchCriteria',
    											fieldLabel: 'Texto a Buscar',
    											anchor: '0'
    										}
    									},{
    										columnWidth: .5,
    										items: {
    											xtype: 'button',
    											text: 'Buscar',
    											listeners:{
    												click: function(){
							        	          		formSearch.getForm().submit({
																success: function(f,a){
																	store.load({
																		params: {'start':0,'limit':10,'searchCriteria':a.result.searchCriteria}
																	});
							        	          				}
								        	          		});
    													
    												}
    												}
    											}
	        	          	  		 
    									}
    									
    								]
    						},grid]
    				});
		
});