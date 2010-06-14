Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var exposicionForm = new Ext.FormPanel({
		url:'savejson',
		method:'post',
		id:'exposicionformId',
		width:400,
		height:300,
		fileUpload:true,
		title:'Alta de Exposición',
		frame:true,
		renderTo:'formulario_extjs',
		items:[
			{
				xtype:'textfield',
				name:'nombre',
				id:'nombreId',
				allowBlank:false,
				msgTarget:'under',
				fieldLabel:'Nombre'
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
								fn:function(btn){
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
				text: 'Cancelar',
				handler: function(){
					window.location='list'
				}
			}
		]
	});
	Ext.getCmp('nombreId').focus('',10);
});