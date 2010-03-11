Ext.onReady(function(){
	Ext.QuickTips.init();
	var rubroForm = new Ext.FormPanel({
		url:'savejson',
		id:'rubroFormId',
		border:false,
		renderTo:'formulario_extjs',
		frame:true,
		title:'Alta de Rubro',
		height:200,
		width:400,
		items:[
				{xtype:'textfield'
				 ,id:'nombrerubroid'
				 ,name:'nombreRubro'
				 ,allowBlank:false
				 ,msgTarget:'under'
				 ,width:260
				}
		],
		buttons:[
				{
					text:'Guardar'
					,handler:function(){
						rubroForm.getForm().submit({
								success: function(f,a){
									Ext.MessageBox.show({
										title:'Mensaje'
										,msg:'Los datos se guardaron correctamente'
										,icon:Ext.MessageBox.INFO
									});
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
												    				Ext.MessageBox.show({
																		title:'Error'
																		,msg:msg
																		,icon:Ext.MessageBox.ERROR
																		,buttons:Ext.MessageBox.OK
												    				});
											    				}
														    }	
								}
						});
					}
				},{
					text:'Cancelar'
					,handler: function(){
						
						window.location='list';
					}
				}
		]
	});
	
});