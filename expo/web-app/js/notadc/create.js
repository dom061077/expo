Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var formNotadc = new Ext.form.FormPanel({
		url:'savejson',
		renderTo:'formulario_extjs',
		width:470,
		title:'Alta de Nota',
		frame:true,
		items:[
		       {
		    	   xtype:'hidden',
		    	   name:'ordenReserva.id',
		    	   allowBlank:true,
		    	   id:'ordenReservaId'
		       },{
		    	   xtype:'textfield',
		    	   name:'monto',
		    	   allowBlank:false,
		    	   id:'montoId',
		    	   fieldLabel:'Monto',
		    	   msgTarget:'under'
		       }
		]
	});
	
});