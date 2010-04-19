//http://www.youtube.com/watch?v=8ylGmIBSK78

Ext.onReady(function(){
	Ext.QuickTips.init();
	/*var progressBar = new Ext.ProgressBar({
						height: 15,
						width: 300,
						text:''
					});
	*/
	var mensajeglobal='';
	
	var uploadMask = new Ext.LoadMask(Ext.getBody(),{msg:'Copiando Datos...'}); 
	
	function downloadexcelerrores(idcargaexcel){
		//var conn = new Ext.data.Connection();
		Ext.Ajax.request({
			url:'downloadfileerrors',
			method:'POST',
			params:{
				id:idcargaexcel
			},
			success: function(resp,opt){
				
			},
			failure : function(resp,opt){
				
			}
		});
	}
	
	
	function handleUploadsinTaskMgr(){
			 Ext.Ajax.request({
				form: uploadForm.getForm().getEl().dom,
				url:'upload',
				isUpload: true,
     			//headers: {'Content-type':'multipart/form-data'},
				//headers: {'Content-type':'text/html'},
     			success: function(response,opt){
     				var respuestaJson=Ext.util.JSON.decode(response.responseText)
     				mensajeglobal=respuestaJson.responseText;
     				if(respuestaJson){
     					if (respuestaJson.loginredirect)
     						window.location="../logout/index";
     					else{
			   					if (respuestaJson.success==true){
			     										//Ext.MessageBox.hide();
			   											uploadMask.hide();
			     										if (respuestaJson.cantErrores>0){
			     											Ext.MessageBox.show({
			     												title:'Mensaje',
			     												msg:'El proceso del archivo excel arrojo algunos errores, verifique el link de errores',
			     												icon:Ext.MessageBox.INFO
			     											});
															
			     										}else{
										   					Ext.MessageBox.show({
										   						title:'Mensaje',
										   						msg:'El archivo fue procesado correctamente',
										   						icon: Ext.MessageBox.INFO
										   					});
										   					
			     										}
			   					}else{
			     						
			     							uploadMask.hide();//Ext.MessageBox.hide();	
									   		Ext.MessageBox.show({
									   						title:'Error',
									   						msg:respuestaJson.responseText,
									   						icon: Ext.MessageBox.ERROR
									   		});
									   		
			     				}
			     				if(respuestaJson.showlink)
			     					Ext.getCmp('linkdescargaId').show();
			     				else
			     					Ext.getCmp('linkdescargaId').hide();
     					}
	     					
     				}
     			},
				failure: function(response,opt){
					Ext.MessageBox.show({
						title:"Error",
						msg:"Error en la carga de Excel"
					});	
				}
			});			
		
	}
	
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
     				mensajeglobal=respuestaJson.responseText;
     				if (respuestaJson.success){
     					if (respuestaJson.success==true){
     										Ext.MessageBox.hide();
						   					Ext.MessageBox.show({
						   						title:'Mensaje',
						   						msg:'PASO POR EL TRUE'
						   					});
     					}else{
     						
     							Ext.MessageBox.hide();	
						   		Ext.MessageBox.show({
						   						title:'Error',
						   						msg:respuestaJson.responseText
						   		});

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
						   				//Ext.MessageBox.hide();
						   				Ext.TaskMgr.stop(task);
						   				//Ext.MessageBox.show({
						   					//title:'Mensaje',
						   					//msg:mensajeglobal
						   				//});
						   			}
						   			
						   		}
					   		
					   },
					   failure: function(response, request){
					   		//handle failure
					   	Ext.MessageBox.show({
					   		title:"Error",
					   		msg:"Se produjo un error"
					   		
					   	});
					   }
					});	    
			    
			       
			    },
			    interval: 2000 //check progress every 2 seconds	    
			}
			//Ext.TaskMgr.start(task);
		
			return true;

	}
	

	

		
	
	var uploadForm = new Ext.FormPanel({
	        							url:'upload',
	        							method:'post',
	        							id:'uploadFormId',
	        							frame:true,
	        							renderTo:'formularioupload',
	        							width: 400,
	        							height:200,
	        							fileUpload:true,
	        							title:'Registro de Empresas con Archivo Excel',
	        							items:[
	        									{xtype:'textfield',
	        									 name:'archivoExcel',
	        									 fieldLabel:'Archivo',
	        									 inputType:'file'
	        									
	        									}/*,progressBar*/
	        									,{
	        										xtype:'box',
	        										autoEl:{tag: 'a', href: patherroresapp, target:'_blank', html: 'Descargar Errores'},
	        										hidden:true,
	        										id:'linkdescargaId'
	        									}
	        									
	        								],
	        							buttons:[
	        								{text:'Subir Archivo',
	           								 id:'uploadFormId',
	        								 handler: function(){
	        								 	//progressBar.updateProgress(0);
	        								 	var conn = new Ext.data.Connection();
	        								 	conn.request({
	        								 		url:'uploadtest',
	        								 		method:'POST',
	        								 		success:function(resp,opt){
	        								 			var respuesta = Ext.decode(resp.responseText);
	        								 			if(respuesta)
	        								 				if(respuesta.loginredirect){
	        								 					window.location="../logout/index";
	        								 				}else{
	        								 					if(respuesta.success==true){
						        								 	/*Ext.MessageBox.show({
						        								 		title:'Copiando datos',
						        								 		msg:'Espere mientras se procesan los datos',
						        								 		icon: 'wait_icon'
						        								 	});*/
	        								 						uploadMask.show();
						        								 	handleUploadsinTaskMgr();
	        								 					}else
	        								 						Ext.MessageBox.show({
	        								 							title:"Mensaje",
	        								 							msg:"Se produjo un error desconocido en el proceso del archivo",
	        								 							icon: Ext.MessageBox.ERROR
	        								 							
	        								 						});
	        								 				}
	        								 				
	        								 		},
	        								 		failure:function(resp,opt){
	        								 			
	        								 		}
	        								 	});
	        								 	
	        								 	
	        								 }
	        								}
	        							]	
	        							});

		
			
	});

