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
        					renderTo:'formpassword',
        					frame:true,
        					width:400,
        					height:200,
        					items:  [   {xtype: 'textfield',
										 id:'usernameId',
										 fieldLabel:'Usuario',
										 name:'username',
										 width:200,
										 readonly:true
            							},        	        					
        	        					{xtype: 'textfield',
	        							id:  'passwordId',
	        							fieldLabel:'Contraseña anterior',
	        							name:'password',
										inputType: 'password',
	        							width:200,
	        							readOnly:false},{
        								xtype: 'textfield',
        								id:  'newpasswordId',
        								fieldLabel:'Nueva Contraseña',
        								name:'newpassword',
										inputType: 'password',
        								width:200},{
        								xtype: 'textfield',
        								fieldLabel:'Repita Nueva Contraseña',
        								id: 'newpasswordrepeatId',
        								name: 'newpasswordrepeat',
        								width:200}
        							],
        					buttons:[{text: 'Cambiar',
        							  handler: function(){
        							  			formPssw.getForm().submit({
        							  				success: function(f,a){
        							  					},
        							  				failure: function(f,a){
					                    					var msg="";
					                    					if (a.result)
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
    						var jsonstruct=a.response.responseText;
    						var jsonresult=eval('('+jsonstruct+')');
    						Ext.getCmp('usernameId').setValue(jsonresult.data[0].username);
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