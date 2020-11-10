package cn.dexter.poker.redmine.dingding.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by DexterPoker on 5/16/2018.
 */
@Aspect
@Component
public class ControllerLogAspect {

    private static Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    ThreadLocal<Long>    startTime = new ThreadLocal<>();

    @Pointcut("execution(public * cn.dexter.poker.redmine.dingding.controller..*Controller.*(..))")
    public void controllerLog(){
    }

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        logger.info("METHOD : "  + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "ret")
    public void doAfter(Object ret){
        if(Objects.nonNull(ret)) {
            if(ret.toString().length() > 1024) {
                ret = ret.toString().substring(0, 1024);
            }
            logger.info("RESPONSE : " + ret);
        }
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName();
        logger.error("METHOD：" + methodName + " OCCURS EXCEPTION: ", ex);
    }

}