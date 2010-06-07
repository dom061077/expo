Ext.onReady(function (){
	Ext.QuickTips.init();
	
	var cm = new Ext.grid.ColumnModel({
		columns:[
			{
				id: 'numero',
				header: 'Número',
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
		renderTo:'recibo_form',
		width:400,
		height:300,
		title:'Cheques',
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
			}
		]
	});
//	store.load();
});