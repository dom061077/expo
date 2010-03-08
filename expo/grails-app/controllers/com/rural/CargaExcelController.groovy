

package com.rural

class CargaExcelController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ cargaExcelInstanceList: CargaExcel.list( params ), cargaExcelInstanceTotal: CargaExcel.count() ]
    }

    def show = {
        def cargaExcelInstance = CargaExcel.get( params.id )

        if(!cargaExcelInstance) {
            flash.message = "CargaExcel not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ cargaExcelInstance : cargaExcelInstance ] }
    }

    def delete = {
        def cargaExcelInstance = CargaExcel.get( params.id )
        if(cargaExcelInstance) {
            try {
                cargaExcelInstance.delete(flush:true)
                flash.message = "CargaExcel ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "CargaExcel ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "CargaExcel not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def cargaExcelInstance = CargaExcel.get( params.id )

        if(!cargaExcelInstance) {
            flash.message = "CargaExcel not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ cargaExcelInstance : cargaExcelInstance ]
        }
    }

    def update = {
        def cargaExcelInstance = CargaExcel.get( params.id )
        if(cargaExcelInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(cargaExcelInstance.version > version) {
                    
                    cargaExcelInstance.errors.rejectValue("version", "cargaExcel.optimistic.locking.failure", "Another user has updated this CargaExcel while you were editing.")
                    render(view:'edit',model:[cargaExcelInstance:cargaExcelInstance])
                    return
                }
            }
            cargaExcelInstance.properties = params
            if(!cargaExcelInstance.hasErrors() && cargaExcelInstance.save()) {
                flash.message = "CargaExcel ${params.id} updated"
                redirect(action:show,id:cargaExcelInstance.id)
            }
            else {
                render(view:'edit',model:[cargaExcelInstance:cargaExcelInstance])
            }
        }
        else {
            flash.message = "CargaExcel not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def cargaExcelInstance = new CargaExcel()
        cargaExcelInstance.properties = params
        return ['cargaExcelInstance':cargaExcelInstance]
    }

    def save = {
        def cargaExcelInstance = new CargaExcel(params)
        if(!cargaExcelInstance.hasErrors() && cargaExcelInstance.save()) {
            flash.message = "CargaExcel ${cargaExcelInstance.id} created"
            redirect(action:show,id:cargaExcelInstance.id)
        }
        else {
            render(view:'create',model:[cargaExcelInstance:cargaExcelInstance])
        }
    }
}
