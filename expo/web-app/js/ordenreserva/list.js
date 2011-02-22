Ext.onReady(function(){
	Ext.QuickTips.init();
	var sort;
	var dir;
	var ordenStore = new Ext.data.JsonStore({
		totalProperty: 'total',
		remoteSort:true,
		root: 'rows',
		url:'listjson',
		fields:['id','numero','fechaAlta','subTotal','total','totalCancelado','saldo','anio','expoNombre','nombre','sector','lote'],
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
	

	
	function currencyRender(v,params,data){
		/*num = num.toString().replace(/\$|\,/g, '');
		if (isNaN(num)) num = "0";
		sign = (num == (num = Math.abs(num)));
		num = Math.floor(num * 100 + 0.50000000001);
		cents = num % 100;
		num = Math.floor(num / 100).toString();
		if (cents < 10) cents = "0" + cents;
		for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
		num = num.substring(0, num.length - (4 * i + 3)) + '.' + num.substring(num.length - (4 * i + 3));
		return (((sign) ? '' : '-') + '$' + num + ',' + cents);	*/
		return Ext.util.Format.usMoney(v);
		//Ext.util.Format.number( v,'0,000.00');
	}
	
	function ordenRender(v,params,data){
		var numero = 100000000+v;
		return numero.toString().substring(1,9);
		//00000011
	}
	
	var grid = new Ext.grid.GridPanel({
		store:ordenStore,
		columns:[
					{header:"Empresa",dataIndex:'nombre',width:200,sortable:true},
					{header:"Sector",dataIndex:'sector',width:200,sortable:true},
					{header:"Lote",dataIndex:'lote',width:100,hidden:false,sortable:true},
					{header:"Sub Total",dataIndex:'subTotal',width:100,renderer:currencyRender},					
					{header:"Total",dataIndex:'total',width:80,renderer:currencyRender,sortable:true},
					{header:"Total Cancelado",dataIndex:'totalCancelado',width:100,renderer:currencyRender},
					{header:"Saldo",dataIndex:'saldo',width:80,renderer:currencyRender},					
					{header:"Exposición",dataIndex:'expoNombre',width:200},
					{header:"Año",dataIndex:'anio',width:80},					
					{header:"id",dataIndex:"id",hidden:true},
					{header:"Número Orden",dataIndex:"numero",width:80,renderer:ordenRender,sortable:true},
					{header:"Fecha",dataIndex:'fechaAlta',width:80,renderer: Ext.util.Format.dateRenderer('d/m/y'),sortable:true}
			],
		stripeRows: true,
		loadMask:true,
		height:250,
		width:600,
		title:'Ordenes de Reserva',
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
																					,msg:'La Orden de Reserva fue Anulada correctamente'
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
        	},{
        		icon: imagePath+'/'
        		,cls:'x-btn-text-icon'
        		,text:'Recibo'
        		,handler: function(){
        			var sm = grid.getSelectionModel();
        			var sel = sm.getSelected();
        			if (sm.hasSelection()){
        				if(sel.data.saldo==0)
        					Ext.MessageBox.show({
        						title:'Mensaje',
        						msg:'No puede generar un recibo de una Orden de Reserva con saldo Cero',
        						buttons:Ext.MessageBox.OK
        					});
        				else 
        					window.location='../recibo/create?ordenreservaId='+sel.data.id;	
        			}else{
        				Ext.MessageBox.show({
        					title:'Advertencia',
        					msg:'Seleccione una fila de la grilla para generar el recibo',
        					icon:Ext.MessageBox.WARNING,
        					buttons:Ext.MessageBox.OK
        				});	
        			}
        		}
			},{
            	icon: imagePath+'/skin/excel.gif'
            	,text:'Exportar'
            	,cls:'x-btn-text-icon'
            	,handler: function(){
            		/*'campos':[Ext.getCmp('campoIdFiltro1').getValue()
														,Ext.getCmp('campoIdFiltro2').getValue()
														,Ext.getCmp('campoIdFiltro3').getValue()],
											'condiciones':[Ext.getCmp('condicionesIdFiltro1').getValue()
														,Ext.getCmp('condicionesIdFiltro2').getValue()
														,Ext.getCmp('condicionesIdFiltro3').getValue()],
											'searchString':[Ext.getCmp('searchStringIdFiltro1').getValue()
														,Ext.getCmp('searchStringIdFiltro2').getValue()
														,Ext.getCmp('searchStringIdFiltro3').getValue()
													]*/
            		
    				open('export?campos='+Ext.getCmp('campoIdFiltro1').getValue()+'&campos='+Ext.getCmp('campoIdFiltro2').getValue()
    					+'&campos='+Ext.getCmp('campoIdFiltro3').getValue()+'&condiciones='+
    					Ext.getCmp('condicionesIdFiltro1').getValue()+'&condiciones='
    					+Ext.getCmp('condicionesIdFiltro2').getValue()+'&condiciones='
    					+Ext.getCmp('condicionesIdFiltro3').getValue()+'&searchString='
    					+Ext.getCmp('searchStringIdFiltro1').getValue()+'&searchString='
    					+Ext.getCmp('searchStringIdFiltro2').getValue()+'&searchString='
    					+Ext.getCmp('searchStringIdFiltro3').getValue()
    					+'&sort='+sort+'&dir='+dir
    					,'_blank')
            	}
        		
        }]/*,
        bbar: new Ext.PagingToolbar({
            	pageSize: 10,
            	store: ordenStore,
            	displayInfo:true,
            	displayMsg: 'Visualizando registros {0} - {1} de {2}',
            	emptyMsg: 'No hay registros para visualizar'
			})*/
		
	});
	
	grid.on('sortchange',function(grid,sortInfo){
		sort = sortInfo.field;
		dir = sortInfo.direction;
		
	});
	var camposStore= new Ext.data.SimpleStore({
									id:0,
									fields:['idcampo','labelcampo'],
									data:[['nombre','Nom.Empresa'],['sector','Sector'],['lote','Lote'],['expo','Exposición']
											,['numero','Número'],['fechaAlta','Fecha Alta']]
								})
	var condicionesStore=new Ext.data.SimpleStore({
									id:0,
									fields:['idcond','nombrecond'],
									data:[['eq','Igual a']
										  ,['ne','No es igual a']
										  ,['gt','Mayor que']
										  ,['lt','Menor que']
										  ,['ge','Mayor o igual que']
										  ,['le','Menor o igual que']
										  ,['ilike2','Contiene']
										  ,['ilike','No Contiene']]
								});									
	
	var formSearch = new  Ext.form.FormPanel({
		url:'search',
		renderTo:'ordenreserva_form',
		id:'formSearchId',
		title:'Ordenes de Reserva',
		width:680,
		frame:true,
		items:[	
				{
					layout:'column',
					
					items:[
						{
							columnWidth: .3,
							layout:'form',
							width:50,
							items:{
								xtype:'combo',
								mode:'local',
								name:'campos',
								id:'campoIdFiltro1',
								hideLabel:false,
								fieldLabel:'Filtro 1',
								labelWidth:60,
								valueField:'idcampo',
								displayField:'labelcampo',
								store: camposStore,
								anchor:'95%'
							}
						},{
							columnWidth: .2,
							layout:'form',
							items:{
								xtype:'combo',
								mode:'local',
								name:'condiciones',
								id:'condicionesIdFiltro1',
								valueField:'idcond',
								displayField:'nombrecond',
								store: condicionesStore,
								boxMaxWidth:100,
								msgTarget:'under',
								hideLabel:true,
								anchor:'95%'
							}
							
						},{
							columnWidth: .3,
							layout:'form',
							items:{
										xtype:'textfield',
										hideLabel:true,
										id:'searchStringIdFiltro1',
										anchor:'95%'
										/*,
										listeners:{
											click: function(){
												ordenStore.load({
														params:{'fieldSearch':Ext.getCmp('combocriteriosId').getValue(),'start':0,'limit':10
															,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()
															,'anulada':Ext.getCmp('soloanuladasId').getValue()
															}
													});
											}
										}*/
							}
						}/*,{
							columnWidth: .3,
							layout:'form',
							items:{
										xtype:'button',
										listeners:{
											click: function(){
												ordenStore.load({
														params:{'fieldSearch':Ext.getCmp('combocriteriosId').getValue(),'start':0,'limit':10
															,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()
															,'anulada':Ext.getCmp('soloanuladasId').getValue()
															}
													});
											}
										}
							}
						}*/
						
					]
				},{
					layout:'column',
					items:[
						{
							columnWidth: .3,
							layout:'form',
							width:50,
							items:{
								xtype:'combo',
								mode:'local',
								name:'campos',
								id:'campoIdFiltro2',
								hideLabel:false,
								fieldLabel:'Filtro 2',
								labelWidth:60,
								valueField:'idcampo',
								displayField:'labelcampo',
								store: camposStore,
								anchor:'95%'
							}
						},{
							columnWidth: .2,
							layout:'form',
							items:{
								xtype:'combo',
								mode:'local',
								name:'condiciones',
								id:'condicionesIdFiltro2',
								valueField:'idcond',
								displayField:'nombrecond',
								store: condicionesStore,
								boxMaxWidth:100,
								msgTarget:'under',
								hideLabel:true,
								anchor:'95%'
							}
							
						},{
							columnWidth: .3,
							layout:'form',
							items:{
										xtype:'textfield',
										hideLabel:true,
										id:'searchStringIdFiltro2',
										anchor:'95%'
										/*,
										listeners:{
											click: function(){
												ordenStore.load({
														params:{'fieldSearch':Ext.getCmp('combocriteriosId').getValue(),'start':0,'limit':10
															,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()
															,'anulada':Ext.getCmp('soloanuladasId').getValue()
															}
													});
											}
										}*/
							}
						}

					]
				},{
					layout:'column',
					items:[
						{
							columnWidth: .3,
							layout:'form',
							width:50,
							items:{
								xtype:'combo',
								mode:'local',
								name:'campos',
								id:'campoIdFiltro3',
								hideLabel:false,
								fieldLabel:'Filtro 3',
								labelWidth:60,
								valueField:'idcampo',
								displayField:'labelcampo',
								store: camposStore,
								anchor:'95%'
							}
						},{
							columnWidth: .2,
							layout:'form',
							items:{
								xtype:'combo',
								mode:'local',
								name:'condiciones',
								id:'condicionesIdFiltro3',
								valueField:'idcond',
								displayField:'nombrecond',
								store: condicionesStore,
								boxMaxWidth:100,
								msgTarget:'under',
								hideLabel:true,
								anchor:'95%'
							}
							
						},{
							columnWidth: .3,
							layout:'form',
							items:{
										xtype:'textfield',
										hideLabel:true,
										id:'searchStringIdFiltro3',
										anchor:'95%'
										/*,
										listeners:{
											click: function(){
												ordenStore.load({
														params:{'fieldSearch':Ext.getCmp('combocriteriosId').getValue(),'start':0,'limit':10
															,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()
															,'anulada':Ext.getCmp('soloanuladasId').getValue()
															}
													});
											}
										}*/
							}
						}

					]
				},{
					xtype:'checkbox',
					name:'soloanuladas',
					id:'soloanuladasId',
					fieldLabel:'Solo Anuladas?'
				},{
						xtype:'button',
						text:'Buscar',
						listeners:{
							click: function(){
								ordenStore.load({
										params:{
											'campos':[Ext.getCmp('campoIdFiltro1').getValue()
														,Ext.getCmp('campoIdFiltro2').getValue()
														,Ext.getCmp('campoIdFiltro3').getValue()],
											'condiciones':[Ext.getCmp('condicionesIdFiltro1').getValue()
														,Ext.getCmp('condicionesIdFiltro2').getValue()
														,Ext.getCmp('condicionesIdFiltro3').getValue()],
											'searchString':[Ext.getCmp('searchStringIdFiltro1').getValue()
														,Ext.getCmp('searchStringIdFiltro2').getValue()
														,Ext.getCmp('searchStringIdFiltro3').getValue()
													],
											'soloAnuladas':Ext.getCmp('soloanuladasId').getValue()		
										}
								});
							}
						}
				},grid
		
		]
	});
	
});