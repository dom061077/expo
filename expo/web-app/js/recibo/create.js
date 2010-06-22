Ext.onReady(function (){
	Ext.QuickTips.init();
	
    function formatDate(value){
        return value ? value.dateFormat('d/m/y') : '';
    }
	
	var cm = new Ext.grid.ColumnModel({
		columns:[
			{
				id: 'numero',
				header: 'Número',
				dataIndex: 'numero',
				width:90,
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header: 'Banco',
				dataIndex:'banco',
				width:180,
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header: 'Importe',
				dataIndex:'importe',
				width:90,
				editor: new Ext.form.NumberField({
					allowBlank:false,
					allowNegative:false,
					maxValue:1000000
				})
			},{
				header:'Fecha Venc.',
				dataIndex:'fechavencimiento',
				width:95,
				renderer:formatDate,
				editor: new Ext.form.DateField({
					format:'d/m/y',
					disabledDays:[0,6],
					disabledDaysText: 'El vencimiento no puede ser un fin de semana'
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
				{name: 'numero',type:'string'},
				{name: 'banco',type:'string'},
				{name: 'importe', type: 'string'},
				{name: 'vencimiento', type: 'date'}
			]
		})
	});
	
	var grid = new Ext.grid.EditorGridPanel({
		store:store,
		cm:cm,
		width:500,
		height:280,
		title:'Pago Cheques',
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
//	store.load();
	
	var formRecibo = new Ext.form.FormPanel({
		url:'createjson',
		renderTo:'recibo_form',
		title:'Alta de Recibo',
		width:520,
		height:470,
		frame:true,
		items:[
			{
				xtype:'hidden',
				id:'ordenreservaId',
				name:'ordenreservaid',
				value:ordenreservaId
			},{
				xtype:'textfield',
				id:'nombreempresaId',
				name:'nombreempresaId',
				width:200,
				disabled:true,
				fieldLabel:'Empresa'
			},{
				xtype:'textfield',
				id:'numeroordenId',
				name:'numeroorden',
				disabled:true,
				fieldLabel:'Orden Nro.'
			},{
				xtype:'numberfield',
				id:'saldoordenId',
				disabled:true,
				name:'saldoorden',
				fieldLabel:'Saldo Orden'
			},{
				xtype: 'textarea',
				id:'conceptoId',
				name:'concepto',
				width:250,
				fieldLabel: 'Concepto',
				msgTarget:'under',
				allowBlank: false,
				maxLength:255
			},{
				xtype: 'numberfield',
				id:'efectivoId',
				msgTarget:'under',
				name:'efectivo',
				width:100,
				fieldLabel:'Pago Efectivo',
				allosBlank:true,
				maxLength: 10
			},{
				xtype:'hidden',
				allowBlank:true,
				name:'chequesjson',
				id:'chequesjsonId'
			},grid
		],
		buttons: [
			{
				text:'Guardar',
				id:'guardarId',
			 	handler: function(){
			 		var chequesjsonArr=[];
			 		var chequesjsonStr = '';
			 		var flagerrorcheques=false;
			 		var saldo=0;
			 		//var saldo=Ext.getCmp('saldoordenId').getValue()-Ext.getCmp('efectivoId').getValue();
			 		var totalpagado=0;
			 		if(Ext.getCmp('efectivoId').getValue()!=null)
			 			Ext.getCmp('efectivoId').setValue(0);
			 			
			 		totalpagado=Ext.getCmp('efectivoId').getValue();	
			 		store.data.each(
			 			function(rec){
			 				if(rec.data.numero.trim()=='' ||
			 				   rec.data.banco.trim()== '' ||
			 				   !rec.data.importe >0){
			 				   		flagerrorcheques=true
			 				   		return false;
			 				   }
		 					chequesjsonArr.push(rec.data);	
		 					totalpagado=totalpagado+rec.data.importe
			 			}
			 		);
			 		saldo = Ext.getCmp('saldoordenId').getValue()-totalpagado;
	 				chequesjsonStr=Ext.encode(chequesjsonArr);
					Ext.getCmp('chequesjsonId').setValue(chequesjsonStr);	
					if (totalpagado<=0){
						Ext.MessageBox.show({
							title:'Error',
							msg:'No hay pago en efectivo ni cheques ingresados',
							icon:Ext.MessageBox.ERROR,
							buttons:Ext.MessageBox.OK
						})
						return false;
					}
						
			 		if (flagerrorcheques){
			 			Ext.MessageBox.show({
			 				title:'Error',
			 				msg:'Hay filas con datos de cheque incompletos en la grilla',
			 				icon:Ext.MessageBox.ERROR,
			 				buttons: Ext.MessageBox.OK
			 			});
			 			return false;
			 		}
			 		else{
			 			if(saldo<0){
			 				Ext.MessageBox.show({
			 					title:'Error',
			 					msg:'El efectivo mís la suma de importes de los cheques es mayor que el saldo de la orden',
			 					icon: Ext.MessageBox.ERROR,
			 					button: Ext.MessageBox.OK
			 				});	
			 			
			 			}else{
			 				formRecibo.getForm().submit({
			 				success: function(f,a){
			 					if(a.result.success){
			 						/*Ext.MessageBox.show({
			 							title:'Datos de retorno json',
			 							msg:'Numero de recibo: '+a.result.numero+', total: '+a.result.total
			 								+', total en letras: '+a.result.totalletras,
			 							buttons:Ext.MessageBox.OK	
			 						});*/
			 						Ext.getCmp('guardarId').disable();
			 						Ext.getCmp('cancelarId').setText('Salir');
			 						Ext.MessageBox.show({
			 							title:'Mensaje',
			 							msg:'El recibo se generó correctamente',
			 							buttons:Ext.MessageBox.OK,
			 							icon:Ext.MessageBox.INFO,
			 							fn:function(btn){
			 								//window.location='../ordenReserva/list';
			 							}
			 						});
			 						window.location='reporte?target=_blank&_format=PDF&_name=recibo&_file=recibo&id='+a.result.id+"&totalletras="+a.result.totalletras;			 				
			 					}else
			 						Ext.MessageBox.show({
			 							title:'Error',
			 							msg:'Ocurrió un error al tratar de genera el recibo',
			 							icon:Ext.MessageBox.ERROR,
			 							buttons:Ext.MessageBox.OK
			 						});
			 				},
			 				failure: function(f,a){
			 					
			 				}
			 			});
			 			}
			 		}
			 		
			 	}
			 
			},{
			 	text:'Cancelar',
			 	id:'cancelarId',
			 	handler: function(){
			 		window.location='../ordenReserva/list';
			 	}
			}
		]
	});
	formRecibo.load({
		url:'../ordenReserva/showtorecibo',
		params:{
			'id':ordenreservaId
		},
		success: function(f,a){
			Ext.getCmp('nombreempresaId').setValue(a.result.data.nombreempresa);
			Ext.getCmp('numeroordenId').setValue(a.result.data.numero);
			Ext.getCmp('saldoordenId').setValue(a.result.data.saldoorden);
		},
		failure: function (thisform,action){
   				alert(action);
		}
	});
	Ext.getCmp('conceptoId').focus('',10);	
});