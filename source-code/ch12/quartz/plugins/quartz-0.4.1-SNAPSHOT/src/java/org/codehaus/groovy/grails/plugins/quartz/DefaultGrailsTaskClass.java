/* Copyright 2006-2008 the original author or authors.
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

import groovy.lang.Closure;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.codehaus.groovy.grails.plugins.quartz.config.TriggersConfigBuilder;
import org.quartz.CronExpression;
import org.quartz.JobExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Grails artefact class which represents a Quartz job.
 *
 * @author Micha?? K??ujszo
 * @author Marcel Overdijk
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 * 
 * @since 0.1
 */
public class DefaultGrailsTaskClass extends AbstractInjectableGrailsClass implements GrailsTaskClass, GrailsTaskClassProperty {
	
	public static final String JOB = "Job";
    private Map triggers = new HashMap();


    public DefaultGrailsTaskClass(Class clazz) {
		super(clazz, JOB);
        validateProperties();
        evaluateTriggers();
    }

    private void evaluateTriggers() {
        // registering additional triggersClosure from 'triggersClosure' closure if present
        Closure triggersClosure = (Closure) GrailsClassUtils.getStaticPropertyValue(getClazz(), "triggers");

        TriggersConfigBuilder builder = new TriggersConfigBuilder(getFullName());

        if(triggersClosure != null) {
            builder.build(triggersClosure);
            triggers = (Map) builder.getTriggers();
        } else {
            // backward compatibility
            if(isCronExpressionConfigured()) {
                triggers = builder.createEmbeddedCronTrigger(getStartDelay(), getCronExpression());
            } else {
                triggers = builder.createEmbeddedSimpleTrigger(getStartDelay(), getTimeout(), getRepeatCount());
            }
        }
    }

    private void validateProperties() {
        Object obj = getPropertyValue(TIMEOUT);
        if( obj != null && !(obj instanceof Integer || obj instanceof Long)) {
            throw new IllegalArgumentException("Timeout property for job class " + getClazz().getName() + " must be Integer or Long");
        }
        if( obj != null && ((Number) obj).longValue() < 0 ) {
            throw new IllegalArgumentException("Timeout property for job class " + getClazz().getName() + " is negative (possibly integer overflow error)");
        }
        obj = getPropertyValue(START_DELAY);
        if( obj != null && !(obj instanceof Integer || obj instanceof Long)) {
            throw new IllegalArgumentException("Start delay property for job class " + getClazz().getName() + " must be Integer or Long");
        }
        if( obj != null && ((Number) obj).longValue() < 0 ) {
            throw new IllegalArgumentException("Start delay property for job class " + getClazz().getName() + " is negative (possibly integer overflow error)");
        }
        obj = getPropertyValue(REPEAT_COUNT);
        if( obj != null && !(obj instanceof Integer)) {
            throw new IllegalArgumentException("Repeat count property for job class " + getClazz().getName() + " must be Integer");
        }
        if( obj != null && ((Number) obj).intValue() < 0 ) {
            throw new IllegalArgumentException("Repeat count property for job class " + getClazz().getName() + " is negative (possibly integer overflow error)");
        }
        obj = getPropertyValue(CRON_EXPRESSION);
        if(obj != null && !CronExpression.isValidExpression(obj.toString())) {
            throw new IllegalArgumentException("Cron expression '" + obj.toString() + "' for job class " + getClazz().getName() + " is not a valid cron expression");
        }
    }

    public void execute() {
        getMetaClass().invokeMethod( getReference().getWrappedInstance(), EXECUTE, new Object[] {} );
	}

    public void execute(JobExecutionContext context) {
        getMetaClass().invokeMethod(getReference().getWrappedInstance(), EXECUTE, new Object[] {context});
    }

    public long getTimeout() {
		Object obj = getPropertyValue( TIMEOUT );
		if( obj == null ) return DEFAULT_TIMEOUT;
		return ((Number)obj).longValue();
	}

	public long getStartDelay() {
		Object obj = getPropertyValue(START_DELAY);
		if( obj == null ) return DEFAULT_START_DELAY;
		return ((Number)obj).longValue();
	}

    public int getRepeatCount() {
        Object obj = getPropertyValue(REPEAT_COUNT);
        if(obj == null) return DEFAULT_REPEAT_COUNT;
        return ((Number)obj).intValue();
    }

    public String getCronExpression() {
		String cronExpression = (String)getPropertyOrStaticPropertyOrFieldValue(CRON_EXPRESSION, String.class);
		if( cronExpression == null || "".equals(cronExpression) ) return DEFAULT_CRON_EXPRESSION;
		return cronExpression;	
	}

	public String getGroup() {
		String group = (String)getPropertyOrStaticPropertyOrFieldValue(GROUP, String.class);
        if( group == null || "".equals(group) ) return DEFAULT_GROUP;
		return group;	
	}

	// not certain about this... feels messy
	public boolean isCronExpressionConfigured() {
		String cronExpression = (String)getPropertyOrStaticPropertyOrFieldValue(CRON_EXPRESSION, String.class);
        return cronExpression != null;
    }

	public boolean isConcurrent() {
		Boolean concurrent = (Boolean)getPropertyValue(CONCURRENT, Boolean.class);
		return concurrent == null ? DEFAULT_CONCURRENT : concurrent.booleanValue();
	}	

	public boolean isSessionRequired() {
		Boolean sessionRequired = (Boolean)getPropertyValue(SESSION_REQUIRED, Boolean.class);
        return sessionRequired == null ? DEFAULT_SESSION_REQUIRED : sessionRequired.booleanValue();
	}

    public boolean getVolatility() {
        Boolean volatility = (Boolean) getPropertyValue(VOLATILITY, Boolean.class);
        return volatility == null ? DEFAULT_VOLATILITY : volatility.booleanValue();
    }

    public boolean getDurability() {
        Boolean durability = (Boolean) getPropertyValue(DURABILITY, Boolean.class);
        return durability == null ? DEFAULT_DURABILITY : durability.booleanValue();
    }

    public Map getTriggers() {
        return triggers;
    }
}