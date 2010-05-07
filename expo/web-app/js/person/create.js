Ext.onReady(function(){
	Ext.QuickTips.init();
	
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
				 fieldLabel:'Usuario'
				},{
				 xtype:'textfield',
				 id:'userrealnameId',
				 name:'userRealName',
				 fieldLabel:'Nombre Real'
				},{
				 xtype:'checkbox',
				 id:'enabledId',
				 name:'enabled',
				 fieldLabel:'Habilitado'
				},{
					xtype:'textfield',
					id:'descriptionId',
					name:'description',
					fieldLabel:'Descripción'
				},{
					xtype:'textfield',
					inputType:'password',
					id:'passwdId',
					name:'passwd',
					fieldLabel:'Contraseña'
				}
		],
	  buttons:[
	           {
	        	   text:'Guardar',handler:function(){
	        	   		
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