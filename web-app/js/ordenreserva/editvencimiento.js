Ext.onReady(function(){
	Ext.QuickTips.init();
        var vencForm = new Ext.FormPanel({
            url:'updatevencjson'
            ,id:'vecFormId'
            ,renderTo:'formulario_extjs'
            ,frame:true
            ,title:'Modificar Vencimiento'
            ,height:200
            ,width:450
            ,items:[
                {
                    xtype:'textfield',
                    id:'vencId',
                    hidden:true,
                    name:'vencimientoId',
                    fieldLabel:'Id de Venc.'
                },
                {
                    xtype:'numberfield',
                    id:'numeroOrdenId',
                    name:'numeroOrden',
                    disabled:true,
                    fieldLabel:'N° de Orden'
                },
                {
                    xtype:'textfield',
                    id:'sectorId',
                    name:'sector',
                    disabled:true,
                    fieldLabel:'Sector'
                },
                
                {
                    xtype:'textfield',
                    id:'loteId',
                    name:'lote',
                    disabled:true,
                    fieldLabel:'Lote'
                },
                {
                    xtype:'numberfield',
                    id:'porcentajeId',
                    name:'porcentaje',
                    disabled:true,
                    fieldLabel:'Porcentaje'
                },
                
                {
                    xtype:'datefield',
                    id:'vencimientoId',
                    name:'vencimiento',
                    alloBlank:false,
                    fieldLabel:'Vencimiento'
                }
            ],buttons:[
                {
                    text:'Guardar',
                    handler:function(){
                        vencForm.getForm().submit({
                            success: function(f,a){   
                                Ext.Msg.alert('Mensaje','Los datos se guardaron correctamente',
					function(btn,text){
						if(btn=='ok'){
							window.location='listvencdescuentos?id='+ordenId;
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
                                                Ext.Msg.alert(msg);
                                        }
                                    }	


                            }
                                    
                        });
                    }
                },{
                    text:'Cancelar',
                    handler:function(){
                        window.location='listvencdescuentos?id='+ordenId;
                    }
                }
            ]
        });
        vencForm.load({
            url:'editvencjson'
            ,params:{
                id:idVencimiento
            },
            success:function(f,a){
                Ext.getCmp('vencId').setValue(a.result.data.id);
                Ext.getCmp('numeroOrdenId').setValue(a.result.data.numeroOrden);
                Ext.getCmp('vencimientoId').setValue(a.result.data.vencimiento);
                Ext.getCmp('porcentajeId').setValue(a.result.data.porcentaje);
                Ext.getCmp('sectorId').setValue(a.result.data.sector);
                Ext.getCmp('loteId').setValue(a.result.data.lote);
                
            }
        });
});