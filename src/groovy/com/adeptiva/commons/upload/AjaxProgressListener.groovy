package com.adeptiva.commons.upload

import org.apache.commons.fileupload.ProgressListener
import javax.servlet.http.HttpSession

class AjaxProgressListener implements ProgressListener {
	
    static final String STATUS_UPLOADING = "UPLOADING"
    static final String STATUS_FAILED = "FAILED"
    static final String STATUS_DONE = "DONE"
    
	private HttpSession session
	
	public setSession(HttpSession session){
		this.session = session	
	}
	
	void updateStatus(String status){
	    session.setAttribute("progressStatus", status)
	}
	
	void update(long pBytesRead, long pContentLength, int pItems){
		
		session.setAttribute("progressMap", ["bytesRead": pBytesRead, "length" : pContentLength, "items" : pItems])
		println("DEBUG UPLOAD: ${pBytesRead} : ${pContentLength} : ${pItems}")
		
		if (pBytesRead == pContentLength){
		    session.setAttribute("progressStatus", STATUS_DONE) 
		}
	
	}
}