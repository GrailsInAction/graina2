import org.codehaus.groovy.grails.commons.GrailsControllerClass
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.GrailsClassUtils

// @todo subItem sorting
class NavigationService {

    static transactional = false
    
    def manuallyRegistered = []
    def byGroup = ['*':[]]
    def hidden = new HashSet()
    def activePathByRequestArgs = [:]
    
    def reset() {
        byGroup = ['*':[]]
        // re-add the manually defined items
        ConfigurationHolder.config.navigation?.each { k, v ->
            doRegisterItem(k, v)
        }
        manuallyRegistered.each { item ->
            doRegisterItem(item.group, item.info)
        }
    }

    protected newSubItem(data, controllerName) {
        def result = [:]
        if (data instanceof Map) {
            result.putAll(data)
        } else {
            result.action = data
        }
        if (!result.title) {
            result.title = GrailsClassUtils.getNaturalName(result.action)
        }
        result.controller = controllerName

        calculatePath(result.path, result)

        return result
    }

    def makeReverseMapKey(controller, action, params) {
        def k = "$controller/$action"
        if (params?.size()) {
            def p = new TreeMap(params)
            p.remove('controller')
            p.remove('action')
            if (p) {
                k += '/' + p.toString()
            }
        }
        return k.toString()
    }
    
    def reverseMapActivePathFor(controller, action, params) {
        // Try first with params
        def kWithParams = makeReverseMapKey(controller, action, params)
        def path = activePathByRequestArgs[kWithParams]
        if (!path) {
            def kWithoutParams = makeReverseMapKey(controller, action, null)
            path = activePathByRequestArgs[kWithoutParams]
        }
        return path ?: kWithParams.tokenize('/')
    }
    
    void calculatePath(pathValue, item) {
        if (pathValue) {
            item.path = pathValue instanceof List ? pathValue : pathValue.tokenize('/')
        } else {
            item.path = [item.controller, item.action]
            if (item.params) {
                def sortedParams = new TreeMap()
                sortedParams += item.params
                item.path << sortedParams.toString()
            }
        }
    }
    
    def populateItem(src, item, controllerGrailsClass) {
        item.action = src.action ?: (controllerGrailsClass ? controllerGrailsClass.defaultAction : 'index')
        item.order = src.order
        item.id = src.id
        item.isVisible = (src['isVisible'] == null) ? true : src.isVisible
        item.title = src.title ?: GrailsClassUtils.getNaturalName(src.action)
        item.params = src.params

        calculatePath(src.path, item)

        item.subItems = src.subItems?.collect { subitem ->
            def si = newSubItem(subitem, item.controller)
            def k = makeReverseMapKey(si.controller, si.action, si.params)
            activePathByRequestArgs[ k ] = si.path
            return si
        }
        return item
    }

    /**
     * Register a navigation item by convention
     */
    def registerItem(GrailsControllerClass controllerGrailsClass) {
        def p = [ 
            controller:controllerGrailsClass.logicalPropertyName
        ]
        def grp 
        def navInfo = '*'
        if (controllerGrailsClass.clazz.metaClass.hasProperty(controllerGrailsClass.clazz, 'navigation')) {
            navInfo = controllerGrailsClass.clazz.navigation
            if (navInfo == false) { 
                return 
            }
            if (navInfo == true) {
                navInfo = '*'
            }
        }
        if (navInfo instanceof Map) {
            populateItem(navInfo, p, controllerGrailsClass)

            grp = navInfo.group
        } else if (navInfo instanceof List) {
            // Handle lists of info 
            navInfo.each { info ->
                def params = [:]
                params.controller = p.controller
                populateItem(info, params, controllerGrailsClass)

                if (info.group) grp = info.group // use last one unless there is a new one

                doRegisterItem(grp, params)
            }
            return
        } else {
            grp = navInfo
        }
        if (!p.action) {
            p.action = controllerGrailsClass.defaultAction
        }
        if (!p.title) {
            p.title = GrailsClassUtils.getNaturalName(controllerGrailsClass.name)
        }
        doRegisterItem(grp, p)
    }
    
    /**
     * Manually register a navigation item
     */
    def registerItem(String group, params) {
        def item = doRegisterItem(group, params)
        manuallyRegistered << [group:group, info:item]
    }
    
    protected doRegisterItem(String group, Collection v) {
        v.eachWithIndex { item, n -> doRegisterItem(group, item, n) }
    }
    
    protected doRegisterItem(String group, Map params, defaultOrderValue = null) {
        params.action = params.action ?: 'index' // @todo should be default action of controller
 
        def item = [:]
        item.controller = params.controller
        if (!params.order) {
            params.order = defaultOrderValue
        }
        populateItem(params, item, null)
        
        def k = makeReverseMapKey(item.controller, item.action, item.params)
        activePathByRequestArgs[ k ] = item.path
        
        if (!group) group = '*'

        def catInfo = byGroup[group]
        if (!catInfo) {
            catInfo = byGroup[group] = []
        }
        catInfo << item
        if (group != '*') {
            byGroup['*'] << item
        }
        
        return item
    }
    
    def hide(String controller) {
        hidden << controller
    }
    
    /**
     * Must be called after you have registered your items, to enforce ordering
     */
    def updated() {
        byGroup.keySet().each { k ->
            byGroup[k] = byGroup[k].findAll { info -> !hidden.contains(info.controller) }
            byGroup[k] = byGroup[k]?.sort { a, b -> 
                if (b.order) {
                    return a.order?.compareTo(b.order) ?: 0
                } else return +1 // items with no ordering come last
            }
        }
        log.info "Navigation items updated: ${byGroup}"
    }
}
