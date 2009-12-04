<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Cambiar Password</title>
        <script type="text/javascript">
        	Ext.onReady(function(){
        			Ext.QuickTips.init();
        			var formPssw = new Ext.FormPanel({
        					url:'../login/updatepssw',
        					id:'formPsswId',
        					frame:true,
        					width:400,
        					height:150,
        					items:  [   {xtype: 'textfield',
	        							id:  'passwordId',
	        							name:'password',
	        							width:200},{
        								xtype: 'textfield',
        								id:  'newpasswordId',
        								name:'newpassword',
        								width:200},{
        								xtype: 'textfield',
        								id: 'retypenewpasswordId',
        								name: 'retypenewpassword',
        								width:200}
        							]
        				})
        	});
        </script>
	</head>
	
	<body>
	
	</body>

</html>