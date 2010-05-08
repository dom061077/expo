Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var rolesStore = new Ext.data.JsonStore({
		
		root:'rows',
		fields:['id','description'],
		url:'../authority/listjson',
		valueField:'id'
		
	});
	
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
					maxLength:'15',
					minLength:'5'
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
				{	text:'Guardar'
					,handler:function(){
						vendedor_form.getForm().submit({
							success: function(f,a){
					        	          					Ext.Msg.alert('Mensaje','Los datos se guardaron correctamente',
																	function(btn,text){
																		if(btn=='ok'){
																			window.location='list';	
																		}
					        	          							}
									        	          	);
							},
							failure: function(f,a){
														    var msg="";
														    if (a.failureType==Ext.form.Action.CONNECT_FAILURE ||
														    	a.failureType==Ext.form.Action.SERVER_INVALID){
														    		Ext.Msg.alert('Error','El servidor no Responde')
														    	}
														    if (a.result){
														    	if (a.result.loginredirect==true){
														    		Ext.Msg.alert('Su sesion de usuario a caducado, ingrese nuevamente');
														    		window.location='../logout/index';
														    		}
														    	if (a.result.errors){
														    		for (var i=0; i<a.result.errors.length;i++){
														    			msg=msg+a.result.errors[i].title+'\r\n';	
												    				}
												    				Ext.Msg.alert(msg);
											    				}
														    }	
								
							}
						});
					}
				},
				{
					text:'Eliminar'
					,handler:function(){
						Ext.MessageBox.show({
							title:'Mensaje'
							,msg:'Está seguro/a de eliminar el registro?'
							,buttons:{yes:true,no:true}
							,icon:Ext.MessageBox.QUESTION
							,fn:function(btn){
								if(btn=='yes'){
															var conn = new Ext.data.Connection();
															conn.request({
																url:'deletejson',
																method:'POST',
																params:{
																	id:Ext.getCmp('vendedorId').getValue()
																},
																success: function(resp,opt){
																	var respuesta = Ext.decode(resp.responseText);
																	
																	if(respuesta){
																		if(respuesta.loginredirect)
																			window.location='../logout/index';
																		else {
																			if (respuesta.success)
																				Ext.MessageBox.show({
																					title:'Mensaje'
																					,msg:'El registro fue borrado'
																					,icon:Ext.MessageBox.INFO
																					,buttons:Ext.MessageBox.OK
																					,fn:function(btn){
																							window.location='list'
																					}
																				});
																			else	
																				Ext.Msg.show({
																					title:"Error",
																					msg:respuesta.msg,
																					icon:Ext.MessageBox.ERROR,
																					buttons:Ext.MessageBox.OK
																				});
																		}
																	}
																	
																},
																failure: function(resp,opt){
																	Ext.Msg.show({
																			title:'Error',
																			msg:'Se produjo un error al intentar eliminar el registro',
																			icon:Ext.MessageBox.INFO,
																			buttons: Ext.MessageBox.OK
																	});						
																}
															});
								}
							}
						});
					}
				},
				{
					text:'Cancelar'
					,handler:function(){
						window.location='list'
					}
				}
			]
		
	});
	formusuario.load({
		url:'showjson'
		,params:{
			id:usuarioId	
		},
		success:function(f,a){
			
			Ext.getCmp('usuarioId').setValue(a.result.data.username);
			Ext.getCmp('userrealnameId').setValue(a.result.data.userRealName);
			Ext.getCmp('enabledId').setValue(a.result.data.enabled);
			Ext.getCmp('descriptionId').setValue(a.result.data.description);			
			Ext.getCmp('passwdId').setValue(a.result.data.passwd);			
			Ext.getCmp('passwdId').setValue(a.result.data.passwd);			
		}
	});
});