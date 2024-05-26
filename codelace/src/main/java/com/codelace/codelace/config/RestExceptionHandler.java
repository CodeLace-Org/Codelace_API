package com.codelace.codelace.config;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codelace.codelace.exception.BadRequestException;
import com.codelace.codelace.exception.InsufficientSubscriptionPlanException;
import com.codelace.codelace.exception.MaxFreeInscriptionException;
import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.exception.SubscriptionNotActiveException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {

    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "The request was not valid. Please fix the errors and try again.");

        Set<String> errors = new HashSet<>();
        List<FieldError> fieldErrors = exception.getFieldErrors();

        for (FieldError fe : fieldErrors) {
            String message = messageSource.getMessage(fe, Locale.getDefault());
            errors.add(message);
        }
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException exception){
        String message = "Resource not found.";

        if (!exception.getMessage().isBlank()) message = exception.getMessage();

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException exception){
        String message = "The request was not valid.";

        if (!exception.getMessage().isBlank()) message = exception.getMessage();

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ProblemDetail handleResourceDuplicateException(ResourceDuplicateException exception){
        String message = "Resource already exists.";

        if (!exception.getMessage().isBlank()) message = exception.getMessage();

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(MaxFreeInscriptionException.class)
    public ProblemDetail handleMaxFreeInscriptionException(MaxFreeInscriptionException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.LOCKED, "The student has already reached the maximum amount of free inscriptions.");
    }

    @ExceptionHandler(InsufficientSubscriptionPlanException.class)
    public ProblemDetail handleInsufficientSubscriptionPlan(InsufficientSubscriptionPlanException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.LOCKED, "The student needs a non-free plan to access this content.");
    }

    @ExceptionHandler(SubscriptionNotActiveException.class)
    public ProblemDetail handleSubscriptionNotActiveException(SubscriptionNotActiveException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.LOCKED, "The student's subscription is not active.");
    }
}
