//http://www.youtube.com/watch?v=8ylGmIBSK78

Ext.onReady(function(){
	Ext.QuickTips.init();
	/*var progressBar = new Ext.ProgressBar({
						height: 15,
						width: 300,
						text:''
					});
	*/

	function handleUpload(){

			//start a TaskRunner which will fetch progress updates
			 Ext.Ajax.request({
				form: uploadForm.getForm().getEl().dom,
				url:'upload',
				isUpload: true,
     			//headers: {'Content-type':'multipart/form-data'},
				//headers: {'Content-type':'text/html'},
     			success: function(response,opt){
     				var respuestaJson=Ext.util.JSON.decode(response.responseText)
     				if (respuestaJson.success){
     					Ext.Msg.show({
     						title:'Resultado de la carga',
     						msg:respuestaJson.responseText,
     						icon: Ext.Msg.INFO,
     						buttons: Ext.Msg.OK
     					});
     					if (respuestaJson.success==true){
     						var conn = new Ext.data.Connection();
     						conn.request({
     							url:'list',
     							method:'POST',
     							params:{
     								//nombrearchivo:respuestaJson.nombrearchivo
     							},
     							success:function(resp,opt){
     								
     							},
     							failure:function(resp,opt){
     								
     							}
     						});
     					}else{
     						//no hacer nada
     					}
     				}
     			}

			});			
			
			//create our progressbar
					
			//progressBar.show();
			var task = {
				
			    run: function(){
					Ext.Ajax.request({
					   url: 'uploadInfo',
					   success: function(response, request){
					   		
					   		var obj = Ext.util.JSON.decode(response.responseText);
					   		
					   		//Our JSON responseText will look like the following 
					   		//{"bytesRead":2235712,"totalSize":368380996,"status":"FAILED"}
					   		//progressBar.updateProgress(obj.bytesRead/obj.totalSize);
					   		//progressBar.updateText('Subiendo Archivo...');
					   		
					   		//If we are done or upload failed, stop running this
					   		//task
					   		if (obj.status === "FAILED")
					   			Ext.TaskMgr.stop(task);
					   		else	
						   		if (obj.status === "DONE"){
							   		//progressBar.updateProgress(obj.salvados/obj.total);
							   		//progressBar.updateText('Copiando archivo...');
						   			if (obj.total>=obj.salvados){
						   				//progressBar.updateText('Copia de archivo completada');
						   				Ext.MessageBox.hide();
						   				Ext.TaskMgr.stop(task);
						   			}
						   			
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
	        							fileUpload:true,
	        							items:[
	        									{xtype:'textfield',
	        									 name:'archivoExcel',
	        									 fieldLabel:'Archivo',
	        									 inputType:'file'
	        									
	        									}/*,progressBar*/
	        								],
	        							buttons:[
	        								{text:'Subir Archivo',
	           								 id:'uploadFormId',
	        								 handler: function(){
	        								 	//progressBar.updateProgress(0);
	        								 	Ext.MessageBox.show({
	        								 		title:'Copiando datos',
	        								 		msg:'Espere mientras se procesan los datos',
	        								 		icon: Ext.MessageBox.INFO
	        								 	});
	        								 	handleUpload();
	        								 	
	        								 }
	        								}
	        							]	
	        							});

		
			
	});

