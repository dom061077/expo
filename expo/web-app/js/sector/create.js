Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var exposicionStore = new Ext.data.JsonStore({
		autoLoad:true,
		totalProperty:'total',
		root:'rows',
		url:'../exposicion/listjson',
		root:'rows',
		fields:['id','nombre']
	});
	
	exposicionStore.load();
	
	var cm = new Ext.grid.ColumnModel({
		columns:[
			{
				id:'nombre',
				header:'Nombre',
				dataIndex:'nombre',
				width:'100',
				editor: new Ext.form.TextField({
					allowBlank:false
				})
			}
		]
		
	});
	
	var store = new Ext.data.Store({
		autoDestroy:true,
		//url:'bancos.xml',
		data:[],
		reader: new Ext.data.ArrayReader({
			record:'lote',
			fields:[
				{name: 'nombre',type:'string'}
			]
		})
	});
	
	var grid = new Ext.grid.EditorGridPanel({
		store:store,
		cm:cm,
		width:450,
		height:280,
		title:'Lotes',
		selModel: new Ext.grid.RowSelectionModel(),
		tbar:[
			{
				text: 'Agregar',
				handler :  function(){
					var Banco = grid.getStore().recordType;
					var b = new Banco({
						nombre:''
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
	
	

	var formSector = new Ext.form.FormPanel({
		url:'savejson',
		renderTo:'formulario_extjs',
		width:470,
		title:'Alta de Sector',
		frame:true,
		items:[
			{
				xtype:'combo',
				store:exposicionStore,
				displayField:'nombre',
				valueField:'id',
				allowBlank:false,
				mode:'local',
				fieldLabel:'Exposición',
				name:'exposicion',
				hiddenField:'expo.id'
			},{
				xtype:'textfield',
				name:'nombre',
				allowBlank:false,
				msgTarget:'under',
				id:'nombreId',
				fieldLabel:'Nombre'
			},{
				xtype:'hidden',
				allowBlank:true,
				name:'lotesjson',
				id:'lotesjsonId'
			},grid
		],
		buttons:[
			{
				text:'Guardar',
				handler:function(){
					formSector.getForm().submit({
						success:function(f,a){
							
						},
						failure:function(f,a){
							
						}
					});
				}
			},{
				text:'Cancelar'
			}
		]
	});
	
});