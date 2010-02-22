package com.adeptiva.commons.upload

import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.apache.commons.fileupload.FileUpload
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.ProgressListener
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartException

import javax.servlet.http.HttpServletRequest

class AjaxMultipartResolver extends CommonsMultipartResolver{
	
	private ProgressListener pListener
	private HttpServletRequest request
	
	public void setProgressListener(ProgressListener p){
		this.pListener = p
	}
	
	public ProgressListener getProgressListener(){
		
		return this.pListener
		
	}
	
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
    														throws MultipartException{
		
	    try{
    		this.request = request
    		
    		println("resolving multipart...")
    		
    		return super.resolveMultipart(request)

	    }catch(Exception ex){
	        //exception is typically thrown when user hits stop or cancel
	        //button halfway through upload
	        this.pListener?.updateStatus(AjaxProgressListener.STATUS_FAILED)
	        throw(ex)
	    }
		
	}
	
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		
		FileUpload fu = super.newFileUpload(fileItemFactory)
		
		if (this.pListener != null){
			this.pListener.session = this.request.session
			fu.setProgressListener(this.pListener)
		}
		
		this.pListener?.updateStatus(AjaxProgressListener.STATUS_UPLOADING)
		
		return fu
	}	
	
}