Ext.onReady(function(){
	Ext.QuickTips.init();
	var progressBar = new Ext.ProgressBar({
						height: 15,
						width: 300,
						text:''
					});
	

	function handleUpload(){

			//start a TaskRunner which will fetch progress updates
			 Ext.Ajax.request({
				form: uploadForm.getForm().getEl().dom,
				url:'upload',
				isUpload: true,
     			headers: {'Content-type':'multipart/form-data'}

			});			
			
			//create our progressbar
					
			progressBar.show();
			var task = {
				
			    run: function(){
					Ext.Ajax.request({
					   url: 'uploadInfo',
					   success: function(response, request){
					   		
					   		var obj = Ext.util.JSON.decode(response.responseText);
					   		
					   		//Our JSON responseText will look like the following 
					   		//{"bytesRead":2235712,"totalSize":368380996,"status":"FAILED"}
					   		progressBar.updateProgress(obj.bytesRead/obj.totalSize);
					   		progressBar.updateText('Subiendo Archivo...');
					   		
					   		//If we are done or upload failed, stop running this
					   		//task
					   		if (obj.status === "FAILED" ||
					   			obj.status === "DONE"){
					   			Ext.TaskMgr.stop(task);
					   			//window.location = 'uploadedFile';
					   		}
					   		
					   },
					   failure: function(response, request){
					   		//handle failure
					   }
					});	    
			    
			       
			    },
			    interval: 2000 //check progress every 2 seconds	    
			}
			Ext.TaskMgr.start(task);
		
			return true;

	}	
	
	var uploadForm = new Ext.FormPanel({
	        							url:'upload',
	        							method:'post',
	        							id:'uploadFormId',
	        							frame:true,
	        							renderTo:'formularioupload',
	        							width: 400,
	        							height:150,
	        							items:[
	        									{xtype:'textfield',
	        									 name:'archivoExcel',
	        									 fieldLabel:'Archivo',
	        									 inputType:'file'
	        									
	        									},progressBar
	        								],
	        							buttons:[
	        								{text:'Subir Archivo',
	           								 id:'uploadFormId',
	        								 handler: function(){
	        								 	handleUpload();
	        								 }
	        								}
	        							]	
	        							});

		
			
	});

