

package com.rural

class SubRubroController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ subRubroInstanceList: SubRubro.list( params ), subRubroInstanceTotal: SubRubro.count() ]
    }

    def show = {
        def subRubroInstance = SubRubro.get( params.id )

        if(!subRubroInstance) {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ subRubroInstance : subRubroInstance ] }
    }

    def delete = {
        def subRubroInstance = SubRubro.get( params.id )
        if(subRubroInstance) {
            try {
                subRubroInstance.delete(flush:true)
                flash.message = "SubRubro ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "SubRubro ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def subRubroInstance = SubRubro.get( params.id )

        if(!subRubroInstance) {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ subRubroInstance : subRubroInstance ]
        }
    }

    def update = {
        def subRubroInstance = SubRubro.get( params.id )
        if(subRubroInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(subRubroInstance.version > version) {
                    
                    subRubroInstance.errors.rejectValue("version", "subRubro.optimistic.locking.failure", "Another user has updated this SubRubro while you were editing.")
                    render(view:'edit',model:[subRubroInstance:subRubroInstance])
                    return
                }
            }
            subRubroInstance.properties = params
            if(!subRubroInstance.hasErrors() && subRubroInstance.save()) {
                flash.message = "SubRubro ${params.id} updated"
                redirect(action:show,id:subRubroInstance.id)
            }
            else {
                render(view:'edit',model:[subRubroInstance:subRubroInstance])
            }
        }
        else {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def subRubroInstance = new SubRubro()
        subRubroInstance.properties = params
        return ['subRubroInstance':subRubroInstance]
    }

    def save = {
        def subRubroInstance = new SubRubro(params)
        if(!subRubroInstance.hasErrors() && subRubroInstance.save()) {
            flash.message = "SubRubro ${subRubroInstance.id} created"
            redirect(action:show,id:subRubroInstance.id)
        }
        else {
            render(view:'create',model:[subRubroInstance:subRubroInstance])
        }
    }
}
