Ext.onReady(function(){
	Ext.QuickTips.init();

	//este store es para una b�squeda local de empresas
	/*var empresaStore = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url:'../empresa/listjson',
		fields:['id','nombre']
	});*/
	
    
/*grilla de b�squeda de empresa*/
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
	            				
	            				Ext.getCmp('idRubro').setValue(respuesta.data.rubro);
	            				Ext.getCmp('idRubro').hiddenField.value=respuesta.data.rubroId;
		        				var subrubroCmb = Ext.getCmp('idSubrubro');
		        				subrubroCmb.clearValue();
		        				subrubroCmb.store.load({
		        					params:{'rubroid':respuesta.data.rubroId}
		        				});
		        				subrubroCmb.enable();
	            				Ext.getCmp('idSubrubro').setValue(respuesta.data.subrubro);
	            				Ext.getCmp('idSubrubro').hiddenField.value=respuesta.data.subrubroId;

						
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
				title : 'Seleccione Empresa y Exposición',
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
											fieldLabel:'Búsqueda',
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
							fieldLabel:'Razón Social',
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
							fieldLabel: 'Dirección',
							allowBlank: false,
							width:260,
							msgTarget:'under',
							name:'direccion'			
						},{
							xtype:'textfield',
							id:'telefono1Id',
							fieldLabel:'Teléfono 1',
							allowBlank: false,
							width:260,
							msgTarget:'under',
							name:'telefono1'	
						},{
							xtype:'textfield',
							id:'telefono2Id',
							fieldLabel:'Teléfono 2',
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
								        		hiddenName:'vendedor.id',
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
								        		hiddenName:'rubro.id',
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
								        		hiddenName:'subrubro.id',
								        		displayField:'nombreSubrubro',
								        		valueField:'id',
								        		mode:'local',
								        		forceSelection:true,
								        		store:subrubroStore,
								        		width:200
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
								        		msgTarget:'under',
								        		layout:'form',
								        		width:260,
								        		name: 'nombreRepresentante'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 1',
								        		allowBlank: true,
								        		msgTarget:'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante1'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 2',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout:'form',
								        		name: 'telefonoRepresentante2'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 3',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante3'
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'E-mail',
								        		allowBlank:true,
								        		msgTarget: 'under',
								        		layout:'form',
								        		name:'email',
								        		vtype:'email'
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
       }
	});
    
	wizard.on('finish',function(wiz,datos){
		/*alert(datos.datosempresaId.cuit);
		alert(datos.datosempresaId.direccion);
		alert(datos.datosempresaId.id);
		alert(datos.datosempresaId.localidadFiscal);
		*/
		var conn = new Ext.data.Connection();
		conn.request({
			url:'generarordenreserva',
			method:'POST',
			params:{
				
			},
			success: function(resp,opt){
				
			},
			failure: function(resp,opt){
				Ext.MessageBox.show({
					title:'Error',
					msg:'Se produjo un error al intentar generar la orden de reserva'
				});	
			}
		});
	});
    
	
	wizard.show();
});

