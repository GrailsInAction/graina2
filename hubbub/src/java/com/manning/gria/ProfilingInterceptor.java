package com.manning.gria;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StopWatch;

/**
 * A basic method interceptor that prints the time taken to execute a
 * method to the console.
 */
public class ProfilingInterceptor {
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        // Start the stopwatch.
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Execute the target method.
        Object retval = joinPoint.proceed();

        // Now stop the stopwatch and print out the time.
        stopWatch.stop();
        System.out.println("Time taken to execute method '" + joinPoint.getSignature().getName() +
                "': " + stopWatch.getLastTaskTimeMillis() + "ms");
        return retval;
    }
}
