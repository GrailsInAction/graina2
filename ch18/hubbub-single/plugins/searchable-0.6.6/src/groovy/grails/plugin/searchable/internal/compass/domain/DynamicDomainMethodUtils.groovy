/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.searchable.internal.compass.domain

import grails.plugin.searchable.internal.compass.SearchableCompassUtils

import org.apache.commons.logging.LogFactory
import org.compass.core.Compass

/**
 * @author Maurice Nicholson
 */
class DynamicDomainMethodUtils {
    static LOG = LogFactory.getLog("grails.plugin.searchable.internal.compass.domain.DynamicDomainMethodUtils")

    static attachDynamicMethods(searchableMethodFactory, domainClasses, Compass compass) {
        for (grailsDomainClass in domainClasses) {
            if (!SearchableCompassUtils.isRootMappedClass(grailsDomainClass, compass)) {
                continue
            }
            LOG.debug("Adding searchable methods to [${grailsDomainClass.clazz.name}]")

            // ------------------------------------------------------------
            // class methods

            /**
             * search: Returns a subset of the instances of this class matching the given query
             */
            grailsDomainClass.metaClass.'static'.search << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "search").invoke(*processStringArgs(args))
            }

            /**
             * moreLikeThis: Returns more hits of this class like this given instance
             */
            grailsDomainClass.metaClass.'static'.moreLikeThis << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "moreLikeThis").invoke(*processStringArgs(args))
            }

            /**
             * searchTop: Returns the top (most relevant) instance of this class matching the given query
             */
            grailsDomainClass.metaClass.'static'.searchTop << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "searchTop").invoke(*processStringArgs(args))
            }

            /**
             * searchEvery: Returns all instance of this class matching the given query
             */
            grailsDomainClass.metaClass.'static'.searchEvery << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "searchEvery").invoke(*processStringArgs(args))
            }

            /**
             * Returns the number of hits for the given query matching instances of this class
             */
            grailsDomainClass.metaClass.'static'.countHits << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "countHits").invoke(*processStringArgs(args))
            }

            /**
             * Get term frequencies for the given args
             */
            grailsDomainClass.metaClass.'static'.termFreqs << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "termFreqs").invoke(*processStringArgs(args))
            }

            /**
             * Suggest an alternative query (correcting possible spelling errors)
             */
            grailsDomainClass.metaClass.'static'.suggestQuery << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "suggestQuery").invoke(*processStringArgs(args))
            }

            /**
             * index: Adds class instances to the search index
             */
            grailsDomainClass.metaClass.'static'.index << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "index").invoke(*processStringArgs(args))
            }

            /**
             * indexAll: Adds class instances to the search index
             */
            grailsDomainClass.metaClass.'static'.indexAll << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "indexAll").invoke(*processStringArgs(args))
            }

            /**
             * unindex: Removes class instances from the search index
             */
            grailsDomainClass.metaClass.'static'.unindex << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "unindex").invoke(*processStringArgs(args))
            }

            /**
             * unindexAll: Removes class instances from the search index
             */
            grailsDomainClass.metaClass.'static'.unindexAll << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "unindexAll").invoke(*processStringArgs(args))
            }

            /**
             * reindexAll: Updates the search index
             */
            grailsDomainClass.metaClass.'static'.reindexAll << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "reindexAll").invoke(*processStringArgs(args))
            }

            /**
             * reindex: Updates the search index
             */
            grailsDomainClass.metaClass.'static'.reindex << { Object[] args ->
                searchableMethodFactory.getMethod(delegate, "reindex").invoke(*processStringArgs(args))
            }

            // ------------------------------------------------------------
            // instance methods

            /**
             * Find more objects like this instance
             * @return a search result containing a subset of similar objects, if any
             */
            grailsDomainClass.metaClass.moreLikeThis << { ->
                searchableMethodFactory.getMethod(delegate.class, "moreLikeThis").invoke(delegate)
            }

            /**
             * Returns more objects like this instance
             * @param options a Map of options
             * @return a search result for similar objects, if any;
             * the exact <em>type</em> of search result can be controlled with the
             * optional <code>result</code> option
             */
            grailsDomainClass.metaClass.moreLikeThis << { Map options ->
                searchableMethodFactory.getMethod(delegate.class, "moreLikeThis").invoke(delegate, options)
            }

            /**
             * Adds the instance to the search index
             */
            grailsDomainClass.metaClass.index << { Object[] args ->
                searchableMethodFactory.getMethod("index").invoke(delegate)
            }

            /**
             * unindex instance method: removes the instance from the search index
             */
            grailsDomainClass.metaClass.unindex << { Object[] args ->
                searchableMethodFactory.getMethod("unindex").invoke(delegate)
            }

            /**
             * reindex instance method: updates the search index to reflect the current instance data
             */
            grailsDomainClass.metaClass.reindex << { Object[] args ->
                searchableMethodFactory.getMethod("reindex").invoke(delegate)
            }
        }
    }

    private static Object[] processStringArgs(Object[] args) {
        boolean wrapped = false
        if (args.length == 1 && args[0] instanceof Object[]) {
            args = args[0]
            wrapped = true
        }

        def newArgs = []
        for (int i = 0; i < args.length; i++) {
            newArgs << (args[i] instanceof CharSequence ? args[i].toString() : args[i])
        }

        newArgs = newArgs as Object[]
        return wrapped ? [newArgs] as Object[] : newArgs
    }
}
