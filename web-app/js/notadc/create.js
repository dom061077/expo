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
	
	
	var formNotadc = new Ext.form.FormPanel({
		url:'savejson',
		renderTo:'formulario_extjs',
		width:470,
		title:'Alta de Nota',
		frame:true,
		items:[
		       {
		    	   xtype:'hidden',
		    	   name:'ordenReserva.id',
		    	   allowBlank:true,
		    	   id:'ordenReservaId'
		       },{
		    	   xtype:'textfield',
		    	   name:'monto',
		    	   allowBlank:false,
		    	   id:'montoId',
		    	   fieldLabel:'Orden de reserva numero',
		    	   msgTarget:'under'
		       },{
		    	   xtype:'textfield',
		    	   name:'nombre',
		    	   id:'nombreid',
		    	   fieldLabel:'Nombre de Comercial'
		       },{
		    	   xtype:'textfield',
		    	   name:'razonSocial',
		    	   id:'razonSocialId'
		    	   
		       },grid
		]
	});
	
});