Ext.onReady(function(){
	Ext.QuickTips.init();
	var vendedorForm = new Ext.FormPanel({
		url:'savejson',
		id:'vendedorFormId',
		border:false,
		renderTo:'formulario_extjs',
		frame:true,
		title:'Alta de Vendedor',
		height:200,
		width:400,
		items:[
				{xtype:'textfield'
				 ,id:'nombreid'
				 ,name:'nombre'
				 ,fieldLabel:'Nombre'
				 ,allowBlank:false
				 ,msgTarget:'under'
				 ,width:260
				}
		
		],
		buttons:[
			{
				text: 'Guardar',
				handler: function(){
					vendedorForm.getForm().submit({
						success: function(f,a){
							Ext.MessageBox.show({
								title:'Mensaje'
								,msg:'Los datos se guardaron correctamente'
								,buttons:Ext.MessageBox.OK
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
												    				Ext.Msg.alert(msg);
											    				}
														    }	
							
						}
					});
				}
			},
			{
				text:'Cancelar',
				handler: function(){
					
				}
			}
			
		]
	});	
});