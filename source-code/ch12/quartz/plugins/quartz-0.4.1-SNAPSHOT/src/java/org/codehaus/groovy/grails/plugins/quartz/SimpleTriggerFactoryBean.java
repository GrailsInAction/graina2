/*
 * Copyright 2004-2005 the original author or authors.
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
package org.codehaus.groovy.grails.plugins.quartz;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;


/**
 * Factory bean which configures SimpleTrigger.
 *
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 * 
 * @since 0.3.2
 */
public class SimpleTriggerFactoryBean implements FactoryBean, InitializingBean, BeanNameAware {

    private SimpleTrigger simpleTrigger;

	private JobDetail jobDetail;

    private String beanName;

    private String name;
    private String group;
    private Long startDelay;
    private Long repeatInterval;
    private Integer repeatCount;
    private boolean volatility;

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() {
        simpleTrigger = new SimpleTrigger();
	    simpleTrigger.setName(name != null ? name : beanName);
	    simpleTrigger.setGroup(group);
        if(startDelay != null) simpleTrigger.setStartTime(new Date(System.currentTimeMillis() + startDelay.longValue()));
        if(repeatInterval != null) simpleTrigger.setRepeatInterval(repeatInterval.longValue());
        if(repeatCount != null) simpleTrigger.setRepeatCount(repeatCount.intValue());

        if (jobDetail != null) {
			simpleTrigger.setJobName(jobDetail.getName());
			simpleTrigger.setJobGroup(jobDetail.getGroup());
		}
		simpleTrigger.setVolatility(volatility);
	}

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject() throws Exception {
        return simpleTrigger;
    }

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class getObjectType() {
        return SimpleTrigger.class;
    }

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(String)
     */
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setStartDelay(Long startDelay) {
        this.startDelay = startDelay;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setVolatility(boolean volatility) {
        this.volatility = volatility;
    }
}
