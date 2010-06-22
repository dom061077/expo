Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var reciboStore = new Ext.data.JsonStore({
		totalProperty:'total',
		root:'rows',
		url:'listjson',
		fields:['id','fechaAlta','numero','nombre','total','ordenreservanumero','numeroordenreserva'],
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
	
	
	var grid=new Ext.grid.GridPanel({
		store:reciboStore,
		columns:[
			{header:"Empresa",dataIndex:'nombre',width:250},
			{header:"Fecha Alta",dataIndex:'fechaAlta',width:90},
			{header:"Nro.Recibo",dataIndex:'nombre',width:100},
			{header:"Total",dataIndex:'total',width:90}			,
			{header:"Nro.Orden de Reserva",dataIndex:'numeroordenreserva',width:100}			
		],
		stripeRows:true,
		height:250,
		width:600,
		title:"Recibos",
        tbar:[{
        		icon: imagePath+'/pdf.gif'
        		,cls:'x-btn-text-icon'
        		,text:'Pre-visualizar'
        		,handler: function(){
        				var sm = grid.getSelectionModel();
        				var sel = sm.getSelected();
        				if (sm.hasSelection()){
        					
							open('ordenreservareporte?tipo=ORIGINAL&_format=PDF&_name=ordenReservaInstance&_file=OrdenReserva&id='+sel.data.id
							,'_blank')
        				}
							
        		}
			},{
				text:'Anular'
				,handler:function(){
					var sm = grid.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						if(sel.data.totalCancelado>0)
							Ext.MessageBox.show({
								title:"Error",
								msg:"No se puede Anular una Orden que tiene recibos pagados",
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						else{
							Ext.MessageBox.show({
								title:'Mensaje',
								msg:'Desea Anular la Orden de Reserva?',
								icon: Ext.MessageBox.QUESTION,
								buttons:Ext.MessageBox.YESNO,
								fn: function(btn){
									if(btn=='yes'){
															var conn = new Ext.data.Connection();
															conn.request({
																url:'anularordenreserva',
																method:'POST',
																params:{
																	id:sel.data.id
																},
																success: function(resp,opt){
																	var respuesta = Ext.decode(resp.responseText);
																	
																	if(respuesta){
																		if(respuesta.loginredirect)
																			window.location='../logout/index';
																		else {
																			if (respuesta.success)
																				Ext.MessageBox.show({
																					title:'Mensaje'
																					,msg:'El registro fue borrado'
																					,icon:Ext.MessageBox.INFO
																					,buttons:Ext.MessageBox.OK
																					,fn:function(btn){
																							window.location='list'
																					}
																				});
																			else	
																				Ext.Msg.show({
																					title:"Error",
																					msg:respuesta.msg,
																					icon:Ext.MessageBox.ERROR,
																					buttons:Ext.MessageBox.OK
																				});
																		}
																	}
																	
																},
																failure: function(resp,opt){
																	Ext.Msg.show({
																			title:'Error',
																			msg:'Se produjo un error al intentar eliminar el registro',
																			icon:Ext.MessageBox.INFO,
																			buttons: Ext.MessageBox.OK
																	});						
																}
															});
										
									}
								}
							});
						}							
					}else{
        				Ext.MessageBox.show({
        					title:'Advertencia',
        					msg:'Seleccione una fila de la grilla para Anular la orden de reserva',
        					icon:Ext.MessageBox.WARNING,
        					buttons:Ext.MessageBox.OK
        				});	
					}
				}
        	}
		],
        bbar: new Ext.PagingToolbar({
            	pageSize: 10,
            	store: ordenStore,
            	displayInfo:true,
            	displayMsg: 'Visualizando recibos {0} - {1} de {2}',
            	emptyMsg: 'No hay recibos para visualizar'
			})
		
	});
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'formulario_extjs',
		id:'formSearchId',
		title:'Recibos',
		width:650,
		frame:true,
		items:[
			{
				layout:'column',
				items:[
					{	columnWidth: .42,
						layout:'form',
						items:{
							xtype:'textfield',
							name:'searchCriteria',
							id:'searchCriteriaId',
							width:160,
							fieldLabel:'Empresa o Nro de Recibo'
						}
					},{
						layout:'form',
						items:{
							xtype:'button',
							text:'Buscar',
							listeners:{
								click:function(){
									reciboStore.load({
										params:{'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()}
									});		
								}
							}
						}
					}
				]
			},grid
		]
	});
	
});