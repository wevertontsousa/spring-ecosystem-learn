package br.com.wevertontsousa.spring_ecosystem_learn.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceInvalidException;
import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceInvalidException.class)
  private ResponseEntity<ProblemDetail> handleResourceInvalidException(ResourceInvalidException exception) {
    return this.standardError(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  private ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception) {
    return this.standardError(HttpStatus.CONFLICT, exception.getMessage());
  }

  private ResponseEntity<ProblemDetail> standardError(HttpStatus statusCode, String title) {
    var problemDetail = ProblemDetail.forStatusAndDetail(statusCode, title);
    return ResponseEntity.status(statusCode).body(problemDetail);
  }

}
