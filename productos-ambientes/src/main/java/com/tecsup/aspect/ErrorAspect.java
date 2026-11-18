package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Aspect
@Component
public class ErrorAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private HttpServletRequest request;

    @AfterThrowing(
            pointcut = "execution(* com.tecsup.service..*.*(..))",
            throwing = "ex"
    )
    public void capturarError(JoinPoint joinPoint, Exception ex) {

        String metodo = joinPoint.getSignature().getName();
        String mensaje = construirMensaje(ex);
        String usuario = obtenerUsuario();

        System.out.println("ERROR AOP: " + mensaje);

        auditoriaService.registrar(
                "ERROR",
                metodo,
                "Error en " + metodo + "(): " + mensaje,
                usuario
        );
    }

    private String obtenerUsuario() {
        String usuario = request.getHeader("Usuario");
        return (usuario != null) ? usuario : "ANONIMO";
    }

    private String construirMensaje(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            return cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
        }
        String msg = ex.getMessage();
        return (msg != null && !msg.isBlank()) ? msg : ex.getClass().getSimpleName();
    }
}