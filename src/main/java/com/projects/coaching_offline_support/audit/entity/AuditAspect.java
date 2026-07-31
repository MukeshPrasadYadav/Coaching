package com.projects.coaching_offline_support.audit.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coaching_offline_support.audit.repository.AuditLogRepository;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.notification.NotificationStrategy;
import com.projects.coaching_offline_support.notification.NotificationStrategyFactory;
import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;
import com.projects.coaching_offline_support.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final NotificationStrategyFactory notificationStrategyFactory;


    private final ObjectMapper objectMapper;   // Inject Jackson

    private final ExpressionParser parser = new SpelExpressionParser();

    private final String POINTCUT = "@annotation(auditable)";

    @AfterReturning(pointcut = POINTCUT,returning = "result")
    public void auditSuccess(JoinPoint joinPoint,Auditable auditable,Object result){
        createAuditLog(joinPoint,auditable,result,null);
        sendNotification(auditable);

    }

    @AfterThrowing(pointcut = POINTCUT , throwing = "ex")
    public void auditFailure(JoinPoint joinPoint, Auditable auditable, Exception ex){
        createAuditLog(joinPoint,auditable,null,ex.getMessage());

    }

    public void createAuditLog(JoinPoint joinPoint,Auditable auditable, Object result,String ex ){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();

        UUID actor = null;

        if (!methodName.equals("signin") && !methodName.equals("signUp")) {
            actor = CurrentUser.get().getId();
        }
        String description = evaluateSpEL(
                auditable.description(),
                joinPoint,
                result
        );


        AuditLog auditLog = AuditLog.builder()
                .actor(actor)
                .logType(auditable.logType())
                .actionType(auditable.actionType())
                .description(description)
                .build();

        auditLogRepository.save(auditLog);
        log.info("Audit log created: {} - {} by {}",
                auditable.actionType(), auditable.description(), actor);

    }

    private String evaluateSpEL(String expression, JoinPoint joinPoint, Object result) {
        if (expression == null || expression.isBlank()) {
            return "";
        }

        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            Object[] args = joinPoint.getArgs();
            String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

            if (paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    context.setVariable(paramNames[i], args[i]);   // e.g. #request, #id, etc.
                }
            }
            context.setVariable("result", result);

            return parser.parseExpression(
                    expression,
                    new TemplateParserContext()
            ).getValue(context, String.class);
        } catch (Exception e) {
            log.warn("Failed to evaluate SpEL: {}", expression, e);
            return "";
        }
    }

    public void sendNotification(Auditable auditable){
        if(auditable.notificationType()  == NotificationType.NONE) return;
        try{
           NotificationStrategy strategy = notificationStrategyFactory.get(NotificationMode.EMAIL); // later change it with notification type from aspect
            System.out.println("staratery"+strategy);
           strategy.send();

        } catch (Exception e) {
            log.error("Notification failed for {}", auditable.notificationType(), e);
        }

    }

}