Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var ordenStore = new Ext.data.JsonStore({
		totalProperty: 'total',
		root: 'rows',
		url:'listjson',
		fields:['id','numero','fechaAlta','total','anio','expoNombre','empresaNombre'],
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
		store:ordenStore,
		columns:[
					{header:"id",dataIndex:"id",hidden:true},
					{header:"Número",dataIndex:"numero",width:80},
					{header:"Fecha",dataIndex:'fechaAlta',width:80,renderer: Ext.util.Format.dateRenderer('d/m/y')},
					{header:"Total",dataIndex:'total',width:80},
					{header:"Año",dataIndex:'anio',width:80},					
					{header:"Exposición",dataIndex:'expoNombre',width:200},					
					{header:"Empresa",dataIndex:'empresaNombre',width:200}
			],
		stripeRows: true,	
		height:250,
		width:440,
		title:'Ordenes de Reserva',
        tbar:[{
        		icon: imagePath+'/pdf.gif'
        		,cls:'x-btn-text-icon'
        		,text:'Reporte'
        		,handler: function(){
        				var sm = grid.getSelectionModel();
        				var sel = sm.getSelected();
        				if (sm.hasSelection()){
        					
							open('ordenreservareporte?tipo=ORIGINAL&_format=PDF&_name=ordenReservaInstance&_file=OrdenReserva&id='+sel.data.id
							,'_blank')
        				}
							
        		}
        	},{
        		icon: imagePath+'/'
        		,cls:'x-btn-text-icon'
        		,text:'Recibo'
        		,handler: function(){
        			var sm = grid.getSelectionModel();
        			var sel = sm.getSelected();
        			if (sm.hasSelection())
        				window.location='../recibo/create?ordenreservaId='+sel.data.id;	
        			else{
        				Ext.MessageBox.show({
        					title:'Advertencia',
        					msg:'Seleccione una fila de la grilla para generar el recibo',
        					icon:Ext.MessageBox.WARNING,
        					buttons:Ext.MessageBox.OK
        				});	
        			}
        		}
        }],
        bbar: new Ext.PagingToolbar({
            	pageSize: 10,
            	store: ordenStore,
            	displayInfo:true,
            	displayMsg: 'Visualizando ordenes {0} - {1} de {2}',
            	emptyMsg: 'No hay ordenes para visualizar'
			})
		
	});
	
	var formSearch = new  Ext.form.FormPanel({
		url:'search',
		renderTo:'ordenreserva_form',
		id:'formSearchId',
		title:'Ordenes de Reserva',
		width:470,
		frame:true,
		items:[
				{
					layout:'column',
					anchor: '0',
					items:[
						{	columnWidth: .5,
							layout:'form',
							items: {
										xtype: 'textfield',
										id:'searchCriteriaId',
										name:'searchCriteria',
										fieldLabel:'Texto a Buscar',
										anchor:'0'
								}
						},{
							columnWidth: .5,
							layout:'form',
							items:{
										xtype:'button',
										text:'Buscar',
										listeners:{
											click: function(){
												ordenStore.load({
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
	
});