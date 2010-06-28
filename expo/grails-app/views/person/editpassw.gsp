<html>
	<head>
		<% 
			out << "<script type='text/javascript'>";
			out << "var userId = ${id};";
			

			out << "</script>";
		%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Cambiar Password</title>
        <script type="text/javascript">
        	Ext.onReady(function(){
        			Ext.QuickTips.init();
        			var formPssw = new Ext.FormPanel({
        					url:'updatepsswJson',
        					id:'formPsswId',
        					title:'Cambio de Contraseña',
        					renderTo:'formpassword',
        					frame:true,
        					width:400,
        					height:250,
        					items:  [   {
				   							 xtype: 'textfield',
											 id:'usernamedisabledId',
											 fieldLabel:'Usuario',
											 name:'usernamedisabled',
											 disabled:true,
											 width:200
											
        								},{
	           								 xtype:'hidden',
	        								 id:'usernameId',
	        								 name:'username',
											 hideLabel:true
											 	
            							},{
                							xtype: 'textfield',
		        							id:  'passwordId',
		        							fieldLabel:'Contraseña anterior',
		        							name:'password',
											inputType: 'password',
											maxLength:15,
											minLength:6,										 
											msgTarget:'under',
		        							width:200,
		        							msgTarget:'under',
		        							allowBlank:false,
		        							readOnly:false
		        						},{
	        								xtype: 'textfield',
	        								id:  'newpasswordId',
	        								fieldLabel:'Nueva Contraseña',
	        								name:'newpassword',
	        								allowBlank:false,
	        								msgTarget:'under',
											minLength:6,
											maxLength:15,
											msgTarget:'under',	        								
											inputType: 'password',
	        								width:200
        								},{
	        								xtype: 'textfield',
	        								fieldLabel:'Repita Contraseña',
	        								inputType:'password',
	        								id: 'newpasswordrepeatId',
	        								name: 'newpasswordrepeat',
	        								allowBlank:false,
											minLength:6,
											maxLength:15,
											msgTarget:'under',	 
											minLength:6,
											maxLength:15,
											msgTarget:'under',        								
	        								width:200
        								}
        							],
        					buttons:[{text: 'Cambiar',
        							  handler: function(){
        							  			formPssw.getForm().submit({
        							  				success: function(f,a){
        							  						if(a.result.success){
        							  							if(a.result.denegado)
        							  								Ext.Msg.show({
        							  									title:'Mensaje',
        							  									msg:'Acceso DENEGADO, consulte con su Administrador',
        							  									icon:Ext.MessageBox.INFO,
        							  									buttons:Ext.MessageBox.OK,
        							  									fn:function(btn){
        							  										
        							  									}
        							  								});
        							  							else	
		        							  						Ext.Msg.show({
		            							  						title:'Mensaje',
		            							  						msg:'Su contraseña fue modificada con éxito',
		            							  						buttons: Ext.MessageBox.OK,
		            							  						fn: function(btn){
		            							  							window.location='../';
		        							  							}
		        							  						});
        							  						}
        							  						
        							  					},
        							  				failure: function(f,a){
					                    					var msg="";
														    if (a.failureType==Ext.form.Action.CONNECT_FAILURE ||
															    	a.failureType==Ext.form.Action.SERVER_INVALID){
															    		Ext.Msg.alert('Error','El servidor no Responde')
															    	}
					                    					
					                    					if (a.result){
														    	if (a.result.loginredirect==true){
														    		Ext.Msg.show({
															    		title:'Mensaje',
																		msg:'Su sesion de usuario a caducado, ingrese nuevamente',
																		buttons: Ext.MessageBox.OK,																		
														    			icon: Ext.MessageBox.WARNING,
														    			fn: function(btn){
														    				window.location='../logout/index';
														    			}
														    			
														    		});
														    		
														    	}
						                    					
														    	if (a.result.errors){
														    		 for (var i=0; i<a.result.errors.length;i++){
														    			msg=msg+a.result.errors[i].title+'\r\n';	
												    				}
																	Ext.Msg.show({
																		title:'Errores',
																		msg:msg,
																		icon: Ext.MessageBox.ERROR	
																	});	
												    				
											    				}
					                    					}
        							  					}
        							  			});	
        							  		}
        							 }
        					]		
        				});
    				formPssw.load({
        				url:'editpasswJson',
        				params: {
        					'id': userId
    					},
    					success: function(f,a){
    						//var jsonstruct=a.response.responseText;
    						//var jsonresult=eval('('+jsonstruct+')');
    						Ext.getCmp('usernameId').setValue(a.result.data.username);
    						Ext.getCmp('usernamedisabledId').setValue(a.result.data.username);
    					}
        			});
        			Ext.getCmp('passwordId').focus('',10);
        	});
        </script>
        
	</head>
	
	<body>
		
		<div id="formpassword">
		</div>
	</body>



</html>