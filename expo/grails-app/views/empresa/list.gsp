
<%@ page import="com.rural.Empresa" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Empresa List</title>
        <script type="text/javascript">
        	Ext.onReady(function(){
            	var store = new Ext.data.JsonStore({
							totalProperty: 'total',
							root: 'rows',
							url: 'listjson',
							fields:[
									'nombre','nombreRepresentante','telefono1'
							]
            		});
            	store.load({params:{start:0, limit:10}});
            	var grid = new Ext.grid.GridPanel({
                		store:store,
                		columns: [
                		          {header: "Nombre",width:100,sortable:true,dataIndex:'nombre'},
                		          {header: "Representante", width:100,sortable:true,dataIndex:'nombreRepresentante'},
                		          {header: "telefono1",width:20}
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
    			
    			var formSearch = new Ext.form.FormPanel({
    					renderTo: 'empresa-grid',
    					id:'formSearchId',
    					title:'Listado de Empresas',
    					width:500,
    					frame:true,
    					items: [{
    							layout: 'column',
    							anchor: '0',
    							items: [{
    										columnWidth: .5,
    										layout: 'form',
    										items: {
    											xtype: 'textfield',
    											name: 'textbusqueda',
    											fieldLabel: 'Texto a Buscar',
    											anchor: '0'
    										}
    									},{
    										columnWidth: .5,
    										items: {
    											xtype: 'button',
    											text: 'Buscar'
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
