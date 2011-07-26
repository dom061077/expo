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
								Ext.getCmp('idProvincia').setValue(respuesta.data.provinciaNombre);
								Ext.getCmp('idProvincia').hiddenField.value=respuesta.data.provinciaId;
								
								var localidadCmb = Ext.getCmp('idLocalidad');
								localidadCmb.clearValue();
								localidadCmb.store.load({
										params:{'provincia_id':Ext.getCmp('idProvincia').hiddenField.value}
								});
																
								Ext.getCmp('idLocalidad').setValue(respuesta.data.localidadNombre);
								Ext.getCmp('idLocalidad').hiddenField.value=respuesta.data.localidadId;
								
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
								if(respuesta.data.conordenes){
										/*Ext.getCmp('nombreId').el.dom.readOnly = true;
										Ext.getCmp('razonsocialId').el.dom.readOnly = true;
										Ext.getCmp('cuitId').el.dom.readOnly = true;
										Ext.getCmp('direccionId').el.dom.readOnly = true;
										Ext.getCmp('direccionfiscalId').el.dom.readOnly = true;
										Ext.getCmp('telefono1Id').el.dom.readOnly = true;
										Ext.getCmp('telefono2Id').el.dom.readOnly = true;
										Ext.getCmp('idProvincia').el.dom.readOnly = true;
										Ext.getCmp('idLocalidad').el.dom.readOnly = true;
										
			            				Ext.getCmp('idVendedor').el.dom.readOnly = true;
			            				Ext.getCmp('idNombreRepresentante').el.dom.readOnly = true;
			            				Ext.getCmp('idTelefonoRepresentante1').el.dom.readOnly = true;
			            				Ext.getCmp('idTelefonoRepresentante2').el.dom.readOnly = true;
			            				Ext.getCmp('idTelefonoRepresentante3').el.dom.readOnly = true;
			            				Ext.getCmp('idEmail').el.dom.readOnly = true;
			            				Ext.getCmp('idSitioweb').el.dom.readOnly = true;
			            				
			            				Ext.getCmp('idCodigopostal').el.dom.readOnly = true;
			            				Ext.getCmp('idCargorep').el.dom.readOnly = true;
			            				Ext.getCmp('idSubrubro').el.dom.readOnly = true;
										Ext.getCmp('idDnirep').el.dom.readOnly = true;*/
									
								}
								
						
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
                        width:600,
                        loadMask:true,
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
		url: '../lote/listjsonstock',
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
				{name: 'sector_id',type:'string'},
				{name: 'lote_id',type:'string'},
				{name: 'precio',type:'float'},
				{name: 'descuento',type:'float'},
				{name: 'fechaVigencia', type:'date'},
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
		startValue:'Seleccione un Lote',
		listeners:{
					'select':function(cmb,rec,idx){
						var sm = gridDetalleServicioContratado.getSelectionModel();
					 	var sel = sm.getSelected();
					 	if(sm.hasSelection()){
					 		var conn = new Ext.data.Connection();
					 		conn.request({
					 			url:'../lote/getprecio',
					 			method:'POST',
					 			params:{
					 				id:Ext.getCmp('comboboxLoteId').hiddenField.value
					 			},
					 			success: function(resp,opt){
					 				var respuesta=Ext.decode(resp.responseText);
					 				if(respuesta){
					 					if(respuesta.loginredirect)
					 						window.location='../logout/index';
					 					else{
					 						if(respuesta.success){
					 							if(sel.data.descuento>0){
					 								sel.data.precio=respuesta.precio
					 								sel.data.subTotal=sel.data.precio-respuesta.precio*sel.data.descuento/100;
					 							}else
					 								sel.data.subTotal=respuesta.precio
					 							
					 						}
					 					}
					 							
					 				}
					 			}
					 			
					 		});
					 		
					 		
					 		sel.commit();
					 		//gridDetalleServicioContratado.stopEditing();
					 	}
			}
				
		}
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
					params:{'sector_id':Ext.getCmp('comboboxSectorId').hiddenField.value,
							'expo_id':Ext.getCmp('exposicionCombo').hiddenField.value,
							'anio':Ext.getCmp('idAnio').getValue()
					}
				});
				comboboxLote.hiddenField.value=1;
				var sm = gridDetalleServicioContratado.getSelectionModel();
			 	var sel = sm.getSelected();
			 	if(sm.hasSelection()){
 					//gridDetalleServicioContratado.getStore().remove(sel);//a seguir
			 		//gridDetalleServicioContratado.startEditing(gridDetalleServicioContratado.getStore().getCount()-1,2);
			 		var conn = new Ext.data.Connection();
			 		conn.request({
			 			url:'../sector/getdescuento',
			 			method:'POST',
			 			params:{
			 				id:Ext.getCmp('comboboxSectorId').hiddenField.value
			 			},
			 			success: function(resp,opt){
			 				var respuesta=Ext.decode(resp.responseText);
			 				if(respuesta){
			 					if(respuesta.loginredirect)
			 						window.location='../logout/index';
			 					else{
			 						if(respuesta.success){
			 							sel.data.descuento=respuesta.porcentaje;
			 							sel.data.fechaVencimiento=respuesta.fechaVencimiento;
			 							sel.data.precio=respuesta.precio
			 							sel.data.subTotal=sel.data.precio-respuesta.precio*sel.data.descuento/100;
			 						}
			 					}
			 							
			 				}
			 			}
			 			
			 		});
			 		
			 		
			 		sel.commit();
			 		
			 		//gridDetalleServicioContratado.stopEditing();
			 	}
			}
		}
	});

	var cmdetalle = new Ext.grid.ColumnModel({
		columns:[
			{
				header:'Sector',
				dataIndex: 'sector_id',
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
				header:'Precio $',
				dataIndex:'precio',
				width:80
				//editor: new Ext.form.NumberField({
				//	allowBlank:false
				//})
			},{
				header:'Descuento %',
				dataIndex:'descuento',
				width:80/*,
				editor: new Ext.form.NumberField()*/
			},{
				header:'Vencimiento',
				dataIndex:'fechaVencimiento',
				renderer: Ext.util.Format.dateRenderer('d/m/y'),
				width:80
			},{
				header:'Sub-Total',
				dataIndex:'subTotal',
				width:80
				
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
			singleSelect:true,
			store:storeDetalle,
			selModel: new Ext.grid.RowSelectionModel(),
			tbar:[
				{text:'Agregar',
				 handler: function (){
				 	if(gridDetalleServicioContratado.getStore().getCount()<3){
				 		var Detalle = grid.getStore().recordType;
				 		var d = new Detalle({
				 			sector_id:'',
				 			lote_id:'',
				 			precio:0,
				 			descuento:0,
				 			fechaVigencia:new Date(),
				 			subTotal:0
				 		});
						gridDetalleServicioContratado.getStore().insert(
							(gridDetalleServicioContratado.getStore().getCount()-1>=0?gridDetalleServicioContratado.getStore().getCount():0)
							,d);
						gridDetalleServicioContratado.startEditing(gridDetalleServicioContratado.getStore().getCount()-1,0);
						gridDetalleServicioContratado.getSelectionModel().selectLastRow();
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
					allowBlank:false,
					maxLength:60,
					maxLengthText:'Cantidad máxima de caracteres es de 60'
																		
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
				editor: new Ext.form.TextField({allowBlank:false,maxLength:50,maxLengthText:'Cantidad máxima de caracteres es de 50'})
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
	

	
	var formSubrubro=new Ext.form.FormPanel ({
		url:'../subRubro/savejson',
		id:'formSubrubroId',
		frame:true,
		width:450,
		height:150,
		items:[
			{
				xtype:'textfield',
				name:'nombreSubrubro',
				id:'nombreSubrubroId',
				fieldLabel:'Nombre Sub-Rubro',
				width:200
			}/*,{
				xtype:'combo',
				fieldLabel:'Rubro',
				id:'altarubroId',
				name:'rubro.id',
				store:rubroStore,
				mode:'local',
				valueField:'id',
				forceSelection:true,
				allowBlank:false,
				displayField:'nombreRubro',
				width:200
			}*/
		],
		buttons:[
			{
				text:'Guardar',
				handler:function(){
					formSubrubro.getForm().submit({
						params:{
			 					'rubro.id':Ext.getCmp('idRubro').hiddenField.value
			 				},
						success:function(f,a){
							var subrubro = Ext.getCmp('idSubrubro')
							subrubro.clearValue();
	        				subrubro.store.load({
	        					params:{'rubroid':Ext.getCmp('idRubro').hiddenField.value}
	        				});
	        				subrubro.enable();
	        				winSubrubro.hide();
							Ext.getCmp('idSubrubro').setValue(a.result.nombreSubrubro);
	        				Ext.getCmp('idSubrubro').hiddenField.value=a.result.idSubRubro;
							
						},
						failure:function(f,a){
						    var msg="";
						    if (a.failureType==Ext.form.Action.CONNECT_FAILURE ||
						    	a.failureType==Ext.form.Action.SERVER_INVALID){
						    		Ext.Msg.alert('Error','El servidor no Responde')
						    	}
						    if (a.result){
						    	if (a.result.loginredirect==true){
						    		Ext.Msg.alert('Su sesion de usuario a caducado, ingrese nuevamente');
						    		window.location='../logout/index';
						    		}
						    	if (a.result.errors){
						    		for (var i=0; i<a.result.errors.length;i++){
						    			msg=msg+a.result.errors[i].title+'\r\n';	
				    				}
				    				Ext.Msg.alert(msg);
			    				}
						    }	
							
						}
					
					});
				}
			},{
				text:'Cancelar',
				handler:function(){
					winSubrubro.hide();
				}
			}
		]
	});
	
	var winSubrubro=new Ext.Window({
		title:'Agregar Sub-Rubro',
		resizable:false,
		modal:true,
        formPanel: null,
        width:400,
        height:200,
        closeAction:'hide',
        plain: true,
        items:[formSubrubro]		
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
		width:750,
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
				title:'Datos Impositivos',
				id:'exposicionId',
				monitorValid:true,
				items:[
					{
							xtype:'textfield',
							id:'empresaId',
							hideLabel:true,
							fieldLabel:'id de Empresa',
							name:'id',
							hidden:true
						},{
							xtype:'textfield',
							id:'cuitId',
							fieldLabel:'C.U.I.T',
							allowBlank:true,
							msgTarget:'under',
							width:95,
							vtype:'cuit',
							name:'cuit'
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
							id:'direccionfiscalId',
							fieldLabel:'Dirección Fiscal',
							allowBlank: false,
							width:260,
							msgTarget: 'under',
							name: 'direccionFiscal'
							
					},{
							xtype:'textfield',
							id:'nombreId',
							fieldLabel:'Nombre Comercial',
							allowBlank:false,
							msgTarget:'under',
							width:260,
							name:'nombre'
					},{
						xtype:'radio',
					 	id:'resinsradioId',
					 	fieldLabel:'Res.Ins.',
					 	name:'resins'
					},{
						xtype:'radio',
					  	id:'noinsradioId',
					  	fieldLabel:'No Ins.',
					  	name:'noins'
					 },{
					  xtype:'radio',
					  id:'exentoId',	
					  fieldLabel:'Exento',
					  name:'exento'
					 },{
					  xtype:'radio',
					  id:'consfinalId',
					  fieldLabel:'Cons.Final',
					  name:'consfinal'
					 },{
					  xtype:'radio',
					  id:'monotributoId',
					  fieldLabel:'Monotributo',
					  name:'monotributo'
					 },{
					  xtype:'combo',
					  fieldLabel:'I.V.A',
					  id:'resinsValorCmbId',
					  name:'resinsValorCmb',
					  hiddenName:'resinsValor',
					  valueField:'id',
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
					 }
				]
			}),
			new Ext.ux.Wiz.Card({
				title: 'Datos de Empresa',
				id:'datosempresaId',
				monitorValid: true,
				autoScroll:true,
				width:450,
				items: [
							
						{
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
								        			width:390,
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
										        		forceSelection:true,
										        		store:subrubroStore,
										        		width:260}
										        		
								        			},{
								        			width:90,
								        			layout:'form',
								        			border:false,
								     					items:
								     						{
								        						xtype:'button', 
								        						text:'Agregar',
								        						listeners:{
								        							click: function(){
								        								if(Ext.getCmp('idRubro').validate()){
								        									Ext.getCmp('nombreSubrubroId').setValue('');
								        									winSubrubro.show();
								        								}else
								        									Ext.MessageBox.show({
								        										title:'Error',
								        										msg:'Seleccione un rubro antes de agregar el Sub-Rubro',
								        										buttons:Ext.MessageBox.OK,
								        										icon:Ext.MessageBox.ERROR
								        									});
								        							}
								        						}
								        					}
								        			}
								        		]
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
												 width:100,
												 forceSelection:true,
												 allowBlank:false,
												 msgTarget:'under',
												 store: new Ext.data.SimpleStore({
												 	fields:['id','descripcion'],
												 	data : [['2010','2010'],['2011','2011'],['2012','2012']]
												 })
												 },{
													  xtype:'textarea',
													  fieldLabel:'Observación',
													  maxLength:255,
													  width:200,
													  name:'observacion',
													  id:'observacionId'
												 }		  		
	        								]
			}),
			new Ext.ux.Wiz.Card({
				title:'Datos del Servicio Contratado',
				id:'datosserviciocontratadoId',
				monitorValid:true,
				width:600,
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
       if(this.currentCard==2){
//       		if(!Ext.getCmp('idSubrubro').validate())
//       			this.cardPanel.getLayout().setActiveItem(this.currentCard - 1)
				
				var flag=false;
				if (Ext.getCmp('noinsradioId').checked)
					flag=true;	
				if (Ext.getCmp('exentoId').checked)
					flag=true;
				if (Ext.getCmp('consfinalId').checked){
					flag=true;
				}
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
					this.cardPanel.getLayout().setActiveItem(this.currentCard - 1)
				}
				
				if ((Ext.getCmp('exentoId').checked || Ext.getCmp('monotributoId').checked || Ext.getCmp('resinsradioId').checked) 
					&& Ext.getCmp('cuitId').getValue()==''){
					Ext.MessageBox.show({
						title:'Error',
						msg:'Si no es un Consumidor Final o responsable No Inscripto, el ingreso de C.U.I.T es obligatorio',
						icon:Ext.MessageBox.ERROR,
						button:Ext.MessageBox.OK
					})	
					this.cardPanel.getLayout().setActiveItem(this.currentCard - 1)
				}
		   
		   
		   
		   
		  	
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
		var storeDetalle = gridDetalleServicioContratado.getStore();
		var storeOtrosConceptos = gridOtrosConceptos.getStore();
		var detallejsonArr=[];
		var detallejsonStr = '';
		var otrosconceptosjsonArr=[];
		var otrosconceptosjsonStr='';
		var productosjsonArr = [];
		var productosjsonStr ='';
		var flaglotevacio=false;
		storeDetalle.data.each(function(rec){
				/*if(! rec.data.lote_id>0){
					flaglotevacio=true
					return false;
				}*/
				detallejsonArr.push(rec.data);
			}
		);
		/*if (flaglotevacio){
				Ext.MessageBox.show({
						title:'Error',
						msg:'El detalle del servicio contratado tiene una linea con lote incorrecto. Seleccione un lote correcto',
						icon:Ext.MessageBox.ERROR,
						button:Ext.MessageBox.OK
					});
				return false;	
		}*/
					
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
				id:datos.exposicionId.id,
				'empresa.nombre':datos.exposicionId.nombre,
				'empresa.codigoPostal':datos.datosempresaId.codigoPostal,
				'empresa.nombreRepresentante':datos.datoscontactoId.nombreRepresentante,
				'empresa.telefono1':datos.datosempresaId.telefono1,
				'empresa.telefono2':datos.datosempresaId.telefono2,
				'empresa.cuit':datos.exposicionId.cuit,
				'empresa.direccion':datos.datosempresaId.direccion,
				'empresa.telefonoRepresentante1':datos.datoscontactoId.telefonoRepresentante1,
				'empresa.telefonoRepresentante2':datos.datoscontactoId.telefonoRepresentante2,
				'empresa.telefonoRepresentante3':datos.datoscontactoId.telefonoRepresentante3,
				//'empresa.subrubro.rubro.id':datos.datosempresaId.rubro_id,
				'empresa.subrubro.id':datos.datosempresaId.subrubro_id,
				//'empresa.subrubro.nombreSubrubro':Ext.getCmp(''),
				'empresa.dniRep':datos.datoscontactoId.dniRep,
				'empresa.email':datos.datosempresaId.email,
				'empresa.cargoRep':datos.datoscontactoId.cargoRep,
				'empresa.dniRep':datos.datoscontactoId.dniRep,
				'empresa.sitioWeb':datos.datosempresaId.sitioWeb,
				'empresa.razonSocial':datos.exposicionId.razonSocial,
				'empresa.direccionFiscal':datos.exposicionId.direccionFiscal,
				'empresa.localidad.id':datos.datosempresaId.localidad_id,
				//'empresa.localidadFiscal':datos.datosempresaId.localidadFiscal,
				//'empresa.provinciaFiscal':datos.datosempresaId.provinciaFiscal,
				'empresa.codigoPostal':datos.datosempresaId.codigoPostal,
				'empresa.vendedor.id':datos.datosempresaId.vendedor_id,
				detallejson:detallejsonStr,
				otrosconceptosjson:otrosconceptosjsonStr,
				productosjson:productosjsonStr,
				anio:datos.datoscontactoId.anio,
				//fechaVencimiento:Ext.getCmp('fechaVencimientoOrdenId').getValue(),//datos.datosserviciocontratadoId.fechaVencimientoOrden,
				//fechaVencimiento_year:Ext.getCmp('fechaVencimientoOrdenId').getValue().getFullYear(),
				//fechaVencimiento_month:Ext.getCmp('fechaVencimientoOrdenId').getValue().getMonth(),
				//fechaVencimiento_day:Ext.getCmp('fechaVencimientoOrdenId').getValue().getDate(),
				'expo.id':datos.datoscontactoId.exposicionid,
				ivaGralCheck:(datos.exposicionId.resins=='on'?true:false),
				ivaRniCheck:(datos.exposicionId.noins=='on'?true:false),
				exentoCheck:(datos.exposicionId.exento=='on'?true:false),
				consFinalCheck:(datos.exposicionId.consfinal=='on'?true:false),
				monotributoCheck:(datos.exposicionId.monotributo=='on'?true:false),
				porcentajeResIns:datos.exposicionId.resinsValor,
				porcentajeResNoIns:0,
				observacion:datos.datoscontactoId.observacion
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
						var msg="";
						if(respuesta.errors){
							for(var i=0;i<respuesta.errors.length;i++){
								msg=msg+respuesta.errors[i].title+'\r\n';
							}
							Ext.MessageBox.show({
								title:'Error',
								msg:msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
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
						,buttons:Ext.MessageBox.OK
						,fn:function(btn){
						}
					});
				}
				
			}
		});
		
	});
	
    
	
	wizard.show();
	Ext.getCmp('searchCriteriaId').focus('',10);	
});

