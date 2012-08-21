Ext.onReady(function(){
	Ext.QuickTips.init();

	var rolesStore = new Ext.data.JsonStore({
		
		root:'rows',
		fields:['id','description'],
		url:'../authority/listjson',
		valueField:'id'
		
	});
	
	rolesStore.load();
	/*var gridroles = new Ext.grid.GridPanel({
		store:rolesStore,
		tbar:[
			{
				icon: imagePath+'/skin/database_delete.png',
				cls:'x-btn-text-icon',
				handler: function(){
					var sm = gridroles.getSelectionModel();
					var sel = sm.getSelected();
					if (sm.hasSelection()){
						gridroles.getStore().remove(sel);
					}
				},
				title:'Roles de Usuario',
				columns:[
					{header:"id",dataIndex:"id",hidden:true},
					{header:"Descripción",dataIndex:"description"}
				]
			}
		],
		height:200,
		width:300
		
	});*/
	
	var comboRoles = new Ext.form.ComboBox({
		layout:'form',
		id:'idComboroles',
		fieldLabel:'Rol',
		mode:'local',
		store:rolesStore,
		displayField:'description',
		listWidth:200,
		valueField:'id',
		forceSelection:true,
		allowBlank:false,
		hiddenName:'rol',
		msgTarget:'under'
	});
	
	var formusuario = new Ext.form.FormPanel({
		url:'savejson',
		id:'formusuarioId',
		border:false,
		renderTo:'formulario_extjs',
		frame:true,
		title:'Alta de Usuario',
		height:350,
		width:450,
		items:[
				{xtype:'textfield',
				 id:'usernameId',
				 name:'username',
				 allowBlank:false,
				 msgTarget:'under',
				 fieldLabel:'Usuario'
				},{
				 xtype:'textfield',
				 id:'userrealnameId',
				 name:'userRealName',
				 allowBlank:false,
				 msgTarget:'under',
				 fieldLabel:'Nombre Real'
				},{
				 xtype:'checkbox',
				 id:'enabledId',
				 name:'enabled',
				 fieldLabel:'Habilitado'
				},{
					xtype:'textfield',
					id:'descriptionId',
					allowBlank:false,
					msgTarget:'under',
					name:'description',
					fieldLabel:'Descripción'
				},{
					xtype:'textfield',
					inputType:'password',
					id:'passwdId',
					allowBlank:false,
					msgTarget:'under',
					name:'passwd',
					fieldLabel:'Contraseña',
					maxLength:15,
					minLength:6
				},{
					layout:'column',
					anchor:'0',
					items:[
						{ layout:'column',
						  anchor:'0',
						  items:[
						  	{
						  		columnWidth:.8,
						  		anchor:'0',
						  		layout:'form',
						  		items:comboRoles
						  	}/*,{
						  		layout:'form',
						  		items:[
						  			{
						  				xtype:'button',
						  				text:'Agregar',
						  				handler:function(){
						  					
						  				}
						  			}
						  		]
						  	}*/
						  ]
						}
					]
				}/*,gridroles*/
		],
	  buttons:[
	           {
	        	   text:'Guardar',handler:function(){
	        			formusuario.getForm().submit({
	        				success: function(f,a){
	        					Ext.MessageBox.show({
									title:"Mensaje",
									msg:"El Usuario se agregó correctamente",
									icon:Ext.MessageBox.INFO,
									buttons:Ext.MessageBox.OK,
									fn:function(btn){
										window.location='create';
									}
	        					});
	        					
	        				},
		        			failure: function(form, action) {
		        		        switch (action.failureType) {
		        		            case Ext.form.Action.CLIENT_INVALID:
		        		                Ext.Msg.alert('Fallo', 'Algunos campos tienen valores incorrectos');
		        		                break;
		        		            case Ext.form.Action.CONNECT_FAILURE:
		        		                Ext.Msg.alert('Fallo', 'Error de comunicacion Ajax');
		        		                break;
		        		            case Ext.form.Action.SERVER_INVALID:
		        		               Ext.Msg.alert('Fallo', action.result.msg);
		        		       }
		        		    }	        			
	        			});   		
	        	   }	
	           },{
	        	   text:'Cancelar',
	        	   handler:function(){
	        	   		window.location='list'
	        	   }
	           }
	  ]
		
	});
	Ext.getCmp('usernameId').focus('',10);
	
});