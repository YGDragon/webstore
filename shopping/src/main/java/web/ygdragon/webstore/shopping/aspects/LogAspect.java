package web.ygdragon.webstore.shopping.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    @Around("@annotation(web.ygdragon.webstore.shopping.aspects.LogLeadTime)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("INFO >> Method called: " + joinPoint.getSignature().getName());
        long timeStart = LocalDateTime.now().getNano();

        Object proceed = joinPoint.proceed();

        long timeFinish = LocalDateTime.now().getNano();
        System.out.println("Method execution time: " + ((timeFinish - timeStart) / 1000000) + " миллисекунд.");
        return proceed;
    }

}
