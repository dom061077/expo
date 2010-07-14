Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var exposicionForm = new Ext.form.FormPanel({
		url:'updatejson',
		id:'exposicionformId',
		frame:true,
		title:'Modificación de Exposición',
		width:400,
		height:300,
		fileUpload:true,
		renderTo:'formulario_extjs',
		items:[
			{
				xtype:'textfield',
				id:'exposicionId',
				name:'id',
				hidden:true,
				value:exposicionId,
				hideLabel:true
			},{
				xtype:'textfield',
				name:'nombre',
				id:'nombreId',
				allowBlank:false,
				msgTarget:'under',
				fieldLabel:'Nombre'
			},{
				xtype:'box',
				id:'imagelogoId',
				anchor:'',
				isFormField:false,
				fieldLabel:'Logo cargado',
				clearInvalid : Ext.emptyFn,
				//validate: Ext.emptyFn,
        		markInvalid : Ext.emptyFn,
        		isValid : function () { return true; },
				autoEl:{
					 tag:'div', children:[{
					 tag:'img'
					 //,qtip:'You can also have a tooltip on the image'
					 ,src:''
					 },{
					 tag:'div'
					 ,style:'margin:0px 0px 0px 0px; position: fixed;'
					 //,html:'Logo actual de la expo'
					 }]					
				}
			},{
				xtype:'textfield',
				name:'image',
				inputType:'file',
				allowBlank:false,
				msgTarget:'under',
				fieldLabel:'Logo de Expo'
			}
		
		],
		buttons:[
			{
				text:'Guardar',
				handler: function(){
					exposicionForm.getForm().submit({
						//url:'savejson',
						//waitMsg:'Guardando Exposiciósn',
						success: function(f,a){
							Ext.MessageBox.show({
								title:'Mensaje',
								msg:'La Exposicion se guardo correctamente',
								buttons:Ext.MessageBox.OK,
								fn: function(){
									window.location='list';
								}
							});
						},
						failure: function(f,a){
							Ext.MessageBox.show({
								title:'Error',
								msg:a.result.msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					});
				}
			},{
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
																url:'delete',
																method:'POST',
																params:{
																	id:Ext.getCmp('exposicionId').getValue()
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
				},{
					text: 'Cancelar',
					handler: function(){
						window.location='list';
						//alert(imagePath+'pdf.gif');
						//Ext.getCmp('imagelogoId').el.dom.firstChild.src=imagePath+'/pdf.gif'
					}
				}				
			
		]
	});
	exposicionForm.load({
		url:'showjson'
		,params:{
			id:exposicionId	
		},
		success: function(f,a){
			Ext.getCmp('nombreId').setValue(a.result.data.nombre);
			Ext.getCmp('imagelogoId').el.dom.firstChild.src=a.result.pathfile;
		},
		failure: function (thisform,action){
			alert(action);
		}
    					
	});
	
});
