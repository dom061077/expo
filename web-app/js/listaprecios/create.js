Ext.onReady(function(){
	Ext.QuickTips.init();
	var editor = new Ext.ux.grid.RowEditor({
		saveText:'Guardar',
		errorSumary:false
	});


	var expoStore = new Ext.data.JsonStore({
		autoLoad:true,
		root:'rows',
		url:'../exposicion/listjson',
		fields:['id','nombre'],
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
	
	var comboExpo = new Ext.form.ComboBox({
		store:expoStore,
		id:'comboExpoId',
		displayField:'nombre',
		msgTarget:'under',
		valueField:'id',
		mode:'local',
		hiddenName:'expo',
		allowBlank:false
		,listeners:{
			'select':function(){
				alert('SELECCION EXPO');
			}
		}
	});
	
	var ListaPrecioSector = Ext.data.Record.create([{
			name: 'id',
			type: 'integer'
		},{
			name: 'anio',
			type: 'integer'
		},{
			name:'precio',
			type:'float'
		},{
			name:'lote',
			type:'integer'
		},{
			name:'sector',
			type:'integer'
		},{
			name:'expo',
			type:'integer'
				
		}]);
	
	var listapreciosStore = new Ext.data.JsonStore({
			autoLoad:true,
			totalProperty:'total',
			root:'rows',
			url:'../listaPrecios/listjson',
			fields:['id','expo','sector','lote','anio','precio'],
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
	
	
		listapreciosStore.on('add',function(store,records,index){
			var conn = new Ext.data.Connection();
			conn.request({
				url:'../listaPrecios/savejson',
				method:'POST',
				params:{
					'precio':records[0].data.precio,
					'anio':records[0].data.anio,
					'lote.id':records[0].data.lote,
					'sector.id':records[0].data.sector,
					'expo.id':records[0].data.expo
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
						listapreciosStore.load({
							params:{'sectorId':sectorId,'loteId':loteId,'expoId':expoId}
						});
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
								msg:'Se produjo al intentar guardar el registro'
								,buttons:Ext.MessageBox.OK
								,fn:function(btn){
								}
							});
						}
					
				}
			});
	});
	
	listapreciosStore.on('update',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'../listaPrecios/updatejson',
			method:'POST',
			params:{
				'id':records.data.id,
				'expo.id':records.data.expo,
				'sector.id':records.data.sector,
				'lote.id':records.data.lote,
				'anio':records.data.anio,
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
	
	listapreciosStore.on('remove',function(store,records,index){
		var conn = new Ext.data.Connection();
		conn.request({
			url:'../listaPrecios/deletejson',
			method:'POST',
			params:{
				'id':records.data.id
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
							msg:'Se produjo un error al intentar eliminar el registro'
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
		plugins: [editor],
		header:true,
		footer:true,
		selModel: new Ext.grid.RowSelectionModel(),
		stripeRows:true,
		store:listapreciosStore,
		width:550,
		height:400,
		tbar:[{
				text:'Eliminar',
				handler:function(){
					editor.stopEditing();
	                var s = gridprecios.getSelectionModel().getSelections();
    	            for(var i = 0, r; r = s[i]; i++){
        	            listapreciosStore.remove(r);
            	    }
				}
			},{
				text:'Agregar',
				msgTarget:'under',
				handler:function(){
					var e = new ListaPrecioSector({
						id:0,
		                vigencia:(new Date()).clearTime(),
		                precio:0
		            });
	                //editor.stopEditing();
	                listapreciosStore.insert(0, e);
	                //grid.getView().refresh();
	                //grid.getSelectionModel().selectRow(0);
	                //editor.startEditing(0);
				}
			}
		],
		columns:[
		    {header:'id',dataIndex:'id',hidden:true},
		    {header:'Expo',dataIndex:'expo',width:100
		    	,msgTarget:'under'
			    ,editor:comboExpo,sortable:false
			    ,renderer:function(value, p, record){return 'Hola';}},
		    {header:'Sector',dataIndex:'sector',width:150
			    ,editor:{
			    	xtype: 'numberfield',
			    	msgTarget:'under',
			    	allowBlank:true
			    },sortable:false},
		    {header:'Lote',dataIndex:'lote',width:100
				    ,editor:{
			    	xtype: 'numberfield',
			    	msgTarget:'under',
			    	allowBlank:true
			    },sortable:false},
			    
		    {header:'Año',dataIndex:'anio',width:80
				    ,editor:{
				    	xtype: 'numberfield',
				    	msgTarget:'under',
				    	allowBlank:false
				    },sortable:false},
		    {header:'Precio',dataIndex:'precio',width:80
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
		items:gridprecios
	});
	
	
});