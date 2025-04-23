package com.ed.sysbankcards.util.AOP;


import com.ed.sysbankcards.model.dto.card.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(public * com.ed.sysbankcards.service..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();

        // шифруем номер карты
        Object[] safeArgs = maskSensitiveData(args);

        //log.info("Entering method: {}.{} with arguments: {}", className, methodName, Arrays.toString(safeArgs));

        //long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            //long executionTime = System.currentTimeMillis() - startTime;
            log.info("Exiting method: {}.{} with result: {}.",
                    className, methodName, result);
            return result;
        } catch (Throwable throwable) {
            log.error("Exception in method: {}.{} with arguments: {}. Error: {}",
                    className, methodName, Arrays.toString(safeArgs), throwable.getMessage(), throwable);
            throw throwable;
        }
    }

    private Object[] maskSensitiveData(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    if (arg instanceof ReplenishmentCardRequest request) {
                        return new ReplenishmentCardRequest(
                                maskCardNumber(request.getCardNumber()),
                                request.getAmount()
                        );
                    } else if (arg instanceof BlockCardRequest request) {
                        return new BlockCardRequest(maskCardNumber(request.getCardNumber()));
                    } else if (arg instanceof ShowTransactionalByCardRequest request) {
                        return new ShowTransactionalByCardRequest(maskCardNumber(request.getCardNumber()));
                    } else if (arg instanceof TransferFundsBetweenUserCardsRequest request) {
                        return new TransferFundsBetweenUserCardsRequest(
                                maskCardNumber(request.getFromCardNumber()),
                                maskCardNumber(request.getToCardNumber()),
                                request.getAmount(),
                                request.getCurrency()
                        );
                    } else if (arg instanceof WithdrawFundsRequest request) {
                        return new WithdrawFundsRequest(
                                maskCardNumber(request.getCardNumber()),
                                request.getAmount(),
                                request.getCurrency()
                        );
                    } else if (arg instanceof String cardNumber && cardNumber.matches("\\d{16}")) {
                        return maskCardNumber(cardNumber);
                    } else if (arg instanceof CreateCardRequest request) {
                        return new CreateCardRequest(maskCardNumber(request.getCardNumber()),
                                request.getCardOwner(),
                                request.getExpiryDate());
                    } else if (arg instanceof UpdateCardRequest request) {
                        return new UpdateCardRequest(maskCardNumber(request.getCardNumber()),
                                request.getNewCardNumber(),
                                request.getNewExpiryDate());
                    } else if (arg instanceof ActivateCardRequest request) {
                        return new ActivateCardRequest(maskCardNumber(request.getCardNumber()));
                    } else if (arg instanceof DeleteCardRequest request) {
                        return new DeleteCardRequest(maskCardNumber(request.getCardNumber()));
                    } else if (arg instanceof SetLimitsForCardRequest request) {
                        return new SetLimitsForCardRequest(maskCardNumber(request.getCardNumber()),
                                request.getDailyLimit(),
                                request.getMonthlyLimit());
                    }

                    return arg;
                })
                .toArray();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
