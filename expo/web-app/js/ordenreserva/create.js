Ext.onReady(function(){
	Ext.QuickTips.init();

	//este store es para una b�squeda local de empresas
	/*var empresaStore = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url:'../empresa/listjson',
		fields:['id','nombre']
	});
	/^\d{2}\-\d{8}\-\d{1}$/,
	*/
	
    
/*grilla de b�squeda de empresa*/
	
	
	
	Ext.apply(Ext.form.VTypes,{
	cuitVal: /^\d{2}\-\d{8}\-\d{1}$/,
			 
	//cuitMask:/^\d{2}\-\d{8}\-\d{1}$/,
	//cuitRe:/^\d{2}\-\d{8}\-\d{1}$/,
	cuitText:'Ingrese un C.U.I.T correcto',
	cuit :		function CPcuitValido(cuit) {
		if (!Ext.form.VTypes.cuitVal.test(cuit))
			return false;
		if (typeof(cuit) == 'undefined')
			return true;
		if (cuit == '')
			return true;
	    var vec= new Array(10);
	    esCuit=false;
	    cuit_rearmado="";
	    errors = ''
	    for (i=0; i < cuit.length; i++) {   
	        caracter=cuit.charAt( i);
	        if ( caracter.charCodeAt(0) >= 48 && caracter.charCodeAt(0) <= 57 )     {
	            cuit_rearmado +=caracter;
	        }
	    }
	    cuit=cuit_rearmado;
	    if ( cuit.length != 11) {  // si to estan todos los digitos
	        esCuit=false;
	        //errors = 'Cuit <11 ';
	        //alert( "CUIT Menor a 11 Caracteres" );
	    } else {
	        x=i=dv=0;
	        // Multiplico los d�gitos.
	        vec[0] = cuit.charAt(  0) * 5;
	        vec[1] = cuit.charAt(  1) * 4;
	        vec[2] = cuit.charAt(  2) * 3;
	        vec[3] = cuit.charAt(  3) * 2;
	        vec[4] = cuit.charAt(  4) * 7;
	        vec[5] = cuit.charAt(  5) * 6;
	        vec[6] = cuit.charAt(  6) * 5;
	        vec[7] = cuit.charAt(  7) * 4;
	        vec[8] = cuit.charAt(  8) * 3;
	        vec[9] = cuit.charAt(  9) * 2;
	                    
	        // Suma cada uno de los resultado.
	        
	        for( i = 0;i<=9; i++) {
	            x += vec[i];
	        }
	        dv = (11 - (x % 11)) % 11;
	        if ( dv == cuit.charAt( 10) ) {
	            esCuit=true;
	        }
	    }
	    return esCuit;
	}	

			/*function (cuit){
				
				if (!Ext.form.VTypes.cuitVal.test(cuit))
					return false;
				if (typeof(cuit) == 'undefined')
					return true;
				if (cuit == '')
					return true;
				var coeficiente = new Array(10);
				var cuit_rearmado='';
				coeficiente[0]=5;
				coeficiente[1]=4;
				coeficiente[2]=3;
				coeficiente[3]=2;
				coeficiente[4]=7;
				coeficiente[5]=6;
				coeficiente[6]=5;
				coeficiente[7]=4;
				coeficiente[8]=3;
				coeficiente[9]=2;
				var resultado=1;
				var verificador;
				var veri_nro;
				cuit_rearmado= cuit.replace('-','');
				cuit_rearmado = cuit_rearmado.replace('-','');
			
				if (cuit_rearmado.length != 11) {  // si to estan todos los digitos
					resultado=0;
				} else {
					sumador = 0;
					verificador = cuit_rearmado.substr(10, 1); //tomo el digito verificador
			
					for (i=0; i <=9; i=i+1) { 
						sumador = sumador + (cuit_rearmado.substr(i, 1)) * coeficiente[i];//separo cada digito y lo multiplico por el coeficiente
					}
			
					resultado = sumador % 11;
					resultado = 11 - resultado;  //saco el digito verificador
					veri_nro = verificador;
			
					if (veri_nro != resultado) {
						//resultado=0;
						return false;
					} else { 
						//cuit_rearmado = cuit_rearmado.substr(0, 2) + "-" + cuit_rearmado.substr(2, 8) + "-" + cuit_rearmado.substr(10, 1);
						return true;
					}
				}		
						
			}*/
		});
	
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
								Ext.getCmp('direccionfiscalId').setValue(respuesta.data.direccionFiscal);
								Ext.getCmp('telefono1Id').setValue(respuesta.data.telefono1);
								Ext.getCmp('telefono2Id').setValue(respuesta.data.telefono2);
								Ext.getCmp('idProvincia').setValue(respuesta.data.provinciaId);
								Ext.getCmp('idProvincia').hiddenField.value=respuesta.data.provinciaId;
								Ext.getCmp('idLocalidad').setValue(respuesta.data.localidadFiscal);
								Ext.getCmp('idLocalidad').hiddenField.value=respuesta.data.localidadId;
	            				Ext.getCmp('idVendedor').setValue(respuesta.data.localidadId);
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
    
	var loteModel = Ext.data.Record.create([
		'id','nombre'
	]);
	var storeLote = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url: '../lote/listjson',
		fields: ['id','nombre'],
		reader: new Ext.data.ArrayReader(
			{id:'id'},
			loteModel
			)
		
	});
	function lote_nombre(val){
		if(val>0){
			var busqueda=storeLote.queryBy(function(rec){
					return rec.data.id == val;
				});
			if (busqueda.itemAt(0))
				return busqueda.itemAt(0).data.nombre;
			else
				return '';
		}else
			return '';
	}
	var sectorModel = Ext.data.Record.create([
		'id','nombre'
	]);
	
	
	var storeSector = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url:'../sector/listjson',
		fields: ['id','nombre'],
		reader: new Ext.data.ArrayReader(
			{id:'id'},
			sectorModel	
		)
	});
	
	function sector_nombre(val){
		if(val>0){
			var busqueda=storeSector.queryBy(function(rec){
					return rec.data.id == val;
				});
			if (busqueda.itemAt(0))
				return busqueda.itemAt(0).data.nombre;
			else
				return '';
		}else
			return '';
		
	}
	

	
	/*var detalleModel =  Ext.data.Record.create([
		'id',
		'sector',
		'lote_id',
		{name:'subTotal',type:'float'}
	]);	*/

	
	
	var storeDetalle = new Ext.data.Store({
		autoDestroy:true,
		//url:'bancos.xml',
		data:[],
		reader: new Ext.data.ArrayReader({
			record:'detalleservicio',
			fields:[
				{name: 'sector',type:'string'},
				{name: 'lote_id',type:'string'},
				{name: 'subTotal', type: 'float'}
			]
		})
	});	
	
	var comboboxLote = new Ext.form.ComboBox({
		triggerAction: 'all',
		id:'comboboxLoteId',
		mode: 'local',
		store : storeLote,
		displayField: 'nombre',
		valueField:'id',
		hiddenName:'lote_id',
		hiddenField:'id',
		startValue:'Seleccione un Lote'
	});
	
	var comboboxSector = new Ext.form.ComboBox({
		triggerAction: 'all',
		id:'comboboxSectorId',
		mode: 'local',
		store: storeSector,
		displayField: 'nombre',
		valueField: 'id',
		hiddenName:'sector_id',
		hiddenField:'id',
		listeners: {
			'select': function(cmb,rec,idx){
				var lote = Ext.getCmp('comboboxLoteId')
				lote.store.load({
					params:{'sector_id':Ext.getCmp('comboboxSectorId').hiddenField.value}
				});
				comboboxLote.hiddenField.value=1;
			}
		}
	});

	var cmdetalle = new Ext.grid.ColumnModel({
		columns:[
			{
				header:'Sector',
				dataIndex: 'sector',
				width:250,
				editor: comboboxSector,
				renderer:sector_nombre
			},{
				header:'Lote',
				dataIndex:'lote_id',
				width:160,
				editor: comboboxLote,
				renderer:lote_nombre
			},{
				header:'Importe',
				dataIndex:'subTotal',
				width:80,
				editor: new Ext.form.NumberField({
					allowBlank:false
				})
			}
		]
		
	});	
	
	
	/*var sector_edit = new Ext.form.TextField({
		allowBlank:false,
		maxLength:20
	});
	
	var lote_edit = new Ext.form.TextField({
		allowBlank:false,
		maxLength:20
	});
	
	var subtotal_edit = new Ext.form.NumberField({
		allowBlank:false,
		maxLength:10,
		minValue:1
	});*/
	
	var gridDetalleServicioContratado = new Ext.grid.EditorGridPanel({
			frame:false,
			title:'Detalle Servicio Contratado',
			cm:cmdetalle,
			height:200,
			width:500,
			store:storeDetalle,
			selModel: new Ext.grid.RowSelectionModel(),
			tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridDetalleServicioContratado.getStore().getCount()<3){
				 		var Detalle = grid.getStore().recordType;
				 		var d = new Detalle({
				 			sector:'',
				 			lote_id:'',
				 			importe:0
				 		});
						gridDetalleServicioContratado.getStore().insert(
							(gridDetalleServicioContratado.getStore().getCount()-1>=0?gridDetalleServicioContratado.getStore().getCount():0)
							,d);
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
			]/*,
			columns:[
				{header:'Sector',dataIndex:'sector',editor:comboboxSector, renderer:sector_nombre},
				{header:'Lote',dataIndex:'lote_id',editor:comboboxLote,renderer:lote_nombre},
				{header:'Importe',dataIndex:'subTotal',editor:subtotal_edit}
			]*/
	});
        			
	
//------------------------------------------------------------------------------------        			
	
//------------------Grid para Editar Otros Conceptos------------------
	/*var otrosConceptosModel = Ext.data.Record.create([
		'id',
		'descripcion',
		{name:'subTotal',type:'float'}
	]);*/
	cmotrosconceptos = new Ext.grid.ColumnModel({
		columns:[
			{
				header:'Concepto',
				dataIndex:'descripcion',
				width:200,
				editor:new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header:'Importe',
				dataIndex:'subTotal',
				type:'float',
				width:100,
				editor:new Ext.form.NumberField({
					allowBlank:false
				})
			}
		]
	});
	
	var storeOtrosConceptos = new Ext.data.Store({
		autoDestroy:true,
		data:[],
		cm:cmotrosconceptos,
		reader: new Ext.data.ArrayReader({
			record:'otrosconceptos',
			fields:[
				{name:'descripcion',type:'string'},
				{name:'subTotal',type:'float'}
			]}
		)
	});
	

	
	var gridOtrosConceptos = new Ext.grid.EditorGridPanel({
			frame:false,
			title:'Otros conceptos',
			height:250,
			cm:cmotrosconceptos,
			width:400,
			store:storeOtrosConceptos,
			selModel: new Ext.grid.RowSelectionModel(),
			tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridOtrosConceptos.getStore().getCount()<3){
				 		var OtrosConceptos = gridOtrosConceptos.getStore().recordType;
				 		var oc = new OtrosConceptos({
				 			descripcion:'',
				 			subTotal:0
				 		});
						gridOtrosConceptos.getStore().insert(
							gridOtrosConceptos.getStore().getCount()-1>=0?gridOtrosConceptos.getStore().getCount():0	
							,oc);
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
			]/*,
			columns:[
				{header:'Descripción',dataIndex:'descripcion',editor:descripcion_edit},
				{header:'Importe',dataIndex:'subTotal',editor:subTotal_edit}
			]*/
	});
	
 //--------------------------------------------------------------------	

//------------------------Grid Productos que se exponen--------------
	
	var cmproductos = new Ext.grid.ColumnModel({
		columns : [
			{
				header:'Producto',
				dataIndex:'descripcion',
				width:200,
				editor: new Ext.form.TextField({allowBlank:false})
			}
		]
	});
	
	var storeProductosExpuestos = new Ext.data.Store({
		autoDestroy:true,
		data:[],
		reader: new Ext.data.ArrayReader({
			record:'productosexpuestos',
			fields:[
				{name:'descripcion', type:'string'}		
			]
		})
	});
	var gridProductosExpuestos = new Ext.grid.EditorGridPanel({
		frame:false,
		title:'Productos que se Exponen',
		height:250,
		width:500,
		cm:cmproductos,
		store:storeProductosExpuestos,
		selModel: new Ext.grid.RowSelectionModel(),
		tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridProductosExpuestos.getStore().getCount()<3){
				 		var ProductosExpuestos = gridProductosExpuestos.getStore().recordType;
				 		var prodex = new ProductosExpuestos({descripcion:''});
						gridProductosExpuestos.getStore().insert(
							gridProductosExpuestos.getStore().getCount()-1>=0?gridProductosExpuestos.getStore().getCount():0
							,prodex);
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
				 	var sm = gridProductosExpuestos.getSelectionModel();
				 	var sel = sm.getSelected();
				 	if(sm.hasSelection()){
	 					gridProductosExpuestos.getStore().remove(sel);
				 	}
				 }
				}
			]
	});
//-------------------------------------------------------------------	
	var vendedoresStore = new Ext.data.JsonStore({
		autoLoad:true,
		root:'rows',
		url: '../vendedor/listjson',
		fields: ['id','nombre'],
		listeners:{
			loadexception: function(proxy,store,response,e){
				/*var jsonObject = Ext.util.JSON.decode(response.responseText);
				if(jsonObject.denegado)
					Ext.MessageBox.show({
						title:'Mensaje',
						msg:'No est� autorizado a ingresar a esta opción'
					});*/
			}
		}
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
	
//datos store del combo provincia y localidad
	var provinciaStore = new Ext.data.JsonStore({
		root:'rows',
		url:'../provincia/listjson',
		fields:['id','nombre']
	});
	
	provinciaStore.load();
	
	var localidadStore = new Ext.data.JsonStore({
		root:'rows',
		url:'../localidad/listjson',
		fields:['id','nombreLoc']
	});
	
	var ivaStore = new Ext.data.JsonStore({
		autoLoad:true,
		url:'ivajson',
		root:'rows',
		fields:['id','descripcion']
	});	
	
	
	var wizard = new Ext.ux.Wiz({
		title:'Orden de Reserva',
		closable:true,
		modal:true,
		height:520,
		previousButtonText:'Anterior',
		nextButtonText:'Siguiente',
		cancelButtonText:'Cancelar',
		finishButtonText:'Finalizar',
		width:650,
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
											fieldLabel:'Búsqueda de Empresas',
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
							fieldLabel:'Nombre Comercial',
							allowBlank:false,
							msgTarget:'under',
							width:260,
							name:'nombre'
						},{
							xtype:'textfield',
							id:'razonsocialId',
							fieldLabel:'Razón Social',
							width:260,
							allowBlank:false,
							msgTarget:'under',
							name:'razonSocial'
							
						},{
							xtype:'textfield',
							id:'cuitId',
							fieldLabel:'C.U.I.T',
							allowBlank:false,
							msgTarget:'under',
							width:95,
							vtype:'cuit',
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
							id:'direccionfiscalId',
							fieldLabel:'Dirección Fiscal',
							allowBlank: false,
							width:260,
							msgTarget: 'under',
							name: 'direccionFiscal'
						},{
							xtype:'textfield',
							id:'telefono1Id',
							fieldLabel:'Teléfono 1',
							allowBlank: false,
							width:150,
							msgTarget:'under',
							name:'telefono1'	
						},{
							xtype:'textfield',
							id:'telefono2Id',
							fieldLabel:'Teléfono 2',
							allowBlank: true,
							width:150,
							msgTarget:'under',
							name:'telefono2'
						},{
								        		xtype: 'combo',
								        		fieldLabel: 'Provincia',
								        		id:'idProvincia',
								        		name: 'provinciaFiscal',
								        		allowBlank:false,
								        		valueField:'id',
								        		displayField:'nombre',
								        		mode:'local',
								        		store:provinciaStore,
								        		forceSelection:true,
								        		hiddenName:'provincia_id',
								        		layout:'form',
								        		msgTarget:'under',
								        		width: 260,
								        		minListWidth:260,
								        		listeners:{
								        			'select':function(cmd,rec,idx){
								        				var localidadCmb = Ext.getCmp('idLocalidad');
								        				localidadCmb.clearValue();
								        				localidadCmb.store.load({
								        					params:{'provincia_id':Ext.getCmp('idProvincia').hiddenField.value}
								        				});
								        			}
								        		}
								        	},{
								        		xtype: 'combo',
								        		id: 'idLocalidad',
								        		fieldLabel: 'Localidad',
								        		allowBlank: false,
								        		name: 'localidadFiscal',
								        		hiddenName:'localidad_id',
								        		valueField:'id',
								        		displayField:'nombreLoc',
								        		mode:'local',
								        		store:localidadStore,
								        		layout:'form',
								        		msgTarget:'under',
								        		forceSelection:true,
								        		minListWidth:260,
								        		width: 260,
								        		listeners:{
								        			'select':function(cmd,rec,idx){
								        				var conn = new Ext.data.Connection();
								        				conn.request({
								        					url:'../localidad/getcp',
								        					method:'POST',
								        					params:{
								        						id:Ext.getCmp('idLocalidad').getValue()
								        					},
								        					success: function(resp,opt){
								        						var respuesta=Ext.decode(resp.responseText);
								        						if(respuesta){
								        							if(respuesta.loginredirect)
								        								window.location='../logout/index';
								        							else{
								        								//if(respuesta.success){
								        									Ext.getCmp('idCodigopostal').setValue(respuesta.codigoPostal);
								        								//}
								        							}
								        						}
								        					},
								        					failure: function(resp,opt){
								        								Ext.Msg.show({
																			title:'Error',
																			msg:'Se produjo un error al recuperar el código postal',
																			icon:Ext.MessageBox.ERROR,
																			buttons:Ext.MessageBox.OK,
																			fn:function(btn){
																				
																			}
																		});
								        					}
								        				})
								        			}
								        		}
								        	},{
							        			xtype:'textfield',
							        			fieldLabel:'Codigo Postal',
							        			name:'codigoPostal',
							        			id:'idCodigopostal',
							        			msgTarget:'under',
							        			width:95,
							        			allowBlank:false
								        	},{
								        		xtype:'textfield',
								        		fieldLabel:'E-mail',
								        		allowBlank:false,
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
								        		xtype: 'combo',
								        		fieldLabel:'Rubro de Empresa',
								        		id:'idRubro',
								        		allowBlank:false,
								        		msgTarget: 'under',
								        		hideMode:'offsets',
								        		minListWidth:260,
								        		name: 'rubro',
								        		hiddenName:'rubro_id',
								        		displayField:'nombreRubro',
								        		valueField:'id',
								        		mode : 'local',
								        		forceSelection:true,
								        		store:rubroStore,
								        		width:260,
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
								        		layout:'column',
								        		border:false,
								        		anchor:'0',
								        		items:[{
								        			width:400,
								        			layout:'form',
								        			border:false,
								        			items:
										        		{xtype: 'combo',
										        		fieldLabel:'Sub-Rubro de Empresa',
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
										        		//forceSelection:true,
										        		store:subrubroStore,
										        		width:260}
										        		
								        			},{
								        				xtype:'button',
								        				text:'Agregar'
								        			}
								        		]
							        		}
				]
			}),
			new Ext.ux.Wiz.Card({
				title:'Contacto y Exposición',
				id:'datoscontactoId',
				monitorValid:true,
	        							items:[{xtype: 'textfield',
								        		fieldLabel: 'Representante ante la EXPO',
								        		allowBlank: false,
								        		id:'idNombreRepresentante',
								        		msgTarget:'under',
								        		layout:'form',
								        		width:260,
								        		name: 'nombreRepresentante'
								        	},{
								        		xtype:'textfield',
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
								        		allowBlank:false,
								        		msgTarget:'under',
								        		layout:'form',
								        		name:'cargoRep'
								        		
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 1',
								        		allowBlank: false,
								        		id:'idTelefonoRepresentante1',
								        		msgTarget:'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante1'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 2',
								        		allowBlank: true,
								        		id:'idTelefonoRepresentante2',
								        		msgTarget: 'under',
								        		layout:'form',
								        		name: 'telefonoRepresentante2'
								        	},{
								        		xtype: 'textfield',
								        		fieldLabel:'Teléfono 3',
								        		id:'idTelefonoRepresentante3',
								        		allowBlank: true,
								        		msgTarget: 'under',
								        		layout: 'form',
								        		name: 'telefonoRepresentante3'
								        	},{
												xtype:'combo',
												fieldLabel:'Exposición',
												name:'exposicionField',
												hiddenName:'expo_id',
												valueField:'id',
												hiddenName:'exposicionid',
												hiddenField:'id',
												displayField:'nombre',
												msgTarget:'under',
												store:exposicionStore,
												width:200,
												listWidth:200,
												mode:'local',
												id:'exposicionCombo',
												allowBlank:false,
												forceSelection:true,
												allowBlank:false,
												listeners: {
														'select': function(cmb,rec,idx){
															var sector = Ext.getCmp('comboboxSectorId');
															//lote.clearValue();
															sector.store.load({
																params:{'exposicion_id':Ext.getCmp('exposicionCombo').hiddenField.value}
															});
															sector.enable();
														}
													}
												},
												{xtype:'combo',
												 id:'idAnio',
												 fieldLabel:'Año',
												 name:'anio',
												 displayField:'descripcion',
												 valueField:'id',
												 mode:'local',
												 forceSelection:true,
												 allowBlank:false,
												 msgTarget:'under',
												 store: new Ext.data.SimpleStore({
												 	fields:['id','descripcion'],
												 	data : [['2010','2010'],['2011','2011'],['2012','2012']]
												 })
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
				title:'Datos Impositivos',
				id:'exposicionId',
				monitorValid:true,
				items:[
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
					 {xtype:'combo',
					  fieldLabel:'I.V.A',
					  name:'resinsValorCmb',
					  hiddenName:'resinsValor',
					  valueField:'id',
					  hiddenField:'id',						  
					  hiddenField:'id',
					  displayField:'descripcion',
					  msgTarget:'under',
					  store:ivaStore,
					  width:100,
					  listWidth:100,
					  allowBlank:false,
					  forceSelection:true,
					  mode:'local',
					  id:'resinsvalorId'
					  
					 /*,
					 {xtype:'numberfield',
					  fieldLabel:'Valor No Ins.',
					  name:'noinsValor',
					  allowBlank:false,
					  minValue:0,
					  id:'noinsvalorId',
					  msgTarget:'under',
					  value:0
					 }*/
					 },{
						  xtype:'textarea',
						  fieldLabel:'Observación',
						  maxLength:255,
						  width:200,
						  name:'observacion',
						  id:'observacionId'
			          },{
			        		xtype: 'combo',
			        		id: 'idVendedor',
			        		fieldLabel:'Vendedor',
			        		allowBlank: false,
			        		name: 'vendedor',
			        		hiddenName:'vendedor_id',
			        		displayField:'nombre',
			        		hideMode:'offsets',
			        		minListWidth:260,
			        		layout:'card',
			        		valueField: 'id',
			        		mode: 'local',
			        		store: vendedoresStore,
			        		msgTarget:'under',
			        		forceSelection:true,
			        		width: 260
					 }
				]
			})
			
		]		
	});
	
	wizard.on('previousstep',function(wizard){
		wizard.headPanel.el.dom.firstChild.firstChild.data='';			
	});
	
	
	wizard.on('nextstep',function(wizard){
	   var sel = grid.getSelectionModel().getSelected();
	   if(this.currentCard==0)
	   		wizard.headPanel.el.dom.firstChild.firstChild.data='';
	   
	   if(this.currentCard==1){
			   if(sel){
				  		loaddatosempresa(sel.data.id);
				  		wizard.headPanel.el.dom.firstChild.firstChild.data='SE MODIFICARA LA EMPRESA EXISTENTE';
			   }else{
			   			wizard.headPanel.el.dom.firstChild.firstChild.data='SE DARA DE ALTA LA EMPRESA';
						Ext.getCmp('empresaId').setValue(null);
						Ext.getCmp('nombreId').setValue(null);
						Ext.getCmp('razonsocialId').setValue(null);
						Ext.getCmp('cuitId').setValue(null);
						Ext.getCmp('direccionId').setValue(null);
						Ext.getCmp('direccionfiscalId').setValue(null);
						Ext.getCmp('telefono1Id').setValue(null);
						Ext.getCmp('telefono2Id').setValue(null);
						Ext.getCmp('idProvincia').setValue(null);
						Ext.getCmp('idLocalidad').setValue(null);
						Ext.getCmp('idVendedor').setValue(null);
						Ext.getCmp('idVendedor').hiddenField.value=null;
						Ext.getCmp('idNombreRepresentante').setValue(null);
						Ext.getCmp('idTelefonoRepresentante1').setValue(null);
						Ext.getCmp('idTelefonoRepresentante2').setValue(null);
						Ext.getCmp('idTelefonoRepresentante3').setValue(null);
						Ext.getCmp('idEmail').setValue(null);
						Ext.getCmp('idSitioweb').setValue(null);
						Ext.getCmp('idRubro').setValue(null);
						Ext.getCmp('idRubro').hiddenField.value=null;
						Ext.getCmp('idCodigopostal').setValue(null);
						Ext.getCmp('idCargorep').setValue(null);
						var subrubroCmb = Ext.getCmp('idSubrubro');
						subrubroCmb.clearValue();
						subrubroCmb.store.load({
							params:{'rubroid':null}
						});
						subrubroCmb.enable();
						Ext.getCmp('idSubrubro').setValue(null);
						Ext.getCmp('idSubrubro').hiddenField.value=null;
						Ext.getCmp('idDnirep').setValue(null);
			   			
			   }
	   }
       if(this.currentCard==2)
		  	Ext.getCmp('nombreId').focus('',10);
		    //if(this.currentCard==4 && gridDetalleServicioContratado.getStore().getCount()==0)
		    //	this.cardPanel.getLayout().setActiveItem(this.currentCard - 1);
		    	
       //}
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
	
	/*
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
				msg:'Seleccione una condición de I.V.A',
				icon:Ext.MessageBox.ERROR,
				buttons:Ext.MessageBox.OK
			});
	});*/
	
		
	wizard.on('finish',function(wiz,datos){
		/*alert(datos.datosempresaId.cuit);
		alert(datos.datosempresaId.direccion);
		alert(datos.datosempresaId.id);
		alert(datos.datosempresaId.localidadFiscal);
		*/
		if (datos.exposicionId.resinsValor==0 && datos.exposicionId.noinsValor ){
			Ext.MessageBox.show({
				title:'Error',
				msg:'Ingrese una valor mayor a cero para I.V.A Res. Ins.',
				icon:Ext.MessageBox.ERROR,
				buttons:Ext.MessageBox.OK
			});
			return false;
		}
		
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
		if (!flag){
			Ext.MessageBox.show({
				title:'Error',
				msg:'Seleccione una condición de I.V.A',
				icon:Ext.MessageBox.ERROR,
				buttons:Ext.MessageBox.OK
			});
			return false;
		}
		
		var storeDetalle = gridDetalleServicioContratado.getStore();
		var storeOtrosConceptos = gridOtrosConceptos.getStore();
		var detallejsonArr=[];
		var detallejsonStr = '';
		var otrosconceptosjsonArr=[];
		var otrosconceptosjsonStr='';
		var productosjsonArr = [];
		var productosjsonStr ='';
		var flagdetallecero=false;
		storeDetalle.data.each(function(rec){
				if(! rec.data.subTotal>0){
					flagdetallecero=true
					return false;
					/*Ext.MessageBox.show({
						title:'Error',
						msg:'El detalle del servicio contratado tiene una linea con importe cero',
						icon:Ext.MessageBox.ERROR,
						button:Ext.MessageBox.OK,
						fn:function(btn){
							return false;
						}
					});	*/
				}
				detallejsonArr.push(rec.data);
			}
		);
		if (flagdetallecero){
				Ext.MessageBox.show({
						title:'Error',
						msg:'El detalle del servicio contratado tiene una linea con importe cero',
						icon:Ext.MessageBox.ERROR,
						button:Ext.MessageBox.OK
					});
				return false;	
		}
					
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
				'empresa.nombre':datos.datosempresaId.nombre,
				'empresa.codigoPostal':datos.datosempresaId.codigoPostal,
				'empresa.nombreRepresentante':datos.datoscontactoId.nombreRepresentante,
				'empresa.telefono1':datos.datosempresaId.telefono1,
				'empresa.telefono2':datos.datosempresaId.telefono2,
				'empresa.cuit':datos.datosempresaId.cuit,
				'empresa.direccion':datos.datosempresaId.direccion,
				'empresa.telefonoRepresentante1':datos.datoscontactoId.telefonoRepresentante1,
				'empresa.telefonoRepresentante2':datos.datoscontactoId.telefonoRepresentante2,
				'empresa.telefonoRepresentante3':datos.datoscontactoId.telefonoRepresentante3,
				'empresa.subrubro.rubro.id':datos.datosempresaId.rubro_id,
				'empresa.subrubro.id':datos.datosempresaId.subrubro_id,
				'empresa.subrubro.nombreSubrubro':datos.datosempresaId.subrubro,
				'empresa.dniRep':datos.datoscontactoId.dniRep,
				'empresa.email':datos.datosempresaId.email,
				'empresa.cargoRep':datos.datoscontactoId.cargoRep,
				'empresa.dniRep':datos.datoscontactoId.dniRep,
				'empresa.sitioWeb':datos.datosempresaId.sitioWeb,
				'empresa.razonSocial':datos.datosempresaId.razonSocial,
				'empresa.direccionFiscal':datos.datosempresaId.direccionFiscal,
				'empresa.localidad.id':datos.datosempresaId.localidad_id,
				//'empresa.localidadFiscal':datos.datosempresaId.localidadFiscal,
				//'empresa.provinciaFiscal':datos.datosempresaId.provinciaFiscal,
				'empresa.codigoPostal':datos.datosempresaId.codigoPostal,
				'empresa.vendedor.id':datos.exposicionId.vendedor_id,
				detallejson:detallejsonStr,
				otrosconceptosjson:otrosconceptosjsonStr,
				productosjson:productosjsonStr,
				anio:datos.datoscontactoId.anio,
				'expo.id':datos.datoscontactoId.exposicionid,
				ivaGralCheck:(datos.exposicionId.resins=='on'?true:false),
				ivaRniCheck:(datos.exposicionId.noins=='on'?true:false),
				exentoCheck:(datos.exposicionId.exento=='on'?true:false),
				consFinalCheck:(datos.exposicionId.consfinal=='on'?true:false),
				monotributoCheck:(datos.exposicionId.monotributo=='on'?true:false),
				porcentajeResIns:datos.exposicionId.resinsValor,
				porcentajeResNoIns:0,
				observacion:datos.exposicionId.observacion
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
				}else{
					if(respuesta.success)
						window.location='ordenreservareporte?target=_blank&_format=PDF&_name=ordenReservaInstance&_file=OrdenReserva&id='+respuesta.ordenid;
					else
						Ext.MessageBox.show({
							title:'error',
							msg:respuesta.msg,
							icon:Ext.MessageBox.ERROR,
							buttons:Ext.MessageBox.OK
						});
				}
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

