Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var sort='';
	var dir='';
	
	var filters = new Ext.ux.grid.GridFilters({
		encode:true,
		local:false,
		menuFilterText:'Filtro',
		emptyText:'Ingrese Filtro...',
		filters:[
		         {	type:'string',
		        	 dataIndex: 'nombre'
		         },{
		        	type:'string',
		        	dataIndex:'expo'
		         },{
		        	type:'numeric',
		        	dataIndex:'numero'
		         },{
		        	type:'numeric',
		        	dataIndex:'numeroordenreserva'
		         }	 
		]
	});
	
	var notaStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		remoteSort:true,
		root:'rows',
		url:'listjson',
		fields:['id','nombre','fechaAlta','numero','total','tipo','tipoGen','numeroordenreserva','expo','totalletras'],
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
		return Ext.util.Format.usMoney(v);
	}
	
	function notaRender(v,params,data){
		var numero = 100000000+v;
		return numero.toString().substring(1,9);
		//00000011
	}
	
	var grid=new Ext.grid.GridPanel({
		store:notaStore,
		plugins:[filters],
		columns:[
			{header:"Id",dataIndex:'id',hidden:true},
			{header:"Empresa",dataIndex:'nombre',width:250,sortable:true},
			{header:"Fecha Alta",dataIndex:'fechaAlta',width:90,renderer: Ext.util.Format.dateRenderer('d/m/y'),sortable:true},
			{header:"Nro.Nota",dataIndex:'numero',width:100,renderer:notaRender,sortable:true},
			{header:"Total",dataIndex:'total',width:90,renderer:currencyRender},
			{header:"Tipo",dataIndex:'tipo',width:90,sortable:true},			
			{header:"Creación",dataIndex:'tipoGen',width:90,sortable:true},			
			{header:"Nro.Orden de Reserva",dataIndex:'numeroordenreserva',width:100,renderer:notaRender,sortable:true},
			{header:"Exposición",dataIndex:'expo',width:100,sortable:true},			
			{header:"Total Letras",dataIndex:'totalletras',width:100,hidden:true}
		],
		stripeRows:true,
		height:450,
		width:750,
		loadMask:true,
		title:"Notas",
        tbar:[{
        		icon: imagePath+'/pdf.gif'
        		,cls:'x-btn-text-icon'
        		,text:'Pre-visualizar'
        		,handler: function(){
        				var sm = grid.getSelectionModel();
        				var sel = sm.getSelected();
        				if (sm.hasSelection()){

							open('reporte?target=_blank&_format=PDF&_name=notadc&_file=notadc&id='+sel.data.id+"&totalletras="+sel.data.totalletras
    						+'&sort='+sort+'&dir='+dir							
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
								msg:'Desea Anular la Nota?',
								icon: Ext.MessageBox.QUESTION,
								buttons:Ext.MessageBox.YESNO,
								fn: function(btn){
									if(btn=='yes'){
															var conn = new Ext.data.Connection();
															conn.request({
																url:'anularnota',
																method:'POST',
																params:{
																	id:sel.data.id
																},
																success: function(resp,opt){
																	var respuesta = Ext.decode(resp.responseText);
																	var msg='';
																	if(respuesta){
																		if(respuesta.loginredirect)
																			window.location='../logout/index';
																		else {
																			if (respuesta.success)
																				Ext.MessageBox.show({
																					title:'Mensaje'
																					,msg:'La nota fue Anulada correctamente'
																					,icon:Ext.MessageBox.INFO
																					,buttons:Ext.MessageBox.OK
																					,fn:function(btn){
																							window.location='list'
																					}
																				});
																			else{
																		    	if (respuesta.errors){
																		    		for (var i=0; i<respuesta.errors.length;i++){
																		    			msg=msg+respuesta.errors[i].title+'\r\n';	
																    				}
																    				
															    				}

																				Ext.Msg.show({
																					title:"Error",
																					msg:msg,
																					icon:Ext.MessageBox.ERROR,
																					buttons:Ext.MessageBox.OK
																				});
																			}
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
            	icon: imagePath+'/skin/excel.gif'
            	,text:'Exportar'
            	,cls:'x-btn-text-icon'
            	,handler: function(){
            		var filters = grid.filters.getFilterData();
            		var tmp = [];
		            for (var i = 0; i < filters.length; i++) {
		                f = filters[i];
		                tmp.push(Ext.apply(
		                    {},
		                    {field: f.field},
		                    f.data
		                ));
		            }
		            
		            open('excel?soloanuladas='+Ext.getCmp('soloanuladasId').getValue()
	    					+'&filter='+Ext.util.JSON.encode(tmp)
	    					+'&sort='+sort+'&dir='+dir
	    					,'_blank')
            		
    				/*open('excel?searchCriteria='+Ext.getCmp('searchCriteriaId').getValue()+'&fieldSearch='
    					+Ext.getCmp('combocriteriosId').getValue()+'&anulada='+Ext.getCmp('soloanuladasId').getValue()
    					+'&sort='+sort+'&dir='+dir    					
						,'_blank')*/					
            	}
        		
        	},{
        		text:'Quitar Filtros'
				,handler: function(){
					grid.filters.clearFilters();
				}
        	}
		],
        bbar: new Ext.PagingToolbar({
            	pageSize: 30,
            	store: notaStore,
            	displayInfo:true,
            	displayMsg: 'Visualizando notas {0} - {1} de {2}',
            	emptyMsg: 'No hay notas para visualizar'
			})
		
	});
	
	grid.on('sortchange',function(grid,sortInfo){
		sort = sortInfo.field;
		dir = sortInfo.direction;
		
	});	
	
	
	var formSearch = new Ext.form.FormPanel({
		url:'search',
		renderTo:'formulario_extjs',
		id:'formSearchId',
		title:'Notas',
		width:800,
		frame:true,
		items:[
			{
				xtype:'checkbox',
				name:'soloanuladas',
				id:'soloanuladasId',
				fieldLabel:'Solo Notas Anuladas',
				listeners:{
							check: function(check,checked){
								notaStore.load();
							}
						}
			},grid
		]
	});

	notaStore.on("beforeload",function(){
			notaStore.baseParams={
				soloanuladas:Ext.getCmp('soloanuladasId').getValue(),
				start:0,
				limit:30
			}
	});	
	
});