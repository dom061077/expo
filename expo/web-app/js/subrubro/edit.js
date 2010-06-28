Ext.onReady(function (){
	Ext.QuickTips.init();
	
	var rubroStore = new Ext.data.JsonStore({
		url:'../rubro/listrubrojson'
		,fields:['id','nombreRubro']
		,root:'rows'
		,totalProperty:'total'
	});
	
	rubroStore.load();	
	var subrubroForm = new Ext.FormPanel({
		url:'updatejson'
		,id:'subrubroFormId'
		,border:false
		,renderTo:'formulario_extjs'
		,frame:true
		,title:'Modificar Sub Rubro'
		,height:200
		,width:450
		,items:[
				{
					xtype:'textfield'
					,hideLabel:true
					,id:'subrubroId'
					,name:'id'
					,hidden:true
				},{
					xtype:'combo'
					,fieldLabel:'Rubro'
					,disabled:true
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
					,id:'nombresubrubroid'
					,name:'nombreSubrubro'
					,allowBlank:false
					,fieldLabel:'Nombre'
					,msgTarget:'under'
					,width:200
				}
		]			,buttons:[
				{	text:'Guardar'
					,handler:function(){
						subrubroForm.getForm().submit({
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
																	id:Ext.getCmp('subrubroId').getValue()
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
	
	Ext.getCmp('nombresubrubroid').focus();
	subrubroForm.load({
		url:'showjson'
		,params:{
			'id':subrubroId
		},
		success:function(f,a){
			//Ext.getCmp('nombrerubroid').setValue(a.result.data.nombreRubro);
		}
	});
	


});