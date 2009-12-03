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
        					items: [{
	        							xtype: 'textfield',
	        							id:'passwdId',
	        							name:'passwd',
	        							width:200
        							{
        								xtype: 'text'
        							
        							}
        						]
        					
        				})
        	});
        </script>
	</head>
	
	<body>
	
	</body>

</html>