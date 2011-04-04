package com.rural.utils


import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass;
import java.util.StringTokenizer;

class GUtilDomainClass{
	def domainClass
	def params
	def field
	def criteria

	GUtilDomainClass(def domainClass,def params){
		this.domainClass=domainClass
		this.params=params
	}
	
	private def getDomainClassFieldValue(Class definition,String field,String value){
		DefaultGrailsDomainClass obj = new DefaultGrailsDomainClass(definition);
		
		return obj.getPropertyByName(field).getType().newInstance(value);
	}
	
	static final String		EQ="eq";
	static final String		NOTEQUAL="ne";
	static final String 	LESSTHAN="lt";
	static final String 	LESSTHANEQUALS="le";
	static final String 	GREATERTHAN="gt";
	static final String 	GREATERTHANEQUALS="ge";
	static final String		ISIN="in";
	static final String		ISNOTIN="ni";
	static final String		CONTAINS="cn";
	static final String		DOESNOTCONTAIN="nc";
	
	
	
	
	
	
	private String criteriaSearch(String cr){
		if(cr!=null){
			if(EQ.toUpperCase().equals(cr.toUpperCase()))
				return "eq";
			if(NOTEQUAL.toUpperCase().equals(cr.toUpperCase()))
				return "ne";
			if(LESSTHAN.toUpperCase().equals(cr.toUpperCase()))
				return "lt";
			if(LESSTHANEQUALS.toUpperCase().equals(cr.toUpperCase()))
				return "le";
			if(GREATERTHAN.toUpperCase().equals(cr.toUpperCase()))
				return "gt";
			if(GREATERTHANEQUALS.toUpperCase().equals(cr.toUpperCase()))
				return "ge";
			if(ISIN.toUpperCase().equals(cr.toUpperCase()))
				return "in";
			if(ISNOTIN.toUpperCase().equals(cr.toUpperCase()))
				return "in";
			
			if(CONTAINS.toUpperCase().equals(cr.toUpperCase()) || DOESNOTCONTAIN.toUpperCase().equals(cr.toUpperCase()))
				return "ilike";
			
				
		}	
		return "";
		
	}
	
	def list(){
		//log.info "INGRESANDO AL METODO list DE LA CLASE GUtilClass"
		DefaultGrailsDomainClass dc = new DefaultGrailsDomainClass(domainClass);
		def list=[]
    	def pagingConfig = [
    		max: Integer.parseInt(params.rows),
    		offset: Integer.parseInt(params.page)-1
    	] 
		def searchOper
		def searchValue
		if((Boolean.parseBoolean(params._search))){
			searchOper = criteriaSearch(params.searchOper)
			try{
				if(params.searchOper.equals(ISIN) || params.searchOper.equals(ISNOTIN)){
					def st = new StringTokenizer(params.searchString,",")
					searchValue=[]
					st.each{
						searchValue.add(
								dc.getPropertyByName(params.searchField).getType().newInstance(it)
							)	
					}
				}else
					searchValue = dc.getPropertyByName(params.searchField).getType().newInstance(params.searchString)
			}catch(Exception e){
				list
				return list
			}
			list = domainClass.createCriteria().list(pagingConfig){
					if(params.searchOper.equals(CONTAINS) || searchOper.equals(DOESNOTCONTAIN) )
						searchValue="%"+searchValue+"%"

					//log.debug "OPERACION DE BUSQUEDA: "+searchOp+" VALOR DE BUSQUEDA: $searchValue"				
					if(params.searchOper.equals(DOESNOTCONTAIN)||params.searchOper.equals(ISNOTIN)){
						//log.debug "NEGACION DE LA BUSQUEDA LIKE"
						not {
							"${searchOper}"(params.searchField,searchValue)
						}
					}else{
						//if(params.searchOper.equals(ISIN) || params.searchOper.equals(ISNOTIN))
						//	searchOper="'"+searchOper+"'"
						
						"${searchOper}"(params.searchField,searchValue)
					}	
					
					order(params.sidx,params.sord)
				}
		}else{
			list=domainClass.createCriteria().list(pagingConfig){
					order(params.sidx,params.sord)
				}
		}
		return list
		
	}

}