Ext.onReady(function (){
	Ext.QuickTips.init();
	
	var cm = new Ext.grid.ColumnModel({
		columns:[
			{
				id: 'numero',
				header: 'NÃºmero',
				dataIndex: 'numero',
				width:100,
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header: 'Banco',
				dataIndex:'banco',
				width:200,
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			},{
				header: 'Importe',
				dataIndex:'importe',
				width:100,
				editor: new Ext.form.TextField({
					allowBlank:false,
					allowNegative:false,
					maxValue:1000000
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
				{name: 'importe', type: 'string'}
			]
		})
	});
	
	var grid = new Ext.grid.EditorGridPanel({
		store:store,
		cm:cm,
		width:400,
		height:300,
		title:'Cheques',
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
					store.insert(0,b);
					grid.startEditing(0,0);
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
		width:470,
		height:470,
		frame:true,
		items:[
			{
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
				fieldLabel:'Efectivo',
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
			 	handler: function(){
			 		var chequesjsonArr=[];
			 		var chequesjsonStr = '';
			 		var flagerrorcheques=false;
			 		store.data.each(
			 			function(rec){
			 				if(rec.data.numero.trim()=='' ||
			 				   rec.data.banco.trim()== '' ||
			 				   !rec.data.importe >0){
			 				   		flagerrorcheques=true
			 				   		return false;
			 				   }
		 					chequesjsonArr.push(rec.data);	
			 			}
			 		);
	 				chequesjsonStr=Ext.encode(chequesjsonArr);
					Ext.getCmp('chequesjsonId').setValue(chequesjsonStr);			 		
			 		if (flagerrorcheques){
			 			Ext.MessageBox.show({
			 				title:'Error',
			 				msg:'Hay filas con datos de cheque incompletos en la grilla',
			 				icon:Ext.MessageBox.ERROR,
			 				buttons: Ext.MessageBox.OK
			 			});
			 			return false;
			 		}else{
			 			formRecibo.getForm().submit({
			 				success: function(f,a){
			 					if(a.result.success){
			 						Ext.MessageBox.show({
			 							title:'Datos de retorno json',
			 							msg:'Numero de recibo: '+a.result.numero+', total: '+a.result.total
			 								+', total en letras: '+a.result.totalenletras,
			 							buttons:Ext.MessageBox.OK	
			 						});
			 					}else
			 						Ext.MessageBox.show({
			 							title:'Error',
			 							msg:'Ocurrión un error al tratar de genera el recibo',
			 							icon:Ext.MessageBox.ERROR,
			 							buttons:Ext.MessageBox.OK
			 						});
			 				},
			 				failure: function(f,a){
			 					
			 				}
			 			});
			 		}
			 		
			 	}
			 
			},{
			 	text:'Cancelar',
			 	handler: function(){
			 		window.location='../orderReserva/list';
			 	}
			}
		]
	});
	Ext.getCmp('conceptoId').focus('',10);	
});