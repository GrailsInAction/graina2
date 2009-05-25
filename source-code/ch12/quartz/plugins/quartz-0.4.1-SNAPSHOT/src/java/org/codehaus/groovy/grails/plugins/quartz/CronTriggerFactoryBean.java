/*
 * Copyright 2006-2008 the original author or authors.
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
import org.quartz.CronTrigger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;
import java.text.ParseException;

/**
 * Factory bean which configures CronTrigger.
 *
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 * 
 * @since 0.3.2
 */
public class CronTriggerFactoryBean implements FactoryBean, InitializingBean, BeanNameAware {

    private CronTrigger cronTrigger;

	private JobDetail jobDetail;

    private String beanName;

    private String name;
    private String group;
    private Long startDelay;
    private String cronExpression;
    private boolean volatility;

    public void afterPropertiesSet() throws ParseException {
        cronTrigger = new CronTrigger();
	    cronTrigger.setName(name != null ? name : beanName);
	    cronTrigger.setGroup(group);
        if(startDelay != null) cronTrigger.setStartTime(new Date(System.currentTimeMillis() + startDelay.longValue()));
        if(cronExpression != null) cronTrigger.setCronExpression(cronExpression);

        if (jobDetail != null) {
			cronTrigger.setJobName(jobDetail.getName());
			cronTrigger.setJobGroup(jobDetail.getGroup());
		}

        cronTrigger.setVolatility(volatility);
	}

    /**
     * {@inheritDoc}
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject() throws Exception {
        return cronTrigger;
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

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setVolatility(boolean volatility) {
        this.volatility = volatility;
    }
}
