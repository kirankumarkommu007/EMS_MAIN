package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.demo.service.*.*(..)) || execution(* com.example.demo.jwt.JwtUtil.generateToken(..))")
    public void allMethods() {}

    @Before("allMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Starting method: {} with arguments: {}", joinPoint.getSignature().toShortString(), java.util.Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "allMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Ending method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }
    
    @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with exception: {}", joinPoint.getSignature().toShortString(), exception.getMessage(), exception);

    }
}
