Ext.onReady(function(){
	Ext.QuickTips.init();
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
			fields:['id','expoNombre','nombre','porcentaje','precio'],
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
			url:'../sector/updatejson',
			method:'POST',
			params:{
				'id':records.data.id,
				'porcentaje':records.data.porcentaje,
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
						//loteId=sel.data.id;
						//sectorId=null;
						listdescuentosStore.load({
							//params:{'loteId':loteId,'expoId':expoId}
						});
						descuentoswin.title='Tarifario de Lotes';
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
		    {	header:'Descuentos',dataIndex:'porcentaje',width:80,type:'float'
		    	,editor:{
			    	xtype: 'numberfield',
			    	msgTarget:'under',
			    	allowBlank:false
			    }
		    },{
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
	
	var griddescuentos = new Ext.grid.GridPanel({
		columns:[
		         {header:'id',dataIndex:'id',hidden:true},
		         {header:'Porcentaje',dataIndex:'porcentaje',width:80,type:'float'
		        	 ,editor:{
		        		 xtype: 'numberfield',
		        		 msgTarget:'under',
		        		 allowBlank:false
		        	 }
		         },
		         {header:'Fecha Vence',dataIndex:'fechaVencimiento',width:80,type:'float'
		        	 ,editor:{
		        		 xtype:'numberfield',
		        		 msgTarget:'under',
		        		 allowBlank:false
		        	 }
		         }
		],
		store:listdescuentosStore,
		header:true,
		footer:true,
		selModel: new Ext.grid.RowSelectionModel(),
		stripeRows:true,
		loadMask:true,
		tbar:[
		      {
		    	  text:'Agregar',
		    	  handler:function(){
						var e = new ListaPrecioSector({
							id:0,
			                porcentaje:0,
			                fechaVencimiento:(new Date()).clearTime()
			            });
		                //editor.stopEditing();
		                listapreciosStore.insert(0, e);
		                //grid.getView().refresh();
		                //grid.getSelectionModel().selectRow(0);
		                //editor.startEditing(0);
		    		  
		    	  }
		      },{
		    	  text:'Eliminar',
		    	  handler:function(){
		    		  
		    	  }
		      }
		],
		plugins:[editorDescuentos]
	});
	
	var formdescuentos = new Ext.form.FormPanel({
		frame:true,
		items:[
		       {
		    	   xtype:'textfield',
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
		height:280,
		items:[formdescuentos]
	});
	 
});