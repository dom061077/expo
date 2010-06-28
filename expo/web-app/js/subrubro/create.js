Ext.onReady(function(){
	Ext.QuickTips.init();
	var rubroStore = new Ext.data.JsonStore({
		url:'../rubro/listrubrojson'
		,fields:['id','nombreRubro']
		,root:'rows'
		,totalProperty:'total'
	});
	
	rubroStore.load();
	
	var subrubroForm = new Ext.FormPanel({
		url:'savejson',
		id:'subrubroFormId',
		border:false,
		renderTo:'formulario_extjs',
		frame:true,
		title:'Alta de Sub Rubro',
		height:200,
		width:400,
		items:[
				{
					xtype:'combo'
					,fieldLabel:'Rubro'
					,name:'rubro'
					,id:'rubroId'
					,allowBlank:false
					,forceSelection:true
					,width:260
					,mode:'local'
					,store:rubroStore
					,displayField:'nombreRubro'
					,valueField:'id'
					,hiddenName:'rubro.id'
					,msgTarget:'under'
				},{
					 xtype:'textfield'
					 ,fieldLabel:'Nombre'
					 ,id:'nombresubrubroid'
					 ,name:'nombreSubrubro'
					 ,allowBlank:false
					 ,msgTarget:'under'
					 ,width:260
				}
		],
		buttons:[
				{
					text:'Guardar'
					,handler:function(){
						subrubroForm.getForm().submit({
								success: function(f,a){
									Ext.MessageBox.show({
										title:'Mensaje'
										,msg:'Los datos se guardaron correctamente'
										,icon:Ext.MessageBox.INFO
										,buttons:Ext.MessageBox.OK
										,fn:function(btn){
											window.location='create';
										}
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