package cl.duoc.mineria.reportes.exception;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Los parámetros para generar el consolidado de turno no son correctos."
        );

        problem.setTitle("Solicitud de Reporte Inválida");
        problem.setProperty("timestamp", Instant.now());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Dato inválido"
        ));

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ReportAnalysisException.class)
    public ProblemDetail handleReportAnalysis(ReportAnalysisException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );

        problem.setTitle("Conflicto en la Generación del Reporte");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ProblemDetail handleWebClientResponseError(WebClientResponseException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Error de comunicación inter-servicio. El sistema de reportes no pudo recolectar las métricas porque una de las APIs externas de la faena devolvió un error."
        );

        problem.setTitle("External Service Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("codigoErrorExterno", ex.getStatusCode().value());
        problem.setProperty("detalleServicioAfectado", ex.getResponseBodyAsString());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralExeption(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocurrió un error inesperado dentro del servidor al procesar el análisis de turnos."
        );

        problem.setTitle("Internal Server Errors en Reportes");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        return problem;
    }
}
