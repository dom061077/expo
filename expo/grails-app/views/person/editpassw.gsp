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
        					url:'../login/updatepssw',
        					id:'formPsswId',
        					renderTo:'formpassword',
        					frame:true,
        					width:400,
        					height:150,
        					items:  [   {xtype: 'textfield',
										 id:'usernameId',
										 fieldLabel:'Usuario',
										 name:'username',
										 width:200,
										 readOnly:false
            							},        	        					
        	        					{xtype: 'textfield',
	        							id:  'passwordId',
	        							fieldLabel:'Contraseña anterior',
	        							name:'password',
	        							width:200,
	        							readOnly:false},{
        								xtype: 'textfield',
        								id:  'newpasswordId',
        								fieldLabel:'Nueva Contraseña',
        								name:'newpassword',
        								width:200},{
        								xtype: 'textfield',
        								fieldLabel:'Repita Nueva Contraseña',
        								id: 'retypenewpasswordId',
        								name: 'retypenewpassword',
        								width:200}
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
        	});
        </script>
        
	</head>
	
	<body>
		
		<div id="formpassword">
		</div>
	</body>



</html>