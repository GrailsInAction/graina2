/* Copyright 2004-2005 the original author or authors.
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
package org.codehaus.groovy.grails.plugins.quartz.config

import org.codehaus.groovy.grails.plugins.quartz.CronTriggerFactoryBean
import org.codehaus.groovy.grails.plugins.quartz.SimpleTriggerFactoryBean
import org.codehaus.groovy.grails.plugins.quartz.GrailsTaskClassProperty as GTCP
import org.quartz.Trigger

/**
 * Groovy Builder for parsing triggers configuration info.
 *
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 *
 * @since 0.3
 */
public class TriggersConfigBuilder extends BuilderSupport {
    private triggerNumber = 0
    private jobName

    def triggers = [:]

    public TriggersConfigBuilder(String jobName) {
        super()
        this.jobName = jobName
    }

    public build(closure) {
        closure.delegate = this
        closure.call()
        return triggers
    }

    protected void setParent(parent, child) {}

    protected createNode(name) {
        createNode(name, null, null)
    }

    protected createNode(name, value) {
        createNode(name, null, value)
    }

    protected createNode(name, Map attributes) {
        createNode(name, attributes, null)
    }

    protected Object createNode(name, Map attributes, Object value) {
        def trigger = createTrigger(name, attributes, value)
        triggers[trigger.name] = trigger
        trigger
    }

    public Expando createTrigger(name, Map attributes, value) {
        def trigger
        if(name == 'simpleTrigger') {
            trigger = new Expando(
                    clazz: SimpleTriggerFactoryBean,
                    startDelay: attributes?.startDelay != null ? attributes?.startDelay : GTCP.DEFAULT_START_DELAY,
                    repeatInterval: attributes?.timeout != null ? attributes?.timeout : GTCP.DEFAULT_TIMEOUT,
                    repeatCount: attributes?.repeatCount != null ? attributes?.repeatCount : GTCP.DEFAULT_REPEAT_COUNT,
                    volatility: attributes?.volatility != null ? attributes?.volatility : GTCP.DEFAULT_VOLATILITY
            )
        } else if(name == 'cronTrigger') {
            trigger = new Expando(
                    clazz: CronTriggerFactoryBean,
                    startDelay: attributes?.startDelay != null ? attributes?.startDelay : GTCP.DEFAULT_START_DELAY,
                    cronExpression: attributes?.cronExpression ?: GTCP.DEFAULT_CRON_EXPRESSION,
                    volatility: attributes?.volatility != null ? attributes?.volatility : GTCP.DEFAULT_VOLATILITY
            )
        } else if(name == 'customTrigger') {
            trigger = new Expando(
                    clazz: attributes?.triggerClass,
                    attributes
            )
            if(!trigger.clazz) throw new Exception("Custom trigger must have 'triggerClass' attribute")
            if(!Trigger.isAssignableFrom(trigger.clazz)) throw new Exception("Custom trigger class must implement org.quartz.Trigger interface.")
        } else {
            throw new Exception("Invalid format")
        }

        trigger.group = attributes?.group ?: GTCP.DEFAULT_TRIGGERS_GROUP
        trigger.name = value ? "${jobName}${value.size() == 0 ? '' : value[0].toUpperCase()}${value[1..-1]}" : "${jobName}${triggerNumber++}"
        trigger
    }

    public Map createEmbeddedSimpleTrigger(long startDelay, long timeout, int repeatCount) {
        return [(jobName):createTrigger('simpleTrigger', [startDelay:startDelay, timeout:timeout, repeatCount:repeatCount], '')]
    }

    public Map createEmbeddedCronTrigger(long startDelay, String cronExpression) {
        return [(jobName):createTrigger('cronTrigger', [startDelay:startDelay, cronExpression:cronExpression], '')] 
    }
}
