Ext.onReady(function(){
	Ext.QuickTips.init();
	var sort;
	var dir;

    /*var expander = new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
            '<p><b>Company:</b> {company}</p><br>',
            '<p><b>Summary:</b> {desc}</p>'
        )
    });*/

    var expander = new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
            '{detalledesc}'
        )

    });

    function getMyStuff(record,index){
        //	Using JQuery to 'load' the expanded row with content pulled remotely
        $('#remData'+index).load('www.google.com');
    };
	
	var ordenStore = new Ext.data.JsonStore({
		totalProperty: 'total',
		//reader:reader,
		//groupField:'nombre',
		//autoLoad:true,
		remoteSort:true,
		root: 'rows',
		url:'listjson',
		fields:['id','ordenId','numero','fechaAlta','subTotal','subTotalConDescuento','subTotalOtrosConceptos','total','totalcondesc','debito','credito','recibo','saldo','saldocondescuento','detalledesc','anio','expoNombre','nombre','razonSocial','usuario','vendedor','sector','lote'],
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
	
	ordenStore.on("beforeload",function(){
			ordenStore.baseParams={
				soloanuladas:Ext.getCmp('soloanuladasId').getValue()
			}
	});
	
	var filters = new Ext.ux.grid.GridFilters({
        // encode and local configuration options defined previously for easier reuse
        encode: true, // json encode the filter query
        local: false,   // defaults to false (remote filtering)
        //autoReload: false,
        menuFilterText:'Filtro',
        emptyText:'Ingrese Filtro...',
        filters: [{
         	type: 'numeric',
            dataIndex: 'ordenId'
            
        }, {	
            type: 'string',
            dataIndex: 'nombre'
            //,updateBuffer:2000
        }, {	
            type: 'string',
            dataIndex: 'razonSocial'
        }, {
            type: 'string',
            dataIndex: 'sector',
            disabled: false
        }, {
            type: 'string',
            dataIndex: 'lote'
        }, {
            type: 'numeric',
            dataIndex: 'subTotal',
            disabled:true
        }, {
            type: 'numeric',
            dataIndex: 'subTotalOtrosConceptos',
            disabled:true
        }, {
            type: 'numeric',
            dataIndex: 'totalCancelado',
            disabled:true
        }, {
            type: 'numeric',
            dataIndex: 'saldo',
            disabled:true
            
        },{
        	type:'string',
        	dataIndex:'expoNombre'
        },{
        	type:'numeric',
        	dataIndex:'anio',
        	updateTask:2000,
        	menuItemCfgs:{
        		emptyText:'Ingrese Filtro...'
        	}
        },{
        	type:'numeric',
        	dataIndex:'id',
        	disabled:true
        },{
        	type:'numeric',
        	dataIndex:'numero',
        	updateTask:2000,
        	menuItemCfgs:{
        		emptyText:'Ingrese Filtro...'
        	}
        },{
        	type: 'string',
        	dataIndex: 'usuario'
        },{
        	type: 'string',
        	dataIndex: 'vendedor'
        	
        },{
        	type:'date',
        	dateFormat:'d/m/Y',
        	dataIndex:'fechaAlta',
        	beforeText:'Antes de',
        	afterText:'Después de ',
        	onText:'en el día'
        }]
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
		plugins:[filters,expander],
		//collapsible: true,
        //animCollapse: false,
		//view: new Ext.grid.GroupingView(),
		columns:[
            expander,
            {header:"Id Orden",dataIndex:'ordenId',width:200,sortable:false,hidden:true},
            {header:"Empresa",dataIndex:'nombre',width:200,sortable:true},
            {header:"Sub Total",dataIndex:'subTotal',width:100,renderer:customCurrency},
            {header:"Sub Total Con Desc.",dataIndex:'subTotalConDescuento',width:100,renderer:customCurrency},
            {header:"$Otros Concep.",dataIndex:'subTotalOtrosConceptos',width:100,renderer:customCurrency},
            {header:"Total",dataIndex:'total',width:80,renderer:customCurrency,sortable:false},
            {header:"Tot.con Desc.",dataIndex:'totalcondesc',width:80,renderer:customCurrency},
            {header:"Saldo",dataIndex:'saldo',width:80/*,renderer:function(val,meta,record){
             var saldo = record.data.total - record.data.credito - record.data.recibo + record.data.debito
             return Ext.util.Format.number(saldo,'0.000,00/i');
             }*/},
            {header:"Débitos",dataIndex:'debito',width:80,renderer:customCurrency},
            {header:"Créditos",dataIndex:'credito',width:80,renderer:customCurrency},
            {header:"Recibos",dataIndex:'recibo',width:80,renderer:customCurrency},
            //{header:"Saldo con Desc.",dataIndex:'saldocondescuento',width:80,renderer:customCurrency},
            {header:"Razón Social",dataIndex:'razonSocial',width:200,sortable:false},
					{header:"Exposición",dataIndex:'expoNombre',width:200,sortable:true},
					{header:"Año",dataIndex:'anio',width:80},					
					{header:"Sector",dataIndex:'sector',width:200,sortable:true},
					{header:"Lote",dataIndex:'lote',width:100,hidden:false,sortable:true},
					{header:"id",dataIndex:"id",hidden:true},
					{header:"Número Orden",dataIndex:"numero",width:80,renderer:ordenRender,sortable:true},
					{header:"Usuario",dataIndex:'usuario',width:150,sortable:false},
					{header:"Vendedor",dataIndex:'vendedor',width:150,sortable:false},					
					{header:"Fecha",dataIndex:'fechaAlta',width:80,renderer: Ext.util.Format.dateRenderer('d/m/y'),sortable:true}
			],
		stripeRows: true,
		loadMask:true,
		height:400,
		width:850,
		title:'Ordenes de Reserva',
        tbar:[{
        		icon: imagePath+'/pdf.gif'
        		,cls:'x-btn-text-icon'
        		,text:'Pre-visualizar'
        		,handler: function(){
        				var sm = grid.getSelectionModel();
        				var sel = sm.getSelected();
        				if (sm.hasSelection()){
        					
							open('ordenreservareporte?tipo=ORIGINAL&_format=PDF&_name=ordenReservaInstance&_file=OrdenReserva&id='+sel.data.ordenId
							,'_blank')
        				}
							
        		}
        		
			},{
				text:'Anular'
				,handler:function(){
					var sm = grid.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						if(sel.data.recibo>0)
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
																	id:sel.data.ordenId
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
        				if((sel.data.total-sel.data.credito-sel.data.recibo+sel.data.debito)==0)
        					Ext.MessageBox.show({
        						title:'Mensaje',
        						msg:'No puede generar un recibo de una Orden de Reserva con saldo Cero',
        						buttons:Ext.MessageBox.OK
        					});
        				else 
        					window.location='../recibo/create?ordenreservaId='+sel.data.ordenId;	
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
													],
											'soloanuladas':Ext.getCmp('soloanuladasId').getValue()*/
            		
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
            		
            		
    				open('export?soloanuladas='+Ext.getCmp('soloanuladasId').getValue()
    					+'&filter='+Ext.util.JSON.encode(tmp)
    					+'&sort='+sort+'&dir='+dir
    					,'_blank')
            	}
			/*},{
				text:'Quitar Filtros'
				,handler: function(){
					grid.filters.clearFilters();
        		}
        		
			},{
				text:'Mostrar Todo'
				,handler: function(){
					grid.filters.clearFilters();
					ordenStore.load();
				}*/	
			},{
				text:'Comprobantes Débito/Crédito',
				handler: function(){
        			var sm = grid.getSelectionModel();
        			var sel = sm.getSelected();
        			if (sm.hasSelection()){
        					window.location='../notaDC/create?ordenreservaId='+sel.data.ordenId;	
        			}else{
        				Ext.MessageBox.show({
        					title:'Advertencia',
        					msg:'Seleccione una fila de la grilla para generar el recibo',
        					icon:Ext.MessageBox.WARNING,
        					buttons:Ext.MessageBox.OK
        				});	
        			}
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
									data:[['nombre','Nom.Empresa'],['fechaAlta','Fecha Alta'],['sector.nombre','Sector'],['lote.nombre','Lote'],['anio','Año Exposición'],['expo.nombre','Nombre Exposición'],['numero','Número']]
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
										  ,['ilike','Contiene']
										  ,['ilike2','No Contiene']]
								});
	var condicionesStoreLimitada=new Ext.data.SimpleStore({
									id:0,
									fields:['idcond','nombrecond'],
									data:[['eq','Igual a']
										  ,['ne','No es igual a']
										  ,['gt','Mayor que']
										  ,['lt','Menor que']
										  ,['ge','Mayor o igual que']
										  ,['le','Menor o igual que']]
								});								
	function campocombofn(combo,record,index){
		/*Ext.getCmp('condicionesIdFiltro1').clearValue();		
		Ext.getCmp('condicionesIdFiltro2').clearValue();
		Ext.getCmp('condicionesIdFiltro3').clearValue();			

		if (record.data.idcampo=="fechaAlta" || record.data.idcampo=="nombre"){
			Ext.getCmp('condicionesIdFiltro1').store=condicionesStoreLimitada;		
			Ext.getCmp('condicionesIdFiltro2').store=condicionesStoreLimitada;
			Ext.getCmp('condicionesIdFiltro3').store=condicionesStoreLimitada;			
		}else{
			Ext.getCmp('condicionesIdFiltro1').store=condicionesStore;		
			Ext.getCmp('condicionesIdFiltro2').store=condicionesStore;
			Ext.getCmp('condicionesIdFiltro3').store=condicionesStore;			
			
		}*/
		
	}
	
	var formSearch = new  Ext.form.FormPanel({
		url:'search',
		renderTo:'ordenreserva_form',
		id:'formSearchId',
		title:'Ordenes de Reserva',
		width:880,
		frame:true,
		items:[	
				{
						xtype:'checkbox',
						name:'soloanuladas',
						id:'soloanuladasId',
						fieldLabel:'Solo Anuladas',
						listeners:{
							check: function(check,checked){
								ordenStore.load();
							}
						}	
				},grid
		
		]
	});
	
});