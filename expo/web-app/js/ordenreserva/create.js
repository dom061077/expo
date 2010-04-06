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
				title : 'Seleccione Empresa y Exposici贸n',
				frame:false,
				monitorValid : true,
					allowBlank:false,
					bodyCssClass: 'x-border-layout-ct',
					bodyStyle: {
					            padding: '5px'
					},				
				items : [{
							layout:'column',
							
							anchor:'0',
							items:[
									{
										width:350,
										layout:'form',
										items:{
											xtype:'textfield',
											fieldLabel:'B煤squeda',
											width:200,
																						
										}
									},
									{
										width:60,
										//layout:'form',
										frame:false,
										items:{
											xtype:'button',
											text:'Buscar'
										}
								
									}
									
							]
					},grid
				]
			}),
			new Ext.ux.Wiz.Card({
				title: 'Datos de Empresa',
				monitorValid: true,
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
							fieldLabel:'Raz贸n Social,',
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
				monitorValid:true,
				items: [{
				
				}]
			}),
			new Ext.ux.Wiz.Card({
				title:'Expos en las que Particip贸',
				monitorValid:false,
				items:[{
				}]
			}),
			new Ext.ux.Wiz.Card({
				title:'Expos en las que puede participar',
				monitorValid:false,
				items:[{
				
				}]
			})
		]		
	});
	
	wizard.on('nextstep',function(wizard){
			 
			alert('Valor de Id: '+Ext.getCmp('empresaseleccionId').getValue());
			
	});
    
    
	wizard.show();
});

