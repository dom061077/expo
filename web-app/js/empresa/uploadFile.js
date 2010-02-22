Ext.onReady(function(){

	var btnUpload = Ext.get("btnUpload");
	btnUpload.on("click", handleUpload);

});


function handleUpload(){

	//start a TaskRunner which will fetch progress updates
	Ext.Ajax.request({
		form: "frmFiles",
		isUpload: true
	});			
	
	//create our progressbar
	var progressBar = new Ext.ProgressBar({
				applyTo: "progressBar",
				height: 15,
				width: 300
			});
			
	progressBar.show();
	var task = {
		
	    run: function(){
			Ext.Ajax.request({
			   url: '/filebag/storage/uploadInfo',
			   success: function(response, request){
			   		
			   		var obj = Ext.util.JSON.decode(response.responseText);
			   		
			   		//Our JSON responseText will look like the following 
			   		//{"bytesRead":2235712,"totalSize":368380996,"status":"FAILED"}
			   		progressBar.updateProgress(obj.bytesRead/obj.totalSize);
			   		
			   		//If we are done or upload failed, stop running this
			   		//task
			   		if (obj.status === "FAILED" ||
			   			obj.status === "DONE"){
			   			Ext.TaskMgr.stop(task);
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