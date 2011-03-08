Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var formLogo = new Ext.form.FormPanel({
		url:'savejson',
		title:"Alta de Logo",
		renderTo:'formulario_extjs',
		width:470,
		fileUpload:true,
		frame:true,
		items:[
			{
				xtype:'textfield',
				id:'exposicionId',
				name:'expo.id',
				value:exposicionId,
				hidden:true,
				hideLabel:true
			},{
				xtype:'textfield',
				id:'exposicionNombreId',
				name:'exposicionNombre',
				value:exposicionNombre,
				fieldLabel:'Exposición',
				disabled:true
			},{
				xtype:'numberfield',
				id:'anioId',
				name:'anio',
				msgTarget:'under',
				fieldLabel:'Año',
				maxLength:4,
				allowBlank:false
				
			},{
				xtype:'textfield',
				id:'imageId',
				name:'image',
				inputType:'file',
				msgTarget:'under',
				fieldLabel:'Logo',
				allowBlank:false
			}
		],
		buttons:[
			{
				text:'Guardar',
				id:'guardarId',
				handler:function(){
					formLogo.getForm().submit({
						success: function(f,a){
							if(a.result.success){
								Ext.MessageBox.show({
								title:'Mensaje',
								msg:a.result.msg,
								icon:Ext.MessageBox.INFO,
								buttons:Ext.MessageBox.OK,
								fn:function(btn){
									window.location=list
								}
							});
							}else{
								Ext.MessageBox.show({
								title:'Error',
								msg:a.result.msg,
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
							}
							
						},
						failure: function(f,a){
							Ext.MessageBox.show({
								title:'Error',
								msg:'Ocurrió un error, intente más tarde',
								icon:Ext.MessageBox.ERROR,
								buttons:Ext.MessageBox.OK
							});
						}
					});
				}
			},{
				text: 'Cancelar',
				handler: function(){
					window.location='list?expoid='+exposicionId+'&exponombre='+exposicionNombre
				}
			}
		]
	});
	Ext.getCmp('anioId').focus('',10);
	
});