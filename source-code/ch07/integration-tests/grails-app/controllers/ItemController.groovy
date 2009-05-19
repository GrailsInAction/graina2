

class ItemController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ itemInstanceList: Item.list( params ), itemInstanceTotal: Item.count() ]
    }

    def show = {
        def itemInstance = Item.get( params.id )

        if(!itemInstance) {
            flash.message = "Item not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ itemInstance : itemInstance ] }
    }

    def delete = {
        def itemInstance = Item.get( params.id )
        if(itemInstance) {
            try {
                itemInstance.delete()
                flash.message = "Item ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Item ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Item not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def itemInstance = Item.get( params.id )

        if(!itemInstance) {
            flash.message = "Item not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ itemInstance : itemInstance ]
        }
    }

    def update = {
        def itemInstance = Item.get( params.id )
        if(itemInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(itemInstance.version > version) {
                    
                    itemInstance.errors.rejectValue("version", "item.optimistic.locking.failure", "Another user has updated this Item while you were editing.")
                    render(view:'edit',model:[itemInstance:itemInstance])
                    return
                }
            }
            itemInstance.properties = params
            if(!itemInstance.hasErrors() && itemInstance.save()) {
                flash.message = "Item ${params.id} updated"
                redirect(action:show,id:itemInstance.id)
            }
            else {
                render(view:'edit',model:[itemInstance:itemInstance])
            }
        }
        else {
            flash.message = "Item not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def itemInstance = new Item()
        itemInstance.properties = params
        return ['itemInstance':itemInstance]
    }

    def save = {
        def itemInstance = new Item(params)
        if(!itemInstance.hasErrors() && itemInstance.save()) {
            flash.message = "Item ${itemInstance.id} created"
            redirect(action:show,id:itemInstance.id)
        }
        else {
            render(view:'create',model:[itemInstance:itemInstance])
        }
    }
}
