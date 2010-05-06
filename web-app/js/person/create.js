Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var formusuario = new Ext.form.FormPanel({
		url:'savejson',
		id:'formusuarioId',
		border:false,
		renderTo:'formulario_extjs',
		frame:true,
		title:'Alta de Usuario',
		height:450,
		width:450,
		items:[
				{xtype:'textfield',
				 id:'usernameId',
				 name:'username',
				 fieldLabel:'Nombre de Usuario'
				},{
				 xtype:'textfield',
				 id:'userrealnameId',
				 name:'userRealName',
				 fieldLabel:'Nombre Real'
				},{
				 xtype:'checkbox',
				 id:'enabledId',
				 name:''
				}
		]
		
	});
	
});