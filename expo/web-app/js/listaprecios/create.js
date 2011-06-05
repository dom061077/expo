Ext.onReady(function(){
	Ext.QuickTips.init();

		listapreciosStore.on('add',function(store,records,index){
			var conn = new Ext.data.Connection();
			conn.request({
				url:'../listaPrecios/savejson',
				method:'POST',
				params:{
					'precio':records[0].data.precio,
					'vigencia':records[0].data.vigencia,
					'sector.id':sectorId,
					'lote.id':loteId,
					'expo.id':expoId
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
				'vigencia':records.data.vigencia,
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
		width:400,
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
		    //{header:"Fecha",dataIndex:'fechaAlta',width:80,renderer: Ext.util.Format.dateRenderer('d/m/y'),sortable:true}
		    {header:'Vigencia',dataIndex:'vigencia',width:150,renderer: Ext.util.Format.dateRenderer('d/m/y')
				    ,editor:{
				    	xtype: 'datefield',
				    	allowBlank:false
				    },sortable:false},
		    {header:'Precio',dataIndex:'precio',width:150
		    		,editor:{
		    			xtype:'textfield',
		    			allowBlank:false
		    		}
		    }
        ]
	
	});
	
});