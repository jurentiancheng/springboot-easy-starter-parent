package com.easyapi.annotations;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class SystemLogAop {


    @Pointcut("@annotation(com.easyapi.annotations.SystemLog)")
    void pointCut() {

    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String logStartFormat  = "开始- %s, 请求，condition：%s";
        String logEndFormat = "结束- %s 请求，result: %s";
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Annotation[] annotations = signature.getMethod().getAnnotations();
        String apiDesc;
        long count = Arrays.stream(annotations).filter(annotation -> annotation instanceof ApiOperation).count();
        if (count > 0) {
            ApiOperation apiOperationAnn = signature.getMethod().getAnnotation(ApiOperation.class);
            apiDesc = apiOperationAnn.value();
        } else {
            SystemLog systemLogAnno = signature.getMethod().getAnnotation(SystemLog.class);
            apiDesc = systemLogAnno.value();
        }
        log.info(String.format(logStartFormat, apiDesc, JSON.toJSONString(joinPoint.getArgs())));
        try {
            Object proceed = joinPoint.proceed();
            SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);
            if (!systemLog.hideResult()) {
                log.info(String.format(logEndFormat, apiDesc, JSON.toJSONString(proceed)));
            }
        }catch (Exception e) {
            throw new Throwable(e);
        }
        return null;
    }

}
