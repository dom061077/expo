Ext.onReady(function(){
	Ext.QuickTips.init();
	var sectorId=0;
	var editor = new Ext.ux.grid.RowEditor({
		saveText:'Guardar',
		cancelText:'Cancelar',
		errorText:'Errores',
		errorSumary:false
	});
	
	var editorDescuentos = new Ext.ux.grid.RowEditor({
		saveText:'Guardar',
		cancelText:'Cancelar',
		errorText:'Errores',
		errorSymary:false
	});
	
				  	
	var filters = new Ext.ux.grid.GridFilters({
		encode:true,
		local:false,
		menuFilterText:'Filtro',
		emptyText:'Ingrese Filtro...',
		filters:[{
				type:'numeric',
				dataIndex:'id',
				disabled:true
			},{
				type:'string',
				dataIndex:'expoNombre',
				disabled:false
			},{
				type:'string',
				dataIndex:'nombre',
				disabled:false
			}
		]
	});

	var listapreciosStore = new Ext.data.JsonStore({
			autoLoad:true,
			totalProperty:'total',
			root:'rows',
			url:'listjsonprecios',
			fields:['id','expoNombre','nombre','precio'],
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
	
		
	listapreciosStore.on('update',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'updatejson',
			method:'POST',
			params:{
				'id':records.data.id,
				'precio':records.data.precio
			},
			success:function(resp,opt){
					var respuesta=Ext.decode(resp.responseText);
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
						if(respuesta.success){
							
						}else{
							var msg="";
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
			failure:function(resp,opt){
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
							msg:'Se produjo un error al intentar modificar el registro'
							,buttons:Ext.MessageBox.OK
							,fn:function(btn){
							}
						});
					}
				
			}
		});
		
	});
	

	
	var gridprecios = new Ext.grid.GridPanel({
		id:'gridpreciosId',
		loadMask:true,
		plugins: [editor,filters],
		header:true,
		footer:true,
		selModel: new Ext.grid.RowSelectionModel(),
		stripeRows:true,
		store:listapreciosStore,
		width:550,
		height:400,
		tbar:[{
				text:'Quitar Filtros',
				handler:function(){
					gridprecios.filters.clearFilters();
				}
			},{
				text:'Descuentos',
				handler:function(){
					var sm = gridprecios.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						sectorId=sel.data.id;
						listdescuentosStore.load({
							params:{'sectorId':sectorId}
						});
						descuentoswin.title='Tarifario de Lotes';
						formdescuentos.getForm().findField('expoNombre').setValue(sel.data.expoNombre);
						formdescuentos.getForm().findField('sectorNombre').setValue(sel.data.nombre);
						descuentoswin.show();
					}else{
						Ext.MessageBox.show({
                    		title:'Mensaje',
                    		msg:'Seleccione un sector para cargar los descuentos',
                    		icon:Ext.MessageBox.INFORMATION,
                    		buttons:Ext.MessageBox.OK
                    	});
					}
					
				}
			}
		],
		columns:[
		    {	header:'id',dataIndex:'id',hidden:true},
		    {	header:'Expo',dataIndex:'expoNombre',width:150,sortable:true},
			{	header:'Sector',dataIndex:'nombre',width:100,sortable:true},
			{
		    	header:'Tarifa',dataIndex:'precio',width:80,type:'float'
		    	,editor:{
		    		xtype:'numberfield',
		    		msgTarget:'under',
		    		allowBlank:false
		    	}	
		    }
        ]
	
	});
	
	var form = new Ext.form.FormPanel({
		renderTo:'listaprecios_extjs',
		frame:true,
		items:gridprecios,
		title:'Descuentos por Sector'
	});
	//------------------componentes de descuento-----------------
	
	var DescuentoSector = Ext.data.Record.create([{
			name: 'id',
			type: 'integer'
		},{
			name:'porcentaje',
			type:'float'
		},{
			name:'fechaVencimiento',
			type:'date'
		}]);	
	
	
	var listdescuentosStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'listjsondescuentos',
		fields:['id','porcentaje','fechaVencimiento'],
		listeners:{
			loadexception:function(proxy, store,response,e){
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
	
	listdescuentosStore.on('add',
		function(store,records,index){
			var conn = new Ext.data.Connection();
			conn.request({
				url:'addjsondescuentos',
				method:'POST',
				params:{
					'sectorId':sectorId,
					'porcentaje':records[0].data.porcentaje,
					'fechaVencimiento_year':records[0].data.fechaVencimiento.getFullYear(),
					'fechaVencimiento_month':records[0].data.fechaVencimiento.getMonth()+1,
					'fechaVencimiento_day':records[0].data.fechaVencimiento.getDate()
				},
				success:function(resp,opt){
					var respuesta=Ext.decode(resp.responseText);
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
						if(respuesta.success){
							var record = listdescuentosStore.getAt(0);
							record.set('id',respuesta.id);
							record.commit();
						}else{
							var msg="";
							for(var i=0;i<respuesta.errors.length;i++){
								msg=msg+respuesta.errors[i].title+'\r\n';
							}
							Ext.MessageBox.show({
								title:'Error',
								msg:msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK,
								fn: function(button){
									listdescuentosStore.load({
										params:{'sectorId':sectorId}
									});
								}
							});
						}
						//listdescuentosStore.load({
						//	params:{'sectorId':sectorId}
						//});
					}					
				},
				failure:function(resp,opt){
					
				}
			});
			
		}
	);
	

	listdescuentosStore.on('update',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'updatejsondescuentos',
			method:'POST',
			params:{
				'id':records.data.id,
				'porcentaje':records.data.porcentaje,
				'fechaVencimiento_year':records.data.fechaVencimiento.getFullYear(),
				'fechaVencimiento_month':records.data.fechaVencimiento.getMonth()+1,
				'fechaVencimiento_day':records.data.fechaVencimiento.getDate()
			},
			success:function(resp,opt){
					var respuesta=Ext.decode(resp.responseText);
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
						if(respuesta.success){
							
						}else{
							var msg="";
							for(var i=0;i<respuesta.errors.length;i++){
								msg=msg+respuesta.errors[i].title+'\r\n';
							}
							Ext.MessageBox.show({
								title:'Error',
								msg:msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
							listdescuentosStore.load({
								params:{'sectorId':sectorId}
							});
						}
					}					
			},
			failure:function(resp,opt){
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
							msg:'Se produjo un error al intentar modificar el registro'
							,buttons:Ext.MessageBox.OK
							,fn:function(btn){
							}
						});
					}
				
			}
		});
		
	});
	
	
	var griddescuentos = new Ext.grid.GridPanel({
		columns:[
		         {header:'id',dataIndex:'id',hidden:true},
		         {header:'Porcentaje',dataIndex:'porcentaje',width:80,type:'float'
		        	 ,editor:{
		        		 xtype: 'numberfield',
		        		 msgTarget:'under',
		        		 //maxValue:100,
		        		 //maxLength:3,
		        		 allowBlank:false
		        	 }
		         },
		         {header:'Fecha Vence',dataIndex:'fechaVencimiento',width:80,type:'date'
		         	 ,renderer:formatDate
		        	 ,editor: new Ext.form.DateField({
						format:'d/m/y'
						//,disabledDays:[0,6],
						//disabledDaysText: 'El vencimiento no puede ser un fin de semana'
						})
		         }
		],
		store:listdescuentosStore,
		header:true,
		footer:true,
		height:300,
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
		selModel: new Ext.grid.RowSelectionModel(),
		stripeRows:true,
		loadMask:true,
		tbar:[
		      {
		    	  text:'Agregar',
		    	  handler:function(){
						var e = new DescuentoSector({
							id:0,
			                porcentaje:1,
			                fechaVencimiento:(new Date()).clearTime()
			            });
		                //editor.stopEditing();
		                listdescuentosStore.insert(0, e);
		                //grid.getView().refresh();
		                //grid.getSelectionModel().selectRow(0);
		                //editor.startEditing(0);
		    		  
		    	  }
		      },{
		    	  text:'Eliminar',
		    	  handler:function(){
		    		  editor.stopEditing();
		    		  var sm = griddescuentos.getSelectionModel();
		    		  var sel = sm.getSelected();
		    		  if(sm.hasSelection()){
		    		  	var conn = new Ext.data.Connection();
		    		  	conn.request({
		    		  		url:'deletejsondescuentos',
		    		  		params:{
		    		  			id:sel.data.id
		    		  		},
		    		  		success:function(resp,opt){
		    		  			if(resp)
		    		  				listdescuentosStore.remove(sel);
		    		  		},
		    		  		failure:function(resp,opt){
		    		  			
		    		  		}
		    		  	});
		    		  }
	                  var s = griddescuentos.getSelectionModel().getSelections();
    	              for(var i = 0, r; r = s[i]; i++){
        	            listapreciosStore.remove(r);
            	      }
		    	  }
		      }
		],
		plugins:[editorDescuentos]
	});
	
	var formdescuentos = new Ext.form.FormPanel({
		frame:true,
		title:'Carga de Descuentos para...',
		items:[
		       	{
		       		xtype:'textfield',
		       		name:'expoNombre',
		       		disabled:true,
		       		fieldLabel:'Exposición'
				},{
		    	   xtype:'textfield',
		    	   name:'sectorNombre',
		    	   disabled:true,
		    	   fieldLabel:'Sector'
				},griddescuentos]
	});
	
	var descuentoswin = new Ext.Window({
		applyTo:'descuentoswin_extj',
		title:'Descuentos por sector',
		resizable:false,
		closeAction:'hide',
		modal:true,
		formPanel:null,
		width:450,
		height:450,
		items:[formdescuentos]
	});
	 
});