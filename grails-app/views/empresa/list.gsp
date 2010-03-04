
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Listado de Empresas</title>
        <script type="text/javascript">
        	Ext.onReady(function(){
            	var store = new Ext.data.JsonStore({
							totalProperty: 'total',
							root: 'rows',
							url: 'listjson',
							fields:[
									'id','nombre','nombreRepresentante','telefono1'
							]
            		});
            	store.load({params:{start:0, limit:10}});
            	var grid = new Ext.grid.GridPanel({
                		store:store,
                		columns: [
                		          {header: "id",dataIndex:'id',hidden:true},		  	
                		          {header: "Nombre",width:200,sortable:true,dataIndex:'nombre'},
                		          {header: "Representante", width:200,sortable:true,dataIndex:'nombreRepresentante'},
                		          {header: "telefono1",width:100}
                          		],
                        stripeRows: true,
                        height:250,
                        width:450,
                        title:'Empresas',
                        iconCls: 'icon-grid', 
                        bbar: new Ext.PagingToolbar({
                            	pageSize: 10,
                            	store: store,
                            	displayInfo:true,
                            	displayMsg: 'Visualizando empresas {0} - {1} de {2}',
                            	emptyMsg: 'No hay empresas para visualizar'
            				})
        			});
    			//grid.render('empresa-grid');
    			grid.on('rowdblclick',function(grid, rowIndex, e){
		                   var r = grid.getStore().getAt(rowIndex);
		                   var selectedId = r.get('id');
		                  //console.log(selectedId);
		                  store.reload({params: {id_ft: selectedId}});
		                  window.location = 'edit?id='+selectedId;
						  	 	
    					});
    			var formSearch = new Ext.form.FormPanel({
        				url:'search',
    					renderTo: 'empresa-grid',
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
        </script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Agregar Empresa</g:link></span>
        </div>
        <div id="empresa-grid">
        	
        </div>
    </body>
</html>