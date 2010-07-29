
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
							],
		        			listeners: {
			                    loadexception: function(proxy, store, response, e) {
						                    var jsonObject = Ext.util.JSON.decode(response.responseText);
						                    if (jsonObject)
							                    if (jsonObject.loginredirect == true)
							                    		window.location='../logout/index';
	                    
						                   }
	        						
		        				}							
            		});
            		
        		store.on("beforeload",function(){
        			
						store.baseParams={
								searchCriteria:Ext.getCmp('searchCriteriaId').getValue()
						}
        		});
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
                        width:540,
                        title:'Empresas',
                        iconCls: 'icon-grid', 
                        tbar:[{
                        		icon: imagePath+'/skin/excel.gif'
                        		,cls:'x-btn-text-icon'
                        		,handler: function(){
    									open('exportempresastoexcel?searchCriteria='+Ext.getCmp('searchCriteriaId').getValue(),'_blank')
                        		}
                        }],
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
    					width:550,
    					frame:true,
    					items: [{
    							layout: 'column',
    							
    							anchor: '0',
    							items: [{
    										columnWidth: .5,
    										layout: 'form',
    										items: {
    											xtype: 'textfield',
    											id:'searchCriteriaId',
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
																	document.getElementById('searchCriteriaJasperId').value=a.result.searchCriteria;
																	store.load({
																		params: {'start':0,'limit':10,'searchCriteria':a.result.searchCriteria}
																	});
							        	          				},
							        	          				failure: function(form,action){
							        	          			        switch (action.failureType) {
								        	          		            case Ext.form.Action.CLIENT_INVALID:
								        	          		                Ext.MessageBox.show({
									        	          		                title:'Fallo',
									        	          		                msg:'No se puede enviar la petición con valores incorrectos',
										        	          		            icon:Ext.MessageBox.ERROR,
										        	          		            buttons:Ext.MessageBox.OK
								        	          		                });
								        	          		                break;
								        	          		            case Ext.form.Action.CONNECT_FAILURE:
									        	          		            Ext.MessageBox.show({
										        	          		            title:'Fallo',
										        	          		            msg:'Error de comunicación con el servidor',
										        	          		            icon:Ext.MessageBox.ERROR,
										        	          		            buttons:Ext.MessageBox.OK
									        	          		            });
								        	          		                break;
								        	          		            case Ext.form.Action.SERVER_INVALID:
								        	          		               Ext.Msg.alert('Failure', action.result.msg);
							        	          		       		}
							        	          										        	          					
							        	          				}
								        	          		});
    													
    												}
    												}
    											}
	        	          	  		 
    									}
    									
    								]
    						},grid]
    				});
            	store.load({params:{start:0, limit:10}});
    			
    			
    			
        	});
        </script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">Agregar Empresa</g:link></span>
        </div>
        <div id="empresa-grid">
        	
        </div>
        
		<g:jasperReport
		                                   controller="empresa"
		                                   action="ordendereserva"
		                                   jasper="report1"
		                                   format="PDF"
		                                   name="empresas">
		         <input type="hidden" name="searchCriteriaJasper" id="searchCriteriaJasperId" value=""/>                          
		                                   
		                                   
         </g:jasperReport>
		        
        
    </body>
</html>
