Ext.onReady(function(){
	Ext.QuickTips.init();
	var editor = new Ext.ux.grid.RowEditor({
		saveText:'Guardar',
		cancelText:'Cancelar',
		errorText:'Errores',
		errorSumary:false
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
				dataIndex:'sectorNombre',
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
			url:'../lote/listjsonprecios',
			fields:['id','expoNombre','sectorNombre','nombre','precio'],
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
			url:'../lote/updatejson',
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
			}
		],
		columns:[
		    {header:'id',dataIndex:'id',hidden:true},
		    {header:'Expo',dataIndex:'expoNombre',width:150,sortable:true},
			{header:'Sector',dataIndex:'sectorNombre',width:100,sortable:true},
		    {header:'Lote',dataIndex:'nombre',width:100,sortable:true},
		    {header:'Precio',dataIndex:'precio',width:80
		    	,editor:{
			    	xtype: 'numberfield',
			    	msgTarget:'under',
			    	allowBlank:false
			    }
		    }
        ]
	
	});
	
	var form = new Ext.form.FormPanel({
		renderTo:'listaprecios_extjs',
		frame:true,
		items:gridprecios
	});
	
	
	
});