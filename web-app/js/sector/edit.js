Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var exposicionStore = new Ext.data.JsonStore({
		//autoLoad:true,
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
	
	var storeLote = new Ext.data.JsonStore({
		//autoLoad:true,
		root:'rows',
		url: '../lote/listjson',
		fields: ['id','nombre']
	});
	storeLote.load({
		params:{'sector_id':sectorId}
	});
	var grid = new Ext.grid.GridPanel({
		store:storeLote,
		width:450,
		height:280,
		title:'Lotes',
		columns:[
			{header:"id",dataIndex:"id",hidden:true},
			{header:"Nro.",dataIndex:"nombre",width:200}
		]
		/*,
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
		]*/
	});
	
	

	var formSector = new Ext.form.FormPanel({
		url:'updatejson',
		renderTo:'formulario_extjs',
		width:470,
		title:'Modificación de Sector',
		frame:true,
		items:[
			{
				xtype:'textfield',
				id:'sectorId',
				name:'id',
				hidden:true,
				value:sectorId,
				hideLabel:true
			},{
				xtype:'combo',
				store:exposicionStore,
				disabled:true,
				id:'exposicionId',
				displayField:'nombre',
				valueField:'id',
				allowBlank:false,
				msgTarget:'under',
				mode:'local',
				fieldLabel:'Exposición',
				name:'exposicion',
				hiddenName:'expo.id'
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
								if(a.result.success){
									Ext.MessageBox.show({
										title:'Mensaje',
										msg:'El sector y sus lotes se registraron correctamente',
										icon:Ext.MessageBox.INFO,
										buttons:Ext.MessageBox.OK,
										fn: function(btn){
											window.location='list';
										}
									});
								}else{
			 						Ext.MessageBox.show({
			 							title:'Error',
			 							msg:'Ocurrió un error al tratar de registra el sector con sus lotes',
			 							icon:Ext.MessageBox.ERROR,
			 							buttons:Ext.MessageBox.OK
			 						});
								}
							},
							failure:function(f,a){
								
							}
						});
				}
			},{
				text:'Eliminar',
				handler: function(){
					
				}
			},{
				text:'Cancelar',
				handler:function(){
					window.location='list';
				}
			}
		]
	});
	
	formSector.load({
		url:'showjson'
		,params:{
			id:sectorId
		},
		success: function(f,a){
			Ext.getCmp('nombreId').setValue(a.result.data.nombre);
			Ext.getCmp('exposicionId').hiddenField.value=a.result.expo_id
			
		},
		failure: function (thisform,action){
			alert(action);
		}
		
	});
	
});