

package com.rural

class ListaPreciosController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ listaPreciosInstanceList: ListaPrecios.list( params ), listaPreciosInstanceTotal: ListaPrecios.count() ]
    }

    def show = {
        def listaPreciosInstance = ListaPrecios.get( params.id )

        if(!listaPreciosInstance) {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ listaPreciosInstance : listaPreciosInstance ] }
    }

    def delete = {
        def listaPreciosInstance = ListaPrecios.get( params.id )
        if(listaPreciosInstance) {
            try {
                listaPreciosInstance.delete(flush:true)
                flash.message = "ListaPrecios ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "ListaPrecios ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def listaPreciosInstance = ListaPrecios.get( params.id )

        if(!listaPreciosInstance) {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ listaPreciosInstance : listaPreciosInstance ]
        }
    }

    def update = {
        def listaPreciosInstance = ListaPrecios.get( params.id )
        if(listaPreciosInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(listaPreciosInstance.version > version) {
                    
                    listaPreciosInstance.errors.rejectValue("version", "listaPrecios.optimistic.locking.failure", "Another user has updated this ListaPrecios while you were editing.")
                    render(view:'edit',model:[listaPreciosInstance:listaPreciosInstance])
                    return
                }
            }
            listaPreciosInstance.properties = params
            if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()) {
                flash.message = "ListaPrecios ${params.id} updated"
                redirect(action:show,id:listaPreciosInstance.id)
            }
            else {
                render(view:'edit',model:[listaPreciosInstance:listaPreciosInstance])
            }
        }
        else {
            flash.message = "ListaPrecios not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def listaPreciosInstance = new ListaPrecios()
        listaPreciosInstance.properties = params
        return ['listaPreciosInstance':listaPreciosInstance]
    }

    def save = {
        def listaPreciosInstance = new ListaPrecios(params)
        if(!listaPreciosInstance.hasErrors() && listaPreciosInstance.save()) {
            flash.message = "ListaPrecios ${listaPreciosInstance.id} created"
            redirect(action:show,id:listaPreciosInstance.id)
        }
        else {
            render(view:'create',model:[listaPreciosInstance:listaPreciosInstance])
        }
    }
}
