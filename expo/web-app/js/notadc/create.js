Ext.onReady(function(){
	Ext.QuickTips.init();

	var cm = new Ext.grid.ColumnModel({
		columns:[
			{
				header: 'Descripción',
				dataIndex:'descripcion',
				width:180,
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header: 'Cantidad',
				dataIndex:'cantidad',
				width:90,
				editor: new Ext.form.NumberField({
					allowBlank:false,
					//allowNegative:false,
					maxValue:1000000
				})
			},{
				header: 'Importe',
				dataIndex:'importe',
				width:90,
				editor: new Ext.form.NumberField({
					allowBlank:false
					//allowNegative:false,
					
				})
			}
		]
	});
	
	var store = new Ext.data.Store({
		autoDestroy:true,
		//url:'bancos.xml',
		data:[],
		reader: new Ext.data.ArrayReader({
			record:'banco',
			fields:[
				{name: 'descripcion',type:'string'},
				{name: 'cantidad',type:'numeric'},
				{name: 'importe', type: 'string'}
			]
		})
	});

	var grid = new Ext.grid.EditorGridPanel({
		store:store,
		cm:cm,
		width:500,
		height:280,
		title:'Detalle de Nota',
		selModel: new Ext.grid.RowSelectionModel(),
		tbar:[
			{
				text: 'Agregar',
				handler :  function(){
					var Banco = grid.getStore().recordType;
					var b = new Banco({
						numero:'',
						banco:'',
						importe:0
					});
					grid.stopEditing();
					store.insert(
						grid.getStore().getCount()-1>=0?grid.getStore().getCount():0
					,b);
					grid.startEditing(
					grid.getStore().getCount()-1>=0?grid.getStore().getCount()-1:0
					,0);
				}
			},{
				text: 'Borrar',
				handler: function(){
					var sm = grid.getSelectionModel();
					var sel = sm.getSelected();
					if(sm.hasSelection()){
						grid.getStore().remove(sel);
					}else{
						Ext.MessageBox.show({
							title:'Mensaje',
							msg:'Seleccione una fila para de la grilla para poder borrar',
							icon: Ext.MessageBox.WARNING,
							buttons: Ext.MessageBox.OK
						});
						
					}
				}
			}
		]
	});
	
	
	var formNotadc = new Ext.form.FormPanel({
		url:'savejson',
		renderTo:'formulario_extjs',
		width:470,
		title:'Alta de Nota',
		frame:true,
		items:[
		       
		       {
		    	   xtype:'fieldset',
		    	   checkboxToggle:true,
		    	   title: 'Informacion de la Orden de Reserva',
		    	   defaultType : 'textfield',
		    	   collapsed:false,
		    	   defaults:{
		    		 // anchor: '100%'
		    	   },
		    	   items:[
		   		       {
				    	   xtype:'hidden',
				    	   name:'ordenReserva.id',
				    	   allowBlank:true,
				    	   id:'ordenReservaId'
				       },{
				    	   xtype:'textfield',
				    	   name:'ordenReserva.numero',
				    	   allowBlank:false,
				    	   id:'ordenReservaNumero',
				    	   fieldLabel:'Orden Nro.',
				    	   disabled:true,
				    	   msgTarget:'under'
				       },{
				    	   xtype:'textfield',
				    	   name:'ordenReserva.nombre',
				    	   id:'nombreid',
				    	   disabled:true,
				    	   fieldLabel:'Nombre Comercial',
				    	   width : 350 
				       },{
				    	   xtype:'textfield',
				    	   name:'ordenReserva.razonSocial',
				    	   fieldLabel:'Razón Social',
				    	   disabled:true,
				    	   id:'razonSocialId',
				    	   width: 350
				       },{
				    	   xtype:'textfield',
				    	   name:'saldo',
				    	   fieldLabel:'Saldo',
				    	   id:'saldoId',
				    	   disabled:true
				       }		    	          
		    	   ]
		       },{
		    	   xtype:'combo',
		    	   fieldLabel:'Tipo de Nota',
		    	   store: new Ext.data.JsonStore({
		    		   		root:'rows',
		    		   		autoLoad:true,
		    		   		url:'listtiponotajson',
		    		   		fields:['id','name']
		    	   }),
		    	   mode:'local',
		    	   id:'tipoId',
		    	   displayField:'name',
		    	   valueField:'id',
		    	   hiddenName:'tipo',
		    	   hiddenField:'id'
		       },{
		    	   xtype:'hidden',
		    	   allowBlank:true,
		    	   name:'detallejson',
		    	   id:'detallejsonId'
		       }		       
		       ,grid
		],
	    buttons:[
	             {
	             	text:'Guardar',
	             	id:'guardarId',
	             	handler:function(){
	 			 		var detallejsonArr=[];
	 			 		var detallejsonStr = '';
	 			 		var saldo=0;
	 			 		var totalnota=0;
	 			 		var flagerrordetalle=false;
	 			 		store.data.each(
	 			 			function(rec){
	 			 				if(rec.data.descripcion.trim()=='' || !rec.data.cantidad>0
	 			 						|| !rec.data.importe>0){
	 			 					flagerrordetalle=true;
	 			 					return false;
	 			 				}
	 			 				detallejsonArr.push(rec.data);
	 			 				totalnota=totalnota+rec.data.importe;
	 			 			}	
	 			 		);
	 			 		if(totalnota<=0){
	 			 			Ext.MessageBox.show({
	 			 				title:'Error',
	 			 				msg:'No hay ninguna linea de detalle ingresada',
				 					icon:Ext.MessageBox.ERROR,
									buttons:Ext.MessageBox.OK	
	 			 			});
	 			 			return false;
	 			 		}
	 			 		if(flagerrordetalle){
	 			 			Ext.MessageBox({
	 			 				title:'Error',
	 			 				msg:'Hay lineas de detalle con datos incompletos en la grilla',
	 			 				icon:Ext.MessageBox.ERROR,
	 			 				buttons: Ext.MessageBox.OK
	 			 			});
	 			 			return false;
	 			 		}
	 			 		
	 			 		if(saldo<totalnota){
	 			 			Ext.MessageBox.show({
	 			 				title:'Error',
	 			 				msg:'',
	 			 				icon:Ext.MessageBox.ERROR,
	 			 				buttons:Ext.MessageBox.OK
	 			 			});
	 			 		}else{
	 			 			formNotadc.getForm().submit({
	 			 				success: function(f,a){
	 			 					if(a.result.success){
	 			 						Ext.getCmp('guardarId').disable();
	 			 						Ext.getCmp('cancelarId').setText('Salir');
	 			 						Ext.MessageBox.show({
	 			 							title:'Mensaje',
	 			 							msg:'La Nota se generó correctamente',
	 			 							buttons:Ext.MessageBox.OK,
	 			 							icon:Ext.MessageBox.INFO,
	 			 							fn:function(btn){
	 			 								//window.location='../ordenReserva/list';
	 			 							}
	 			 						});
	 			 						//window.location='reporte?target=_blank&_format=PDF&_name=nota&_file=nota&id='+a.result.id+"&totalletras="+a.result.totalletras;		    			 						
	 			 					}else{
	 			 						Ext.MessageBox.show({
	 			 							title:'Error',
	 			 							msg:a.result.message,
	 			 							icon:Ext.MessageBox.ERROR,
	 			 							buttons:Ext.MessageBox.OK
	 			 						});
	 			 					}
	 			 				},
	 			 				failure:function(f,a){
	 			 					
	 			 				}
	 			 			});
	 			 		}
	 			 			
	             	}	
	             },{
	             	text:'Cancelar',
	             	id:'cancelarId',
	             	handler:function(){
	             		window.location='../notaDC/list';
	             	}
	             }
	    ]	
	});
	
	
	formNotadc.load({
		url:'showjson',
		params:{
			'id':ordenreservaId
		},
		success: function(f,a){
			//Ext.getCmp('nombreempresaId').setValue(a.result.data.nombreempresa);
			//Ext.getCmp('numeroordenId').setValue(a.result.data.numero);
			//Ext.getCmp('saldoordenId').setValue(a.result.data.saldoorden);
			
		},
		failure: function (thisform,action){
				var msg='';
				if(!action.result.success){
   					if (action.result.errors){
						    		for (var i=0; i<action.result.errors.length;i++){
						    			msg=msg+action.result.errors[i].title+'\r\n';	
				    				}
				    				Ext.Msg.alert(msg);
				    				Ext.MessageBox.show({
								    	title:'Error',
								    	msg:'Seleccione un rubro antes de agregar el Sub-Rubro',
								    	buttons:Ext.MessageBox.OK,
								   		icon:Ext.MessageBox.ERROR
								   });
			    				}
   					
				}
		}
	});
	
	
});