Ext.onReady(function(){
	Ext.QuickTips.init();

	//este store es para una b鷖queda local de empresas
	/*var empresaStore = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url:'../empresa/listjson',
		fields:['id','nombre']
	});*/
	
    
/*grilla de b鷖queda de empresa*/
	function loaddatosempresa(empresaid){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'../empresa/editempresajson',
			method:'POST',
			params:{
				id:empresaid
			},
			success: function(resp,opt){
				var respuesta=Ext.decode(resp.responseText);
				
				if(respuesta){
					if(respuesta.loginredirect)
						window.location='../logout/index';
					else{
						if(respuesta.success){
								Ext.getCmp('empresaId').setValue(respuesta.data.id);
								Ext.getCmp('nombreId').setValue(respuesta.data.nombre);
								Ext.getCmp('razonsocialId').setValue(respuesta.data.razonSocial);
								Ext.getCmp('cuitId').setValue(respuesta.data.cuit);
								Ext.getCmp('direccionId').setValue(respuesta.data.direccion);
								Ext.getCmp('telefono1Id').setValue(respuesta.data.telefono1);
								Ext.getCmp('telefono2Id').setValue(respuesta.data.telefono2);
								Ext.getCmp('idProvincia').setValue(respuesta.data.provinciaFiscal);
								Ext.getCmp('idLocalidad').setValue(respuesta.data.localidadFiscal);
	            				Ext.getCmp('idVendedor').setValue(respuesta.data.vendedor);
	            				Ext.getCmp('idVendedor').hiddenField.value=respuesta.data.vendedorId;
	            				Ext.getCmp('idNombreRepresentante').setValue(respuesta.data.nombreRepresentante);
	            				Ext.getCmp('idTelefonoRepresentante1').setValue(respuesta.data.telefonoRepresentante1);
	            				Ext.getCmp('idTelefonoRepresentante2').setValue(respuesta.data.telefonoRepresentante2);
	            				Ext.getCmp('idTelefonoRepresentante3').setValue(respuesta.data.telefonoRepresentante3);
	            				Ext.getCmp('idEmail').setValue(respuesta.data.email);
	            				Ext.getCmp('idSitioweb').setValue(respuesta.data.sitioWeb);
	            				Ext.getCmp('idRubro').setValue(respuesta.data.rubro);
	            				Ext.getCmp('idRubro').hiddenField.value=respuesta.data.rubroId;
	            				Ext.getCmp('idCodigopostal').setValue(respuesta.data.codigoPostal);
	            				Ext.getCmp('idCargorep').setValue(respuesta.data.cargoRep);
		        				var subrubroCmb = Ext.getCmp('idSubrubro');
		        				subrubroCmb.clearValue();
		        				subrubroCmb.store.load({
		        					params:{'rubroid':respuesta.data.rubroId}
		        				});
		        				subrubroCmb.enable();
	            				Ext.getCmp('idSubrubro').setValue(respuesta.data.subrubro);
	            				Ext.getCmp('idSubrubro').hiddenField.value=respuesta.data.subrubroId;
								Ext.getCmp('idDnirep').setValue(respuesta.data.dniRep);
								
						
						}else{
							Ext.Msg.show({
								title:'Error',
								msg:'Se produjo un error al recuperar los datos de la empresa',
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									wizard.cardPanel.getLayout().setActiveItem(wizard.currentCard - 1);
								}
							});
						}
					}
				}
			}
		});
	}
	
            	var store = new Ext.data.JsonStore({
							totalProperty: 'total',
							root: 'rows',
							url: '../empresa/listjson',
							fields:[
									'id','nombre','nombreRepresentante','telefono1'
							],
		        			listeners: {
			                    loadexception: function(proxy, store, response, e) {
						                    var jsonObject = Ext.util.JSON.decode(response.responseText);
						                    if (response.status==0)
						                    	Ext.MessageBox.show({
						                    		title:'Error',
						                    		msg:'Error de comunicaci贸n con el servidor',
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
                        width:440,
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
//---------------------------------	

        			
//-------------------Grid para Editar el detalle de servicios contratados-------------
	var detalleModel =  Ext.data.Record.create([
		'id',
		'sector',
		'lote',
		{name:'subTotal',type:'float'}
	]);        			
	var storeDetalle = new Ext.data.Store({
		data:[
			/*[1,'sector1','lote1',2.5],
			[2,'sector1','lote1',2.5],
			[3,'sector1','lote1',2.5]*/			
		],
		reader: new Ext.data.ArrayReader(
			{id:'id'},
			detalleModel
			)
	});
	
	var sector_edit = new Ext.form.TextField({
		allowBlank:false,
		maxLength:20
	});
	
	var lote_edit = new Ext.form.TextField({
		allowBlank:false,
		maxLength:20
	});
	
	var subtotal_edit = new Ext.form.NumberField({
		allowBlank:false,
		maxLength:10
	});
	
	var gridDetalleServicioContratado = new Ext.grid.EditorGridPanel({
			frame:false,
			title:'Detalle Servicio Contratado',
			height:200,
			width:400,
			store:storeDetalle,
			selModel: new Ext.grid.RowSelectionModel(),
			tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridDetalleServicioContratado.getStore().getCount()<3){
						gridDetalleServicioContratado.getStore().insert(0
							,new detalleModel({id:0,sector:'Ingrese el Sector',lote:'Ingrese el Lote',subTotal:0}));
						gridDetalleServicioContratado.startEditing(gridDetalleServicioContratado.getStore().getCount()-1,0);
				 	}else{
				 		Ext.MessageBox.show({
				 			title:'Advertencia',
				 			msg:'Solo se pueden agregar tres lineas de detalle',
				 			icon:Ext.MessageBox.WARNING,
				 			buttons:Ext.MessageBox.OK
				 		});
				 	}
				 }
				},{
				 text:'Borrar',
				 handler: function(){
				 	var sm = gridDetalleServicioContratado.getSelectionModel();
				 	var sel = sm.getSelected();
				 	if(sm.hasSelection()){
	 					gridDetalleServicioContratado.getStore().remove(sel);
				 	}
				 }
				}
			],
			columns:[
				/*{header:'id',dataIndex:'id'},*/
				{header:'Sector',dataIndex:'sector',editor:sector_edit},
				{header:'Lote',dataIndex:'lote',editor:lote_edit},
				{header:'Importe',dataIndex:'subTotal',editor:subtotal_edit}
			]
	});
        			
	
//------------------------------------------------------------------------------------        			
	
//------------------Grid para Editar Otros Conceptos------------------
	var otrosConceptosModel = Ext.data.Record.create([
		'id',
		'descripcion',
		{name:'subTotal',type:'float'}
	]);
	var storeOtrosConceptos = new Ext.data.Store({
		data:[],
		reader: new Ext.data.ArrayReader(
			{id:'id'},
			['id','descripcion',{name:'subTotal',type:'float'}]
			)
	});
	
	var descripcion_edit = new Ext.form.TextField({
		allowBlank:false,
		maxLenght:20
	});
	
	var subTotal_edit = new Ext.form.NumberField({
		allowBlank:false,
		maxLenght:20
	});
	
	var gridOtrosConceptos = new Ext.grid.EditorGridPanel({
			frame:false,
			title:'Otros conceptos',
			height:250,
			width:250,
			store:storeOtrosConceptos,
			selModel: new Ext.grid.RowSelectionModel(),
			tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridOtrosConceptos.getStore().getCount()<3){
						gridOtrosConceptos.getStore().insert(0
							,new detalleModel({id:0,descripcion:'Ingrese Texto',subTotal:0}));
						gridOtrosConceptos.startEditing(gridOtrosConceptos.getStore().getCount()-1,0);
				 	}else{
				 		Ext.MessageBox.show({
				 			title:'Advertencia',
				 			msg:'Solo se pueden agregar tres lineas de detalle',
				 			icon:Ext.MessageBox.WARNING,
				 			buttons:Ext.MessageBox.OK
				 		});
				 	}
				 }
				},{
				 text:'Borrar',
				 handler: function(){
				 	var sm = gridOtrosConceptos.getSelectionModel();
				 	var sel = sm.getSelected();
				 	if(sm.hasSelection()){
	 					gridOtrosConceptos.getStore().remove(sel);
				 	}
				 }
				}
			],
			columns:[
				{header:'Descripci贸n',dataIndex:'descripcion',editor:descripcion_edit},
				{header:'Importe',dataIndex:'subTotal',editor:subTotal_edit}
			]
	});
	
//--------------------------------------------------------------------	

//------------------------Grid Productos que se exponen--------------
	var productosExpuestosModel = Ext.data.Record.create([
		'id',
		'descripcion'
	]);
	var productodesc_edit = new Ext.form.TextField({
	
	});
	
	
	var storeProductosExpuestos = new Ext.data.Store({
		data:[],
		reader: new Ext.data.ArrayReader(
			{id:'id'},
			['id','descripcion']
		)
	});
	var gridProductosExpuestos = new Ext.grid.EditorGridPanel({
		frame:false,
		title:'Productos que se Exponen',
		height:250,
		width:250,
		store:storeProductosExpuestos,
		selModel: new Ext.grid.RowSelectionModel(),
		tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridProductosExpuestos.getStore().getCount()<3){
						gridProductosExpuestos.getStore().insert(0
							,new detalleModel({id:0,descripcion:'Ingrese texto'}));
						gridProductosExpuestos.startEditing(gridProductosExpuestos.getStore().getCount()-1,0);
				 	}else{
				 		Ext.MessageBox.show({
				 			title:'Advertencia',
				 			msg:'Solo se pueden agregar tres lineas de detalle',
				 			icon:Ext.MessageBox.WARNING,
				 			buttons:Ext.MessageBox.OK
				 		});
				 	}
				 }
				},{
				 text:'Borrar',
				 handler: function(){
				 	var sm = gridOtrosConceptos.getSelectionModel();
				 	var sel = sm.getSelected();
				 	if(sm.hasSelection()){
	 					gridOtrosConceptos.getStore().remove(sel);
				 	}
				 }
				}
			],
			columns:[
				{header:'Descripci贸n',dataIndex:'descripcion',editor:productodesc_edit}
			]
	});
//-------------------------------------------------------------------	
	var vendedoresStore = new Ext.data.JsonStore({
		autoLoad:true,
		root:'rows',
		url: '../vendedor/listjson',
		fields: ['id','nombre']
	});
	
	var rubroStore = new Ext.data.JsonStore({
		autoLoad:true,
		root:'rows',
		url:'../rubro/listrubrojson',
		fields: ['id','nombreRubro']
	});
	
	var subrubroStore = new Ext.data.JsonStore({
		root:'rows',
		url:'../rubro/listsubrubrojson',
		fields: ['id','nombreSubrubro']
	});
	
	var exposicionStore = new Ext.data.JsonStore({
		root:'rows',
		url:'../exposicion/listjson',
		fields:['id','nombre']
	});
	
	exposicionStore.load();
	
	
	
	var wizard = new Ext.ux.Wiz({
		title:'Orden de Reserva',
		closable:true,
		modal:true,
		height:500,
		previousButtonText:'Anterior',
		nextButtonText:'Siguiente',
		cancelButtonText:'Cancelar',
		finishButtonText:'Finalizar',
		width:600,
		headerConfig:{
			title:'Alta de Orden de Reserva',
			stepText : "Paso {0} de {1}: {2}"
		},
        cardPanelConfig : {
            defaults : {
                baseCls    : 'x-small-editor',
                bodyStyle  : 'padding:40px 15px 5px 120px;background-color:#F6F6F6;',
                border     : false    
            }
        },   
		cards : [
			new Ext.ux.Wiz.Card({
				title : 'Seleccione Empresa',
				id:'seleccionempresacardId',
				frame:false,
				allowBlank:false,
				items : [{
							layout:'column',
							border:false,
							anchor:'0',
							items:[
									{
										width:350,
										layout:'form',
										border:false,
										items:{
											xtype:'textfield',
											fieldLabel:'B煤squeda',
											id:'searchCriteriaId',
											width:200
											
																						
										}
									},
									{
										width:60,
										//layout:'form',
										border:false,
										items:{
											xtype:'button',
											text:'Buscar',
											listeners:{
												click: function(){
														store.load({params: {'start':0,'limit':10,'searchCriteria':Ext.getCmp('searchCriteriaId').getValue()}});
												}
											}
										}
								
									}
									
							]
					},grid
				]
			}),
			new Ext.ux.Wiz.Card({
				title: 'Datos de Empresa',
				id:'datosempresaId',
				monitorValid: true,
				autoScroll:true,
				width:450,
				items: [{
							xtype:'textfield',
							id:'empresaId',
							hideLabel:true,
							fieldLabel:'id de Empresa',
							name:'id',
							hidden:true
						},{
							xtype:'textfield',
							id:'nombreId',
							fieldLabel:'Nombre',
							allowBlank:false,
							msgTarget:'under',
							width:260,
							name:'nombre'
						},{
							xtype:'textfield',
							id:'razonsocialId',
							fieldLabel:'Raz贸n Social',
							allowBlank:false,
							msgTarget:'under',
							name:'razonSocial'
							
						},{
							xtype:'textfield',
							id:'cuitId',
							fieldLabel:'C.U.I.T',
							allowBlank:false,
							msgTarget:'under',
							name:'cuit'
						},{
							xtype:'textfield',
							id:'direccionId',
							fieldLabel: 'Direcci贸n',
							allowBlank: false,
							width:260,
							msgTarget:'under',
							name:'direccion'			
						},{
							xtype:'textfield',
							id:'telefono1Id',
							fieldLabel:'Tel茅fono 1',
							allowBlank: false,
							width:260,
							msgTarget:'under',
							name:'telefono1'	
						},{
							xtype:'textfield',
							id:'telefono2Id',
							fieldLabel:'Tel茅fono 2',
							allowBlank: false,
							width:260,
							msgTarget:'under',
							name:'telefono2'
						},{
								        		xtype: 'textfield',
								        		fieldLabel: 'Provincia',
								        		id:'idProvincia',
								        		name: 'provinciaFiscal',
								        		allowBlank:false,
								        		layout:'form',
								        		msgTarget:'under',
								        		width: 120
								        	},{
								        		xtype: 'textfield',
								        		id: 'idLocalidad',
								        		fieldLabel: 'Localidad',
								        		allowBlank: false,
								        		name: 'localidadFiscal',
								        		layout:'form',
								        		msgTarget:'under',
								        		forceSelection:true,
								        		width: 200
								        	},{
								        		xtype: 'combo',
								        		id: 'idVendedor',
								        		fieldLabel:'Vendedor',
								        		allowBlank: false,
								        		name: 'vendedor',
								        		hiddenName:'vendedor_id',
								        		displayField:'nombre',
								        		hideMode:'offsets',
								        		minListWidth:200,
								        		layout:'card',
								        		valueField: 'id',
								        		mode: 'local',
								        		store: vendedoresStore,
								        		msgTarget:'under',
								        		forceSelection:true,
								        		width: 200
								        	},{
								        		xtype: 'combo',
								        		fieldLabel:'Rubro',
								        		id:'idRubro',
								        		allowBlank:false,
								        		msgTarget: 'under',
								        		hideMode:'offsets',
								        		minListWidth:200,
								        		name: 'rubro',
								        		hiddenName:'rubro_id',
								        		displayField:'nombreRubro',
								        		valueField:'id',
								        		mode : 'local',
								        		forceSelection:true,
								        		store:rubroStore,
								        		width:200,
								        		listeners:{
								        			'select': function(cmd,rec,idx){
								        				var subrubroCmb = Ext.getCmp('idSubrubro');
								        				subrubroCmb.clearValue();
								        				subrubroCmb.store.load({
								        					params:{'rubroid':Ext.getCmp('idRubro').hiddenField.value}
								        				});
								        				subrubroCmb.enable();
								        			}
								        		}
								        	},{
								        		xtype: 'combo',
								        		fieldLabel:'Sub-Rubro',
								        		id:'idSubrubro',
								        		store:subrubroStore,
								        		allowBlank:false,
								        		hideMode:'offsets',
								        		minListWidth:200,
								        		msgTarget:'under',
								        		name: 'subrubro',
								        		hiddenName:'subrubro_id',
								        		displayField:'nombreSubrubro',
								        		valueField:'id',
								        		mode:'local',
								        		forceSelection:true,
								        		store:subrubroStore,
								        		width:200
							        		},{
							        			xtype:'textfield',
							        			fieldLabel:'Codigo Postal',
							        			name:'codigoPostal',
							        			id:'idCodigopostal',
							        			allowBlank:false
							        		}
				]
			}),
			new Ext.ux.Wiz.Card({
				title:'Contacto',
				id:'datoscontactoId',
				monitorValid:true,
	        							items:[{xtype: 'textfield',
								        		fieldLabel: 'Representante',
								        		allowBlank: false,
								        		id:'idNombreRepresentante',
								        		msgTarget:'under',
								        		layout:'form',
								        		width:260,
								        		name: 'nombreRepresentante'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Tel茅fono 1',
								        		allowBlank: true,
								        		id:'idTelefonoRepresentante1',
								        		msgTarget:'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante1'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Tel茅fono 2',
								        		allowBlank: true,
								        		id:'idTelefonoRepresentante2',
								        		msgTarget: 'under',
								        		layout:'form',
								        		name: 'telefonoRepresentante2'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Tel茅fono 3',
								        		id:'idTelefonoRepresentante3',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante3'
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'E-mail',
								        		allowBlank:true,
								        		msgTarget: 'under',
								        		id:'idEmail',
								        		layout:'form',
								        		name:'email',
								        		vtype:'email'
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'Sitio Web',
								        		allowBlank:true,
								        		msgTarget:'under',
								        		id:'idSitioweb',
								        		name:'sitioWeb'
								        	},{
								        		xtype:'numberfield',
								        		fieldLabel:'D.N.I',
								        		allowBlank:false,
								        		msgTarget:'under',
								        		id:'idDnirep',
								        		layout:'form',
								        		name:'dniRep'
								        		
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'Cargo Representante',
								        		id:'idCargorep',
								        		layout:'form',
								        		name:'cargoRep'
								        	}
	        							]
			}),
			new Ext.ux.Wiz.Card({
				title:'Datos del Servicio Contratado',
				id:'datosserviciocontratadoId',
				monitorValid:true,
				items:[gridDetalleServicioContratado]
			}),
			new Ext.ux.Wiz.Card({
				title:'Otros Conceptos',
				id:'otrosconceptosId',
				monitorValid:true,
				items:[gridOtrosConceptos]
			}),
			new Ext.ux.Wiz.Card({
				title:'Productos que se Exponen',
				id:'productosexpuestosId',
				monitorValid:true,
				items:[gridProductosExpuestos]
			}),
			new Ext.ux.Wiz.Card({
				title:'Exposici贸n',
				id:'exposicionId',
				monitorValid:true,
				items:[{
					xtype:'combo',
					fieldLabel:'Exposici贸n',
					name:'exposicionField',
					hiddenName:'expo_id',
					valueField:'id',
					displayField:'nombre',
					msgTarget:'under',
					store:exposicionStore,
					width:200,
					listWidth:200,
					mode:'local',
					id:'exposicionCombo',
					allowBlank:false,
					forceSelection:true,
					allowBlank:false},
					{xtype:'combo',
					 id:'idAnio',
					 fieldLabel:'A帽o',
					 name:'anio',
					 displayField:'descripcion',
					 valueField:'id',
					 mode:'local',
					 forceSelection:true,
					 store: new Ext.data.SimpleStore({
					 	fields:['id','descripcion'],
					 	data : [['2010','2010'],['2011','2011'],['2012','2012']]
					 })},
					{xtype:'radio',
					 id:'resinsradioId',
					 fieldLabel:'Res.Ins.',
					 name:'resins'
					 },
					 {xtype:'radio',
					  id:'noinsradioId',
					  fieldLabel:'No Ins.',
					  name:'noins'
					 },
					 {xtype:'radio',
					  id:'exentoId',	
					  fieldLabel:'Exento',
					  name:'exento'
					 },
					 {xtype:'radio',
					  id:'consfinalId',
					  fieldLabel:'Cons.Final',
					  name:'consfinal'
					 },
					 {xtype:'radio',
					  id:'monotributoId',
					  fieldLabel:'Monotributo',
					  name:'monotributo'
					 },
					 {xtype:'numberfield',
					  fieldLabel:'Valor Res.Ins.',
					  name:'resinsValor',
					  id:'resinsvalorId',
					  msgTarget:'under'
					 },
					 {xtype:'numberfield',
					  fieldLabel:'Valor No Ins.',
					  name:'noinsValor',
					  id:'noinsvalorId',
					  msgTarget:'under'
					 }
				]
			})
			
		]		
	});
	wizard.on('nextstep',function(wizard){
	   var sel = grid.getSelectionModel().getSelected();
       if (this.currentCard > 0 && !sel) {
	           this.cardPanel.getLayout().setActiveItem(this.currentCard - 1);
       }else{
       		if(this.currentCard==1)
		       loaddatosempresa(sel.data.id);
		    if(this.currentCard==2)
		    	Ext.getCmp('nombreId').focus('',10);
		    if(this.currentCard==4 && gridDetalleServicioContratado.getStore().getCount()==0)
		    	this.cardPanel.getLayout().setActiveItem(this.currentCard - 1);
		    	
       }
	});
	
	function radioHandler(check,checked){
		if(Ext.getCmp('noinsradioId').getName()!=check.getName()&& checked)
			Ext.getCmp('noinsradioId').setValue(false);
		if(Ext.getCmp('exentoId').getName()!=check.getName()&& checked)
			Ext.getCmp('exentoId').setValue(false);
		if(Ext.getCmp('consfinalId').getName()!=check.getName()&& checked)
			Ext.getCmp('consfinalId').setValue(false);
		if(Ext.getCmp('monotributoId').getName()!=check.getName()&& checked)
			Ext.getCmp('monotributoId').setValue(false);
		if(Ext.getCmp('resinsradioId').getName()!=check.getName() && checked)
			Ext.getCmp('resinsradioId').setValue(false);
	}
	Ext.getCmp('resinsradioId').on('check',function(check,checked){
		radioHandler(check,checked);			
	});
	Ext.getCmp('noinsradioId').on('check',function(check,checked){
		radioHandler(check,checked);			
	});
	Ext.getCmp('exentoId').on('check',function(check,checked){
		radioHandler(check,checked);
	});
	Ext.getCmp('consfinalId').on('check',function(check,checked){
		radioHandler(check,checked);
	});
	Ext.getCmp('monotributoId').on('check',function(check,checked){
		radioHandler(check,checked);
	});
	
	Ext.getCmp('resinsvalorId').on('valid',function(wiz,datos){
		var flag=false;
		if (Ext.getCmp('noinsradioId').checked)
			flag=true;	
		if (Ext.getCmp('exentoId').checked)
			flag=true;
		if (Ext.getCmp('consfinalId').checked)
			flag=true;
		if (Ext.getCmp('monotributoId').checked)
			flag=true;
		if (Ext.getCmp('resinsradioId').checked)
			flag=true;
		if (!flag)
			Ext.MessageBox.show({
				title:'Error',
				msg:'Seleccione una condici贸n de I.V.A',
				icon:Ext.MessageBox.ERROR,
				buttons:Ext.MessageBox.OK
			});
	});
	
		
	wizard.on('finish',function(wiz,datos){
		/*alert(datos.datosempresaId.cuit);
		alert(datos.datosempresaId.direccion);
		alert(datos.datosempresaId.id);
		alert(datos.datosempresaId.localidadFiscal);
		*/
		var storeDetalle = gridDetalleServicioContratado.getStore();
		var storeOtrosConceptos = gridOtrosConceptos.getStore();
		var detallejsonArr=[];
		var detallejsonStr = '';
		var otrosconceptosjsonArr=[];
		var otrosconceptosjsonStr='';
		var productosjsonArr = [];
		var productosjsonStr ='';
		storeDetalle.data.each(function(rec){
				detallejsonArr.push(rec.data);
			}
		);
		storeOtrosConceptos.data.each(function(rec){
				otrosconceptosjsonArr.push(rec.data);
			}
		);
		storeProductosExpuestos.data.each(function(rec){
			productosjsonArr.push(rec.data);
		});
		
		detallejsonStr=Ext.encode(detallejsonArr);
		otrosconceptosjsonStr = Ext.encode(otrosconceptosjsonArr);
		productosjsonStr = Ext.encode(productosjsonArr);
		var conn = new Ext.data.Connection();
		conn.request({
			url:'generarordenreserva',
			method:'POST',
			params:{
				id:datos.datosempresaId.id,
				nombre:datos.datosempresaId.nombre,
				codigoPostal:datos.datosempresaId.codigoPostal,
				nombreRepresentante:datos.datoscontactoId.nombreRepresentante,
				telefono1:datos.datosempresaId.telefono1,
				telefono2:datos.datosempresaId.telefono2,
				cuit:datos.datosempresaId.cuit,
				direccion:datos.datosempresaId.direccion,
				telefonoRepresentante1:datos.datoscontactoId.telefonoRepresentante1,
				telefonoRepresentante2:datos.datoscontactoId.telefonoRepresentante2,
				telefonoRepresentante3:datos.datoscontactoId.telefonoRepresentante3,
				'subrubro.id':datos.datosempresaId.subrubro_id,
				dniRep:datos.datoscontactoId.dniRep,
				email:datos.datoscontactoId.email,
				cargoRep:datos.datoscontactoId.cargoRep,
				dniRep:datos.datoscontactoId.dniRep,
				sitioWeb:datos.datoscontactoId.sitioWeb,
				observaciones:datos.datosempresaId.observaciones,
				razonSocial:datos.datosempresaId.razonSocial,
				direccionFiscal:datos.datosempresaId.direccionFiscal,
				localidadFiscal:datos.datosempresaId.localidadFiscal,
				provinciaFiscal:datos.datosempresaId.provinciaFiscal,
				codigoPostal:datos.datosempresaId.codigoPostal,
				'vendedor.id':datos.datosempresaId.vendedor_id,
				detallejson:detallejsonStr,
				otrosconceptosjson:otrosconceptosjsonStr,
				productosjson:productosjsonStr,
				anio:datos.exposicionId.anio,
				'expo.id':datos.exposicionId.expo_id
				
				
			},
			success: function(resp,opt){
				var respuesta = Ext.decode(resp.responseText);
				if(respuesta.result){
					if(respuesta.result.loginredirect==true)
						Ext.MessageBox.show({
							title:'Mensaje',
							icon:Ext.MessageBox.INFO,
							buttons:Ext.MessageBox.OK,
							fn:function(btn){
								window.location='../logout/index';
							}
						});
				}else				
					window.location='ordenreservareporte?target=_blank&_format=PDF&_name=ordenReservaInstance&_file=OrdenReserva&id='+respuesta.ordenid
			},
			failure: function(resp,opt){
				var respuesta = Ext.decode(resp.responseText);
				if(respuesta.result){
					if(respuesta.result.loginredirect==true)
						Ext.MessageBox.show({
							title:'Mensaje',
							icon:Ext.MessageBox.INFO,
							buttons:Ext.MessageBox.OK,
							fn:function(btn){
								window.location='../logout/index';
							}
						});
				}else{
					Ext.MessageBox.show({
						title:'Error',
						msg:'Se produjo un error al intentar generar la orden de reserva'
					});
				}
			}
		});
	});
    
	
	wizard.show();
	Ext.getCmp('searchCriteriaId').focus('',10);	
});

